package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.event.MuteEvent;
import com.giorgimode.subzero.view.action.Resource;
import uk.co.caprica.vlcj.player.MediaPlayer;

final class MuteAction extends MediaPlayerAction {

    MuteAction(Resource resource, MediaPlayer mediaPlayer) {
        super(resource, mediaPlayer);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Application.application().post(MuteEvent.INSTANCE);
    }
}
