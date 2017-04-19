package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import com.giorgimode.subzero.view.action.Resource;
import uk.co.caprica.vlcj.player.MediaPlayer;

final class RateAction extends MediaPlayerAction {

    private final float rate;

    RateAction(Resource resource, MediaPlayer mediaPlayer, float rate) {
        super(resource, mediaPlayer);
        this.rate = rate;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getMediaPlayer().setRate(rate);
    }
}
