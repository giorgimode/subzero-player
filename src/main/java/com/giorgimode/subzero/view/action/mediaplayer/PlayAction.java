package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import uk.co.caprica.vlcj.player.MediaPlayer;
import com.giorgimode.subzero.view.action.Resource;

import javax.swing.Action;

final class PlayAction extends MediaPlayerAction {

    PlayAction(Resource resource, MediaPlayer mediaPlayer) {
        super(resource, mediaPlayer);
        putValue(Action.NAME             , resource.name());
        putValue(Action.MNEMONIC_KEY     , resource.mnemonic  ());
        putValue(Action.ACCELERATOR_KEY  , resource.shortcut  ());
        putValue(Action.SHORT_DESCRIPTION, resource.tooltip   ());
        putValue(Action.SMALL_ICON       , resource.menuIcon  ());
        putValue(Action.LARGE_ICON_KEY   , resource.buttonIcon());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.play();
        }
        else {
            mediaPlayer.pause();
        }
    }
}
