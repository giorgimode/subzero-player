package com.giorgimode.subzero.view.main;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.view.action.Resource;
import com.giorgimode.subzero.view.action.mediaplayer.TitleAction;
import uk.co.caprica.vlcj.player.TrackDescription;

import javax.swing.Action;
import java.util.List;

final class TitleTrackMenu extends TrackMenu {

    TitleTrackMenu() {
        super(Resource.resource("menu.playback.item.title"));
    }

    @Override
    protected Action createAction(TrackDescription trackDescription) {
        return new TitleAction(trackDescription.description(), Application.application().mediaPlayerComponent()
                                                                          .getMediaPlayer(), trackDescription.id());
    }

    @Override
    protected List<TrackDescription> onGetTrackDescriptions() {
        return Application.application().mediaPlayerComponent().getMediaPlayer().getTitleDescriptions();
    }

    @Override
    protected int onGetSelectedTrack() {
        return Application.application().mediaPlayerComponent().getMediaPlayer().getTitle();
    }
}
