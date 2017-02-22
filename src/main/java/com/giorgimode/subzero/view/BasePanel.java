package com.giorgimode.subzero.view;

import static com.giorgimode.subzero.Application.application;

import javax.swing.JPanel;

import com.giorgimode.subzero.event.ShutdownEvent;

import com.google.common.eventbus.Subscribe;

public abstract class BasePanel extends JPanel {

    public BasePanel() {
        application().subscribe(this);
    }

    @Subscribe
    public final void onShutdown(ShutdownEvent event) {
        onShutdown();
    }

    /**
     * Override, e.g. to save component preferences.
     */
    protected final void onShutdown() {
    }
}
