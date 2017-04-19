package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import uk.co.caprica.vlcj.player.AudioDevice;
import uk.co.caprica.vlcj.player.MediaPlayer;

public final class AudioDeviceAction extends MediaPlayerAction {

    private final AudioDevice audioDevice;

    public AudioDeviceAction(AudioDevice audioDevice, MediaPlayer mediaPlayer) {
        super(audioDevice.getLongName(), mediaPlayer);
        this.audioDevice = audioDevice;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getMediaPlayer().setAudioOutputDevice(null, audioDevice.getDeviceId());
    }
}
