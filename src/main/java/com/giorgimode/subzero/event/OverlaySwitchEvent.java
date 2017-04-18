package com.giorgimode.subzero.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OverlaySwitchEvent {

    public static final OverlaySwitchEvent INSTANCE = new OverlaySwitchEvent();

}
