package testers;

import com.sun.awt.AWTUtilities;
import com.sun.jna.platform.WindowUtils;

import javax.swing.JWindow;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

public class Overlay extends JWindow {

    private static final long serialVersionUID = 1L;
    private final Window owner;
    List<SubtitlePanel> subtitlePanelList;

    public Overlay(Window owner, List<String> translatedList) {
        super(owner, WindowUtils.getAlphaCompatibleGraphicsConfiguration());
        this.owner = owner;
        subtitlePanelList = new ArrayList<>();
        AWTUtilities.setWindowOpaque(this, false);
        setLayout(null);
        translatedList.forEach(this::addSubtitlePanel);
        updateLocationAll();
    }


    private void addSubtitlePanel(String text) {
        SubtitlePanel subtitlePanel = new SubtitlePanel(text);
        subtitlePanelList.add(subtitlePanel);
        add(subtitlePanel);
    }

    public void updateLocationAll() {
        int spaceBetweenPanels = owner.getHeight() / 18;
        int subtitlePanelHeight = owner.getHeight() / 25;

        int area51_below = owner.getHeight() / 4;
        int area51_left = owner.getWidth() / 35;

        subtitlePanelList.get(subtitlePanelList.size() - 1).setLocation(area51_left, owner.getHeight() - area51_below);
        subtitlePanelList.get(subtitlePanelList.size() - 1).setSize(new Dimension(owner.getWidth() - 3 * area51_left, subtitlePanelHeight));
        updateFont(subtitlePanelList.get(subtitlePanelList.size() - 1));

        if (subtitlePanelList.size() < 2) {
            return;
        }
        for (int i = subtitlePanelList.size() - 2; i >= 0; i--) {
            int previousSubtitleHeight = (int) subtitlePanelList.get(i + 1).getLocation().getY();
            subtitlePanelList.get(i).setLocation(area51_left, previousSubtitleHeight - spaceBetweenPanels - subtitlePanelHeight);
            subtitlePanelList.get(i).setSize(new Dimension(owner.getWidth() - 3 * area51_left, subtitlePanelHeight));
            updateFont(subtitlePanelList.get(i));
        }
    }

    private void updateFont(SubtitlePanel subtitlePanel) {
        Font oldfont = subtitlePanel.getTextArea().getFont();
        subtitlePanel.getTextArea().setFont(new Font(oldfont.getName(), oldfont.getStyle(), owner.getHeight() / 50));

    }

    public void populateNewWords(List<String> translatedList) {
        subtitlePanelList.forEach(this::remove);
        subtitlePanelList = new ArrayList<>();
        translatedList.forEach(this::addSubtitlePanel);
        updateLocationAll();
    }
}