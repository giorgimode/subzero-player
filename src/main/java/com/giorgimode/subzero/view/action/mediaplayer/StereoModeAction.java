package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import com.giorgimode.subzero.view.action.Resource;
import uk.co.caprica.vlcj.player.MediaPlayer;

final class StereoModeAction extends MediaPlayerAction {

    private final Object mode;

    StereoModeAction(Resource resource, MediaPlayer mediaPlayer, Object mode) {
        super(resource, mediaPlayer);
        this.mode = mode;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // FIXME
        // setChannel?
    }
}
