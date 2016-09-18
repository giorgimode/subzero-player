package com.giorgimode.subzero.service;

import com.giorgimode.dictionary.api.DictionaryService;
import com.giorgimode.dictionary.impl.WordnetDictionaryService;
import com.giorgimode.subtitle.api.SubtitleService;
import com.giorgimode.subzero.event.PausedEvent;
import com.giorgimode.subzero.event.SubtitleAddedEvent;
import com.google.common.eventbus.Subscribe;
import edu.mit.jwi.data.ILoadPolicy;
import uk.co.caprica.vlcj.player.MediaPlayer;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.giorgimode.subzero.Application.application;

public class EnhancedTranslatorService {
    protected final MediaPlayer mediaPlayer;
    private Subtitle currentSubtitle;
    private Map<Integer, File> subtitleMap;
    private SubtitleService subtitleService;
    private DictionaryService dictionaryService;

    public EnhancedTranslatorService(MediaPlayer mediaPlayer) {
        application().subscribe(this);
        this.mediaPlayer = mediaPlayer;
        subtitleMap = new HashMap<>();
        loadDictionary();
    }

    @Subscribe
    public void onSubtitleAddedEvent(SubtitleAddedEvent event) {

    }

    @Subscribe
    public void onPaused(PausedEvent event) {
        long start = System.currentTimeMillis();
        if (subtitleService != null && dictionaryService != null) {
            String[][] currentWords = subtitleService.getCurrentWords(mediaPlayer.getTime());
            String[] line1 = currentWords[0];
            System.out.println(Arrays.toString(line1));
            System.out.println("=======================");
            Map<String, String> definitions = dictionaryService.retrieveDefinitions(line1);
          //  definitions.entrySet().forEach(d -> System.out.println(d + "\n"));
        }
        long timespent = System.currentTimeMillis() - start;
        System.out.println("TIME FOR GETTING DEFINITIONS:\n" + timespent);
    }

    public Subtitle getCurrentSubtitle() {
        return currentSubtitle;
    }

    public void setCurrentSubtitle(Subtitle currentSubtitle) {
        this.currentSubtitle = currentSubtitle;
    }

    public void add(File subtitleFile) {
        // mediaPlayer.getSpuDescriptions()
        int trackId = mediaPlayer.getSpuCount();
        subtitleMap.put(trackId, subtitleFile);
        subtitleService = new SubtitleService(subtitleFile);
        System.out.println("zaza");
    }

    private void loadDictionary(){
        dictionaryService = WordnetDictionaryService.getInMemoryInstance(ILoadPolicy.BACKGROUND_LOAD);
    }

}
