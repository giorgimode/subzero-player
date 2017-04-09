package com.giorgimode.subzero.view.action;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.event.LanguagePairMenuEvent;
import com.giorgimode.subzero.event.ShowDebugEvent;
import com.giorgimode.subzero.event.ShowEffectsEvent;
import com.giorgimode.subzero.event.ShowMessagesEvent;
import com.giorgimode.subzero.event.SubtitleAddedEvent;
import com.giorgimode.subzero.view.main.AboutDialog;
import com.giorgimode.subzero.view.main.MainFrame;
import lombok.extern.slf4j.Slf4j;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;

import static com.giorgimode.subzero.Application.application;
import static com.giorgimode.subzero.view.main.MainFrame.ACTION_EXIT_FULLSCREEN;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by modeg on 2/22/2017.
 */
@Slf4j
public class ActionFactory {
    private MainFrame mainFrame;

    public ActionFactory(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    private static EmbeddedMediaPlayer mediaPlayer = application().mediaPlayerComponent().getMediaPlayer();

    public void mediaOpenAction() {
        Action mediaOpenAction = createStandardAction("menu.media.item.openFile", (actionEvent) -> {
            if (JFileChooser.APPROVE_OPTION == mainFrame.getFileChooser().showOpenDialog(mainFrame)) {
                File file = mainFrame.getFileChooser().getSelectedFile();
                String mrl = file.getAbsolutePath();
                application().addRecentMedia(mrl);
                mediaPlayer.playMedia(mrl);
            }
        });
        mainFrame.setMediaOpenAction(mediaOpenAction);
    }

    public void mediaOpenMrlAction() {
        Resource resource = Resource.resource("menu.media.item.openMrlFile");
        Action mediaOpenMrlAction = new StandardAction(resource) {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mrl = createInputDialog(resource, "Enter the media URL", "Media URL");
                while (mrl != null && isNotValidURL(mrl)) {
                    JLabel label = new JLabel("Enter the URL in the following format: http://example.com/media");
                    label.setForeground(Color.RED);
                    mrl = createInputDialog(resource, label, "Incorrect URL");
                }

                if (isNotBlank(mrl)) {
                    mediaPlayer.playMedia(mrl);
                }
            }
        };
        mainFrame.setMediaOpenMrlAction(mediaOpenMrlAction);
    }

    public void mediaQuitAction() {
        Action mediaQuitAction = createStandardAction("menu.media.item.quit", (actionEvent) -> {
            if (mainFrame.getTranslationOverlay() != null) {
                mainFrame.getTranslationOverlay().dispose();
            }
            mainFrame.dispose();
            System.exit(0);
        });
        mainFrame.setMediaQuitAction(mediaQuitAction);
    }

    public void videoFullscreenAction() {
        StandardAction videoFullscreenAction = createStandardAction("menu.video.item.fullscreen",
                (actionEvent) -> mediaPlayer.toggleFullScreen());
        mainFrame.setVideoFullscreenAction(videoFullscreenAction);
    }

    public void videoAlwaysOnTopAction() {
        StandardAction videoAlwaysOnTopAction = createStandardAction("menu.video.item.alwaysOnTop", (actionEvent) -> {
            boolean onTop;
            Object source = actionEvent.getSource();
            if (source instanceof JCheckBoxMenuItem) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) source;
                onTop = menuItem.isSelected();
            } else {
                throw new IllegalStateException("Don't know about source " + source);
            }
            mainFrame.setAlwaysOnTop(onTop);
        });
        mainFrame.setVideoAlwaysOnTopAction(videoAlwaysOnTopAction);
    }

    public void subtitleAddSubtitleFileAction() {
        StandardAction subtitleAddAction = createStandardAction("menu.subtitle.item.addSubtitleFile", (actionEvent) -> {
            if (JFileChooser.APPROVE_OPTION == mainFrame.getFileChooser().showOpenDialog(mainFrame)) {
                File file = mainFrame.getFileChooser().getSelectedFile();
                addSubtitle(file);
            }
        });
        mainFrame.setAddSubtitleFileAction(subtitleAddAction);
    }

    public void addSubtitle(File file) {
        mediaPlayer.setSubTitleFile(file);
        application().addSubtitleToCollection(file.getPath());
        application().post(new SubtitleAddedEvent(file));
    }

    public void subtitleAddSubtitleFileAction2() {
        StandardAction subtitleAddAction2 = createStandardAction("menu.subtitle.item.addSubtitleFile2", (actionEvent) -> {
            if (JFileChooser.APPROVE_OPTION == mainFrame.getFileChooser().showOpenDialog(mainFrame)) {
                File file = mainFrame.getFileChooser().getSelectedFile();
                mainFrame.getTranslatorService().addSubtitleFile2(file);
            }
        });
        mainFrame.setAddSubtitleFileAction2(subtitleAddAction2);
    }

    public void toolsEffectsAction() {
        Action toolsEffectsAction = createStandardAction("menu.tools.item.effects",
                (actionEvent) -> Application.application().post(ShowEffectsEvent.INSTANCE));
        mainFrame.setToolsEffectsAction(toolsEffectsAction);
    }

    public void languagePacksAction() {
        Action languagePacksAction = createStandardAction("menu.subtitle.item.languagepack",
                (actionEvent) -> Application.application().post(LanguagePairMenuEvent.INSTANCE));
        mainFrame.setLanguagePackAction(languagePacksAction);
    }

    public void toolsMessagesAction() {
        Action toolsMessagesAction = createStandardAction("menu.tools.item.messages",
                (actionEvent) -> Application.application().post(ShowMessagesEvent.INSTANCE));
        mainFrame.setToolsMessagesAction(toolsMessagesAction);
    }

    public void toolsDebugAction() {
        Action toolsDebugAction = createStandardAction("menu.tools.item.debug",
                (actionEvent) -> Application.application().post(ShowDebugEvent.INSTANCE));
        mainFrame.setToolsDebugAction(toolsDebugAction);
    }

    public void viewStatusBarAction() {
        StandardAction viewStatusBarAction = createStandardAction("menu.view.item.statusBar",
                (actionEvent) -> {
                    boolean visible;
                    Object source = actionEvent.getSource();
                    if (source instanceof JCheckBoxMenuItem) {
                        JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) source;
                        visible = menuItem.isSelected();
                    } else {
                        throw new IllegalStateException("Don't know about source " + source);
                    }
                    mainFrame.getStatusBar().setVisible(visible);
                    JPanel bottomPane = mainFrame.getBottomPane();
                    bottomPane.invalidate();
                    bottomPane.revalidate();
                    bottomPane.getParent().invalidate();
                    bottomPane.getParent().revalidate();
                    mainFrame.invalidate();
                    mainFrame.revalidate();
                });
        mainFrame.setViewStatusBarAction(viewStatusBarAction);
    }

    public void helpAboutAction() {
        Action helpAboutAction = createStandardAction("menu.help.item.about", (actionEvent) -> {
            AboutDialog dialog = new AboutDialog(mainFrame);
            dialog.setLocationRelativeTo(mainFrame);
            dialog.setVisible(true);
        });
        mainFrame.setHelpAboutAction(helpAboutAction);
    }

    public void exitFullScreenAction() {
        getActionMap().put(ACTION_EXIT_FULLSCREEN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.toggleFullScreen();
                mainFrame.getVideoFullscreenAction().select(false);
            }
        });
    }

    public ButtonGroup addActionFirst(List<Action> actions, JMenu menu) {
        ButtonGroup buttonGroup = addActions(actions, menu);
        Enumeration<AbstractButton> en = buttonGroup.getElements();
        if (en.hasMoreElements()) {
            StandardAction action = (StandardAction) en.nextElement().getAction();
            action.select(true);
        }
        return buttonGroup;
    }

    public ButtonGroup addActions(List<Action> actions, JMenu menu) {
        ButtonGroup buttonGroup = new ButtonGroup();
        for (Action action : actions) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(action);
            buttonGroup.add(menuItem);
            menu.add(menuItem);
        }
        return buttonGroup;
    }

    private static StandardAction createStandardAction(String title, Consumer<ActionEvent> consumer) {
        return new StandardAction(Resource.resource(title)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                consumer.accept(e);
            }
        };
    }

    private ActionMap getActionMap() {
        JComponent c = (JComponent) mainFrame.getContentPane();
        return c.getActionMap();
    }

    private boolean isNotValidURL(String mrl) {
        try {
            new URL(mrl);
            return false;
        } catch (MalformedURLException e) {
            log.error("Incorrect url format: {}", mrl);
            return true;
        }
    }

    private String createInputDialog(Resource resource, Object message, String title) {
        return (String) JOptionPane.showInputDialog(mainFrame, message, title,
                JOptionPane.INFORMATION_MESSAGE, resource.menuIcon(), null, null);
    }
}
