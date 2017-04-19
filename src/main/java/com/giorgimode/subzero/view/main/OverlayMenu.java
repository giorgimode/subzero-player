package com.giorgimode.subzero.view.main;

import com.giorgimode.subzero.event.ShutdownEvent;
import com.giorgimode.subzero.translator.OverlayType;
import com.giorgimode.subzero.view.action.Resource;
import com.giorgimode.subzero.view.action.mediaplayer.SwitchOverlayAction;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import uk.co.caprica.vlcj.player.MediaPlayer;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import java.util.prefs.Preferences;

import static com.giorgimode.subzero.Application.application;

@Slf4j
public class OverlayMenu {
    private final JMenu menu;

    @SuppressWarnings("ConstantConditions")
    OverlayMenu() {
        application().subscribe(this);
        Resource resource = Resource.resource("menu.subtitle.item.switchOverlay");
        this.menu = new JMenu(resource.name());
        this.menu.setMnemonic(resource.mnemonic());
        applyPreferences();
        onCreateMenu(this.menu);
    }

    public final JMenu menu() {
        return menu;
    }

    private void onCreateMenu(JMenu menu) {
        MediaPlayer mediaPlayer = application().mediaPlayerComponent().getMediaPlayer();
        ButtonGroup buttonGroup = new ButtonGroup();

        for (OverlayType overlayType : OverlayType.values()) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(createAction(mediaPlayer, overlayType));
            buttonGroup.add(menuItem);
            if (application().selectedOverlayType() == overlayType) {
                menuItem.setSelected(true);
            }
            menu.add(menuItem);
        }
    }

    private SwitchOverlayAction createAction(MediaPlayer mediaPlayer, OverlayType overlayType) {
        return new SwitchOverlayAction(mediaPlayer, overlayType);
    }

    private void applyPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(OverlayMenu.class);

        String overlayTypeString = prefs.get("overlayType", "");
        OverlayType selectedOverlayType = OverlayType.fromString(overlayTypeString);
        application().selectedOverlayType(selectedOverlayType);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public final void onShutdown(ShutdownEvent event) {
        log.debug("saving preferences");
        OverlayType selectedOverlayType = application().selectedOverlayType();
        Preferences prefs = Preferences.userNodeForPackage(OverlayMenu.class);
        if (selectedOverlayType != null) {
            prefs.put("overlayType", selectedOverlayType.getValue());
        } else {
            prefs.put("overlayType", OverlayType.OFF.getValue());
        }
    }

}
