package com.giorgimode.subzero.view;

import com.giorgimode.subzero.event.ShutdownEvent;
import com.google.common.eventbus.Subscribe;

import javax.swing.JPanel;

import static com.giorgimode.subzero.Application.application;

public abstract class BasePanel extends JPanel {

    protected BasePanel() {
        application().subscribe(this);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public final void onShutdown(ShutdownEvent event) {
        onShutdown();
    }

    /**
     * Override, e.g. to save component preferences.
     */
    private void onShutdown() {
    }
}
