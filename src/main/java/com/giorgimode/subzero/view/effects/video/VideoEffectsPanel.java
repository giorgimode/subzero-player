package com.giorgimode.subzero.view.effects.video;

import static com.giorgimode.subzero.Application.resources;

import javax.swing.JTabbedPane;

import com.giorgimode.subzero.view.BasePanel;
import net.miginfocom.swing.MigLayout;

public class VideoEffectsPanel extends BasePanel {

    public VideoEffectsPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        VideoAdjustPanel videoAdjustPanel = new VideoAdjustPanel();

        tabbedPane.add(videoAdjustPanel, resources().getString("dialog.effects.tabs.video.adjust"));

        setLayout(new MigLayout("fill", "grow", "grow"));
        add(tabbedPane, "grow");
    }

}
