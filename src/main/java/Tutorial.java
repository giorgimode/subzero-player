import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Tutorial {

    private final JFrame frame;

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

    public Tutorial(String[] args) {
        frame = new JFrame("My First Media Player");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        frame.setContentPane(mediaPlayerComponent);
        frame.setVisible(true);
        mediaPlayerComponent.getMediaPlayer().playMedia(args[0]);
    }
}