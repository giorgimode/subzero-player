package com.giorgimode.subzero.view.main;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.customHandler.ClickListener;
import com.giorgimode.subzero.customHandler.VlcjPlayerEventAdapter;
import com.giorgimode.subzero.event.AfterExitFullScreenEvent;
import com.giorgimode.subzero.event.BeforeEnterFullScreenEvent;
import com.giorgimode.subzero.event.ShowDebugEvent;
import com.giorgimode.subzero.event.ShowEffectsEvent;
import com.giorgimode.subzero.event.ShowMessagesEvent;
import com.giorgimode.subzero.event.SnapshotImageEvent;
import com.giorgimode.subzero.event.SubtitleAddedEvent;
import com.giorgimode.subzero.service.EnhancedTranslatorService;
import com.giorgimode.subzero.view.BaseFrame;
import com.giorgimode.subzero.view.MouseMovementDetector;
import com.giorgimode.subzero.view.action.Resource;
import com.giorgimode.subzero.view.action.StandardAction;
import com.giorgimode.subzero.view.action.mediaplayer.MediaPlayerActions;
import com.giorgimode.subzero.view.effects.overlay.Overlay;
import com.giorgimode.subzero.view.snapshot.SnapshotView;
import com.google.common.eventbus.Subscribe;
import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

@SuppressWarnings("serial")
public final class MainFrame extends BaseFrame {

    private static final String ACTION_EXIT_FULLSCREEN = "exit-fullscreen";

    private static final KeyStroke KEYSTROKE_ESCAPE = KeyStroke.getKeyStroke("ESCAPE");

    private static final KeyStroke KEYSTROKE_TOGGLE_FULLSCREEN = KeyStroke.getKeyStroke("F11");

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private Action mediaOpenAction;
    private Action mediaQuitAction;

    private StandardAction videoFullscreenAction;
    private StandardAction videoAlwaysOnTopAction;

    private EnhancedTranslatorService enhancedTranslatorService;

    private Action subtitleAddSubtitleFileAction;

    private Action toolsEffectsAction;
    private Action toolsMessagesAction;
    private Action toolsDebugAction;

    private StandardAction viewStatusBarAction;

    private Action helpAboutAction;

    private final JMenuBar menuBar;

    private final JMenu mediaMenu;
    private final JMenu mediaRecentMenu;

    private final JMenu playbackMenu;
    private final JMenu playbackTitleMenu;
    private final JMenu playbackChapterMenu;
    private final JMenu playbackSpeedMenu;

    private final JMenu audioMenu;
    private final JMenu audioTrackMenu;
    private final JMenu audioDeviceMenu;
    private final JMenu audioStereoMenu;

    private final JMenu videoMenu;
    private final JMenu videoTrackMenu;
    private final JMenu videoZoomMenu;
    private final JMenu videoAspectRatioMenu;
    private final JMenu videoCropMenu;

    private final JMenu subtitleMenu;
    private final JMenu subtitleTrackMenu;

    private final JMenu toolsMenu;

    private final JMenu viewMenu;

    private final JMenu helpMenu;

    private JFileChooser fileChooser;

    private final PositionPane positionPane;

    private final ControlsPane controlsPane;

    private StatusBar statusBar;

    private final VideoContentPane videoContentPane;

    private JPanel bottomPane;

    private final MouseMovementDetector mouseMovementDetector;

    private final Overlay overlay;

    private final EmbeddedMediaPlayer mediaPlayer;

    public MainFrame() {
        super("SubZero player");

        this.mediaPlayerComponent = Application.application().mediaPlayerComponent();
        mediaPlayer = mediaPlayerComponent.getMediaPlayer();
        mediaPlayerComponent.getVideoSurface().addMouseListener(new ClickListener() {
            public void singleClick(MouseEvent e) {
                mediaPlayer.pause();
            }

            public void doubleClick(MouseEvent e) {
                mediaPlayer.toggleFullScreen();
            }
        });
        MediaPlayerActions mediaPlayerActions = Application.application().mediaPlayerActions();

        createStandardActions();

        enhancedTranslatorService = new EnhancedTranslatorService();

        menuBar = new JMenuBar();

        mediaMenu = new JMenu(resourceName("menu.media"));
        mediaMenu.setMnemonic(resourceMnemonic("menu.media"));
        mediaMenu.add(new JMenuItem(mediaOpenAction));
        mediaRecentMenu = new RecentMediaMenu(Resource.resource("menu.media.item.recent")).menu();
        mediaMenu.add(mediaRecentMenu);
        mediaMenu.add(new JSeparator());
        mediaMenu.add(new JMenuItem(mediaQuitAction));
        menuBar.add(mediaMenu);

        playbackMenu = new JMenu(resourceName("menu.playback"));
        playbackMenu.setMnemonic(resourceMnemonic("menu.playback"));

        playbackTitleMenu = new TitleTrackMenu().menu();

        // Chapter could be an "on-demand" menu too, and it could be with radio buttons...

        playbackMenu.add(playbackTitleMenu);

        playbackChapterMenu = new ChapterMenu().menu();

        playbackMenu.add(playbackChapterMenu);
        playbackMenu.add(new JSeparator());
        playbackSpeedMenu = new JMenu(resourceName("menu.playback.item.speed"));
        playbackSpeedMenu.setMnemonic(resourceMnemonic("menu.playback.item.speed"));
        for (Action action : mediaPlayerActions.playbackSpeedActions()) {
            playbackSpeedMenu.add(new JMenuItem(action));
        }
        playbackMenu.add(playbackSpeedMenu);
        playbackMenu.add(new JSeparator());
        for (Action action : mediaPlayerActions.playbackSkipActions()) {
            playbackMenu.add(new JMenuItem(action));
        }
        playbackMenu.add(new JSeparator());
        for (Action action : mediaPlayerActions.playbackChapterActions()) {
            playbackMenu.add(new JMenuItem(action));
        }
        playbackMenu.add(new JSeparator());
        for (Action action : mediaPlayerActions.playbackControlActions()) {
            playbackMenu.add(new JMenuItem(action) { // FIXME need a standardmenuitem that disables the tooltip like this, very poor show...
                @Override
                public String getToolTipText() {
                    return null;
                }
            });
        }
        menuBar.add(playbackMenu);

        audioMenu = new JMenu(resourceName("menu.audio"));
        audioMenu.setMnemonic(resourceMnemonic("menu.audio"));

        audioTrackMenu = new AudioTrackMenu().menu();

        audioMenu.add(audioTrackMenu);
        audioDeviceMenu = new AudioDeviceMenu().menu();
        audioMenu.add(audioDeviceMenu);
        audioStereoMenu = new JMenu(resourceName("menu.audio.item.stereoMode"));
        audioStereoMenu.setMnemonic(resourceMnemonic("menu.audio.item.stereoMode"));
        for (Action action : mediaPlayerActions.audioStereoModeActions()) {
            audioStereoMenu.add(new JRadioButtonMenuItem(action));
        }
        audioMenu.add(audioStereoMenu);
        audioMenu.add(new JSeparator());
        for (Action action : mediaPlayerActions.audioControlActions()) {
            audioMenu.add(new JMenuItem(action));
        }
        menuBar.add(audioMenu);

        videoMenu = new JMenu(resourceName("menu.video"));
        videoMenu.setMnemonic(resourceMnemonic("menu.video"));

        videoTrackMenu = new VideoTrackMenu().menu();

        videoMenu.add(videoTrackMenu);
        videoMenu.add(new JSeparator());
        videoMenu.add(new JCheckBoxMenuItem(videoFullscreenAction));
        videoMenu.add(new JCheckBoxMenuItem(videoAlwaysOnTopAction));
        videoMenu.add(new JSeparator());
        videoZoomMenu = new JMenu(resourceName("menu.video.item.zoom"));
        videoZoomMenu.setMnemonic(resourceMnemonic("menu.video.item.zoom"));
        addActions(mediaPlayerActions.videoZoomActions(), videoZoomMenu/*, true*/); // FIXME how to handle zoom 1:1 and fit to window - also, probably should not use addActions to select
        videoMenu.add(videoZoomMenu);
        videoAspectRatioMenu = new JMenu(resourceName("menu.video.item.aspectRatio"));
        videoAspectRatioMenu.setMnemonic(resourceMnemonic("menu.video.item.aspectRatio"));
        addActions(mediaPlayerActions.videoAspectRatioActions(), videoAspectRatioMenu, true);
        videoMenu.add(videoAspectRatioMenu);
        videoCropMenu = new JMenu(resourceName("menu.video.item.crop"));
        videoCropMenu.setMnemonic(resourceMnemonic("menu.video.item.crop"));
        addActions(mediaPlayerActions.videoCropActions(), videoCropMenu, true);
        videoMenu.add(videoCropMenu);
        videoMenu.add(new JSeparator());
        videoMenu.add(new JMenuItem(mediaPlayerActions.videoSnapshotAction()));
        menuBar.add(videoMenu);

        subtitleMenu = new JMenu(resourceName("menu.subtitle"));
        subtitleMenu.setMnemonic(resourceMnemonic("menu.subtitle"));
        subtitleMenu.add(new JMenuItem(subtitleAddSubtitleFileAction));

        subtitleTrackMenu = new SubtitleTrackMenu().menu();

        subtitleMenu.add(subtitleTrackMenu);
        menuBar.add(subtitleMenu);

        toolsMenu = new JMenu(resourceName("menu.tools"));
        toolsMenu.setMnemonic(resourceMnemonic("menu.tools"));
        toolsMenu.add(new JMenuItem(toolsEffectsAction));
        toolsMenu.add(new JMenuItem(toolsMessagesAction));
        toolsMenu.add(new JSeparator());
        toolsMenu.add(new JMenuItem(toolsDebugAction));
        menuBar.add(toolsMenu);

        viewMenu = new JMenu(resourceName("menu.view"));
        viewMenu.setMnemonic(resourceMnemonic("menu.view"));
        viewMenu.add(new JCheckBoxMenuItem(viewStatusBarAction));
        menuBar.add(viewMenu);

        helpMenu = new JMenu(resourceName("menu.help"));
        helpMenu.setMnemonic(resourceMnemonic("menu.help"));
        helpMenu.add(new JMenuItem(helpAboutAction));
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        videoContentPane = new VideoContentPane();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(videoContentPane, BorderLayout.CENTER);
        contentPane.setTransferHandler(new MediaTransferHandler() {
            @Override
            protected void onMediaDropped(String[] uris) {
                mediaPlayer.playMedia(uris[0]);
            }
        });

        setContentPane(contentPane);

        fileChooser = new JFileChooser();

        bottomPane = new JPanel();
        bottomPane.setLayout(new BorderLayout());

        JPanel bottomControlsPane = new JPanel();
        bottomControlsPane.setLayout(new MigLayout("fill, insets 0 n n n", "[grow]", "[]0[]"));

        positionPane = new PositionPane(mediaPlayer);
        bottomControlsPane.add(positionPane, "grow, wrap");

        controlsPane = new ControlsPane(mediaPlayerActions);
        bottomPane.add(bottomControlsPane, BorderLayout.CENTER);
        bottomControlsPane.add(controlsPane, "grow");

        statusBar = new StatusBar();
        bottomPane.add(statusBar, BorderLayout.SOUTH);

        contentPane.add(bottomPane, BorderLayout.SOUTH);
        mouseMovementDetector = new VideoMouseMovementDetector(mediaPlayerComponent.getVideoSurface(), 500, mediaPlayerComponent);

        MediaPlayerEventAdapter playerEventAdapter = new VlcjPlayerEventAdapter()
                .setMouseMovementDetector(mouseMovementDetector)
                .setVideoContentPane(videoContentPane)
                .setStatusBar(statusBar)
                .setPositionPane(positionPane)
                .setFileChooser(fileChooser)
                .setParentComponent(this);

        mediaPlayer.addMediaPlayerEventListener(playerEventAdapter);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                overlay.updateOverlay();
            }
        });

        getActionMap().put(ACTION_EXIT_FULLSCREEN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.toggleFullScreen();
                videoFullscreenAction.select(false);
            }
        });

        overlay = new Overlay(this, new HashMap<>());
        mediaPlayer.setOverlay(overlay);
        mediaPlayer.enableOverlay(false);

        applyPreferences();


        setMinimumSize(new Dimension(370, 240));
    }

    private Integer resourceMnemonic(String id) {
        return Resource.resource(id).mnemonic();
    }

    private String resourceName(String id) {
        return Resource.resource(id).name();
    }

    private void createStandardActions() {
        mediaOpenAction = createStandardAction("menu.media.item.openFile", (actionEvent) -> {
            if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(MainFrame.this)) {
                File file = fileChooser.getSelectedFile();
                String mrl = file.getAbsolutePath();
                Application.application().addRecentMedia(mrl);
                mediaPlayer.playMedia(mrl);
            }
        });

        mediaQuitAction = createStandardAction("menu.media.item.quit", (actionEvent) -> {
            dispose();
            System.exit(0);
        });

        videoFullscreenAction = createStandardAction("menu.video.item.fullscreen",
                (actionEvent) -> mediaPlayer.toggleFullScreen());


        videoAlwaysOnTopAction = createStandardAction("menu.video.item.alwaysOnTop", (actionEvent) -> {
            boolean onTop;
            Object source = actionEvent.getSource();
            if (source instanceof JCheckBoxMenuItem) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) source;
                onTop = menuItem.isSelected();
            } else {
                throw new IllegalStateException("Don't know about source " + source);
            }
            setAlwaysOnTop(onTop);
        });

        subtitleAddSubtitleFileAction = createStandardAction("menu.subtitle.item.addSubtitleFile", (actionEvent) -> {
            if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(MainFrame.this)) {
                File file = fileChooser.getSelectedFile();
                mediaPlayer.setSubTitleFile(file);
                enhancedTranslatorService.add(file);
                Application.application().post(SubtitleAddedEvent.INSTANCE);
            }
        });

        toolsEffectsAction = createStandardAction("menu.tools.item.effects",
                (actionEvent) -> Application.application().post(ShowEffectsEvent.INSTANCE));

        toolsMessagesAction = createStandardAction("menu.tools.item.messages",
                (actionEvent) -> Application.application().post(ShowMessagesEvent.INSTANCE));

        toolsDebugAction = createStandardAction("menu.tools.item.debug",
                (actionEvent) -> Application.application().post(ShowDebugEvent.INSTANCE));

        viewStatusBarAction = createStandardAction("menu.view.item.statusBar",
                (actionEvent) -> {
                    boolean visible;
                    Object source = actionEvent.getSource();
                    if (source instanceof JCheckBoxMenuItem) {
                        JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) source;
                        visible = menuItem.isSelected();
                    } else {
                        throw new IllegalStateException("Don't know about source " + source);
                    }
                    statusBar.setVisible(visible);
                    bottomPane.invalidate();
                    bottomPane.revalidate();
                    bottomPane.getParent().invalidate();
                    bottomPane.getParent().revalidate();
                    MainFrame.this.invalidate();
                    MainFrame.this.revalidate();
                });

        helpAboutAction = createStandardAction("menu.help.item.about", (actionEvent) -> {
            AboutDialog dialog = new AboutDialog(MainFrame.this);
            dialog.setLocationRelativeTo(MainFrame.this);

            dialog.setVisible(true);
        });
    }

    private ButtonGroup addActions(List<Action> actions, JMenu menu, boolean selectFirst) {
        ButtonGroup buttonGroup = addActions(actions, menu);
        if (selectFirst) {
            Enumeration<AbstractButton> en = buttonGroup.getElements();
            if (en.hasMoreElements()) {
                StandardAction action = (StandardAction) en.nextElement().getAction();
                action.select(true);
            }
        }
        return buttonGroup;
    }

    private ButtonGroup addActions(List<Action> actions, JMenu menu) {
        ButtonGroup buttonGroup = new ButtonGroup();
        for (Action action : actions) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(action);
            buttonGroup.add(menuItem);
            menu.add(menuItem);
        }
        return buttonGroup;
    }

    private void applyPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(MainFrame.class);
        setBounds(
                prefs.getInt("frameX", 100),
                prefs.getInt("frameY", 100),
                prefs.getInt("frameWidth", 800),
                prefs.getInt("frameHeight", 600)
        );
        boolean alwaysOnTop = prefs.getBoolean("alwaysOnTop", false);
        setAlwaysOnTop(alwaysOnTop);
        videoAlwaysOnTopAction.select(alwaysOnTop);
        boolean statusBarVisible = prefs.getBoolean("statusBar", false);
        statusBar.setVisible(statusBarVisible);
        viewStatusBarAction.select(statusBarVisible);
        fileChooser.setCurrentDirectory(new File(prefs.get("chooserDirectory", ".")));
        String recentMedia = prefs.get("recentMedia", "");
        if (recentMedia.length() > 0) {
            List<String> mrls = Arrays.asList(prefs.get("recentMedia", "").split("\\|"));
            Collections.reverse(mrls);
            for (String mrl : mrls) {
                Application.application().addRecentMedia(mrl);
            }
        }
    }

    @Override
    protected void onShutdown() {
        if (wasShown()) {
            Preferences prefs = Preferences.userNodeForPackage(MainFrame.class);
            prefs.putInt("frameX", getX());
            prefs.putInt("frameY", getY());
            prefs.putInt("frameWidth", getWidth());
            prefs.putInt("frameHeight", getHeight());
            prefs.putBoolean("alwaysOnTop", isAlwaysOnTop());
            prefs.putBoolean("statusBar", statusBar.isVisible());
            prefs.put("chooserDirectory", fileChooser.getCurrentDirectory().toString());

            String recentMedia;
            List<String> mrls = Application.application().recentMedia();
            if (!mrls.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String mrl : mrls) {
                    if (sb.length() > 0) {
                        sb.append('|');
                    }
                    sb.append(mrl);
                }
                recentMedia = sb.toString();
            } else {
                recentMedia = "";
            }
            prefs.put("recentMedia", recentMedia);
        }
    }

    @Subscribe
    public void onBeforeEnterFullScreen(BeforeEnterFullScreenEvent event) {
        menuBar.setVisible(false);
        bottomPane.setVisible(false);
        // As the menu is now hidden, the shortcut will not work, so register a temporary key-binding
        registerEscapeBinding();
    }

    @Subscribe
    public void onAfterExitFullScreen(AfterExitFullScreenEvent event) {
        deregisterEscapeBinding();
        menuBar.setVisible(true);
        bottomPane.setVisible(true);
    }

    @Subscribe
    public void onSnapshotImage(SnapshotImageEvent event) {
        new SnapshotView(event.image());
    }

    private void registerEscapeBinding() {
        getInputMap().put(KEYSTROKE_ESCAPE, ACTION_EXIT_FULLSCREEN);
        getInputMap().put(KEYSTROKE_TOGGLE_FULLSCREEN, ACTION_EXIT_FULLSCREEN);
    }

    private void deregisterEscapeBinding() {
        getInputMap().remove(KEYSTROKE_ESCAPE);
        getInputMap().remove(KEYSTROKE_TOGGLE_FULLSCREEN);
    }

    private InputMap getInputMap() {
        JComponent c = (JComponent) getContentPane();
        return c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private ActionMap getActionMap() {
        JComponent c = (JComponent) getContentPane();
        return c.getActionMap();
    }

    private StandardAction createStandardAction(String title, Consumer<ActionEvent> consumer) {
        return new StandardAction(Resource.resource(title)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                consumer.accept(e);
            }
        };
    }
}
