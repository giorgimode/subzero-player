package com.giorgimode.subzero.view.effects.overlay;

import javax.swing.JTextPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Map;

/**
 * Created by modeg on 1/22/2017.
 */
public class SubtitlePanelStyle implements PanelStyle {

    public static final String DEFINITION_STYLE_NAME = "definitionStyle";
    public static final String SYNSET_STYLE_NAME = "synsetStyle";
    public static final String ROOT_STYLE_NAME = "rootStyle";

    public void applyStyle(JTextPane textPane, Map.Entry<String, Map<String, List<String>>> wordDefinitionEntryMap) throws BadLocationException {
        StyledDocument styledDocument = textPane.getStyledDocument();


        Style rootStyle = styledDocument.addStyle(ROOT_STYLE_NAME, null);
        StyleConstants.setBold(rootStyle, true);
        StyleConstants.setFontSize(rootStyle, 26);

        Style synsetStyle = styledDocument.addStyle(SYNSET_STYLE_NAME, null);
        StyleConstants.setFontSize(synsetStyle, 14);
        StyleConstants.setItalic(synsetStyle, true);
        StyleConstants.setUnderline(synsetStyle, true);

        Style definitionStyle = styledDocument.addStyle(DEFINITION_STYLE_NAME, null);
        StyleConstants.setFontSize(definitionStyle, 18);

        String rootWord = wordDefinitionEntryMap.getKey();
        styledDocument.insertString(styledDocument.getLength(), rootWord + "\n", rootStyle);

        Map<String, List<String>> definitionList = wordDefinitionEntryMap.getValue();
        for (Map.Entry<String, List<String>> lists : definitionList.entrySet()) {
            String synset = String.format("(%s) ", lists.getKey());
            synset = synset.concat(lists.getValue().size() > 0 ? "\n" : "");
            styledDocument.insertString(styledDocument.getLength(), synset, synsetStyle);

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lists.getValue().size(); i++) {
                String counter = lists.getValue().size() > 1 ? (i + 1 + ") ") : "";
                builder
                        .append(getSpaces(" ", 50))
                        .append(counter)
                        .append(lists.getValue().get(i))
                        .append("\n");

            }
            styledDocument.insertString(styledDocument.getLength(), getSpaces(" ", 50) + builder.toString().trim() + "\n", definitionStyle);
            styledDocument.insertString(styledDocument.getLength(), getSpaces("_", 100) + "\n", definitionStyle);
        }

        styledDocument.setParagraphAttributes(0, 1, rootStyle, false);
    }

    private String getSpaces(String symbol, int times) {
        String s = "";
        for (int i = 0; i < times; i++) {
            s = s.concat(symbol);
        }
        return s;
    }
}
