package com.giorgimode.subzero.view.main;

import static com.giorgimode.subzero.Application.application;
import static com.giorgimode.subzero.view.action.Resource.resource;

import java.util.List;

import javax.swing.Action;

import uk.co.caprica.vlcj.player.TrackDescription;
import com.giorgimode.subzero.view.action.mediaplayer.SubtitleTrackAction;

final class SubtitleTrackMenu extends TrackMenu {

    SubtitleTrackMenu() {
        super(resource("menu.subtitle.item.track"));
    }

    @Override
    protected Action createAction(TrackDescription trackDescription) {
        return new SubtitleTrackAction(trackDescription.description(), application().mediaPlayerComponent().getMediaPlayer(), trackDescription.id());
    }

    @Override
    protected List<TrackDescription> onGetTrackDescriptions() {
        return application().mediaPlayerComponent().getMediaPlayer().getSpuDescriptions();
    }

    @Override
    protected int onGetSelectedTrack() {
        return application().mediaPlayerComponent().getMediaPlayer().getSpu();
    }
}
