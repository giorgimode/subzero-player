package com.giorgimode.subzero.view.effects.audio;

import javax.swing.JTabbedPane;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.view.BasePanel;
import net.miginfocom.swing.MigLayout;

public class AudioEffectsPanel extends BasePanel {

    public AudioEffectsPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        EqualizerPanel equalizerPanel = new EqualizerPanel();

        tabbedPane.add(equalizerPanel, Application.resources().getString("dialog.effects.tabs.audio.equalizer"));

        setLayout(new MigLayout("fill", "grow", "grow"));
        add(tabbedPane, "grow");
    }
}
