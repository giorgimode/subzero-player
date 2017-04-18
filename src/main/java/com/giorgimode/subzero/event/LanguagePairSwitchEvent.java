package com.giorgimode.subzero.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LanguagePairSwitchEvent {

    public static final LanguagePairSwitchEvent INSTANCE = new LanguagePairSwitchEvent();

}
