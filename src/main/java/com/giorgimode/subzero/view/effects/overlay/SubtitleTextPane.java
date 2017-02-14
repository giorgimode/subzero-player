package com.giorgimode.subzero.view.effects.overlay;

import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;

/**
 * Created by modeg on 2/14/2017.
 */
public class SubtitleTextPane extends JTextPane {
    public void refresh() {
        setStyledDocument(new DefaultStyledDocument());
        Style defaultStyle = StyleContext.
                getDefaultStyleContext().
                getStyle(StyleContext.DEFAULT_STYLE);
        setParagraphAttributes(defaultStyle, true);
        setCharacterAttributes(defaultStyle, true);
        setEditorKit(createDefaultEditorKit());
    }
}
