package com.giorgimode.subzero.view.effects.overlay;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.List;
import java.util.Map;

/**
 * Created by modeg on 1/22/2017.
 */
public class SubtitlePanelStyle implements PanelStyle {

    public void applyStyle(JTextPane textArea, Map.Entry<String, Map<String, List<String>>> wordDefinitionEntryMap) throws BadLocationException {
        StyledDocument styledDocument = textArea.getStyledDocument();

        Style rootStyle = styledDocument.addStyle("rootStyle", null);
        StyleConstants.setBold(rootStyle, true);
        StyleConstants.setFontSize(rootStyle, 26);

        Style synsetStyle = styledDocument.addStyle("synsetStyle", null);
        StyleConstants.setFontSize(synsetStyle, 14);
        StyleConstants.setItalic(synsetStyle, true);
        StyleConstants.setUnderline(synsetStyle, true);

        Style definitionStyle = styledDocument.addStyle("normal", null);
        StyleConstants.setFontSize(definitionStyle, 18);

        String rootWord = wordDefinitionEntryMap.getKey();
        styledDocument.insertString(styledDocument.getLength(), rootWord + ": ", styledDocument.getStyle("rootStyle"));

        Map<String, List<String>> definitionList = wordDefinitionEntryMap.getValue();
        for (Map.Entry<String, List<String>> lists : definitionList.entrySet()) {
            String synset = String.format("(%s) ", lists.getKey());
            synset = synset.concat(lists.getValue().size() > 0 ? "\n" : "");
            styledDocument.insertString(styledDocument.getLength(), synset, styledDocument.getStyle("synsetStyle"));

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lists.getValue().size(); i++) {
                String counter = lists.getValue().size() > 1 ? (i + 1 + ") ") : "";
                builder
                        .append(getSpaces(" ", 50))
                        .append(counter)
                        .append(lists.getValue().get(i))
                        .append("\n");

            }
            styledDocument.insertString(styledDocument.getLength(), getSpaces(" ", 50) + builder.toString().trim() + "\n", styledDocument.getStyle("normal"));
            styledDocument.insertString(styledDocument.getLength(), getSpaces("_", 100) + "\n", styledDocument.getStyle("normal"));
       //     styledDocument.insertString(styledDocument.getLength(), "<html>Abovce<hr size=5>Below</html>", styledDocument.getStyle("normal"));

        }

        styledDocument.setParagraphAttributes(0, 1, rootStyle, false);
    }

    private String getSpaces(String symbol, int times) {
        //  int length = synsetLength < 50 ? 50 - synsetLength : 100 - synsetLength;
        String s = "";
        for (int i = 0; i < times; i++) {
            s = s.concat(symbol);
        }
        return s;
    }
}
