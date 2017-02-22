package com.giorgimode.subzero.view.action.mediaplayer;

import com.giorgimode.subzero.view.action.Resource;
import com.giorgimode.subzero.view.action.StandardAction;
import uk.co.caprica.vlcj.player.MediaPlayer;

abstract class MediaPlayerAction extends StandardAction {

    protected final MediaPlayer mediaPlayer;

    MediaPlayerAction(Resource resource, MediaPlayer mediaPlayer) {
        super(resource);
        this.mediaPlayer = mediaPlayer;
    }

    MediaPlayerAction(String name, MediaPlayer mediaPlayer) {
        super(name);
        this.mediaPlayer = mediaPlayer;
    }
}
