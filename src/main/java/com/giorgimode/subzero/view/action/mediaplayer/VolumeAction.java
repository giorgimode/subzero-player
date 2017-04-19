package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.event.VolumeEvent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import com.giorgimode.subzero.view.action.Resource;

final class VolumeAction extends MediaPlayerAction {

    private final int delta;

    VolumeAction(Resource resource, MediaPlayer mediaPlayer, int delta) {
        super(resource, mediaPlayer);
        this.delta = delta;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getMediaPlayer().setVolume(getMediaPlayer().getVolume() + delta);
        Application.application().post(VolumeEvent.INSTANCE);
    }
}
