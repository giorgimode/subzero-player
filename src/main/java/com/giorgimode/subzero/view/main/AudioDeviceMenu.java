package com.giorgimode.subzero.view.main;

import java.awt.Component;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.view.OnDemandMenu;
import com.giorgimode.subzero.view.action.Resource;
import uk.co.caprica.vlcj.player.AudioDevice;
import uk.co.caprica.vlcj.player.MediaPlayer;
import com.giorgimode.subzero.view.action.mediaplayer.AudioDeviceAction;

public final class AudioDeviceMenu extends OnDemandMenu {

    private static final String KEY_AUDIO_DEVICE = "audio-device";

    public AudioDeviceMenu() {
        super(Resource.resource("menu.audio.item.device"));
    }

    @Override
    protected void onCreateMenu(JMenu menu) {
        MediaPlayer mediaPlayer = Application.application().mediaPlayerComponent().getMediaPlayer();
        ButtonGroup buttonGroup = new ButtonGroup();
        for (AudioDevice audioDevice : mediaPlayer.getAudioOutputDevices()) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(new AudioDeviceAction(audioDevice, mediaPlayer));
            menuItem.putClientProperty(KEY_AUDIO_DEVICE, audioDevice);
            buttonGroup.add(menuItem);
            menu.add(menuItem);
        }
    }

    @Override
    protected void onPrepareMenu(JMenu menu) {
        String audioDeviceId = Application.application().mediaPlayerComponent().getMediaPlayer().getAudioOutputDevice();
        for (Component c : menu.getMenuComponents()) {
            JRadioButtonMenuItem menuItem = (JRadioButtonMenuItem) c;
            AudioDevice audioDevice = (AudioDevice) menuItem.getClientProperty(KEY_AUDIO_DEVICE);
            if (audioDevice.getDeviceId().equals(audioDeviceId)) {
                menuItem.setSelected(true);
                break;
            }
        }
    }
}
