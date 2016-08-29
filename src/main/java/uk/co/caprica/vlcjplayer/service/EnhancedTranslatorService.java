package uk.co.caprica.vlcjplayer.service;

import com.google.common.eventbus.Subscribe;
import org.gio.submaster.api.SubtitleData;
import org.gio.submaster.api.SubtitleReader;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcjplayer.event.PausedEvent;
import uk.co.caprica.vlcjplayer.event.SubtitleAddedEvent;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static uk.co.caprica.vlcjplayer.Application.application;

public class EnhancedTranslatorService {
    protected final MediaPlayer mediaPlayer;
    private Subtitle currentSubtitle;
    private Map<Integer,File> subtitleMap;
    private SubtitleParserService parserService;
    private   SubtitleData info;

    public EnhancedTranslatorService(MediaPlayer mediaPlayer) {
        application().subscribe(this);
        this.mediaPlayer = mediaPlayer;
        subtitleMap = new HashMap<>();
        parserService = new SubtitleParserService();
    }

    @Subscribe
    public void onSubtitleAddedEvent(SubtitleAddedEvent event) {

    }

    @Subscribe
    public void onPaused(PausedEvent event) {
       if (info != null){
           String[][] currentWords = info.getCurrentWords(mediaPlayer.getTime());
           String[] line1 = currentWords[0];
           System.out.println(Arrays.toString(line1));
           System.out.println("=======================");
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
        subtitleMap.put(trackId,subtitleFile);
        parserService.parseSubtitle(subtitleFile);
        info = SubtitleReader.read(new File("src/test/resources/good2.srt"));
        System.out.println("zaza");
    }

}
