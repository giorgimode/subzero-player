package com.giorgimode.subzero.translator;

import com.giorgimode.dictionary.api.DictionaryService;
import com.giorgimode.dictionary.impl.CcDictionaryService;
import com.giorgimode.dictionary.LanguageEnum;
import com.giorgimode.dictionary.impl.WordnetDictionaryService;
import com.giorgimode.subtitle.api.SubtitleService;
import com.giorgimode.subtitle.api.SubtitleUnit;
import com.giorgimode.subzero.event.LanguagePairSwitchEvent;
import com.giorgimode.subzero.event.OverlaySwitchEvent;
import com.giorgimode.subzero.event.PausedEvent;
import com.giorgimode.subzero.event.SubtitleAddedEvent;
import com.giorgimode.subzero.event.SubtitleSwitchEvent;
import com.giorgimode.subzero.util.Utils;
import com.giorgimode.subzero.view.effects.overlay.TranslationOverlay;
import com.google.common.eventbus.Subscribe;
import edu.mit.jwi.data.ILoadPolicy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import uk.co.caprica.vlcj.player.Marquee;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.SwingWorker;
import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.giorgimode.subzero.Application.application;

@Slf4j
public class TranslatorService {
    private SubtitleService subtitleService;
    private SubtitleService subtitleService2;
    private DictionaryService dictionaryService;
    private final EmbeddedMediaPlayer mediaPlayer;

    public TranslatorService() {
        application().subscribe(this);
        mediaPlayer = application().mediaPlayerComponent().getMediaPlayer();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onPaused(PausedEvent event) {
        loadInBackground(this::loadOverlay);
    }

    private void addSubtitleFile(File subtitleFile) {
        log.debug("loading subtitle service");
        loadInBackground(() -> subtitleService = new SubtitleService(subtitleFile));
    }

    public void addSubtitleFile2(File subtitleFile) {
        log.debug("added second subtitle");
        loadInBackground(() -> subtitleService2 = new SubtitleService(subtitleFile));
        application().selectedOverlayType(OverlayType.SECOND_SUBTITLE);
        mediaPlayer.enableOverlay(false);
        discardDictionaryData();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onSubtitleAdded(SubtitleAddedEvent subtitleAddedEvent) {
        log.debug("subtitle added");
        if (application().selectedOverlayType() == OverlayType.TRANSLATION) {
            subtitleService = null;
            addSubtitleFile(subtitleAddedEvent.getFile());
            if (dictionaryService == null) {
                loadInBackground(this::loadDictionary);
            }
        } else {
            discardDictionaryData();
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onSubtitleSwitched(SubtitleSwitchEvent subtitleSwitchEvent) {
        log.debug("subtitle track switched");
        if (application().selectedOverlayType() == OverlayType.TRANSLATION) {
            subtitleService = null;
            loadInBackground(() -> {
                File subtitleFile = new File(application().currentSubtitleFilePath());
                if (subtitleFile.exists()) {
                    subtitleService = new SubtitleService(subtitleFile);
                    mediaPlayer.setSubTitleFile(subtitleFile);
                }
            });
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onLanguagePairSwitch(LanguagePairSwitchEvent languagePairSwitchEvent) {
        log.debug("language pair switched");
        dictionaryService = null;
        application().selectedOverlayType(OverlayType.TRANSLATION);
        loadInBackground(this::loadDictionary);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onOverlaySwitch(OverlaySwitchEvent overlaySwitchEvent) {
        log.debug("overlay type switched");
        discardDictionaryData();
        if (application().selectedOverlayType() == OverlayType.TRANSLATION) {
            loadInBackground(this::loadDictionary);
            File subtitleFile = new File(application().currentSubtitleFilePath());
            if (subtitleFile.exists()) {
                addSubtitleFile(subtitleFile);
            }
        }
    }

    private void loadOverlay() {
        if (application().selectedOverlayType() == OverlayType.TRANSLATION) {
            log.debug("loading translation overlay");
            mediaPlayer.enableOverlay(true);
            showTranslationOverlay();
        } else if (application().selectedOverlayType() == OverlayType.SECOND_SUBTITLE) {
            log.debug("loading second subtitle overlay");
            showSecondSubtitleOverlay();
        }
    }

    private void loadInBackground(VoidFunction function) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                function.run();
                return null;
            }
        }.execute();
    }

    private void showTranslationOverlay() {
        if (subtitleService != null && dictionaryService != null) {
            String[] allWords = getSubtitleWordsAtPauseTime(subtitleService);

            Map<String, Map<String, List<String>>> definitions = dictionaryService.retrieveDefinitions(allWords);
            if (definitions != null && definitions.entrySet().size() > 0) {
                ((TranslationOverlay) mediaPlayer.getOverlay()).populateNewWords(definitions);
                log.debug("translation overlay loaded");
            }
        }
    }

    private void showSecondSubtitleOverlay() {
        if (subtitleService2 != null) {
            SubtitleUnit subtitleUnit = subtitleService2.get(mediaPlayer.getTime());
            List<String> subtitleRowList = subtitleUnit.getText();
            int maxSize = subtitleRowList.stream()
                    .sorted((e1, e2) -> e1.length() > e2.length() ? -1 : 1)
                    .findFirst().orElseGet(() -> "").length();

            // doing calculations to mimic center-justified behavior
            String subtitleText = getSubtitleText(subtitleRowList, maxSize);

            int width = (int) mediaPlayer.getVideoDimension().getWidth();
            int height = (int) mediaPlayer.getVideoDimension().getHeight();
            int centerX = width / 2;
            int centerY = height / 2;
            int x = centerX - maxSize / 2 * 11;
            int y = centerY + centerY / (subtitleRowList.size() + 1);
            Marquee marquee = Marquee.marquee()
                    .text(subtitleText)
                    .location(x, y)
                    .opacity(255)
                    .colour(new Color(255, 221, 148))
                    .enable();
            marquee.apply(mediaPlayer);
            log.debug("second subtitle overlay loaded");
        }
    }

    /**
     * Returns a string without HTML tags, justified in the center and
     * added on new lines as in original file
     *
     * @param subtitleRowList List of lines in the subtitle at current time
     * @param maxSize         length of biggest String in the list
     * @return parsed String
     */
    private String getSubtitleText(List<String> subtitleRowList, int maxSize) {
        return subtitleRowList.stream()
                .map(text -> text.replaceAll("<.*?>", ""))
                .map(text -> StringUtils.center(text, maxSize))
                .collect(Collectors.joining("\n"));
    }

    private String[] getSubtitleWordsAtPauseTime(SubtitleService subtitleService) {
        String[][] currentWords = subtitleService.getCurrentWords(mediaPlayer.getTime());
        return Arrays.stream(currentWords)
                .filter(sArray -> sArray.length > 0)
                .reduce(ArrayUtils::addAll)
                .orElse(new String[0]);
    }

    private void loadDictionary() {
        log.debug("loading Dictionary");
        LanguageEnum language = application().languageEnum();
        if (language == null) {
            return;
        }
        String path = Utils.parentDir();
        File languageDataDir = new File(path + language.getValue());
        if (ArrayUtils.isEmpty(languageDataDir.listFiles())) {
            return;
        }

        if (language == LanguageEnum.EN_EN) {
            dictionaryService = WordnetDictionaryService.getInMemoryInstance(ILoadPolicy.BACKGROUND_LOAD, path);
        } else {
            dictionaryService = CcDictionaryService.getInMemoryInstance(language, path);
        }
        log.debug("finished loading Dictionary");
    }

    private void discardDictionaryData() {
        subtitleService = null;
        dictionaryService = null;
    }
}
