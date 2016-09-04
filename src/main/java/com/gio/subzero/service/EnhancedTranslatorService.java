package com.gio.subzero.service;

import com.gio.subzero.event.SubtitleAddedEvent;
import com.google.common.eventbus.Subscribe;
import org.gio.submaster.api.SubtitleService;
import uk.co.caprica.vlcj.player.MediaPlayer;
import com.gio.subzero.event.PausedEvent;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.gio.subzero.Application.application;

public class EnhancedTranslatorService {
    protected final MediaPlayer mediaPlayer;
    private Subtitle currentSubtitle;
    private Map<Integer,File> subtitleMap;
    private SubtitleService subtitleService;

    public EnhancedTranslatorService(MediaPlayer mediaPlayer) {
        application().subscribe(this);
        this.mediaPlayer = mediaPlayer;
        subtitleMap = new HashMap<>();
    }

    @Subscribe
    public void onSubtitleAddedEvent(SubtitleAddedEvent event) {

    }

    @Subscribe
    public void onPaused(PausedEvent event) {
       if (subtitleService != null){
           String[][] currentWords = subtitleService.getCurrentWords(mediaPlayer.getTime());
           String[] line1 = currentWords[0];
           System.out.println(Arrays.toString(line1));
           System.out.println("=======================");
           // DictionaryService dictService = new DictionaryService();
           // Map<String,String> translations = dictService.getTranslation(currentWords);
           // DisplayService: display(translations);
       }

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

}
