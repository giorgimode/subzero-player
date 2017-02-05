package com.giorgimode.subzero.view.effects.overlay;

import com.sun.awt.AWTUtilities;
import com.sun.jna.platform.WindowUtils;

import javax.swing.JWindow;
import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Overlay extends JWindow {

    private static final long serialVersionUID = 1L;
    private final Window owner;
    private List<SubtitlePanel> subtitlePanelList;
    private int spaceBetweenPanels;
    private int subtitlePanelHeight;
    private int area51_below;
    private int area51_left;
    private final int numberOfPanelsAllowed;

    public Overlay(Window owner, Map<String, Map<String, List<String>>> translatedList) {
        super(owner, WindowUtils.getAlphaCompatibleGraphicsConfiguration());
        this.owner = owner;
        subtitlePanelList = new ArrayList<>();
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
        SubtitlePanel subtitlePanel = new SubtitlePanel(wordDefinitionEntryMap);
        subtitlePanelList.add(subtitlePanel);
        add(subtitlePanel);
    }

    public void updateOverlay() {
        if (subtitlePanelList.isEmpty()) {
            return;
        }
        setDimensions(owner);
        if (subtitlePanelList.size() > numberOfPanelsAllowed) {
            resizeSubtitlePanels(subtitlePanelList.size());
        }
        subtitlePanelList.get(subtitlePanelList.size() - 1).setLocation(area51_left, owner.getHeight() - area51_below);
        subtitlePanelList.get(subtitlePanelList.size() - 1).setSize(new Dimension(owner.getWidth() - 3 * area51_left, subtitlePanelHeight));
        updateFont(subtitlePanelList.get(subtitlePanelList.size() - 1));
        subtitlePanelList.get(subtitlePanelList.size() - 1).setMaximumAllowedHeight(owner.getHeight() + area51_below / 5);

        if (subtitlePanelList.size() < 2) {
            return;
        }
        for (int i = subtitlePanelList.size() - 2; i >= 0; i--) {
            int previousSubtitleHeight = (int) subtitlePanelList.get(i + 1).getLocation().getY();
            subtitlePanelList.get(i).setLocation(area51_left, previousSubtitleHeight - spaceBetweenPanels - subtitlePanelHeight);
            subtitlePanelList.get(i).setSize(new Dimension(owner.getWidth() - 3 * area51_left, subtitlePanelHeight));
            updateFont(subtitlePanelList.get(i));
            subtitlePanelList.get(i).setMaximumAllowedHeight(owner.getHeight());
        }
    }

    private void resizeSubtitlePanels(int numberOfPanels) {
        double newSize = (numberOfPanels * (subtitlePanelHeight + spaceBetweenPanels) - owner.getHeight() + area51_below) / (double) (2 * numberOfPanels);
        subtitlePanelHeight = (int) (subtitlePanelHeight - newSize);
        spaceBetweenPanels = (int) (spaceBetweenPanels - newSize);
    }

    private void updateFont(SubtitlePanel subtitlePanel) {
        new SubtitlePanelStyle().applyStyle(subtitlePanel);
    }

    public void populateNewWords(Map<String, Map<String, List<String>>> translatedList) {
        subtitlePanelList.forEach(this::remove);
        subtitlePanelList = new ArrayList<>();
        translatedList.entrySet().forEach(this::addSubtitlePanel);
        updateOverlay();
    }
}