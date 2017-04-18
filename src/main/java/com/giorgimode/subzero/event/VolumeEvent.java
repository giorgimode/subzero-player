package com.giorgimode.subzero.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VolumeEvent {
    public static final VolumeEvent INSTANCE = new VolumeEvent();
}
