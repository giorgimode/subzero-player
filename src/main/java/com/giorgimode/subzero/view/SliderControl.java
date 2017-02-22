package com.giorgimode.subzero.view;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class SliderControl extends JPanel implements ChangeListener {

    private final float factor = 100f;
    private final String valueFormat;

    private final JLabel label;
    private final JSlider slider;
    private final JLabel valueLabel;

    public SliderControl(String text, float min, float max, float value, String valueFormat) {
        this.valueFormat = valueFormat;

        label = new SmallStandardLabel();
        label.setText(text);
        label.setHorizontalAlignment(JLabel.CENTER);

        int modelMin = (int)(min * factor);
        int modelMax = (int)(max * factor);
        int modelValue = (int)(value * factor);

        modelValue = Math.min(modelValue, modelMax);
        modelValue = Math.max(modelValue, modelMin);

        slider = new JSlider(JSlider.VERTICAL, modelMin, modelMax, modelValue);
        valueLabel = new SmallStandardLabel();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        slider.setAlignmentX(JSlider.CENTER_ALIGNMENT);
        valueLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        add(slider);
        add(label);
        add(valueLabel);

        slider.getModel().addChangeListener(this);

        updateValue();
    }

    public final JSlider getSlider() {
        return slider;
    }

    @Override
    public final void setEnabled(boolean enabled) {
        slider.setEnabled(enabled);
    }

    @Override
    public final void stateChanged(ChangeEvent e) {
        updateValue();
    }

    private void updateValue() {
        int value = slider.getValue();
        valueLabel.setText(String.format(valueFormat, value / factor));
    }
}
