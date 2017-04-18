package com.giorgimode.subzero.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayingEvent {

    public static final PlayingEvent INSTANCE = new PlayingEvent();
}
