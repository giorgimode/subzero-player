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
import java.awt.Font;
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
        f.setSize(1200, 800);
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

        List<String> strings = Arrays.asList(
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting",
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English",
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

        public void updateLocationAll() {
            int spaceBetweenPanels = owner.getHeight() / 18;
            int subtitlePanelHeight = owner.getHeight() / 25;

            int area51_below = owner.getHeight() / 4;
            int area51_left = owner.getWidth() / 35;

            subtitlePanelList.get(subtitlePanelList.size() - 1).setLocation(area51_left, owner.getHeight() - area51_below);
            subtitlePanelList.get(subtitlePanelList.size() - 1).setSize(new Dimension(owner.getWidth() - 3 * area51_left, subtitlePanelHeight));
            updateFont(subtitlePanelList.get(subtitlePanelList.size() - 1));

            System.out.println("Height1 = " + subtitlePanelList.get(subtitlePanelList.size() - 1).getHeight());
            if (subtitlePanelList.size() < 2) {
                return;
            }
            for (int i = subtitlePanelList.size() - 2; i >= 0; i--) {
                int previousSubtitleHeight = (int) subtitlePanelList.get(i + 1).getLocation().getY();
                subtitlePanelList.get(i).setLocation(area51_left, previousSubtitleHeight - spaceBetweenPanels - subtitlePanelHeight);
                subtitlePanelList.get(i).setSize(new Dimension(owner.getWidth() - 3 * area51_left, subtitlePanelHeight));
                updateFont(subtitlePanelList.get(i));
            }
        }

        private void updateFont(SubtitlePanel subtitlePanel) {
            Font oldfont = subtitlePanel.getTextArea().getFont();
            subtitlePanel.getTextArea().setFont(new Font(oldfont.getName(), oldfont.getStyle(), owner.getHeight() / 50));
        }
    }
}