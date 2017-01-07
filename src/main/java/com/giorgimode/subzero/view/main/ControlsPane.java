package com.giorgimode.subzero.view.main;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.event.ShowEffectsEvent;
import com.giorgimode.subzero.util.CustomSliderUI;
import com.giorgimode.subzero.view.BasePanel;
import com.google.common.eventbus.Subscribe;
import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.binding.LibVlcConst;
import com.giorgimode.subzero.event.PausedEvent;
import com.giorgimode.subzero.event.PlayingEvent;
import com.giorgimode.subzero.event.StoppedEvent;
import com.giorgimode.subzero.view.action.mediaplayer.MediaPlayerActions;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.SliderUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class ControlsPane extends BasePanel {

    private final Icon playIcon = newIcon("play");

    private final Icon pauseIcon = newIcon("pause");

    private final Icon previousIcon = newIcon("previous");

    private final Icon nextIcon = newIcon("next");

    private final Icon fullscreenIcon = newIcon("fullscreen");

    private final Icon extendedIcon = newIcon("extended");

    private final Icon snapshotIcon = newIcon("snapshot");

    private final Icon volumeHighIcon = newIcon("volume-high");

    private final Icon volumeMutedIcon = newIcon("volume-muted");

    private final JButton playPauseButton;

    private final JButton previousButton;

    private final JButton stopButton;

    private final JButton nextButton;

    private final JButton fullscreenButton;

    private final JButton extendedButton;

    private final JButton snapshotButton;

    private final JButton muteButton;

    private final JSlider volumeSlider;

    ControlsPane(MediaPlayerActions mediaPlayerActions) {
        playPauseButton = new BigButton();
        playPauseButton.setAction(mediaPlayerActions.playbackPlayAction());
        previousButton = new StandardButton();
        previousButton.setIcon(previousIcon);
        stopButton = new StandardButton();
        stopButton.setAction(mediaPlayerActions.playbackStopAction());
        nextButton = new StandardButton();
        nextButton.setIcon(nextIcon);
        fullscreenButton = new StandardButton();
        fullscreenButton.setIcon(fullscreenIcon);
        extendedButton = new StandardButton();
        extendedButton.setIcon(extendedIcon);
        snapshotButton = new StandardButton();
        snapshotButton.setAction(mediaPlayerActions.videoSnapshotAction());
        muteButton = new StandardButton();
        muteButton.setIcon(volumeHighIcon);
        volumeSlider = new JSlider();
        volumeSlider.setMinimum(LibVlcConst.MIN_VOLUME);
        volumeSlider.setMaximum(LibVlcConst.MAX_VOLUME);

        setLayout(new MigLayout("fill, insets 0 0 0 0", "[]12[]0[]0[]12[]0[]12[]push[]0[]", "[]"));

        add(playPauseButton);
        add(previousButton, "sg 1");
        add(stopButton, "sg 1");
        add(nextButton, "sg 1");

        add(fullscreenButton, "sg 1");
        add(extendedButton, "sg 1");

        add(snapshotButton, "sg 1");

        add(muteButton, "sg 1");
        add(volumeSlider, "wmax 100");

        SliderUI sliderUI = CustomSliderUI.getSliderUI();
        volumeSlider.setUI(sliderUI);

        volumeSlider.addChangeListener(e ->
                Application.application().mediaPlayerComponent().getMediaPlayer().setVolume(volumeSlider.getValue()));

        // FIXME really these should share common actions

        muteButton.addActionListener(e -> {
            if (Application.application().mediaPlayerComponent().getMediaPlayer().isMute()) {
                muteButton.setIcon(volumeHighIcon);
            } else {
                muteButton.setIcon(volumeMutedIcon);
            }
            Application.application().mediaPlayerComponent().getMediaPlayer().mute();
        });

        fullscreenButton.addActionListener(e -> Application.application().mediaPlayerComponent().getMediaPlayer().toggleFullScreen());

        extendedButton.addActionListener(e -> Application.application().post(ShowEffectsEvent.INSTANCE));
    }

    @Subscribe
    public void onPlaying(PlayingEvent event) {
        playPauseButton.setIcon(pauseIcon); // FIXME best way to do this? should be via the action really?
    }

    @Subscribe
    public void onPaused(PausedEvent event) {
        playPauseButton.setIcon(playIcon); // FIXME best way to do this? should be via the action really?
    }

    @Subscribe
    public void onStopped(StoppedEvent event) {
        playPauseButton.setIcon(playIcon); // FIXME best way to do this? should be via the action really?
    }

    private class BigButton extends JButton {

        private BigButton() {
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setHideActionText(true);
        }
    }

    private class StandardButton extends JButton {

        private StandardButton() {
            setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            setHideActionText(true);
        }
    }

    private Icon newIcon(String name) {
        return new ImageIcon(getClass().getResource("/icons/buttons/" + name + ".png"));
    }
}