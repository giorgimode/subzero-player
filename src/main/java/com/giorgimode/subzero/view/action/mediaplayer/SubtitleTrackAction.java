package com.giorgimode.subzero.view.action.mediaplayer;

import com.giorgimode.subzero.event.SubtitleSwitchEvent;
import com.giorgimode.subzero.view.action.SubtitleTrack;
import uk.co.caprica.vlcj.player.MediaPlayer;

import java.awt.event.ActionEvent;

import static com.giorgimode.subzero.Application.application;

public final class SubtitleTrackAction extends MediaPlayerAction {

    private final int trackId;

    public SubtitleTrackAction(SubtitleTrack subtitleTrack, MediaPlayer mediaPlayer) {
        super(subtitleTrack.getDescription(), mediaPlayer);
        this.trackId = subtitleTrack.getId();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (trackId == 0) {
            getMediaPlayer().setSpu(-1);
        }
        application().setCurrentSubtitleId(trackId);
        application().post(new SubtitleSwitchEvent(trackId));
    }
}
