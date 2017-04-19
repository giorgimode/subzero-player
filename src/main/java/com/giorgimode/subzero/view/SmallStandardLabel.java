package com.giorgimode.subzero.view;

class SmallStandardLabel extends StandardLabel {

    private static final float FONT_SIZE = 9.0f;

    SmallStandardLabel() {
        this(null);
    }

    private SmallStandardLabel(String template) {
        super(template);
        setFont(getFont().deriveFont(FONT_SIZE));
    }
}
