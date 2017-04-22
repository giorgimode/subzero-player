package com.giorgimode.subzero.view.main;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.view.OnDemandMenu;
import com.giorgimode.subzero.view.action.Resource;
import com.giorgimode.subzero.view.action.mediaplayer.ChapterAction;
import uk.co.caprica.vlcj.player.MediaPlayer;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import java.util.List;

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
            ButtonGroup buttonGroup = new ButtonGroup();
            for (String chapter : chapters) {
                JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(new ChapterAction(chapter, mediaPlayer, i++));
                buttonGroup.add(menuItem);
                menu.add(menuItem);
            }
        }
    }
}
