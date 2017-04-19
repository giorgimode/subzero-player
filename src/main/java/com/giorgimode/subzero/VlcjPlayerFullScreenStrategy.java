package com.giorgimode.subzero;

import com.giorgimode.subzero.event.AfterExitFullScreenEvent;
import com.giorgimode.subzero.event.BeforeEnterFullScreenEvent;
import uk.co.caprica.vlcj.player.embedded.DefaultAdaptiveRuntimeFullScreenStrategy;

import java.awt.Window;

final class VlcjPlayerFullScreenStrategy extends DefaultAdaptiveRuntimeFullScreenStrategy {

    VlcjPlayerFullScreenStrategy(Window window) {
        super(window);
    }

    @Override
    protected void beforeEnterFullScreen() {
        Application.application().post(BeforeEnterFullScreenEvent.INSTANCE);
    }

    @Override
    protected void afterExitFullScreen() {
        Application.application().post(AfterExitFullScreenEvent.INSTANCE);
    }
}
