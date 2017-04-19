package com.giorgimode.subzero.view.action.mediaplayer;

import com.giorgimode.subzero.view.action.Resource;
import uk.co.caprica.vlcj.player.MediaPlayer;

import java.awt.event.ActionEvent;

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
        getMediaPlayer().setAspectRatio(aspectRatio);
    }
}
