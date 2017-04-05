package com.giorgimode.subzero;

import com.giorgimode.dictionary.impl.LanguageEnum;
import com.giorgimode.subzero.event.TickEvent;
import com.giorgimode.subzero.translator.OverlayType;
import com.giorgimode.subzero.view.action.mediaplayer.MediaPlayerActions;
import com.google.common.eventbus.EventBus;
import lombok.Getter;
import lombok.Setter;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import javax.swing.SwingUtilities;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Global application state.
 */
public final class Application {

    private static final String RESOURCE_BUNDLE_BASE_NAME = "strings/vlcj-player";

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME);

    private static final int MAX_RECENT_MEDIA_SIZE = 10;

    private final EventBus eventBus;

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private final MediaPlayerActions mediaPlayerActions;

    private final ScheduledExecutorService tickService = Executors.newSingleThreadScheduledExecutor();

    private final Deque<String> recentMedia = new ArrayDeque<>(MAX_RECENT_MEDIA_SIZE);

    private LanguageEnum languageEnum;

    private OverlayType selectedOverlayType;

    @Getter
    private Map<Integer, String> subtitleTracks;

    @Getter @Setter
    private int currentSubtitleId;

    private static final class ApplicationHolder {
        private static final Application INSTANCE = new Application();
    }

    public static Application application() {
        return ApplicationHolder.INSTANCE;
    }

    public static ResourceBundle resources() {
        return resourceBundle;
    }

    private Application() {
        eventBus = new EventBus();
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent() {
            @Override
            protected String[] onGetMediaPlayerFactoryExtraArgs() {
                return new String[]{"--no-osd"}; // Disables the display of the snapshot filename (amongst other things)
            }
        };
        mediaPlayerActions = new MediaPlayerActions(mediaPlayerComponent.getMediaPlayer());
        tickService.scheduleWithFixedDelay(() -> eventBus.post(TickEvent.INSTANCE), 0, 1000, TimeUnit.MILLISECONDS);
        subtitleTracks = new TreeMap<>();
        subtitleTracks.put(0, "DISABLED");
    }

    public void subscribe(Object subscriber) {
        eventBus.register(subscriber);
    }

    public void post(Object event) {
        // Events are always posted and processed on the Swing Event Dispatch thread
        if (SwingUtilities.isEventDispatchThread()) {
            eventBus.post(event);
        } else {
            SwingUtilities.invokeLater(() -> eventBus.post(event));
        }
    }

    public EmbeddedMediaPlayerComponent mediaPlayerComponent() {
        return mediaPlayerComponent;
    }

    public MediaPlayerActions mediaPlayerActions() {
        return mediaPlayerActions;
    }

    public void addRecentMedia(String mrl) {
        if (!recentMedia.contains(mrl)) {
            recentMedia.addFirst(mrl);
            while (recentMedia.size() > MAX_RECENT_MEDIA_SIZE) {
                recentMedia.pollLast();
            }
        }
    }

    public List<String> recentMedia() {
        return new ArrayList<>(recentMedia);
    }

    public void clearRecentMedia() {
        recentMedia.clear();
    }


    public LanguageEnum languageEnum() {
        return languageEnum;
    }

    public void setLanguageEnum(LanguageEnum languageEnum) {
        this.languageEnum = languageEnum;
    }

    public OverlayType selectedOverlayType() {
        return selectedOverlayType;
    }

    public void selectedOverlayType(OverlayType selectedOverlayType) {
        this.selectedOverlayType = selectedOverlayType;
    }

    public void addSubtitleToCollection(String subtitle){
        int subtitileId = subtitleTracks.size();
        subtitleTracks.put(subtitileId, subtitle);
        setCurrentSubtitleId(subtitileId);
    }

    public String currentSubtitleFilePath(){
        return subtitleTracks.get(currentSubtitleId);
    }

}
