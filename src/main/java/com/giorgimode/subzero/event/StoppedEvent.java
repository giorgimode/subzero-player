package com.giorgimode.subzero.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StoppedEvent {

    public static final StoppedEvent INSTANCE = new StoppedEvent();
}
