package com.gio.subzero;

import java.awt.Window;

import com.gio.subzero.event.AfterExitFullScreenEvent;
import com.gio.subzero.event.BeforeEnterFullScreenEvent;
import uk.co.caprica.vlcj.player.embedded.DefaultAdaptiveRuntimeFullScreenStrategy;

final class VlcjPlayerFullScreenStrategy extends DefaultAdaptiveRuntimeFullScreenStrategy {

    VlcjPlayerFullScreenStrategy(Window window) {
        super(window);
    }

    @Override
    protected void beforeEnterFullScreen() {
        Application.application().post(BeforeEnterFullScreenEvent.INSTANCE);
    }

    @Override
    protected  void afterExitFullScreen() {
        Application.application().post(AfterExitFullScreenEvent.INSTANCE);
    }
}
