package com.giorgimode.subzero.view.main;

import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.view.OnDemandMenu;
import com.giorgimode.subzero.view.action.Resource;
import com.giorgimode.subzero.view.action.mediaplayer.ChapterAction;
import uk.co.caprica.vlcj.player.MediaPlayer;

// FIXME there's no reason this couldn't be another radiobutton menu... and show the current chapter - probably more useful that
// way even if not the same as VLC

final class ChapterMenu extends OnDemandMenu {

    ChapterMenu() {
        super(Resource.resource("menu.playback.item.chapter"), true);
    }

    @Override
    protected void onPrepareMenu(JMenu menu) {
        MediaPlayer mediaPlayer = Application.application().mediaPlayerComponent().getMediaPlayer();
        List<String> chapters = mediaPlayer.getChapterDescriptions();
        if (chapters != null && !chapters.isEmpty()) {
            int i = 0;
            for (String chapter : chapters) {
                JMenuItem menuItem = new JMenuItem(new ChapterAction(chapter, mediaPlayer, i++));
                menu.add(menuItem);
            }
        }
    }
}
