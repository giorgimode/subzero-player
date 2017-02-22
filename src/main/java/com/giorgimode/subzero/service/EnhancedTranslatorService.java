package com.giorgimode.subzero.service;

import com.giorgimode.dictionary.api.DictionaryService;
import com.giorgimode.dictionary.impl.CcDictionaryService;
import com.giorgimode.dictionary.impl.CcLanguageEnum;
import com.giorgimode.subtitle.api.SubtitleService;
import com.giorgimode.subzero.event.PausedEvent;
import com.giorgimode.subzero.event.SubtitleAddedEvent;
import com.giorgimode.subzero.view.effects.overlay.Overlay;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.ArrayUtils;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.giorgimode.subzero.Application.application;

public class EnhancedTranslatorService {
    private Map<Integer, File> subtitleMap;
    private SubtitleService subtitleService;
    private DictionaryService dictionaryService;
    private final EmbeddedMediaPlayer mediaPlayer;

    public EnhancedTranslatorService() {
        application().subscribe(this);
        mediaPlayer = application().mediaPlayerComponent().getMediaPlayer();
        subtitleMap = new HashMap<>();
        loadDictionary();
    }

    @Subscribe
    public void onSubtitleAddedEvent(SubtitleAddedEvent event) {

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
//            if (!mediaPlayer.overlayEnabled()) mediaPlayer.enableOverlay(true);
            ((Overlay) mediaPlayer.getOverlay()).populateNewWords(definitions);
        }
    }

    public void add(File subtitleFile) {
        // mediaPlayer.getSpuDescriptions()
        int trackId = mediaPlayer.getSpuCount();
        subtitleMap.put(trackId, subtitleFile);
        subtitleService = new SubtitleService(subtitleFile);
    }

    private void loadDictionary() {
        //    dictionaryService = WordnetDictionaryService.getInMemoryInstance(ILoadPolicy.BACKGROUND_LOAD);
        String path = "D:\\coding\\workspace\\projects\\Dictionary-parser\\src\\main\\resources\\cc\\";
        dictionaryService = CcDictionaryService.getInMemoryInstance(CcLanguageEnum.EN_DE, path);
    }
}
