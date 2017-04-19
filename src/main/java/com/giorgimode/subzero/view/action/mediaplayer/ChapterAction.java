package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import uk.co.caprica.vlcj.player.MediaPlayer;

public final class ChapterAction extends MediaPlayerAction {

    private final int chapter;

    public ChapterAction(String name, MediaPlayer mediaPlayer, int chapter) {
        super(name, mediaPlayer);
        this.chapter = chapter;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getMediaPlayer().setChapter(chapter);
    }
}
