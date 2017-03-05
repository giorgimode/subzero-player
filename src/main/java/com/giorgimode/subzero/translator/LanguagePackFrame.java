package com.giorgimode.subzero.translator;

import com.giorgimode.dictionary.impl.LanguageEnum;
import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.event.LanguagePairMenuEvent;
import com.giorgimode.subzero.event.LanguagePairSwitchEvent;
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
    private final JButton downloadButton;
    private List<LanguageEnum> languageEnums;


    public LanguagePackFrame() {
        super(Application.resources().getString("dialog.effects"));

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JPanel contentPane = new JPanel(null);
        downloadButton = new JButton();

        JPanel languageRootPanel = new JPanel(new GridBagLayout());
        createLanguagePanels(languageRootPanel);

        JScrollPane jScrollPane = new JScrollPane(languageRootPanel);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBounds(50, 10, 200, 300);

        downloadButton.setLocation(70, 320);
        downloadButton.setSize(130, 50);
        contentPane.add(downloadButton);
        contentPane.add(jScrollPane);

        add(contentPane);

        applyPreferences();
    }

    private void createLanguagePanels(JPanel contentPane) {
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
        radioButton.setSelected(false);

        JPanel languagePanel = new JPanel();
        JLabel checkLabel = createLabel("check");
        JLabel noCheckLabel = createLabel("no-check");
        JLabel flagLabel1 = createLabel("flags/" + languagePair.split("-")[0]);
        JLabel flagLabel2 = createLabel("flags/" + languagePair.split("-")[1]);
        if (flagLabel1 == null || flagLabel2 == null) {
            System.out.println("NOT FOUND: " + ((flagLabel1 == null) ? languagePair.split("-")[0] :
                    languagePair.split("-")[1]));
            return null;
        }
        flagLabel1.setText("-");

        languagePanel.setLayout(new BoxLayout(languagePanel, BoxLayout.X_AXIS));
        languageEnums = localLanguages();
        if (languageEnums != null && languageEnums.contains(languageEnum)) {
            languagePanel.add(checkLabel);
        } else {
            languagePanel.add(noCheckLabel);
        }

        if (languageEnum == application().languageEnum()) {
            radioButton.setSelected(true);
            updateDownloadButton(languageEnum);
        }

        languagePanel.add(radioButton);
        languagePanel.add(flagLabel1);
        languagePanel.add(flagLabel2);
        buttonGroup.add(radioButton);
        radioButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LanguageEnum currentLanguage = LanguageEnum.fromString(radioButton.getText().toLowerCase());
                Application.application().setLanguageEnum(currentLanguage);
                updateDownloadButton(currentLanguage);
                application().post(LanguagePairSwitchEvent.INSTANCE);
            }
        });

        return languagePanel;
    }

    private void updateDownloadButton(LanguageEnum currentLanguage) {
        if (languageEnums.contains(currentLanguage)) {
            downloadButton.setText("Download Again...");
        } else {
            downloadButton.setText("Download...");
        }
    }

    private JLabel createLabel(String lang) {
        URL languageUrl = getClass().getResource("/icons/" + lang + ".png");
        if (languageUrl == null) {
            return null;
        }
        Icon languageIcon = new ImageIcon(languageUrl);

        return new JLabel(languageIcon);
    }

    private void applyPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(LanguagePackFrame.class);
        setBounds(
                prefs.getInt("frameX", 300),
                prefs.getInt("frameY", 300),
                prefs.getInt("frameWidth", 320),
                prefs.getInt("frameHeight", 430)
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
    public void onLanguagePairMenuEvent(LanguagePairMenuEvent event) {
        setVisible(true);
    }

    private List<LanguageEnum> localLanguages() {
        String parentDir = application().parentDir();

        File file = new File(parentDir);
        File[] directories = file.listFiles(File::isDirectory);
        if (ArrayUtils.isEmpty(directories)) {
            return new ArrayList<>();
        }
        return Arrays.stream(directories)
                .filter(directory -> ArrayUtils.isNotEmpty(directory.listFiles()))
                .map(File::getName)
                .map(LanguageEnum::fromString)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
