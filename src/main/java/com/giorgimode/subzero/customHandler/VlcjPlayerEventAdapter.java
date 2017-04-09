package com.giorgimode.subzero.customHandler;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.event.PausedEvent;
import com.giorgimode.subzero.event.PlayingEvent;
import com.giorgimode.subzero.event.StoppedEvent;
import com.giorgimode.subzero.view.MouseMovementDetector;
import com.giorgimode.subzero.view.main.PositionPane;
import com.giorgimode.subzero.view.main.StatusBar;
import com.giorgimode.subzero.view.main.VideoContentPane;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.text.MessageFormat;

import static com.giorgimode.subzero.Application.application;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class VlcjPlayerEventAdapter extends MediaPlayerEventAdapter {
    private PositionPane positionPane;

    private StatusBar statusBar;

    private VideoContentPane videoContentPane;

    private MouseMovementDetector mouseMovementDetector;

    private Component parentComponent;

    private JFileChooser fileChooser;


    @Override
    public void playing(MediaPlayer mediaPlayer) {
        if (videoContentPane != null && mouseMovementDetector != null) {
            videoContentPane.showVideo();
            mouseMovementDetector.start();
            Application.application().post(PlayingEvent.INSTANCE);
        }
    }

    @Override
    public void paused(MediaPlayer mediaPlayer) {
        if (mouseMovementDetector != null) {
            mouseMovementDetector.stop();
            Application.application().post(PausedEvent.INSTANCE);
        }
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
        if (videoContentPane != null && mouseMovementDetector != null) {
            mouseMovementDetector.stop();
            videoContentPane.showDefault();
            Application.application().post(StoppedEvent.INSTANCE);
        }
    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        if (videoContentPane != null && mouseMovementDetector != null) {
            videoContentPane.showDefault();
            mouseMovementDetector.stop();
            Application.application().post(StoppedEvent.INSTANCE);
        }
        if (Application.application().isMediaMrlAdded() && mediaPlayer.subItemCount() == 0) {
            JOptionPane.showMessageDialog(parentComponent, "No media found on this URL", "Media URL", ERROR_MESSAGE);
            application().setMediaMrlAdded(false);
        }
    }

    @Override
    public void error(MediaPlayer mediaPlayer) {
        if (videoContentPane != null && mouseMovementDetector != null) {
            videoContentPane.showDefault();
            mouseMovementDetector.stop();
            Application.application().post(StoppedEvent.INSTANCE);
            String message = MessageFormat.format(Application.resources().getString("error.errorEncountered"), fileChooser.getSelectedFile().toString());
            JOptionPane.showMessageDialog(parentComponent, message, Application.resources().getString("dialog.errorEncountered"), ERROR_MESSAGE);
        }
    }

    @Override
    public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {
        SwingUtilities.invokeLater(() -> statusBar.setTitle(mediaPlayer.getMediaMeta().getTitle()));
    }

    @Override
    public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {
        SwingUtilities.invokeLater(() -> {
            positionPane.setDuration(newDuration);
            statusBar.setDuration(newDuration);
        });
    }

    @Override
    public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
        SwingUtilities.invokeLater(() -> {
            positionPane.setTime(newTime);
            statusBar.setTime(newTime);
        });
    }

    public VlcjPlayerEventAdapter setVideoContentPane(VideoContentPane videoContentPane) {
        this.videoContentPane = videoContentPane;
        return this;
    }

    public VlcjPlayerEventAdapter setMouseMovementDetector(MouseMovementDetector mouseMovementDetector) {
        this.mouseMovementDetector = mouseMovementDetector;
        return this;
    }

    public VlcjPlayerEventAdapter setStatusBar(StatusBar statusBar) {
        this.statusBar = statusBar;
        return this;
    }

    public VlcjPlayerEventAdapter setPositionPane(PositionPane positionPane) {
        this.positionPane = positionPane;
        return this;
    }

    public VlcjPlayerEventAdapter setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
        return this;
    }

    public VlcjPlayerEventAdapter setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
        return this;
    }
}
