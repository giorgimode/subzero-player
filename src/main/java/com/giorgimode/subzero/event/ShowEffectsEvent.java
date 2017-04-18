package com.giorgimode.subzero.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShowEffectsEvent {

    public static final ShowEffectsEvent INSTANCE = new ShowEffectsEvent();
}
