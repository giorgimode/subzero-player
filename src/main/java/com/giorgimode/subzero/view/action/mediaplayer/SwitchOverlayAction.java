package com.giorgimode.subzero.view.action.mediaplayer;

import com.giorgimode.subzero.event.OverlaySwitchEvent;
import com.giorgimode.subzero.translator.OverlayType;
import com.giorgimode.subzero.view.action.Resource;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;

import static com.giorgimode.subzero.Application.application;

/**
 * Created by modeg on 3/19/2017.
 */
public class SwitchOverlayAction extends MediaPlayerAction {

    private final OverlayType overlayType;

    public SwitchOverlayAction(MediaPlayer mediaPlayer, OverlayType overlayType) {
        super(Resource.resource("menu.subtitle.item.switchOverlay." + overlayType.name()), mediaPlayer);
        this.overlayType = overlayType;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            toggleMediaPlayerOverlay();
            application().selectedOverlayType(overlayType);
            application().post(OverlaySwitchEvent.INSTANCE);
        });
    }

    private void toggleMediaPlayerOverlay() {
        application().selectedOverlayType(overlayType);
        if (mediaPlayer.isPlayable()) {
            ((EmbeddedMediaPlayer) mediaPlayer).enableOverlay(overlayType == OverlayType.TRANSLATION);
        }
    }
}

