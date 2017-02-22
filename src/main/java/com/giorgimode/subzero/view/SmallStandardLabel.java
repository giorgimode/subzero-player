package com.giorgimode.subzero.view;

public class SmallStandardLabel extends StandardLabel {

    private static final float FONT_SIZE = 9.0f;

    public SmallStandardLabel() {
        this(null);
    }

    public SmallStandardLabel(String template) {
        super(template);
        setFont(getFont().deriveFont(FONT_SIZE));
    }
}
