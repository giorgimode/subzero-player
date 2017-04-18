package com.giorgimode.subzero.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AfterExitFullScreenEvent {

    public static final AfterExitFullScreenEvent INSTANCE = new AfterExitFullScreenEvent();
}
