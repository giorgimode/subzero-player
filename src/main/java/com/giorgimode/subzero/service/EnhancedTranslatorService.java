package com.giorgimode.subzero.service;

import com.giorgimode.dictionary.api.DictionaryService;
import com.giorgimode.dictionary.impl.CcDictionaryService;
import com.giorgimode.dictionary.impl.CcLanguageEnum;
import com.giorgimode.dictionary.impl.WordnetDictionaryService;
import com.giorgimode.subtitle.api.SubtitleService;
import com.giorgimode.subzero.event.PausedEvent;
import com.giorgimode.subzero.event.SubtitleAddedEvent;
import com.giorgimode.subzero.view.effects.overlay.Overlay;
import com.google.common.eventbus.Subscribe;
import edu.mit.jwi.data.ILoadPolicy;
import uk.co.caprica.vlcj.binding.internal.libvlc_marquee_position_e;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.giorgimode.subzero.Application.application;
import static uk.co.caprica.vlcj.player.Marquee.marquee;

public class EnhancedTranslatorService {
    private Subtitle currentSubtitle;
    private Map<Integer, File> subtitleMap;
    private SubtitleService subtitleService;
    private DictionaryService dictionaryService;
    private final EmbeddedMediaPlayer mediaPlayer;
    List<String> strings = Arrays.asList(
//            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting",
//            "1It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
//            "2It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
//            "3It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
//            "4It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
//            "5It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
            "6It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. " +
                    "fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English" +
                    "fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English" +
                    "fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English" +
                    "fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English" +
                    "fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English" +
                    "fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English" +
                    "fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English" +
                    "fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English" +
                    "fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English" +
                    "fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English" +
                    "fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English" +
                    "fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English" +
                    "yayacho",
            "7It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
            "8It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
            "9It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
            "10It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
            "Nice try",

            "finally it also works");

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
        Overlay overlay = (Overlay) mediaPlayer.getOverlay();
        long start = System.currentTimeMillis();
  /*      if (subtitleService != null && dictionaryService != null) {
            String[][] currentWords = subtitleService.getCurrentWords(mediaPlayer.getTime());
            String[] line1 = currentWords[0];
            System.out.println(Arrays.toString(line1));
            System.out.println("=======================");
            Map<String, Map<String, List<String>>> definitions = dictionaryService.retrieveDefinitions(line1);
            List<String> words = definitions.entrySet().iterator().next().getValue().entrySet().iterator().next().getValue();
            print(definitions);
            //  definitions.entrySet().forEach(d -> System.out.println(d + "\n"));
            overlay.populateNewWords(words);
        }*/
        overlay.populateNewWords(strings);

        long timespent = System.currentTimeMillis() - start;
        System.out.println("TIME FOR GETTING DEFINITIONS:\n" + timespent);
/*
                marquee()
                .text("experiment")
                .position(libvlc_marquee_position_e.centre)
                .opacity(255)
                .enable(true)
                .apply(mediaPlayer);*/
        mediaPlayer.enableOverlay(true);
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

    private void loadDictionary() {
        //    dictionaryService = WordnetDictionaryService.getInMemoryInstance(ILoadPolicy.BACKGROUND_LOAD);
        String path = "D:\\coding\\workspace\\projects\\Dictionary-parser\\src\\main\\resources\\cc\\";
        dictionaryService = CcDictionaryService.getInMemoryInstance(CcLanguageEnum.EN_DE, path);
    }


    private void print(Map<String, Map<String, List<String>>> definitions) {
        definitions.entrySet().forEach(map -> {
            map.getValue().entrySet().forEach(list -> {
                System.out.println(list.getKey() + ": ");
                list.getValue().forEach(word -> System.out.println(word));
            });
        });
    }


}
