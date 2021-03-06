package com.giorgimode.subzero;

import com.giorgimode.subzero.translator.LanguagePackFrame;
import com.giorgimode.subzero.view.debug.DebugFrame;
import com.giorgimode.subzero.view.effects.EffectsFrame;
import com.giorgimode.subzero.view.main.MainFrame;
import com.giorgimode.subzero.view.messages.NativeLogFrame;
import com.sun.jna.NativeLibrary;
import lombok.extern.slf4j.Slf4j;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.log.NativeLog;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.streams.NativeStreams;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.giorgimode.subzero.Application.application;

/**
 * Application entry-point.
 */
@Slf4j
public final class VlcjPlayer {

    private static final NativeStreams NATIVE_STREAMS;

    // TODO Redirect the native output streams to files, useful since VLC can generate a lot of noisy native logs we don't care about
    // (on the other hand, if we don't look at the logs we might won't see errors)
    static {
//        if (RuntimeUtil.isNix()) {
//            NATIVE_STREAMS = new NativeStreams("stdout.log", "stderr.log");
//        }
//        else {
        NATIVE_STREAMS = null;
//        }
    }

    private final JFrame mainFrame;

    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final JFrame messagesFrame;

    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final JFrame effectsFrame;

    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final JFrame debugFrame;

    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final JFrame languagePackFrame;

    @SuppressWarnings("FieldCanBeLocal")
    private final NativeLog nativeLog;
    private static final String NATIVE_LIBRARY_SEARCH_PATH = "./lib";

    public static void main(String[] args) throws InterruptedException {
        // This will locate LibVLC for the vast majority of cases
        //  new NativeDiscovery().discover();
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        //   NativeLibrary.addSearchPath("libvlc", NATIVE_LIBRARY_SEARCH_PATH);
        //    System.setProperty("VLC_PLUGIN_PATH", NATIVE_LIBRARY_SEARCH_PATH);

        setLookAndFeel();

        SwingUtilities.invokeLater(() -> new VlcjPlayer().start());
    }

    private static void setLookAndFeel() {
        String lookAndFeelClassName;
        if (RuntimeUtil.isNix()) {
            lookAndFeelClassName = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        } else {
            lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
        }
        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
        } catch (Exception e) {
            // Silently fail, it doesn't matter
        }
    }

    private VlcjPlayer() {
        EmbeddedMediaPlayerComponent mediaPlayerComponent = application().mediaPlayerComponent();

        mainFrame = new MainFrame();
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                log.debug("window closing");
                application().dispose();
                if (NATIVE_STREAMS != null) {
                    NATIVE_STREAMS.release();
                }
            }
        });
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        EmbeddedMediaPlayer embeddedMediaPlayer = mediaPlayerComponent.getMediaPlayer();
        embeddedMediaPlayer.setFullScreenStrategy(new VlcjPlayerFullScreenStrategy(mainFrame));

        nativeLog = mediaPlayerComponent.getMediaPlayerFactory().newLog();

        messagesFrame = new NativeLogFrame(nativeLog);
        effectsFrame = new EffectsFrame();
        debugFrame = new DebugFrame();
        languagePackFrame = new LanguagePackFrame(mainFrame);
    }

    private void start() {
        mainFrame.setVisible(true);
    }
}
