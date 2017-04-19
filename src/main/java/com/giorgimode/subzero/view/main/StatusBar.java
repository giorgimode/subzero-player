package com.giorgimode.subzero.view.main;

import com.giorgimode.subzero.event.TickEvent;
import com.giorgimode.subzero.view.BorderedStandardLabel;
import com.google.common.eventbus.Subscribe;
import lombok.Setter;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import static com.giorgimode.subzero.Application.application;
import static com.giorgimode.subzero.time.Time.formatTime;

public final class StatusBar extends JPanel {

    private final JLabel titleLabel;
    private final JLabel rateLabel;
    private final JLabel timeLabel;
    @Setter
    private       long   time;
    private       long   duration;

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

    public void setDuration(long duration) {
        this.duration = duration;
        refresh();
    }

    public void clear() {
        titleLabel.setText(null);
        rateLabel.setText(null);
        timeLabel.setText(null);
    }

    private void refresh() {
        timeLabel.setText(String.format("%s/%s", formatTime(time), formatTime(duration)));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onTick(TickEvent tick) {
        refresh();
    }
}
