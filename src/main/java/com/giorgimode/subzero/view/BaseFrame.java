package com.giorgimode.subzero.view;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.event.ShutdownEvent;

import com.google.common.eventbus.Subscribe;

@SuppressWarnings("serial")
public abstract class BaseFrame extends JFrame {

    private boolean wasShown;

    public BaseFrame(String title) {
        super(title);
        try {
            setIconImage(ImageIO.read(getClass().getResource("/vlcj-logo-frame.png")));
        }
        catch (IOException e) {
        }
        Application.application().subscribe(this);
    }

    @Override
    public final void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            this.wasShown = true;
        }
    }

    @Subscribe
    public final void onShutdown(ShutdownEvent event) {
        onShutdown();
    }

    protected final boolean wasShown() {
        return wasShown;
    }

    /**
     * Override, e.g. to save component preferences.
     */
    protected void onShutdown() {
    }
}
