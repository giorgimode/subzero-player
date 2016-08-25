package uk.co.caprica.vlcjplayer.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcjplayer.event.SubtitleAddedEvent;

import static uk.co.caprica.vlcjplayer.Application.application;

public final class SubtitleTrackAction extends MediaPlayerAction {

    private final int trackId;

    public SubtitleTrackAction(String name, MediaPlayer mediaPlayer, int trackId) {
        super(name, mediaPlayer);
        this.trackId = trackId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mediaPlayer.setSpu(trackId);
        application().post(SubtitleAddedEvent.INSTANCE);
    }
}
