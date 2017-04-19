package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import com.giorgimode.subzero.view.action.Resource;
import uk.co.caprica.vlcj.player.MediaPlayer;

final class SkipAction extends MediaPlayerAction {

    private final long delta;

    SkipAction(Resource resource, MediaPlayer mediaPlayer, long delta) {
        super(resource, mediaPlayer);
        this.delta = delta;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getMediaPlayer().skip(delta);
    }
}
