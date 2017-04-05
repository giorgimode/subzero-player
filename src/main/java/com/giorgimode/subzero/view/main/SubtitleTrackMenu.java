package com.giorgimode.subzero.view.main;

import com.giorgimode.subzero.view.OnDemandMenu;
import com.giorgimode.subzero.view.action.mediaplayer.SubtitleTrackAction;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import java.util.Map;
import java.util.Set;

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
        Set<Map.Entry<Integer, String>> onGetTrackDescriptions = onGetTrackDescriptions();
        if (onGetTrackDescriptions.size() < 2) {
            return;
        }
        for (Map.Entry<Integer, String> entry : onGetTrackDescriptions) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(createAction(entry));
            buttonGroup.add(menuItem);
            menu.add(menuItem);
            if (selectedTrack == entry.getKey()) {
                menuItem.setSelected(true);
            }
        }
    }

    private Action createAction(Map.Entry<Integer, String> entry) {
        return new SubtitleTrackAction(entry, application().mediaPlayerComponent().getMediaPlayer());
    }

    private Set<Map.Entry<Integer, String>> onGetTrackDescriptions() {
        return application().getSubtitleTracks().entrySet();
    }

    private int onGetSelectedTrack() {
        return application().getCurrentSubtitleId();
    }
}
