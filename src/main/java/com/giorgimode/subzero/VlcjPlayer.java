package com.giorgimode.subzero;

import com.giorgimode.subzero.translator.LanguagePackFrame;
import com.giorgimode.subzero.view.debug.DebugFrame;
import com.giorgimode.subzero.view.messages.NativeLogFrame;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.log.NativeLog;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.streams.NativeStreams;
import com.giorgimode.subzero.event.ShutdownEvent;
import com.giorgimode.subzero.view.effects.EffectsFrame;
import com.giorgimode.subzero.view.main.MainFrame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.giorgimode.subzero.Application.application;

/**
 * Application entry-point.
 */
public class VlcjPlayer {

    private static final NativeStreams nativeStreams;

    // Redirect the native output streams to files, useful since VLC can generate a lot of noisy native logs we don't care about
    // (on the other hand, if we don't look at the logs we might won't see errors)
    static {
//        if (RuntimeUtil.isNix()) {
//            nativeStreams = new NativeStreams("stdout.log", "stderr.log");
//        }
//        else {
            nativeStreams = null;
//        }
    }

    private final JFrame mainFrame;

    @SuppressWarnings("unused")
    private final JFrame messagesFrame;

    @SuppressWarnings("unused")
    private final JFrame effectsFrame;

    @SuppressWarnings("unused")
    private final JFrame debugFrame;

    @SuppressWarnings("unused")
    private final JFrame languagePackFrame;

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
        }
        else {
            lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
        }
        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
        }
        catch(Exception e) {
            // Silently fail, it doesn't matter
        }
    }

    public VlcjPlayer() {
        EmbeddedMediaPlayerComponent mediaPlayerComponent = application().mediaPlayerComponent();

        mainFrame = new MainFrame();
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.getMediaPlayer().stop();
                mediaPlayerComponent.release();
                if (nativeStreams != null) {
                    nativeStreams.release();
                }
                application().post(ShutdownEvent.INSTANCE);
            }
        });
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
