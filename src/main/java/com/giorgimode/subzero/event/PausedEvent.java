package com.giorgimode.subzero.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PausedEvent {

    public static final PausedEvent INSTANCE = new PausedEvent();
}
