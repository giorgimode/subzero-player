package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.event.SubtitleAddedEvent;
import uk.co.caprica.vlcj.player.MediaPlayer;

public final class SubtitleTrackAction extends MediaPlayerAction {

    private final int trackId;

    public SubtitleTrackAction(String name, MediaPlayer mediaPlayer, int trackId) {
        super(name, mediaPlayer);
        this.trackId = trackId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mediaPlayer.setSpu(trackId);
        Application.application().post(SubtitleAddedEvent.INSTANCE);
    }
}
