package com.giorgimode.subzero.translator;

import com.giorgimode.dictionary.impl.LanguageEnum;
import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.event.SubtitleAddedEvent;
import com.giorgimode.subzero.view.BaseFrame;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import static com.giorgimode.subzero.Application.application;

@SuppressWarnings("serial")
public class LanguagePackFrame extends BaseFrame {
    private ButtonGroup buttonGroup = new ButtonGroup();
    private final JButton download;


    public LanguagePackFrame() {
        super(Application.resources().getString("dialog.effects"));

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JPanel contentPane = new JPanel(null);


        JPanel allPanels = new JPanel(new GridBagLayout());
        createPanels(allPanels);

        JScrollPane jScrollPane = new JScrollPane(allPanels);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBounds(10, 10, 200, 300);

        download = new JButton("Download Againx...");
        download.setLocation(110, 410);
        download.setSize(170, 50);
        contentPane.add(download);
        contentPane.add(jScrollPane);

        add(contentPane);

        applyPreferences();
    }

    private void createPanels(JPanel contentPane) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;

        Arrays.stream(LanguageEnum.values())
                .map(this::createPanel)
                .filter(Objects::nonNull)
                .forEach((comp) -> contentPane.add(comp, gbc));
    }

    private JPanel createPanel(LanguageEnum languageEnum) {
        String languagePair = languageEnum.getValue();
        JRadioButton radioButton = new JRadioButton(languagePair.toUpperCase());
        URL iconResource1 = getClass().getResource("/icons/flags/" + languagePair.split("-")[0] + ".png");
        URL iconResource2 = getClass().getResource("/icons/flags/" + languagePair.split("-")[1] + ".png");
        URL iconResource3 = getClass().getResource("/icons/check.png");
        URL iconResource4 = getClass().getResource("/icons/no-check.png");

        if (iconResource1 == null || iconResource2 == null) {
            System.out.println("NOT FOUND: " + ((iconResource1 == null) ? languagePair.split("-")[0] :
                    languagePair.split("-")[1]));
            return null;
        }
        Icon icon1 = new ImageIcon(iconResource1);
        Icon icon2 = new ImageIcon(iconResource2);
        Icon icon3 = new ImageIcon(iconResource3);
        Icon icon4 = new ImageIcon(iconResource4);

        radioButton.setSelected(false);

        JPanel panel = new JPanel();
        JLabel label0 = new JLabel(icon3);
        JLabel label00 = new JLabel(icon4);

        JLabel label = new JLabel(icon1);
        label.setText("-");
        JLabel label2 = new JLabel(icon2);

        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        if (localLanguages() != null && localLanguages().contains(languageEnum)) {
            panel.add(label0);
        } else {
            panel.add(label00);
        }

        if (languageEnum == application().languageEnum()) {
            radioButton.setSelected(true);
        }

        panel.add(radioButton);
        panel.add(label);
        panel.add(label2);
        buttonGroup.add(radioButton);
        radioButton.addActionListener(new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (localLanguages().contains(LanguageEnum.fromString(radioButton.getText().toLowerCase()))) {
                download.setText("Download Again...");
            }
            else {
                download.setText("Download...");
            }
        }
    });

        return panel;
    }

    private void applyPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(LanguagePackFrame.class);
        setBounds(
                prefs.getInt("frameX", 300),
                prefs.getInt("frameY", 300),
                prefs.getInt("frameWidth", 500),
                prefs.getInt("frameHeight", 500)
        );
    }

    @Override
    protected void onShutdown() {
        if (wasShown()) {
            Preferences prefs = Preferences.userNodeForPackage(LanguagePackFrame.class);
            prefs.putInt("frameX", getX());
            prefs.putInt("frameY", getY());
            prefs.putInt("frameWidth", getWidth());
            prefs.putInt("frameHeight", getHeight());
        }
    }

    @Subscribe
    public void onSubtitleAddedEvent(SubtitleAddedEvent event) {
        setVisible(true);
    }

    private List<LanguageEnum> localLanguages() {
        String parentDir = application().parentDir();

        File file = new File(parentDir);
        File[] directories = file.listFiles(File::isDirectory);
        if (ArrayUtils.isEmpty(directories) || ArrayUtils.isEmpty(directories[0].listFiles())) {
            return new ArrayList<>();
        }
        return Arrays.stream(directories)
                .map(File::getName)
                .map(LanguageEnum::fromString)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
