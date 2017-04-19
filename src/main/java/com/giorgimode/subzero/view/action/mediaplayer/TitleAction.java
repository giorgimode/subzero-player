package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import uk.co.caprica.vlcj.player.MediaPlayer;

public final class TitleAction extends MediaPlayerAction {

    private final int titleId;

    public TitleAction(String name, MediaPlayer mediaPlayer, int titleId) {
        super(name, mediaPlayer);
        this.titleId = titleId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getMediaPlayer().setTitle(titleId);
    }
}
