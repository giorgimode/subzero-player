package com.giorgimode.subzero.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;

@RequiredArgsConstructor
@Getter
public final class SubtitleAddedEvent {
    private final File file;
}
