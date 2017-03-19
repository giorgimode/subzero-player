package com.giorgimode.subzero.translator;

import com.giorgimode.dictionary.api.DictionaryService;
import com.giorgimode.dictionary.impl.CcDictionaryService;
import com.giorgimode.dictionary.impl.LanguageEnum;
import com.giorgimode.dictionary.impl.WordnetDictionaryService;
import com.giorgimode.subtitle.api.SubtitleService;
import com.giorgimode.subzero.event.LanguagePairSwitchEvent;
import com.giorgimode.subzero.event.PausedEvent;
import com.giorgimode.subzero.event.SubtitleAddedEvent;
import com.giorgimode.subzero.view.effects.overlay.TranslationOverlay;
import com.google.common.eventbus.Subscribe;
import edu.mit.jwi.data.ILoadPolicy;
import org.apache.commons.lang3.ArrayUtils;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.SwingUtilities;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.giorgimode.subzero.Application.application;

public class TranslatorService {
    private SubtitleService subtitleService;
    private SubtitleService subtitleService2;
    private DictionaryService dictionaryService;
    private final EmbeddedMediaPlayer mediaPlayer;
    private OverlayType currentOverlayType;

    public TranslatorService() {
        application().subscribe(this);
        mediaPlayer = application().mediaPlayerComponent().getMediaPlayer();
    }

    public enum OverlayType {
        TRANSLATION, SECOND_SUBTITLE
    }

    @Subscribe
    public void onPaused(PausedEvent event) {
        populateTranslationOverlay();
    }

    private void populateTranslationOverlay() {
        if (subtitleService != null && dictionaryService != null) {
            String[] allWords = getSubtitleWordsAtPauseTime(subtitleService);

            Map<String, Map<String, List<String>>> definitions = dictionaryService.retrieveDefinitions(allWords);
            if (!mediaPlayer.overlayEnabled()) mediaPlayer.enableOverlay(true);
            ((TranslationOverlay) mediaPlayer.getOverlay()).populateNewWords(definitions);
        }
    }

    private void populateSecondSubtitleOverlay() {
        if (subtitleService2 != null ) {
            String[] allWords = getSubtitleWordsAtPauseTime(subtitleService2);

            if (!mediaPlayer.overlayEnabled()) mediaPlayer.enableOverlay(true);
//            ((Overlay) mediaPlayer.getOverlay()).populateNewWords(allWords);
        }
    }

    private String[] getSubtitleWordsAtPauseTime(SubtitleService subtitleService) {
        String[][] currentWords = subtitleService.getCurrentWords(mediaPlayer.getTime());
        return Arrays.stream(currentWords)
                .filter(sArray -> sArray.length > 0)
                .reduce(ArrayUtils::addAll)
                .orElse(new String[0]);
    }

    public void addSubtitleFile(File subtitleFile) {
        subtitleService = new SubtitleService(subtitleFile);
    }

    public void addSubtitleFile2(File subtitleFile) {
        subtitleService2 = new SubtitleService(subtitleFile);
    }

    @Subscribe
    public void onSubtitleAdded(SubtitleAddedEvent subtitleAddedEvent) {
        loadDictionary();
    }

    @Subscribe
    public void onLanguagePairSwitchEvent(LanguagePairSwitchEvent languagePairSwitchEvent) {
        loadDictionary();
    }


    private void loadDictionary() {
        LanguageEnum language = application().languageEnum();
        if (language == null) {
            return;
        }
        String path = application().parentDir();
        File languageDataDir = new File(path + language.getValue());
        if (ArrayUtils.isEmpty(languageDataDir.listFiles())) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            if (language == LanguageEnum.EN_EN) {
                dictionaryService = WordnetDictionaryService.getInMemoryInstance(ILoadPolicy.BACKGROUND_LOAD, path);
            } else {
                dictionaryService = CcDictionaryService.getInMemoryInstance(language, path);
            }
        });
    }

    public OverlayType getCurrentOverlayType() {
        return currentOverlayType;
    }

    public void setCurrentOverlayType(OverlayType currentOverlayType) {
        this.currentOverlayType = currentOverlayType;
    }
}
