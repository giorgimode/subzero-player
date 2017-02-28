package com.giorgimode.subzero.view.action.mediaplayer;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.event.SubtitleAddedEvent;
import uk.co.caprica.vlcj.player.MediaPlayer;

import java.awt.event.ActionEvent;

public final class LanguagePackAction extends MediaPlayerAction {

    public LanguagePackAction(String name, MediaPlayer mediaPlayer) {
        super(name, mediaPlayer);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        mediaPlayer.setSpu(trackId);
        Application.application().post(SubtitleAddedEvent.INSTANCE);
    }
}
