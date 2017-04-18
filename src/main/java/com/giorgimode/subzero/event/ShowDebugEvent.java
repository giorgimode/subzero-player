package com.giorgimode.subzero.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShowDebugEvent {

    public static final ShowDebugEvent INSTANCE = new ShowDebugEvent();
}
