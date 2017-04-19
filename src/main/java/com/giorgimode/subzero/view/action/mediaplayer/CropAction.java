package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import com.giorgimode.subzero.view.action.Resource;
import uk.co.caprica.vlcj.player.MediaPlayer;

final class CropAction extends MediaPlayerAction {

    private final String cropGeometry;

    CropAction(Resource resource, MediaPlayer mediaPlayer, String cropGeometry) {
        super(resource, mediaPlayer);
        this.cropGeometry = cropGeometry;
    }

    CropAction(String name, MediaPlayer mediaPlayer, String cropGeometry) {
        super(name, mediaPlayer);
        this.cropGeometry = cropGeometry;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getMediaPlayer().setCropGeometry(cropGeometry);
    }
}
