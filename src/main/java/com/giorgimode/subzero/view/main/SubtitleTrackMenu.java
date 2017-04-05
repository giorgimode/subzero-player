package com.giorgimode.subzero.view.main;

import static com.giorgimode.subzero.Application.application;
import static com.giorgimode.subzero.view.action.Resource.resource;

import java.util.List;

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
        List<TrackDescription> onGetTrackDescriptions = onGetTrackDescriptions();
        for (int i = 0; i < onGetTrackDescriptions.size(); i++) {
            TrackDescription trackDescription = onGetTrackDescriptions.get(i);
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(createAction(trackDescription, i));
            menuItem.putClientProperty(KEY_TRACK_DESCRIPTION, trackDescription);
            buttonGroup.add(menuItem);
            menu.add(menuItem);
            if (selectedTrack == i) {
                menuItem.setSelected(true);
            }
        }
    }

    private Action createAction(TrackDescription trackDescription, int id) {
        return new SubtitleTrackAction(trackDescription.description(), application().mediaPlayerComponent().getMediaPlayer(), id);
    }

    private List<TrackDescription> onGetTrackDescriptions() {
        return application().mediaPlayerComponent().getMediaPlayer().getSpuDescriptions();
    }

    private int onGetSelectedTrack() {
        return application().getCurrentSubtitleId();
    }
}
