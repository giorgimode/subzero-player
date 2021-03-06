package com.giorgimode.subzero.view.effects.overlay;

import com.sun.awt.AWTUtilities;
import com.sun.jna.platform.WindowUtils;

import javax.swing.JWindow;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TranslationOverlay extends JWindow {

    private static final long serialVersionUID = 1L;
    private final Window                 owner;
    private       List<TranslationPanel> translationPanelList;
    private       int                    spaceBetweenPanels;
    private       int                    translationPanelHeight;
    private       int                    area51Below;
    private       int                    area51Left;
    private final int                    numberOfPanelsAllowed;
    private       PanelStyle             translationPanelStyle;

    public TranslationOverlay(Window owner) {
        super(owner, WindowUtils.getAlphaCompatibleGraphicsConfiguration());
        this.owner = owner;
        translationPanelStyle = new TranslationPanelStyle();
        AWTUtilities.setWindowOpaque(this, false);
        setLayout(null);
        setDimensions(owner);
        numberOfPanelsAllowed = (int) Math.ceil((double) (owner.getHeight() - area51Below) / (translationPanelHeight + spaceBetweenPanels));
    }

    public void updateOverlay() {
        if (translationPanelList == null || translationPanelList.isEmpty()) {
            return;
        }
        setDimensions(owner);
        if (translationPanelList.size() > numberOfPanelsAllowed) {
            resizeTranslationPanels(translationPanelList.size());
        }
        TranslationPanel lowestPanel = translationPanelList.get(translationPanelList.size() - 1);
        updatePanelProperties(lowestPanel, owner.getHeight() - area51Below, owner.getHeight() + area51Below / 5);

        if (translationPanelList.size() < 2) {
            return;
        }
        for (int i = translationPanelList.size() - 2; i >= 0; i--) {
            int previousPanelHeight = (int) translationPanelList.get(i + 1).getLocation().getY();
            int locationY = previousPanelHeight - spaceBetweenPanels - translationPanelHeight;
            updatePanelProperties(translationPanelList.get(i), locationY, owner.getHeight());
        }
    }

    public void populateNewWords(Map<String, Map<String, List<String>>> translatedList) {
        clean();
        translationPanelList = new ArrayList<>();
        translatedList.entrySet().stream()
                      .filter(this::isNotEmptyWordList)
                      .forEach(this::addTranslationPanel);
        updateOverlay();
    }

    public void clean() {
        if (translationPanelList != null && !translationPanelList.isEmpty()) {
            translationPanelList.forEach(this::remove);
        }
    }

    private void setDimensions(Window owner) {
        spaceBetweenPanels = owner.getHeight() / 25;
        translationPanelHeight = owner.getHeight() / 25;

        area51Below = owner.getHeight() * 3 / 8;
        area51Left = owner.getWidth() / 35;
    }

    private void addTranslationPanel(Map.Entry<String, Map<String, List<String>>> wordDefinitionEntryMap) {
        TranslationPanel translationPanel = new TranslationPanel(wordDefinitionEntryMap);
        translationPanelList.add(translationPanel);
        add(translationPanel);
    }

    private void updatePanelProperties(TranslationPanel translationPanel, int locationY, int maxHeight) {
        translationPanel.setLocation(area51Left, locationY);
        int preferredHeight = (int) translationPanel.getJTextPane().getPreferredSize().getHeight();
        translationPanel.setSize(new Dimension(owner.getWidth() - 3 * area51Left, Math.min(2 * preferredHeight, translationPanelHeight)));
        translationPanel.setMaximumAllowedHeight(maxHeight);
        translationPanelStyle.createOnClickStyle(translationPanel);
        translationPanel.getJTextPane().setFont(new Font("Sansserif", Font.BOLD, 12));
        translationPanelStyle.createPreviewStyle(translationPanel);
    }

    private void resizeTranslationPanels(int numberOfPanels) {
        double newSize = (numberOfPanels * (translationPanelHeight + spaceBetweenPanels) - owner.getHeight()
                          + area51Below) / (double) (2 * numberOfPanels);
        translationPanelHeight = (int) (translationPanelHeight - newSize);
        spaceBetweenPanels = (int) (spaceBetweenPanels - newSize);
    }

    private boolean isNotEmptyWordList(Map.Entry<String, Map<String, List<String>>> entry) {
        return entry.getValue() != null && entry.getValue().size() > 0 && entry.getValue().entrySet().size() > 0;
    }
}
