package com.giorgimode.subzero.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LanguagePairMenuEvent {

    public static final LanguagePairMenuEvent INSTANCE = new LanguagePairMenuEvent();

}
