package com.giorgimode.subzero.translator;

public enum OverlayType {
    TRANSLATION("Translation"),
    SECOND_SUBTITLE("Second Subtitle"),
    OFF("off");

    private final String value;

    OverlayType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OverlayType fromString(String lang) {
        for (OverlayType overlayType : OverlayType.values()) {
            if (overlayType.value.equalsIgnoreCase(lang)) {
                return overlayType;
            }
        }
        return OverlayType.OFF;
    }
}