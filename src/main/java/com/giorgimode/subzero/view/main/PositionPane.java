package com.giorgimode.subzero.view.main;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.event.TickEvent;
import com.giorgimode.subzero.view.CustomSliderUI;
import com.giorgimode.subzero.view.StandardLabel;
import com.google.common.eventbus.Subscribe;
import lombok.Setter;
import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.player.MediaPlayer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.plaf.SliderUI;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.giorgimode.subzero.time.Time.formatTime;

public final class PositionPane extends JPanel {

    private final JLabel timeLabel;
    private final JSlider positionSlider;
    private final JLabel durationLabel;
    @Setter
    private       long   time;
    private final MediaPlayer mediaPlayer;
    private final AtomicBoolean sliderChanging = new AtomicBoolean();
    private final AtomicBoolean positionChanging = new AtomicBoolean();

    PositionPane(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;

        timeLabel = new StandardLabel("9:99:99");

        UIManager.put("Slider.paintValue", false); // FIXME how to do this for a single component?
        positionSlider = new JSlider();
        positionSlider.setMinimum(0);
        positionSlider.setMaximum(1000);
        positionSlider.setValue(0);
        SliderUI sliderUI = CustomSliderUI.getSliderUI();
        positionSlider.setUI(sliderUI);

        positionSlider.addChangeListener(e -> {
            if (!positionChanging.get()) {
                JSlider source = (JSlider) e.getSource();
                if (source.getValueIsAdjusting()) {
                    sliderChanging.set(true);
                } else {
                    sliderChanging.set(false);
                }
                mediaPlayer.setPosition(source.getValue() / 1000.0f);
            }
        });

        durationLabel = new StandardLabel("9:99:99");

        setLayout(new MigLayout("fill, insets 0 0 0 0", "[][grow][]", "[]"));

        add(timeLabel, "shrink");
        add(positionSlider, "grow");
        add(durationLabel, "shrink");

        timeLabel.setText("-:--:--");
        durationLabel.setText("-:--:--");

        Application.application().subscribe(this);
    }

    private void refresh() {
        timeLabel.setText(formatTime(time));

        if (!sliderChanging.get()) {
            int value = (int) (mediaPlayer.getPosition() * 1000.0f);
            positionChanging.set(true);
            positionSlider.setValue(value);
            positionChanging.set(false);
        }
    }

    public void setDuration(long duration) {
        durationLabel.setText(formatTime(duration));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onTick(TickEvent tick) {
        refresh();
    }
}
