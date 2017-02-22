package com.giorgimode.subzero.view.main;

import java.util.List;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import uk.co.caprica.vlcj.player.TrackDescription;
import com.giorgimode.subzero.view.OnDemandMenu;
import com.giorgimode.subzero.view.action.Resource;

abstract class TrackMenu extends OnDemandMenu {

    private static final String KEY_TRACK_DESCRIPTION = "track-description";

    TrackMenu(Resource resource) {
        super(resource, true);
    }

    @Override
    protected final void onPrepareMenu(JMenu menu) {
        ButtonGroup buttonGroup = new ButtonGroup();
        int selectedTrack = onGetSelectedTrack();
        for (TrackDescription trackDescription : onGetTrackDescriptions()) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(createAction(trackDescription));
            menuItem.putClientProperty(KEY_TRACK_DESCRIPTION, trackDescription);
            buttonGroup.add(menuItem);
            menu.add(menuItem);
            if (selectedTrack == trackDescription.id()) {
                menuItem.setSelected(true);
            }
        }
    }

    protected abstract Action createAction(TrackDescription trackDescription);

    protected abstract List<TrackDescription> onGetTrackDescriptions();

    protected abstract int onGetSelectedTrack();
}
