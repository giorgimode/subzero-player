package com.gio.subzero.util;

import com.sun.java.swing.plaf.windows.WindowsSliderUI;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * Created by modeg on 8/21/2016.
 */
public class CustomSliderUI {

    public static SliderUI getSliderUI (){
        if (RuntimeUtil.isWindows()) {
           return new WindowsSliderUI(null) {
               protected void scrollDueToClickInTrack(int direction) {
                   // this is the default behaviour, let's comment that out
                   //scrollByBlock(direction);

                   int value = slider.getValue();

                   if (slider.getOrientation() == JSlider.HORIZONTAL) {
                       value = this.valueForXPosition(slider.getMousePosition().x);
                   } else if (slider.getOrientation() == JSlider.VERTICAL) {
                       value = this.valueForYPosition(slider.getMousePosition().y);
                   }
                   slider.setValue(value);
               }
           };
        }

        else {
            return new BasicSliderUI(null) {
                protected void scrollDueToClickInTrack(int direction) {
                    // this is the default behaviour, let's comment that out
                    //scrollByBlock(direction);

                    int value = slider.getValue();

                    if (slider.getOrientation() == JSlider.HORIZONTAL) {
                        value = this.valueForXPosition(slider.getMousePosition().x);
                    } else if (slider.getOrientation() == JSlider.VERTICAL) {
                        value = this.valueForYPosition(slider.getMousePosition().y);
                    }
                    slider.setValue(value);
                }
            };
        }
    }
}
