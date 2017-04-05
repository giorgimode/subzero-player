package com.giorgimode.subzero.view.main;

import static com.giorgimode.subzero.Application.application;
import static com.giorgimode.subzero.view.action.Resource.resource;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import com.giorgimode.subzero.view.OnDemandMenu;
import uk.co.caprica.vlcj.player.TrackDescription;
import com.giorgimode.subzero.view.action.mediaplayer.SubtitleTrackAction;

final class SubtitleTrackMenu extends OnDemandMenu {
    private static final String KEY_TRACK_DESCRIPTION = "track-description";
    SubtitleTrackMenu() {
        super(resource("menu.subtitle.item.track"), true);
    }

    @Override
    protected final void onPrepareMenu(JMenu menu) {
        ButtonGroup buttonGroup = new ButtonGroup();
        int selectedTrack = onGetSelectedTrack();
        Set<Map.Entry<Integer, String>> onGetTrackDescriptions = onGetTrackDescriptions();
        for (Map.Entry<Integer, String> entry : onGetTrackDescriptions) {
            String trackDescription = "Track " + entry.getKey();
            if (entry.getKey() == 0) {
                trackDescription = "Disabled";
            }
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(createAction(entry));
            menuItem.putClientProperty(KEY_TRACK_DESCRIPTION, trackDescription);
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
