/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2015 Caprica Software Limited.
 */

package com.gio.subzero.view.main;

import static com.gio.subzero.Application.application;

import java.util.List;

import javax.swing.Action;

import com.gio.subzero.view.action.Resource;
import com.gio.subzero.view.action.mediaplayer.AudioTrackAction;
import uk.co.caprica.vlcj.player.TrackDescription;

final class AudioTrackMenu extends TrackMenu {

    AudioTrackMenu() {
        super(Resource.resource("menu.audio.item.track"));
    }

    @Override
    protected Action createAction(TrackDescription trackDescription) {
        return new AudioTrackAction(trackDescription.description(), application().mediaPlayerComponent().getMediaPlayer(), trackDescription.id());
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
