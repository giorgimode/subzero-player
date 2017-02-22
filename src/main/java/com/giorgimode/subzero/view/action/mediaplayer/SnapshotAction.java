package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.event.SnapshotImageEvent;
import com.giorgimode.subzero.view.action.Resource;
import uk.co.caprica.vlcj.player.MediaPlayer;

final class SnapshotAction extends MediaPlayerAction {

    SnapshotAction(Resource resource, MediaPlayer mediaPlayer) {
        super(resource, mediaPlayer);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BufferedImage image = mediaPlayer.getSnapshot();
        if (image != null) {
            Application.application().post(new SnapshotImageEvent(image));
        }
    }
}
