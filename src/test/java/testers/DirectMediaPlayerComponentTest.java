package testers;

import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Basic test showing how to use a direct media player component.
 * <p>
 * This test shows how to render the video buffer into a {@link JComponent} via a
 * {@link BufferedImage} inside a {@link RenderCallbackAdapter}.
 * <p>
 * Some applications may like to access the native video buffer directly by
 * overriding {@link DirectMediaPlayerComponent-display(com.sun.jna.Memory)} or
 * providing an implementation of a {@link RenderCallback} via {@link DirectMediaPlayerComponent#onGetRenderCallback}.
 * <p>
 * This test also shows how to paint a lightweight overlay on top of the video.
 */
@SuppressWarnings("serial")
public class DirectMediaPlayerComponentTest extends VlcjTest {

    /**
     * Media player component.
     */
    private final DirectMediaPlayerComponent mediaPlayerComponent;

    /**
     *
     */
    private final int width = 720;

    /**
     *
     */
    private final int height = 480;

    /**
     *
     */
    private final JPanel panel;

    /**
     *
     */
    private final BufferedImage image;

    /**
     * Application entry point.
     *
     * @param args
     */
    public static void main(String[] args) {
/*        if(args.length != 1) {
            System.out.println("Specify an mrl");
            System.exit(1);
        }*/

        final String mrl = "D:\\Torrents\\Horrible.Bosses.2.2014.1080p.BluRay.x264-SPARKS\\Horrible.Bosses.2.2014.1080p.BluRay.x264-SPARKS.mkv";

        setLookAndFeel();

        SwingUtilities.invokeLater(() -> new DirectMediaPlayerComponentTest().start(mrl));
    }

    /**
     * Create a new test.
     */
    private DirectMediaPlayerComponentTest() {
        JFrame frame = new JFrame("vlcj Direct Media Player Component Test");

        final Font font = new Font("Sansserif", Font.BOLD, 36);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.black);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.drawImage(image, null, 0, 0);
                g2.setColor(Color.white);
                g2.setFont(font);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.drawString("lightweight overlay", 100, 200);
            }
        };
        panel.setBackground(Color.black);
        panel.setOpaque(true);
        panel.setPreferredSize(new Dimension(width, height));
        panel.setMinimumSize(new Dimension(width, height));
        panel.setMaximumSize(new Dimension(width, height));

        image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(width, height);

        BufferFormatCallback bufferFormatCallback = new BufferFormatCallback() {
            @Override
            public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
                return new RV32BufferFormat(width, height);
            }
        };

        mediaPlayerComponent = new DirectMediaPlayerComponent(bufferFormatCallback) {
            @Override
            protected RenderCallback onGetRenderCallback() {
                return new TestRenderCallbackAdapter();
            }
        };

        frame.setContentPane(panel);

        frame.setLocation(100, 100);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Start playing a movie.
     *
     * @param mrl mrl
     */
    private void start(String mrl) {
        // One line of vlcj code to play the media...
        mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
    }

    private class TestRenderCallbackAdapter extends RenderCallbackAdapter {

        private TestRenderCallbackAdapter() {
            super(new int[width * height]);
        }

        @Override
        protected void onDisplay(DirectMediaPlayer mediaPlayer, int[] rgbBuffer) {
            // Simply copy buffer to the image and repaint
            image.setRGB(0, 0, width, height, rgbBuffer, 0, width);
            panel.repaint();
        }
    }
}