package uk.co.caprica.vlcjplayer.service;

import com.google.common.eventbus.Subscribe;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcjplayer.event.PausedEvent;
import uk.co.caprica.vlcjplayer.event.SubtitleAddedEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static uk.co.caprica.vlcjplayer.Application.application;

public class EnhancedTranslatorService {
    protected final MediaPlayer mediaPlayer;
    private Subtitle currentSubtitle;
    private Map<Integer,File> subtitleMap;
    private SubtitleParserService parserService;

    public EnhancedTranslatorService(MediaPlayer mediaPlayer) {
        application().subscribe(this);
        this.mediaPlayer = mediaPlayer;
        subtitleMap = new HashMap<>();
        parserService = new SubtitleParserService();
    }

    @Subscribe
    public void onSubtitleAddedEvent(SubtitleAddedEvent event) {
        System.out.println("=======================");
    //    System.out.println(mediaPlayer.getSpuCount());
        System.out.println("=======================");
    }

    @Subscribe
    public void onPaused(PausedEvent event) {
        System.out.println("PausedEvent" );
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
        subtitleMap.put(trackId,subtitleFile);
        parserService.parseSubtitle(subtitleFile);
    }

}
