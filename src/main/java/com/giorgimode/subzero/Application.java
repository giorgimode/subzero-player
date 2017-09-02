package com.giorgimode.subzero;

import com.giorgimode.dictionary.LanguageEnum;
import com.giorgimode.subzero.event.ShutdownEvent;
import com.giorgimode.subzero.event.TickEvent;
import com.giorgimode.subzero.translator.OverlayType;
import com.giorgimode.subzero.view.action.SubtitleTrack;
import com.giorgimode.subzero.view.action.mediaplayer.MediaPlayerActions;
import com.google.common.eventbus.EventBus;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import javax.swing.SwingUtilities;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Global application state.
 */
@Slf4j
public final class Application {

    private static final String RESOURCE_BUNDLE_BASE_NAME = "strings/vlcj-player";

    private static final ResourceBundle RESOURCE_BUNDLE       = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME);
    private static final int            MAX_RECENT_MEDIA_SIZE = 10;

    private final EventBus                     eventBus;
    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private final MediaPlayerActions           mediaPlayerActions;
    private final Deque<String> recentMedia = new ArrayDeque<>(MAX_RECENT_MEDIA_SIZE);
    @Setter
    private LanguageEnum        languageEnum;
    private OverlayType         selectedOverlayType;
    @Getter
    private List<SubtitleTrack> subtitleTracks;
    @Getter
    @Setter
    private int                 currentSubtitleId;
    @Getter
    @Setter
    private boolean             mediaMrlAdded;

    private static final class ApplicationHolder {
        private static final Application INSTANCE = new Application();
    }

    public static Application application() {
        return ApplicationHolder.INSTANCE;
    }

    public static ResourceBundle resources() {
        return RESOURCE_BUNDLE;
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
        ScheduledExecutorService tickService = Executors.newSingleThreadScheduledExecutor();
        tickService.scheduleWithFixedDelay(() -> eventBus.post(TickEvent.INSTANCE), 0, 1000, TimeUnit.MILLISECONDS);
        subtitleTracks = new ArrayList<>();
        subtitleTracks.add(new SubtitleTrack(0, "Disabled"));
    }

    public void subscribe(Object subscriber) {
        eventBus.register(subscriber);
    }

    public void post(Object event) {
        log.debug("start post {}", event.getClass().getName());
        // Events are always posted and processed on the Swing Event Dispatch thread
        if (SwingUtilities.isEventDispatchThread()) {
            eventBus.post(event);
        } else {
            SwingUtilities.invokeLater(() -> eventBus.post(event));
        }
        log.debug("end post {}", event.getClass().getName());
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

    public OverlayType selectedOverlayType() {
        return selectedOverlayType;
    }

    public void selectedOverlayType(OverlayType selectedOverlayType) {
        this.selectedOverlayType = selectedOverlayType;
    }

    public void addSubtitleToCollection(String subtitle) {
        int subtitleId = subtitleTracks.size();
        subtitleTracks.add(new SubtitleTrack(subtitleId, subtitle));
        setCurrentSubtitleId(subtitleId);

    }

    public String currentSubtitleFilePath() {
        return subtitleTracks.get(currentSubtitleId).getSubtitlePath();
    }

    public void dispose() {
        eventBus.unregister(TickEvent.INSTANCE);
        application().post(ShutdownEvent.INSTANCE);
        mediaPlayerComponent.getMediaPlayer().stop();
        mediaPlayerComponent.release();
        System.exit(0);
    }
}
