package com.giorgimode.subzero.view.effects.overlay;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.List;
import java.util.Map;

/**
 * Created by modeg on 1/22/2017.
 */
public class SubtitlePanelStyle implements PanelStyle {

    private static final String DEFINITION_STYLE_NAME = "definitionStyle";
    private static final String SYNSET_STYLE_NAME = "synsetStyle";
    private static final String ROOT_STYLE_NAME = "rootStyle";

    public void applyStyle(SubtitlePanel subtitlePanel) {
        JTextPane textPane = subtitlePanel.getjTextPane();
        Map.Entry<String, Map<String, List<String>>> wordDefinitionEntryMap = subtitlePanel.getWordDefinitionEntryMap();
        StyledDocument styledDocument = new DefaultStyledDocument();
        textPane.setStyledDocument(styledDocument);

        int mainHeight = subtitlePanel.getHeight();
        int mainWidth = subtitlePanel.getWidth();
        int rootFontSize = mainWidth / 100 + mainHeight / 4;
        int definitionFontSize = mainWidth / 100 + mainHeight / 10;
        int synsetFontSize = definitionFontSize * 2 / 3;

        Style rootStyle = styledDocument.addStyle(ROOT_STYLE_NAME, null);
        StyleConstants.setBold(rootStyle, true);
        StyleConstants.setFontSize(rootStyle, rootFontSize);

        Style synsetStyle = styledDocument.addStyle(SYNSET_STYLE_NAME, null);
        StyleConstants.setFontSize(synsetStyle, synsetFontSize);
        StyleConstants.setItalic(synsetStyle, true);
        StyleConstants.setUnderline(synsetStyle, true);

        Style definitionStyle = styledDocument.addStyle(DEFINITION_STYLE_NAME, null);
        StyleConstants.setFontSize(definitionStyle, definitionFontSize);

        String rootWord = wordDefinitionEntryMap.getKey();
        insertText(styledDocument, rootStyle, rootWord);

        Map<String, List<String>> definitionList = wordDefinitionEntryMap.getValue();
        for (Map.Entry<String, List<String>> lists : definitionList.entrySet()) {
            String synset = String.format("(%s) ", lists.getKey());
            synset = synset.concat(lists.getValue().size() > 0 ? "\n" : "");
            // styledDocument.insertString(styledDocument.getLength(), synset, synsetStyle);
            insertText(styledDocument, synsetStyle, synset);

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lists.getValue().size(); i++) {
                String counter = lists.getValue().size() > 1 ? (i + 1 + ") ") : "";
                builder
                        .append(getSpaces(" ", 50))
                        .append(counter)
                        .append(lists.getValue().get(i))
                        .append("\n");

            }
            insertText(styledDocument, definitionStyle, getSpaces(" ", 50) + builder.toString().trim() + "\n");
            insertText(styledDocument, definitionStyle, getSpaces("_", 100));
        }

        styledDocument.setParagraphAttributes(0, 1, rootStyle, false);
    }

    private void insertText(StyledDocument styledDocument, Style style, String word) {
        try {
            styledDocument.insertString(styledDocument.getLength(), word + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private String getSpaces(String symbol, int times) {
        String s = "";
        for (int i = 0; i < times; i++) {
            s = s.concat(symbol);
        }
        return s;
    }
}
