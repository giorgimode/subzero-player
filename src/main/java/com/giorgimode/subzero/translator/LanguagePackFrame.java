package com.giorgimode.subzero.translator;

import com.giorgimode.dictionary.impl.LanguageEnum;
import com.giorgimode.subzero.downloader.DownloadService;
import com.giorgimode.subzero.event.LanguagePairMenuEvent;
import com.giorgimode.subzero.event.LanguagePairSwitchEvent;
import com.giorgimode.subzero.util.Utils;
import com.giorgimode.subzero.view.BaseFrame;
import com.google.common.collect.ImmutableMap;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.giorgimode.subzero.Application.application;

@SuppressWarnings("serial")
public class LanguagePackFrame extends BaseFrame {
    private ButtonGroup buttonGroup = new ButtonGroup();
    private final JButton downloadButton;
    private final JButton saveButton;
    private final JButton cancelButton;
    private List<LanguageEnum> languageEnums;
    private LanguageEnum selectedLanguage;
    private int firstPanelPosY = 5;
    private JLabel[] currentLanguageFlags;
    private Map<LanguageEnum, JLabel> currentLanguageCheckLabels;
    private JLabel currentLanguageTextLabel;
    private JFrame mainFrame;

    public LanguagePackFrame(JFrame mainFrame) {
        super("Choose Language Pair");
        this.mainFrame = mainFrame;
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JPanel contentPane = new JPanel(null);
        downloadButton = new JButton();
        saveButton = new JButton();
        cancelButton = new JButton();
        selectedLanguage = application().languageEnum();
        currentLanguageCheckLabels = new HashMap<>();
        JPanel languageScrollPanel = new JPanel(new GridBagLayout());
        createLanguagePanels(languageScrollPanel);

        JScrollPane jScrollPane = new JScrollPane(languageScrollPanel);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBounds(50, firstPanelPosY + 45, 200, 300);

        createButtons();
        JPanel currentLanguagePanel = currentLanguagePanel();

        contentPane.add(currentLanguagePanel);
        contentPane.add(downloadButton);
        contentPane.add(saveButton);
        contentPane.add(cancelButton);
        contentPane.add(jScrollPane);
        addButtonListener();

        add(contentPane);
        setBounds(450, 100, 350, 500);
    }

    private JPanel currentLanguagePanel() {
        JPanel currentLanguagePanel = new JPanel();
        currentLanguagePanel.setBounds(25, firstPanelPosY, 300, 30);
        String languagePair = application().languageEnum().getValue();
        String fullLanguageNames = mapLanguageNames(languagePair.split("-"));
        currentLanguageTextLabel = new JLabel("Current Language: " + fullLanguageNames + " ");
        Font font = currentLanguageTextLabel.getFont();
        currentLanguageTextLabel.setFont(new Font(font.getFamily(), Font.BOLD, font.getSize() * 5 / 4));
        currentLanguagePanel.add(currentLanguageTextLabel);

        currentLanguageFlags = addFlagPair(languagePair, currentLanguagePanel);

        return currentLanguagePanel;
    }

    private void createButtons() {
        saveButton.setLocation(55, firstPanelPosY + 415);
        saveButton.setSize(90, 30);
        saveButton.setText("Save");
        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                application().setLanguageEnum(selectedLanguage);
                application().post(LanguagePairSwitchEvent.INSTANCE);
                setVisible(false);
                String languagePair = selectedLanguage.getValue();

                URL languageUrl = getClass().getResource("/icons/flags/" + languagePair.split("-")[0] + ".png");
                URL languageUrl2 = getClass().getResource("/icons/flags/" + languagePair.split("-")[1] + ".png");
                if (languageUrl != null && languageUrl2 != null) {
                    currentLanguageFlags[0].setIcon(new ImageIcon(languageUrl));
                    currentLanguageFlags[1].setIcon(new ImageIcon(languageUrl2));
                }
                String fullLanguageNames = mapLanguageNames(languagePair.split("-"));
                currentLanguageTextLabel.setText("Current Language: " + fullLanguageNames + " ");
            }
        });

        cancelButton.setLocation(160, firstPanelPosY + 415);
        cancelButton.setSize(90, 30);
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedLanguage = application().languageEnum();
                setVisible(false);
                buttonGroup.clearSelection();
            }
        });

        downloadButton.setLocation(70, firstPanelPosY + 355);
        downloadButton.setSize(95, 30);
        downloadButton.setMargin(new Insets(2, 2, 2, 2));
        Font font = downloadButton.getFont();
        downloadButton.setFont(new Font(font.getFamily(), font.getStyle(), font.getSize() * 95 / 100));
    }

    private void createLanguagePanels(JPanel contentPane) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;

        Arrays.stream(LanguageEnum.values())
                .map(this::createPanel)
                .filter(Objects::nonNull)
                .forEach((panel) -> contentPane.add(panel, gbc));
    }

    private JPanel createPanel(LanguageEnum languageEnum) {
        String languagePair = languageEnum.getValue();
        JRadioButton radioButton = new JRadioButton(mapLanguageNames(languagePair.split("-")));
        radioButton.setName(languagePair.toLowerCase());
        radioButton.setSelected(false);

        JPanel languagePanel = new JPanel();
        JLabel checkLabel;

        languageEnums = localLanguages();
        if (languageEnums != null && languageEnums.contains(languageEnum)) {
            checkLabel = new JLabel(createIcon("check"));
        } else {
            checkLabel = new JLabel(createIcon("no-check"));
        }
        languagePanel.add(checkLabel);
        currentLanguageCheckLabels.put(languageEnum, checkLabel);
        if (languageEnum == application().languageEnum()) {
            radioButton.setSelected(true);
            updateDownloadButton(languageEnum);
        }


        languagePanel.add(radioButton);
        addFlagPair(languagePair, languagePanel);
        buttonGroup.add(radioButton);
        radioButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    LanguageEnum languageClicked = LanguageEnum.fromString(radioButton.getName().toLowerCase());
                    selectedLanguage = languageClicked;
                    updateDownloadButton(languageClicked);
                });
            }
        });

        return languagePanel;
    }

    private String mapLanguageNames(String[] split) {
        String lang0 = split[0];
        String lang1 = split[1];
        return LANGUAGE_MAPPING.get(lang0) + "-" + LANGUAGE_MAPPING.get(lang1);
    }

    private JLabel[] addFlagPair(String languagePair, JPanel panel) {
        ImageIcon icon = createIcon("flags/" + languagePair.split("-")[0]);
        ImageIcon icon2 = createIcon("flags/" + languagePair.split("-")[1]);
        JLabel flagLabel1 = icon != null ? new JLabel(icon) : null;
        JLabel flagLabel2 = icon2 != null ? new JLabel(icon2) : null;
        if (flagLabel1 != null && flagLabel2 != null) {
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(flagLabel1);
            panel.add(new JLabel("-"));
            panel.add(flagLabel2);
        }
        return new JLabel[]{flagLabel1, flagLabel2};
    }

    private void updateDownloadButton(LanguageEnum currentLanguage) {
        if (languageEnums.contains(currentLanguage)) {
            downloadButton.setText("Download Again...");
            saveButton.setEnabled(true);
        } else {
            downloadButton.setText("Download...");
            saveButton.setEnabled(false);
        }
    }

    private ImageIcon createIcon(String lang) {
        URL languageUrl = getClass().getResource("/icons/" + lang + ".png");
        if (languageUrl == null) {
            return null;
        }
        return new ImageIcon(languageUrl);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onLanguagePairMenuEvent(LanguagePairMenuEvent event) {
        setVisible(true);
    }

    private List<LanguageEnum> localLanguages() {
        String parentDir = Utils.parentDir();

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

    private void addButtonListener() {
        downloadButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("button clicked: " + selectedLanguage.getValue());
                SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        DownloadService downloadService = new DownloadService();
                        return downloadService.downloadLanguagePack(selectedLanguage);
                    }

                    @Override
                    protected void done() {
                        boolean isSuccessful = false;
                        try {
                            isSuccessful = get();
                        } catch (InterruptedException | ExecutionException e1) {
                            e1.printStackTrace();
                        } finally {
                            String result;
                            String title;
                            int informationMessage;

                            if (isSuccessful) {
                                result = "Language pair was download successfully";
                                title = "Download Successful";
                                informationMessage = JOptionPane.INFORMATION_MESSAGE;
                                languageEnums.add(selectedLanguage);
                                updateDownloadButton(selectedLanguage);

                                Icon languageIcon = createIcon("check");
                                if (languageIcon != null) {
                                    currentLanguageCheckLabels.get(selectedLanguage).setIcon(languageIcon);
                                }
                            } else {
                                result = "Language pair download failed. Please try again later";
                                title = "Download Failed";
                                informationMessage = JOptionPane.ERROR_MESSAGE;
                            }
                            JOptionPane.showMessageDialog(mainFrame, result, title, informationMessage);
                        }
                    }
                };
                swingWorker.execute();
            }
        });
    }

    private static final ImmutableMap<String, String> LANGUAGE_MAPPING = new ImmutableMap.Builder<String, String>()
            .put("bg", "Bulgarian")
            .put("bs", "Bosnian")
            .put("cs", "Czech")
            .put("da", "Danish")
            .put("de", "German")
            .put("el", "Greek")
            .put("en", "English")
            .put("eo", "Esperanto")
            .put("es", "Spanish")
            .put("fi", "Finnish")
            .put("fr", "French")
            .put("hr", "Croatian")
            .put("hu", "Hungarian")
            .put("is", "Icelandic")
            .put("it", "Italian")
            .put("la", "Latin")
            .put("nl", "Dutch")
            .put("no", "Norwegian")
            .put("pl", "Polish")
            .put("pt", "Portuguese")
            .put("ro", "Romanian")
            .put("ru", "Russian")
            .put("sk", "Slovak")
            .put("sq", "Albanian")
            .put("sr", "Serbian")
            .put("sv", "Swedish")
            .put("tr", "Turkish")
            .build();
}
