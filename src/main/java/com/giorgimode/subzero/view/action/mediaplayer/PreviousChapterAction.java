package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import com.giorgimode.subzero.view.action.Resource;
import uk.co.caprica.vlcj.player.MediaPlayer;

final class PreviousChapterAction extends MediaPlayerAction {

    PreviousChapterAction(Resource resource, MediaPlayer mediaPlayer) {
        super(resource, mediaPlayer);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mediaPlayer.previousChapter();
    }
}
