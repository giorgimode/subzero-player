package testers;

import com.sun.awt.AWTUtilities;
import com.sun.jna.platform.WindowUtils;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

        Overlay overlay = new Overlay(f);

        f.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_F11:
                        mediaPlayer.enableOverlay(!mediaPlayer.overlayEnabled());
                        break;

                    case KeyEvent.VK_SPACE:
                        overlay.setText("mama mia");
                        mediaPlayer.enableOverlay(!mediaPlayer.overlayEnabled());
                        mediaPlayer.enableOverlay(!mediaPlayer.overlayEnabled());
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


        mediaPlayer.setOverlay(overlay);
        mediaPlayer.enableOverlay(true);

        mediaPlayer.playMedia(mrl);

        LibXUtil.setFullScreenWindow(f, true);
    }

    private class Overlay extends JWindow {

        private static final long serialVersionUID = 1L;
        private String text = "texti full description wow so goood it must by amazing what by";
        private boolean scrollBarEnabled;
        private boolean focused;

        public Overlay(Window owner) {
            super(owner, WindowUtils.getAlphaCompatibleGraphicsConfiguration());
            scrollBarEnabled = false;
            focused = false;
            AWTUtilities.setWindowOpaque(this, false);

            setLayout(null);
            final JTextArea textArea = new JTextArea(2, 2);
            String shortText = text.substring(0, text.length() / 3) + "...";
            textArea.setText(shortText);
            textArea.setFont(new Font("Sansserif", Font.BOLD, 18));
      /*      textArea.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("mouseClicked");
                    scrollBarEnabled = !scrollBarEnabled;
                    textArea.setText(textArea.getText().equals(text) ? shortText : text);
                    textArea.repaint();
                    //       System.out.println(textArea.getText());
                    //    textArea.repaint();
                }
            });*/
            JScrollPane scrollPane = new JScrollPane(textArea) {
                @Override
                public void paint(Graphics g) {
                    super.paint(g);

                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

                    g2.setPaint(new Color(255, 255, 255, 64));

                    g2.fillRect(0, 0, getWidth(), getHeight());

                    //   g2.setPaint(new Color(0, 128, 0, 0));
                    //   g2.setFont(new Font("Sansserif", Font.BOLD, 18));
                    //  if (!scrollBarEnabled) g2.drawString("Translucent", 16, 26);
                }
            };
            textArea.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                   /* System.out.println("mouseClicked" + Math.random());
                    scrollBarEnabled = !scrollBarEnabled;
                    textArea.setText(textArea.getText().equals(text) ? shortText : text);
                    textArea.repaint();
                    //       System.out.println(textArea.getText());
                    //    textArea.repaint();*/
                    focused = true;
                    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    System.out.println("mouseClicked");
                    scrollBarEnabled = true;
                    textArea.setText(text);
                    textArea.repaint();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!focused) {
                        System.out.println("mouseEntered");
                        scrollBarEnabled = true;
                        textArea.setText(text.substring(0, text.length()/2)+ "...");
                        textArea.repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!focused) {
                        System.out.println("mouseExited");
                        scrollBarEnabled = false;
                        textArea.setText(shortText);
                        textArea.repaint();
                    }
                }
            });
            textArea.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    focused = false;
                    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                    textArea.setText(shortText);
                }
            });
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.setOpaque(false);
            textArea.setOpaque(false);
            textArea.setEditable(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBounds(100, 200, 300, 40);
            textArea.setBounds(100, 200, 300, 40);
            scrollPane.setBackground(Color.cyan);
            add(scrollPane);
     /*       JButton b = new JButton("JButton");
            b.setBounds(150, 150, 100, 24);
            add(b);*/

           /* TranslucentComponent c = new TranslucentComponent();
            c.setOpaque(false);
            c.getViewport().setOpaque(false);
            c.setBounds(150, 200, 300, 40);
            add(c);*/
        }

    /*    @Override
        public void paint(Graphics g) {
            super.paint(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gp = new GradientPaint(180.0f, 280.0f, new Color(255, 255, 255, 255), 250.0f, 380.0f, new Color(255, 255, 0, 0));
            g2.setPaint(gp);
            for (int i = 0; i < 3; i++) {
                g2.drawOval(150, 280, 100, 100);
                g2.drawString(getText(), 116, 26);
                g2.fillOval(150, 280, 100, 100);
                g2.translate(120, 20);
            }
        }*/

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    private class TranslucentComponent extends JScrollPane {

        private static final long serialVersionUID = 1L;

        public TranslucentComponent() {
            setOpaque(false);
            getViewport().setOpaque(false);
            JTextArea textArea = new JTextArea(20, 80);
            textArea.setText("dfsfsdfsd");
            setViewportView(textArea);
            setBounds(100, 200, 300, 40);

        }

     /*   @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            g2.setPaint(new Color(255, 128, 128, 64));

          *//*  g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setPaint(new Color(0, 0, 0, 128));
            g2.setFont(new Font("Sansserif", Font.BOLD, 18));
            g2.drawString("Translucent", 16, 26);*//*
        }*/
    }
}