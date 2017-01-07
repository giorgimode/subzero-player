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

package com.giorgimode.subzero.view.main;

import static com.giorgimode.subzero.Application.application;
import static com.giorgimode.subzero.time.Time.formatTime;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import com.giorgimode.subzero.event.TickEvent;
import com.giorgimode.subzero.view.BorderedStandardLabel;

import com.google.common.eventbus.Subscribe;

public final class StatusBar extends JPanel {

    private final JLabel titleLabel;

    private final JLabel rateLabel;

    private final JLabel timeLabel;

    private long time;

    private long duration;

    StatusBar() {
        titleLabel = new BorderedStandardLabel();
        rateLabel = new BorderedStandardLabel();
        timeLabel = new BorderedStandardLabel();

        setLayout(new MigLayout("fillx, insets 2 n n n", "[grow]16[][]", "[]"));
        add(titleLabel, "grow");
        add(rateLabel);
        add(timeLabel);

        application().subscribe(this);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setRate(String rate) {
        rateLabel.setText(rate);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setDuration(long duration) {
        this.duration = duration;
        refresh();
    }

    public void clear() {
        titleLabel.setText(null);
        rateLabel.setText(null);
        timeLabel.setText(null);
    }

    public void refresh() {
        timeLabel.setText(String.format("%s/%s", formatTime(time), formatTime(duration)));
    }

    @Subscribe
    public void onTick(TickEvent tick) {
        refresh();
    }
}
