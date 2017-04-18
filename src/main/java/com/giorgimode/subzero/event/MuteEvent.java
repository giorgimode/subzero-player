package com.giorgimode.subzero.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MuteEvent {
    public static final MuteEvent INSTANCE = new MuteEvent();
}
