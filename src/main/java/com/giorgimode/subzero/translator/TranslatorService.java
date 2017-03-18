package com.giorgimode.subzero.translator;

import com.giorgimode.dictionary.api.DictionaryService;
import com.giorgimode.dictionary.impl.CcDictionaryService;
import com.giorgimode.dictionary.impl.LanguageEnum;
import com.giorgimode.dictionary.impl.WordnetDictionaryService;
import com.giorgimode.subtitle.api.SubtitleService;
import com.giorgimode.subzero.event.LanguagePairSwitchEvent;
import com.giorgimode.subzero.event.PausedEvent;
import com.giorgimode.subzero.event.SubtitleAddedEvent;
import com.giorgimode.subzero.view.effects.overlay.Overlay;
import com.google.common.eventbus.Subscribe;
import edu.mit.jwi.data.ILoadPolicy;
import org.apache.commons.lang3.ArrayUtils;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.SwingUtilities;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.giorgimode.subzero.Application.application;

public class TranslatorService {
    private Map<Integer, File> subtitleMap;
    private SubtitleService subtitleService;
    private DictionaryService dictionaryService;
    private final EmbeddedMediaPlayer mediaPlayer;

    public TranslatorService() {
        application().subscribe(this);
        mediaPlayer = application().mediaPlayerComponent().getMediaPlayer();
        subtitleMap = new HashMap<>();
    }

    @Subscribe
    public void onPaused(PausedEvent event) {
        if (subtitleService != null && dictionaryService != null) {
            String[][] currentWords = subtitleService.getCurrentWords(mediaPlayer.getTime());
            String[] allWords = Arrays.stream(currentWords)
                    .filter(sArray -> sArray.length > 0)
                    .reduce(ArrayUtils::addAll)
                    .orElse(new String[0]);

            Map<String, Map<String, List<String>>> definitions = dictionaryService.retrieveDefinitions(allWords);
            if (!mediaPlayer.overlayEnabled()) mediaPlayer.enableOverlay(true);
            ((Overlay) mediaPlayer.getOverlay()).populateNewWords(definitions);
        }
    }

    public void add(File subtitleFile) {
        int trackId = mediaPlayer.getSpuCount();
        subtitleMap.put(trackId, subtitleFile);
        subtitleService = new SubtitleService(subtitleFile);
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
}
