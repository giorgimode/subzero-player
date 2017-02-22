package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import com.giorgimode.subzero.view.action.Resource;
import uk.co.caprica.vlcj.player.MediaPlayer;

final class AspectRatioAction extends MediaPlayerAction {

    private final String aspectRatio;

    AspectRatioAction(Resource resource, MediaPlayer mediaPlayer, String aspectRatio) {
        super(resource, mediaPlayer);
        this.aspectRatio = aspectRatio;
    }

    AspectRatioAction(String name, MediaPlayer mediaPlayer, String aspectRatio) {
        super(name, mediaPlayer);
        this.aspectRatio = aspectRatio;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mediaPlayer.setAspectRatio(aspectRatio);
    }
}
