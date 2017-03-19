package com.giorgimode.subzero.view.main;

import com.giorgimode.subzero.event.AfterExitFullScreenEvent;
import com.giorgimode.subzero.event.BeforeEnterFullScreenEvent;
import com.giorgimode.subzero.event.PausedEvent;
import com.giorgimode.subzero.event.PlayingEvent;
import com.giorgimode.subzero.event.SnapshotImageEvent;
import com.giorgimode.subzero.view.effects.overlay.TranslationOverlay;
import com.giorgimode.subzero.view.snapshot.SnapshotView;
import com.google.common.eventbus.Subscribe;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import static com.giorgimode.subzero.Application.application;
import static com.giorgimode.subzero.view.main.MainFrame.ACTION_EXIT_FULLSCREEN;

/**
 * Created by modeg on 2/22/2017.
 */
public class MainEventHandler {
    private MainFrame mainFrame;
    private static EmbeddedMediaPlayerComponent mediaPlayerComponent = application().mediaPlayerComponent();
    private static EmbeddedMediaPlayer mediaPlayer = mediaPlayerComponent.getMediaPlayer();
    private static final KeyStroke KEYSTROKE_ESCAPE = KeyStroke.getKeyStroke("ESCAPE");
    private static final KeyStroke KEYSTROKE_TOGGLE_FULLSCREEN = KeyStroke.getKeyStroke("F11");

    MainEventHandler(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        application().subscribe(this);
    }

    @Subscribe
    public void onBeforeEnterFullScreen(BeforeEnterFullScreenEvent event) {
        mainFrame.getPlayerMenuBar().setVisible(false);
        mainFrame.getBottomPane().setVisible(false);
        // As the menu is now hidden, the shortcut will not work, so register a temporary key-binding
        registerEscapeBinding();
    }

    @Subscribe
    public void onAfterExitFullScreen(AfterExitFullScreenEvent event) {
        deregisterEscapeBinding();
        mainFrame.getPlayerMenuBar().setVisible(true);
        mainFrame.getBottomPane().setVisible(true);
    }

    @Subscribe
    public void onSnapshotImage(SnapshotImageEvent event) {
        new SnapshotView(event.image());
    }

    @Subscribe
    public void onPaused(PausedEvent event) {
        mainFrame.getBottomPane().setVisible(true);
    }

    @Subscribe
    public void onPlaying(PlayingEvent event) {
        if (mediaPlayer.isFullScreen()) {
            mainFrame.getBottomPane().setVisible(false);
        }
        TranslationOverlay overlay = (TranslationOverlay) mediaPlayer.getOverlay();
        overlay.clean();
        mediaPlayerComponent.getVideoSurface().requestFocusInWindow();
    }

    private void registerEscapeBinding() {
        getInputMap().put(KEYSTROKE_ESCAPE, ACTION_EXIT_FULLSCREEN);
        getInputMap().put(KEYSTROKE_TOGGLE_FULLSCREEN, ACTION_EXIT_FULLSCREEN);
    }

    private void deregisterEscapeBinding() {
        getInputMap().remove(KEYSTROKE_ESCAPE);
        getInputMap().remove(KEYSTROKE_TOGGLE_FULLSCREEN);
    }

    private InputMap getInputMap() {
        JComponent c = (JComponent) mainFrame.getContentPane();
        return c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
}
