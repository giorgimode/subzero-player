package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import uk.co.caprica.vlcj.player.MediaPlayer;

public final class VideoTrackAction extends MediaPlayerAction {

    private final int trackId;

    public VideoTrackAction(String name, MediaPlayer mediaPlayer, int trackId) {
        super(name, mediaPlayer);
        this.trackId = trackId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mediaPlayer.setVideoTrack(trackId);
    }
}
