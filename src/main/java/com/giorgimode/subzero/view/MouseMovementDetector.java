package com.giorgimode.subzero.view;

import javax.swing.Timer;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

public abstract class MouseMovementDetector {

    private final Component component;
    private final int       timeout;
    private final MouseMotionListener mouseMotionListener = new ActivityListener();
    private Timer   timer;
    private boolean started;
    private boolean moving;

    public MouseMovementDetector(Component component, int timeout) {
        this.component = component;
        this.timeout = timeout;
    }

    public void start() {
        if (!started) {
            timer = new Timer(timeout, e -> timeout());
            component.addMouseMotionListener(mouseMotionListener);
            timer.start();
            timer.setRepeats(false);
            started = true;
            onStarted();
        } else {
            throw new IllegalStateException("Already started");
        }
    }

    public void stop() {
        if (started) {
            component.removeMouseMotionListener(mouseMotionListener);
            timer.stop();
            timer = null;
            started = false;
            onStopped();
        }
    }

    private void movement() {
        if (!moving) {
            moving = true;
            onMouseMoved();
        }
        timer.restart();
    }

    private void timeout() {
        moving = false;
        onMouseAtRest();
    }

    private void onStarted() {
    }

    protected void onMouseAtRest() {
    }

    protected void onMouseMoved() {
    }

    protected void onStopped() {
    }

    private class ActivityListener extends MouseMotionAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            movement();
        }
    }
}
