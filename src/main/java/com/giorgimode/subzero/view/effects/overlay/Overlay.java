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
import java.util.stream.Collectors;

public class Overlay extends JWindow {

    private static final long serialVersionUID = 1L;
    private final Window owner;
    private List<SubtitlePanel> subtitlePanelList;
    private int spaceBetweenPanels;
    private int subtitlePanelHeight;
    private int area51_below;
    private int area51_left;
    private final int numberOfPanelsAllowed;
    private PanelStyle subtitlePanelStyle;

    public Overlay(Window owner, Map<String, Map<String, List<String>>> translatedList) {
        super(owner, WindowUtils.getAlphaCompatibleGraphicsConfiguration());
        this.owner = owner;
        subtitlePanelList = new ArrayList<>();
        subtitlePanelStyle = new SubtitlePanelStyle();
        AWTUtilities.setWindowOpaque(this, false);
        setLayout(null);
        translatedList.entrySet().forEach(this::addSubtitlePanel);
        setDimensions(owner);
        numberOfPanelsAllowed = (int) Math.ceil((double) (owner.getHeight() - area51_below) / (subtitlePanelHeight + spaceBetweenPanels));
        updateOverlay();
    }

    private void setDimensions(Window owner) {
        spaceBetweenPanels = owner.getHeight() / 25;
        subtitlePanelHeight = owner.getHeight() / 25;

        area51_below = owner.getHeight() * 3 / 8;
        area51_left = owner.getWidth() / 35;
    }


    private void addSubtitlePanel(Map.Entry<String, Map<String, List<String>>> wordDefinitionEntryMap) {
        String preview = createPreview(wordDefinitionEntryMap.getValue());
        SubtitlePanel subtitlePanel = new SubtitlePanel(wordDefinitionEntryMap, preview);

        subtitlePanelList.add(subtitlePanel);
        add(subtitlePanel);
    }

    private String createPreview(Map<String, List<String>> wordDefinitionEntryMap) {
        List<String> preview = wordDefinitionEntryMap.values()
                .stream()
                .map(strings -> strings.stream().collect(Collectors.joining(", ")))
                .collect(Collectors.toList());

        if (preview.size() < 2) {
            return preview.get(0);
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < preview.size(); i++) {
            builder.append(i + i).append(") ")
                    .append(preview.get(i))
                    .append("; ");
        }
        return builder.toString();
    }


    public void updateOverlay() {
        if (subtitlePanelList.isEmpty()) {
            return;
        }
        setDimensions(owner);
        if (subtitlePanelList.size() > numberOfPanelsAllowed) {
            resizeSubtitlePanels(subtitlePanelList.size());
        }
        SubtitlePanel lowestPanel = subtitlePanelList.get(subtitlePanelList.size() - 1);
        updatePanelProperties(lowestPanel, owner.getHeight() - area51_below, owner.getHeight() + area51_below / 5);

        if (subtitlePanelList.size() < 2) {
            return;
        }
        for (int i = subtitlePanelList.size() - 2; i >= 0; i--) {
            int previousSubtitleHeight = (int) subtitlePanelList.get(i + 1).getLocation().getY();
            int locationY = previousSubtitleHeight - spaceBetweenPanels - subtitlePanelHeight;
            updatePanelProperties(subtitlePanelList.get(i), locationY, owner.getHeight());
        }
    }

    private void updatePanelProperties(SubtitlePanel subtitlePanel, int locationY, int maxHeight) {
        subtitlePanel.setLocation(area51_left, locationY);
        subtitlePanel.setSize(new Dimension(owner.getWidth() - 3 * area51_left, subtitlePanelHeight));
        createOnClickStyle(subtitlePanel);
        subtitlePanel.setMaximumAllowedHeight(maxHeight);
        subtitlePanel.getjTextPane().setFont(new Font("Sansserif", Font.BOLD, owner.getWidth() / 200 + owner.getHeight() / 100));
    }

    private void resizeSubtitlePanels(int numberOfPanels) {
        double newSize = (numberOfPanels * (subtitlePanelHeight + spaceBetweenPanels) - owner.getHeight() + area51_below) / (double) (2 * numberOfPanels);
        subtitlePanelHeight = (int) (subtitlePanelHeight - newSize);
        spaceBetweenPanels = (int) (spaceBetweenPanels - newSize);
    }

    private void createOnClickStyle(SubtitlePanel subtitlePanel) {
        subtitlePanelStyle.createStyle(subtitlePanel);
    }

    public void populateNewWords(Map<String, Map<String, List<String>>> translatedList) {
        subtitlePanelList.forEach(this::remove);
        subtitlePanelList = new ArrayList<>();
        translatedList.entrySet().forEach(this::addSubtitlePanel);
        updateOverlay();
    }
}