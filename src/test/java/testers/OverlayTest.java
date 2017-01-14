package testers;

import com.sun.awt.AWTUtilities;
import com.sun.jna.platform.WindowUtils;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;

import javax.swing.ImageIcon;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A test player demonstrating how to achieve a transparent overlay and translucent painting.
 * <p>
 * Press SPACE to pause the video play-back.
 * <p>
 * Press F11 to toggle the overlay.
 * <p>
 * If the video looks darker with the overlay enabled, then most likely you are using a compositing
 * window manager that is doing some fancy blending of the overlay window and the main application
 * window. You have to turn off those window effects.
 * <p>
 * Note that it is not possible to use this approach if you also want to use Full-Screen Exclusive
 * Mode. If you want to use an overlay and you need full- screen, then you have to emulate
 * full-screen by changing your window bounds rather than using FSEM.
 * <p>
 * This approach <em>does</em> work in full-screen mode if you use your desktop window manager to
 * put your application into full-screen rather than using the Java FSEM.
 * <p>
 * If you want to provide an overlay that dynamically updates, e.g. if you want some animation, then
 * your overlay should sub-class <code>JWindow</code> rather than <code>Window</code> since you will
 * get double-buffering and eliminate flickering. Since the overlay is transparent you must take
 * care to erase the overlay background properly.
 * <p>
 * Specify a single MRL to play on the command-line.
 */
public class OverlayTest extends VlcjTest {

    public static void main(final String[] args) throws Exception {
 /*       if(args.length != 1) {
            System.out.println("Specify a single MRL");
            System.exit(1);
        }*/
        String mediaURI = "D:\\Torrents\\Horrible.Bosses.2.2014.1080p.BluRay.x264-SPARKS\\Horrible.Bosses.2.2014.1080p.BluRay.x264-SPARKS.mkv";
        SwingUtilities.invokeLater(() -> new OverlayTest(mediaURI));
    }

    public OverlayTest(String mrl) {
        Frame f = new Frame("Test Player");
        f.setIconImage(new ImageIcon(getClass().getResource("/vlcj-logo.png")).getImage());
        f.setSize(800, 600);
        f.setBackground(Color.black);

        f.setLayout(new BorderLayout());
        Canvas vs = new Canvas();
        f.add(vs, BorderLayout.CENTER);
        f.setVisible(true);

        final MediaPlayerFactory factory = new MediaPlayerFactory();

        final EmbeddedMediaPlayer mediaPlayer = factory.newEmbeddedMediaPlayer();
        mediaPlayer.setVideoSurface(factory.newVideoSurface(vs));

        System.out.println("==============================MUTED==============================");
        mediaPlayer.mute();
        System.out.println("==============================MUTED==============================");

        List<String> strings = Arrays.asList("texti full description wow so goood it must by amazing what by", "wow it really worked???",
                "finally it also works");

        Overlay overlay = new Overlay(f, strings);

        f.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_F11:
                        mediaPlayer.enableOverlay(!mediaPlayer.overlayEnabled());
                        break;

                    case KeyEvent.VK_SPACE:
                        mediaPlayer.pause();
                        break;
                }
            }
        });

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayer.release();
                factory.release();
                System.exit(0);
            }
        });

        f.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println("WINDOW STATE CHANGED");
                overlay.updateLocationAll();
            }
        });

        mediaPlayer.setOverlay(overlay);
        mediaPlayer.enableOverlay(true);

        mediaPlayer.playMedia(mrl);

        LibXUtil.setFullScreenWindow(f, true);
    }

    private class Overlay extends JWindow {

        private static final long serialVersionUID = 1L;
        private final Window owner;
        List<SubtitlePanel> subtitlePanelList;

        public Overlay(Window owner, List<String> translatedList) {
            super(owner, WindowUtils.getAlphaCompatibleGraphicsConfiguration());
            this.owner = owner;
            subtitlePanelList = new ArrayList<>();
            AWTUtilities.setWindowOpaque(this, false);
            setLayout(null);
            translatedList.forEach(this::addSubtitlePanel);
            updateLocationAll();
        }


        private void addSubtitlePanel(String text) {
            SubtitlePanel subtitlePanel = new SubtitlePanel(text);
            subtitlePanelList.add(subtitlePanel);
            add(subtitlePanel);
        }

        public void updateLocation(SubtitlePanel subtitlePanel) {
            System.out.println(owner.getWidth() + "--xxxxxxxxxxxxxxxxxxxxx--" + owner.getHeight());
            int area51_below = owner.getHeight() / 4;
            int area51_left = owner.getWidth() / 35;
            int subtitlePanelHeight = owner.getHeight() / 20;

            // subtitlePanel.setLocation(owner.getX() + 50, owner.getY() + 100);
            subtitlePanel.setLocation(area51_left, owner.getHeight() - area51_below);
            subtitlePanel.setSize(new Dimension(owner.getWidth() - 3 * area51_left, subtitlePanelHeight));
        }

        public void updateLocationAll() {
            int spaceBetweenPanels = owner.getHeight() / 14;
            int subtitlePanelHeight = owner.getHeight() / 20;

            int area51_below = owner.getHeight() / 4;
            int area51_left = owner.getWidth() / 35;

            subtitlePanelList.get(subtitlePanelList.size() - 1).setLocation(area51_left, owner.getHeight() - area51_below);
            subtitlePanelList.get(subtitlePanelList.size() - 1).setSize(new Dimension(owner.getWidth() - 3 * area51_left, subtitlePanelHeight));
            System.out.println("Height1 = " + subtitlePanelList.get(subtitlePanelList.size() - 1).getHeight());
            if (subtitlePanelList.size() < 2) {
                return;
            }
            for (int i = subtitlePanelList.size() - 2; i >= 0; i--) {
                int previousSubtitleHeight = (int) subtitlePanelList.get(i + 1).getLocation().getY();
                subtitlePanelList.get(i).setLocation(area51_left, previousSubtitleHeight - spaceBetweenPanels - subtitlePanelHeight);
                subtitlePanelList.get(i).setSize(new Dimension(owner.getWidth() - 3 * area51_left, subtitlePanelHeight));
                System.out.println("Height" + i + ": " + subtitlePanelList.get(i).getLocation().getY());
            }


        }


    }
}