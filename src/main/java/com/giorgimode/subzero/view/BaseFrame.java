package com.giorgimode.subzero.view;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.event.ShutdownEvent;
import com.google.common.eventbus.Subscribe;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.io.IOException;

@SuppressWarnings("serial")
public abstract class BaseFrame extends JFrame {

    private boolean wasShown;

    public BaseFrame(String title) {
        super(title);
        try {
            setIconImage(ImageIO.read(getClass().getResource("/vlcj-logo-frame.png")));
        } catch (IOException ignored) {
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
    @SuppressWarnings("unused")
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
