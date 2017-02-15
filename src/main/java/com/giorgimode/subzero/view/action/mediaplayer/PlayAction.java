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

package com.giorgimode.subzero.view.action.mediaplayer;

import java.awt.event.ActionEvent;

import uk.co.caprica.vlcj.player.MediaPlayer;
import com.giorgimode.subzero.view.action.Resource;

import javax.swing.Action;

final class PlayAction extends MediaPlayerAction {

    PlayAction(Resource resource, MediaPlayer mediaPlayer) {
        super(resource, mediaPlayer);
        putValue(Action.NAME             , resource.name());
        putValue(Action.MNEMONIC_KEY     , resource.mnemonic  ());
        putValue(Action.ACCELERATOR_KEY  , resource.shortcut  ());
        putValue(Action.SHORT_DESCRIPTION, resource.tooltip   ());
        putValue(Action.SMALL_ICON       , resource.menuIcon  ());
        putValue(Action.LARGE_ICON_KEY   , resource.buttonIcon());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.play();
        }
        else {
            mediaPlayer.pause();
        }
    }
}
