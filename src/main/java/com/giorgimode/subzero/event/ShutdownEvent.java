package com.giorgimode.subzero.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShutdownEvent {

    public static final ShutdownEvent INSTANCE = new ShutdownEvent();
}
