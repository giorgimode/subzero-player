package com.giorgimode.subzero.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BeforeEnterFullScreenEvent {

    public static final BeforeEnterFullScreenEvent INSTANCE = new BeforeEnterFullScreenEvent();
}
