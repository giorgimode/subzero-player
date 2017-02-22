package com.giorgimode.subzero.view.main;

import java.awt.Component;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import com.giorgimode.subzero.view.MouseMovementDetector;

final class VideoMouseMovementDetector extends MouseMovementDetector {

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    VideoMouseMovementDetector(Component component, int timeout, EmbeddedMediaPlayerComponent mediaPlayerComponent) {
        super(component, timeout);
        this.mediaPlayerComponent = mediaPlayerComponent;
    }

    @Override
    protected void onMouseAtRest() {
        mediaPlayerComponent.setCursorEnabled(false);
    }

    @Override
    protected void onMouseMoved() {
        mediaPlayerComponent.setCursorEnabled(true);
    }

    @Override
    protected void onStopped() {
        mediaPlayerComponent.setCursorEnabled(true);
    }
}
