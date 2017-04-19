package com.giorgimode.subzero.view.effects.video;

import com.giorgimode.subzero.view.BasePanel;
import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.binding.LibVlcConst;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSlider;

import static com.giorgimode.subzero.Application.application;
import static com.giorgimode.subzero.Application.resources;

/* TODO Note that LibVLC 3.0.0 currently has a problem that the video adjust filter needs to be disabled then enabled again after
 changing ANY property
 */

class VideoAdjustPanel extends BasePanel {

    private final JCheckBox enableCheckBox;
    private final JSlider   hueSlider;
    private final JSlider   brightnessSlider;
    private final JSlider   contrastSlider;
    private final JSlider   saturationSlider;
    private final JSlider   gammaSlider;

    VideoAdjustPanel() {
        EmbeddedMediaPlayerComponent mediaPlayerComponent = application().mediaPlayerComponent();

        enableCheckBox = new JCheckBox(resources().getString("dialog.effects.tabs.video.adjust.enable"));

        JLabel hueLabel = new JLabel();
        hueLabel.setText(resources().getString("dialog.effects.tabs.video.adjust.hue"));

        hueSlider = new JSlider();
        hueSlider.setMinimum(LibVlcConst.MIN_HUE);
        hueSlider.setMaximum(LibVlcConst.MAX_HUE);
        hueLabel.setLabelFor(hueSlider);

        JLabel brightnessLabel = new JLabel();
        brightnessLabel.setText(resources().getString("dialog.effects.tabs.video.adjust.brightness"));

        brightnessSlider = new JSlider();
        brightnessSlider.setMinimum(Math.round(LibVlcConst.MIN_BRIGHTNESS * 100.0f));
        brightnessSlider.setMaximum(Math.round(LibVlcConst.MAX_BRIGHTNESS * 100.0f));
        brightnessLabel.setLabelFor(brightnessSlider);

        JLabel contrastLabel = new JLabel();
        contrastLabel.setText(resources().getString("dialog.effects.tabs.video.adjust.contrast"));

        contrastSlider = new JSlider();
        contrastSlider.setMinimum(Math.round(LibVlcConst.MIN_CONTRAST * 100.0f));
        contrastSlider.setMaximum(Math.round(LibVlcConst.MAX_CONTRAST * 100.0f));
        contrastSlider.setPaintLabels(true);
        contrastSlider.setPaintTicks(true);
        contrastLabel.setLabelFor(contrastSlider);

        JLabel saturationLabel = new JLabel();
        saturationLabel.setText(resources().getString("dialog.effects.tabs.video.adjust.saturation"));

        saturationSlider = new JSlider();
        saturationSlider.setMinimum(Math.round(LibVlcConst.MIN_SATURATION * 100.0f));
        saturationSlider.setMaximum(Math.round(LibVlcConst.MAX_SATURATION * 100.0f));
        saturationLabel.setLabelFor(saturationSlider);

        JLabel gammaLabel = new JLabel();
        gammaLabel.setText(resources().getString("dialog.effects.tabs.video.adjust.gamma"));

        gammaSlider = new JSlider();
        gammaSlider.setMinimum(Math.round(LibVlcConst.MIN_GAMMA * 100.0f));
        gammaSlider.setMaximum(Math.round(LibVlcConst.MAX_GAMMA * 100.0f));

        MediaPlayer mediaPlayer = mediaPlayerComponent.getMediaPlayer();

        enableCheckBox.addActionListener(e -> {
            boolean enable = enableCheckBox.isSelected();
            enableControls(enable);
            mediaPlayer.setAdjustVideo(enable);
        });

        hueSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            mediaPlayer.setHue(source.getValue());
        });

        brightnessSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            mediaPlayer.setBrightness(source.getValue() / 100.0f);
        });

        contrastSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            mediaPlayer.setContrast(source.getValue() / 100.0f);
        });

        saturationSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            mediaPlayer.setSaturation(source.getValue() / 100.0f);
        });

        gammaSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            mediaPlayer.setGamma(source.getValue() / 100.0f);
        });

        contrastSlider.setValue(Math.round(mediaPlayer.getBrightness() * 100.0f));
        brightnessSlider.setValue(Math.round(mediaPlayer.getContrast() * 100.0f));
        hueSlider.setValue(mediaPlayer.getHue());
        saturationSlider.setValue(Math.round(mediaPlayer.getSaturation() * 100.0f));
        gammaSlider.setValue(Math.round(mediaPlayer.getGamma() * 100.0f));

        setLayout(new MigLayout("fill", "[shrink]rel[grow]", ""));
        add(enableCheckBox, "span 2, wrap");
        add(hueLabel, "shrink");
        add(hueSlider, "wrap");
        add(brightnessLabel);
        add(brightnessSlider, "wrap");
        add(contrastLabel);
        add(contrastSlider, "wrap");
        add(saturationLabel);
        add(saturationSlider, "wrap");
        add(gammaLabel);
        add(gammaSlider, "wrap");

        enableControls(false);
    }

    private void enableControls(boolean enable) {
        hueSlider.setEnabled(enable);
        brightnessSlider.setEnabled(enable);
        contrastSlider.setEnabled(enable);
        saturationSlider.setEnabled(enable);
        gammaSlider.setEnabled(enable);
    }
}
