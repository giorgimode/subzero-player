package com.giorgimode.subzero.view;

import com.sun.java.swing.plaf.windows.WindowsSliderUI;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.JSlider;
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * Created by modeg on 8/21/2016.
 */
public class CustomSliderUI {

    public static SliderUI getSliderUI() {
        if (RuntimeUtil.isWindows()) {
            return new WindowsSliderUI(null) {
                protected void scrollDueToClickInTrack(int direction) {
                    updateSliderValue(this, slider);

                }


            };
        } else {
            return new BasicSliderUI(null) {
                protected void scrollDueToClickInTrack(int direction) {
                    updateSliderValue(this, slider);
                }
            };
        }
    }

    private static void updateSliderValue(BasicSliderUI sliderUI, JSlider slider) {
        // this is the default behaviour, let's comment that out
        //scrollByBlock(direction);

        int value = slider.getValue();

        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            value = sliderUI.valueForXPosition(slider.getMousePosition().x);
        } else if (slider.getOrientation() == JSlider.VERTICAL) {
            value = sliderUI.valueForYPosition(slider.getMousePosition().y);
        }
        slider.setValue(value);
    }
}
