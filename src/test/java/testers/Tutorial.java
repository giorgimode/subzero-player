package testers;

import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.internal.libvlc_marquee_position_e;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static uk.co.caprica.vlcj.player.Marquee.marquee;

public class Tutorial {

    private final JFrame frame;
    private final JPanel jpanel;
    private final JPanel controlsPanel;

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private static final String NATIVE_LIBRARY_SEARCH_PATH = "./lib";


    public static void main(final String[] args) {
      //  new NativeDiscovery().discover();
      NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Tutorial(args);
            }
        });
    }

    public Tutorial(String[] args)  {
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        jpanel = new JPanel();
        jpanel.setLayout(new BorderLayout());
        jpanel.add(mediaPlayerComponent.getVideoSurface(), BorderLayout.CENTER);

        controlsPanel = new JPanel();
        JButton applyButton2 = new JButton("Apply");
        applyButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
               /* marquee()
                        .text("my text")
                        //     .location(x, y)
                        .position(libvlc_marquee_position_e.centre)
                        .opacity(255)
                        .enable(true)
                        .apply(mediaPlayerComponent.getMediaPlayer());*/
            }
        });
        controlsPanel.add(applyButton2);

        jpanel.add(controlsPanel, BorderLayout.SOUTH);

        frame = new JFrame("vlcj Marquee Test");
        frame.setContentPane(jpanel);
        frame.setSize(1300, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release(true);
            }
        });

        //  frame.setContentPane(mediaPlayerComponent);


        frame.setVisible(true);
        mediaPlayerComponent.getMediaPlayer().playMedia("D:\\Torrents\\Horrible.Bosses.2.2014.1080p.BluRay.x264-SPARKS\\Horrible.Bosses.2.2014.1080p.BluRay.x264-SPARKS.mkv");

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        marquee()
                .text("my text")
                //     .location(x, y)
                .position(libvlc_marquee_position_e.centre)
                .opacity(255)
                .enable(true)
                .apply(mediaPlayerComponent.getMediaPlayer());
    }

    public class ControlsPanel extends JPanel {


        JTextField textTextField = new JTextField(20);

        JButton applyButton = new JButton("Apply");

        public ControlsPanel() {
            setBorder(new TitledBorder("Marquee Controls"));

            JSeparator separator;

            add(new JLabel("Text"));
            add(textTextField);
            add(applyButton);

            applyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    marquee()
                            .text(textTextField.getText())
                       //     .location(x, y)
                            .position(libvlc_marquee_position_e.centre)
                            .opacity(255)
                            .enable(true)
                            .apply(mediaPlayerComponent.getMediaPlayer());
                }
            });

        }
    }
}

