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

package com.giorgimode.subzero.view.effects.video;

import static com.giorgimode.subzero.Application.resources;

import javax.swing.JTabbedPane;

import com.giorgimode.subzero.view.BasePanel;
import net.miginfocom.swing.MigLayout;

public class VideoEffectsPanel extends BasePanel {

    private final JTabbedPane tabbedPane;

    private final VideoAdjustPanel videoAdjustPanel;

    public VideoEffectsPanel() {
        tabbedPane = new JTabbedPane();

        videoAdjustPanel = new VideoAdjustPanel();
        tabbedPane.add(videoAdjustPanel, resources().getString("dialog.effects.tabs.video.adjust"));

        setLayout(new MigLayout("fill", "grow", "grow"));
        add(tabbedPane, "grow");
    }

}
