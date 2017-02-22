package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import com.giorgimode.subzero.view.action.Resource;
import uk.co.caprica.vlcj.player.MediaPlayer;

final class ZoomAction extends MediaPlayerAction {

    private final float zoom;

    ZoomAction(Resource resource, MediaPlayer mediaPlayer, float zoom) {
        super(resource, mediaPlayer);
        this.zoom = zoom;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mediaPlayer.setScale(zoom);
    }
}
