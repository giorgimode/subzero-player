package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import uk.co.caprica.vlcj.player.MediaPlayer;

public final class AudioTrackAction extends MediaPlayerAction {

    private final int trackId;

    public AudioTrackAction(String name, MediaPlayer mediaPlayer, int trackId) {
        super(name, mediaPlayer);
        this.trackId = trackId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getMediaPlayer().setAudioTrack(trackId);
    }
}
