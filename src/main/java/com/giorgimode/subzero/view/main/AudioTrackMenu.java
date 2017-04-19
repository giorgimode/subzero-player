package com.giorgimode.subzero.view.main;

import com.giorgimode.subzero.view.action.Resource;
import com.giorgimode.subzero.view.action.mediaplayer.AudioTrackAction;
import uk.co.caprica.vlcj.player.TrackDescription;

import javax.swing.Action;
import java.util.List;

import static com.giorgimode.subzero.Application.application;

final class AudioTrackMenu extends TrackMenu {

    AudioTrackMenu() {
        super(Resource.resource("menu.audio.item.track"));
    }

    @Override
    protected Action createAction(TrackDescription trackDescription) {
        return new AudioTrackAction(trackDescription.description(), application().mediaPlayerComponent().getMediaPlayer(), trackDescription
                .id());
    }

    @Override
    protected List<TrackDescription> onGetTrackDescriptions() {
        return application().mediaPlayerComponent().getMediaPlayer().getAudioDescriptions();
    }

    @Override
    protected int onGetSelectedTrack() {
        return application().mediaPlayerComponent().getMediaPlayer().getAudioTrack();
    }
}
