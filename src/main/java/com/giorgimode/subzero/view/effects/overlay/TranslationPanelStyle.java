package com.giorgimode.subzero.view.effects.overlay;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by modeg on 1/22/2017.
 */
class TranslationPanelStyle implements PanelStyle {

    private static final String DEFINITION_STYLE_NAME = "definitionStyle";
    private static final String SYNSET_STYLE_NAME = "synsetStyle";
    private static final String ROOT_STYLE_NAME = "rootStyle";

    public void createOnClickStyle(TranslationPanel translationPanel) {
        Map.Entry<String, Map<String, List<String>>> wordDefinitionEntryMap = translationPanel.getWordDefinitionEntryMap();
        StyledDocument styledDocument = new DefaultStyledDocument();

        int mainHeight = translationPanel.getHeight();
        int mainWidth = translationPanel.getWidth();
        int rootFontSize = mainWidth / 100 + mainHeight / 4;
        int definitionFontSize = mainWidth / 100 + mainHeight / 10;
        int synsetFontSize = definitionFontSize * 2 / 3;

        Style rootStyle = styledDocument.addStyle(ROOT_STYLE_NAME, null);
        setStyle(rootStyle, rootFontSize, false, false, true);

        Style synsetStyle = styledDocument.addStyle(SYNSET_STYLE_NAME, null);
        setStyle(synsetStyle, synsetFontSize, true, true, false);

        Style definitionStyle = styledDocument.addStyle(DEFINITION_STYLE_NAME, null);
        setStyle(definitionStyle, definitionFontSize, false, false, false);

        String rootWord = wordDefinitionEntryMap.getKey();
        insertText(styledDocument, rootStyle, rootWord + "\n");

        Map<String, List<String>> definitionList = wordDefinitionEntryMap.getValue();
        for (Map.Entry<String, List<String>> lists : definitionList.entrySet()) {
            String synset = String.format("(%s) ", lists.getKey());
            synset = synset.concat(lists.getValue().size() > 1 ? "\n" : "");
            insertText(styledDocument, synsetStyle, synset);

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lists.getValue().size(); i++) {
                String counter = lists.getValue().size() > 1 ? (i + 1 + ") ") : "";
                builder
                        .append(repeatedSymbols(" ", 50))
                        .append(counter)
                        .append(lists.getValue().get(i));
                builder.append(lists.getValue().size() > 1 ? "\n" : "");
            }
            insertText(styledDocument, definitionStyle, repeatedSymbols(" ", 50) + builder.toString().trim());
            insertText(styledDocument, rootStyle, "   " + repeatedSymbols("_", 100) + "\n");
        }

        styledDocument.setParagraphAttributes(0, 1, rootStyle, false);
        translationPanel.setOriginalStyledDocument(styledDocument);
    }

    public void createPreviewStyle(TranslationPanel translationPanel) {
        StyledDocument styledDocument = new DefaultStyledDocument();
        int mainHeight = translationPanel.getHeight();
        int mainWidth = translationPanel.getWidth();
        int rootFontSize = (int) (mainWidth / 100.0 + mainHeight / 10.0);
        int previewFontSize = mainWidth / 100 + mainHeight / 20;

        Map.Entry<String, Map<String, List<String>>> wordDefinitionEntryMap = translationPanel.getWordDefinitionEntryMap();

        Style previewRootStyle = styledDocument.addStyle("previewRootStyle", null);
        setStyle(previewRootStyle, rootFontSize, false, false, true);
        insertText(styledDocument, previewRootStyle, wordDefinitionEntryMap.getKey() + ": ");

        List<String> previewTranslations = createPreviewTranslations(wordDefinitionEntryMap.getValue());
        createPreviewBody(styledDocument, previewTranslations, previewFontSize);

        styledDocument.setParagraphAttributes(0, 1, previewRootStyle, false);
        translationPanel.setPreviewStyledDocument(styledDocument);
        translationPanel.getjTextPane().setStyledDocument(styledDocument);
    }

    private void setStyle(Style synsetStyle, int synsetFontSize, boolean isItalic, boolean isUnderLined, boolean isBold) {
        StyleConstants.setFontSize(synsetStyle, synsetFontSize);
        StyleConstants.setItalic(synsetStyle, isItalic);
        StyleConstants.setUnderline(synsetStyle, isUnderLined);
        StyleConstants.setBold(synsetStyle, isBold);
        StyleConstants.setFontFamily(synsetStyle, "Sansserif");
    }

    private void insertText(StyledDocument styledDocument, Style style, String text) {
        try {
            styledDocument.insertString(styledDocument.getLength(), text, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private static String repeatedSymbols(String symbol, int times) {
        String s = "";
        for (int i = 0; i < times; i++) {
            s = s.concat(symbol);
        }
        return s;
    }

    private void createPreviewBody(StyledDocument styledDocument, List<String> preview, int previewFontSize) {
        Style normalStyle = styledDocument.addStyle("previewStyle", null);
        setStyle(normalStyle, previewFontSize, false, false, false);

        Style numberStyle = styledDocument.addStyle("previewStyle", null);
        setStyle(numberStyle, previewFontSize * 6 / 5, false, false, true);

        if (preview.isEmpty()) {
            return;
        } else if (preview.size() < 2) {
            insertText(styledDocument, normalStyle, sanitize(preview.get(0)));
            return;
        }

        for (int i = 0; i < preview.size(); i++) {
            insertText(styledDocument, numberStyle, i + 1 + ") ");
            insertText(styledDocument, normalStyle, sanitize(preview.get(i).trim()) + " ");
        }
    }

    private String sanitize(String result) {
        result = result.replaceAll("\\{.*?} ?", "")
                .replaceAll("\\[.*?] ?", "")
                .replaceAll("<.*?> ?", "")
                .replaceAll("\\(.*?\\) ?", "")
                .replace(" ,", ",");
        return result;
    }

    private List<String> createPreviewTranslations(Map<String, List<String>> wordDefinitionEntryMap) {
        return wordDefinitionEntryMap.values()
                .stream()
                .map(strings -> strings.stream().collect(Collectors.joining(", ")))
                .collect(Collectors.toList());
    }
}
