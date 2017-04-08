package com.giorgimode.subzero.view.main;

import com.giorgimode.subzero.view.OnDemandMenu;
import com.giorgimode.subzero.view.action.SubtitleTrack;
import com.giorgimode.subzero.view.action.mediaplayer.SubtitleTrackAction;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import java.util.List;

import static com.giorgimode.subzero.Application.application;
import static com.giorgimode.subzero.view.action.Resource.resource;

final class SubtitleTrackMenu extends OnDemandMenu {
    SubtitleTrackMenu() {
        super(resource("menu.subtitle.item.track"), true);
    }

    @Override
    protected final void onPrepareMenu(JMenu menu) {
        ButtonGroup buttonGroup = new ButtonGroup();
        int selectedTrack = onGetSelectedTrack();
        List<SubtitleTrack> onGetTrackDescriptions = onGetTrackDescriptions();
        if (onGetTrackDescriptions.size() < 2) {
            return;
        }
        for (SubtitleTrack subtitleTrack : onGetTrackDescriptions) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(createAction(subtitleTrack));
            buttonGroup.add(menuItem);
            menu.add(menuItem);
            if (selectedTrack == subtitleTrack.getId()) {
                menuItem.setSelected(true);
            }
        }
    }

    private Action createAction(SubtitleTrack entry) {
        return new SubtitleTrackAction(entry, application().mediaPlayerComponent().getMediaPlayer());
    }

    private List<SubtitleTrack> onGetTrackDescriptions() {
        return application().getSubtitleTracks();
    }

    private int onGetSelectedTrack() {
        return application().getCurrentSubtitleId();
    }
}
