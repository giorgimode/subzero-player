package com.giorgimode.subzero.view.effects.overlay;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import java.util.List;
import java.util.Map;

/**
 * Created by modeg on 1/22/2017.
 */
public interface PanelStyle {
    void applyStyle(JTextPane textArea, Map.Entry<String, Map<String, List<String>>> wordDefinitionEntryMap) throws BadLocationException;
}
