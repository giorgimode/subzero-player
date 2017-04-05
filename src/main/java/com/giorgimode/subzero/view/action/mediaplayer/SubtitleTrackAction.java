package com.giorgimode.subzero.view.action.mediaplayer;

import com.giorgimode.subzero.event.SubtitleSwitchEvent;
import uk.co.caprica.vlcj.player.MediaPlayer;

import java.awt.event.ActionEvent;

import static com.giorgimode.subzero.Application.application;

public final class SubtitleTrackAction extends MediaPlayerAction {

    private final int trackId;

    public SubtitleTrackAction(String name, MediaPlayer mediaPlayer, int trackId) {
        super(name, mediaPlayer);
        this.trackId = trackId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        application().setCurrentSubtitleId(trackId);
        application().post(new SubtitleSwitchEvent(trackId));
    }
}
