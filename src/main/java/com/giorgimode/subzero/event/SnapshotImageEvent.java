package com.giorgimode.subzero.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.image.BufferedImage;

@RequiredArgsConstructor
@Getter
public final class SnapshotImageEvent {

    private final BufferedImage image;
}
