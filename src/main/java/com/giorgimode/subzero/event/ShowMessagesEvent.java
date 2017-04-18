package com.giorgimode.subzero.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShowMessagesEvent {

    public static final ShowMessagesEvent INSTANCE = new ShowMessagesEvent();
}
