package com.giorgimode.subzero.view.main;

import static com.giorgimode.subzero.Application.application;

import java.util.List;

import javax.swing.Action;

import com.giorgimode.subzero.view.action.Resource;
import uk.co.caprica.vlcj.player.TrackDescription;
import com.giorgimode.subzero.view.action.mediaplayer.VideoTrackAction;

final class VideoTrackMenu extends TrackMenu {

    VideoTrackMenu() {
        super(Resource.resource("menu.video.item.track"));
    }

    @Override
    protected Action createAction(TrackDescription trackDescription) {
        return new VideoTrackAction(trackDescription.description(), application().mediaPlayerComponent().getMediaPlayer(), trackDescription
                .id());
    }

    @Override
    protected List<TrackDescription> onGetTrackDescriptions() {
        return application().mediaPlayerComponent().getMediaPlayer().getVideoDescriptions();
    }

    @Override
    protected int onGetSelectedTrack() {
        return application().mediaPlayerComponent().getMediaPlayer().getVideoTrack();
    }
}
