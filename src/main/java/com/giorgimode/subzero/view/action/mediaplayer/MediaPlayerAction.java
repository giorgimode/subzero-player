package com.giorgimode.subzero.view.action.mediaplayer;

import com.giorgimode.subzero.view.action.Resource;
import com.giorgimode.subzero.view.action.StandardAction;
import lombok.Getter;
import uk.co.caprica.vlcj.player.MediaPlayer;

abstract class MediaPlayerAction extends StandardAction {
    @Getter
    private final MediaPlayer mediaPlayer;

    MediaPlayerAction(Resource resource, MediaPlayer mediaPlayer) {
        super(resource);
        this.mediaPlayer = mediaPlayer;
    }

    MediaPlayerAction(String name, MediaPlayer mediaPlayer) {
        super(name);
        this.mediaPlayer = mediaPlayer;
    }
}
