package com.giorgimode.subzero.view.main;

import javax.swing.JMenuBar;
import java.awt.event.KeyEvent;

/**
 * Created by modeg on 2/15/2017.
 */
public class PlayerMenuBar extends JMenuBar {

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
    }

    protected void processKeyEvent(KeyEvent e, boolean isFullScreen) {
        if (isFullScreen) {
            setVisible(true);
            processKeyEvent(e);
            setVisible(false);
        } else {
            processKeyEvent(e);
        }
    }

}
