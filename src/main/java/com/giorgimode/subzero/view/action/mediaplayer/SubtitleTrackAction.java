package com.giorgimode.subzero.view.action.mediaplayer;

import com.giorgimode.subzero.event.SubtitleSwitchEvent;
import uk.co.caprica.vlcj.player.MediaPlayer;

import java.awt.event.ActionEvent;
import java.util.Map;

import static com.giorgimode.subzero.Application.application;

public final class SubtitleTrackAction extends MediaPlayerAction {

    private final int trackId;

    public SubtitleTrackAction(Map.Entry<Integer, String> entry, MediaPlayer mediaPlayer) {
        super("Track " + entry.getKey(), mediaPlayer);
        this.trackId = entry.getKey();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (trackId == 0) {
            mediaPlayer.setSpu(-1);
        }
        application().setCurrentSubtitleId(trackId);
        application().post(new SubtitleSwitchEvent(trackId));
    }
}
