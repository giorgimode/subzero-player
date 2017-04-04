package com.giorgimode.subzero.view.main;

import com.giorgimode.dictionary.impl.LanguageEnum;
import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.customHandler.VlcjPlayerEventAdapter;
import com.giorgimode.subzero.event.SubtitleAddedEvent;
import com.giorgimode.subzero.translator.TranslatorService;
import com.giorgimode.subzero.view.BaseFrame;
import com.giorgimode.subzero.view.MouseMovementDetector;
import com.giorgimode.subzero.view.action.ActionFactory;
import com.giorgimode.subzero.view.action.Resource;
import com.giorgimode.subzero.view.action.StandardAction;
import com.giorgimode.subzero.view.action.mediaplayer.MediaPlayerActions;
import com.giorgimode.subzero.view.effects.overlay.TranslationOverlay;
import lombok.Getter;
import lombok.Setter;
import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.Preferences;

import static com.giorgimode.subzero.Application.application;

@SuppressWarnings("serial")
public final class MainFrame extends BaseFrame {

    public static final String ACTION_EXIT_FULLSCREEN = "exit-fullscreen";

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private final ActionFactory actionFactory;
    @Setter
    private Action mediaOpenAction;
    @Setter
    private Action mediaQuitAction;
    @Setter
    @Getter
    private StandardAction videoFullscreenAction;
    @Setter
    private StandardAction videoAlwaysOnTopAction;
    @Getter
    private TranslatorService translatorService;
    @Setter
    private Action addSubtitleFileAction;
    @Setter
    private Action addSubtitleFileAction2;
    @Setter
    private Action languagePackAction;
    @Setter
    private Action toolsEffectsAction;
    @Setter
    private Action toolsMessagesAction;
    @Setter
    private Action toolsDebugAction;
    @Setter
    private StandardAction viewStatusBarAction;
    @Setter
    private Action helpAboutAction;
    @Getter
    private final PlayerMenuBar playerMenuBar;

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
    private final JMenu overlayMenu;
    private final JMenu toolsMenu;

    private final JMenu viewMenu;

    private final JMenu helpMenu;
    @Getter
    private JFileChooser fileChooser;

    private final PositionPane positionPane;

    private final ControlsPane controlsPane;
    @Getter
    private StatusBar statusBar;

    private final VideoContentPane videoContentPane;
    @Getter
    private JPanel bottomPane;

    private final MouseMovementDetector mouseMovementDetector;
    @Getter
    private final TranslationOverlay translationOverlay;

    private final EmbeddedMediaPlayer mediaPlayer;

    public MainFrame() {
        super("SubZero player");

        this.mediaPlayerComponent = application().mediaPlayerComponent();
        mediaPlayer = mediaPlayerComponent.getMediaPlayer();
        actionFactory = new ActionFactory(this);
        new MainEventHandler(this);
        mediaPlayerComponent.getVideoSurface().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mediaPlayer.toggleFullScreen();
                }
            }
        });

        mediaPlayerComponent.getVideoSurface().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (mediaPlayer.isFullScreen() && mediaPlayer.isPlaying()) {
                    playerMenuBar.setVisible(e.getYOnScreen() < mediaPlayerComponent.getVideoSurface().getHeight() * 0.1);
                    bottomPane.setVisible(e.getYOnScreen() > mediaPlayerComponent.getVideoSurface().getHeight() * 0.85);
                }
            }
        });

        mediaPlayerComponent.getVideoSurface().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                playerMenuBar.processKeyEvent(e, mediaPlayer.isFullScreen());
                playerMenuBar.setVisible(!mediaPlayer.isFullScreen());
            }
        });

        MediaPlayerActions mediaPlayerActions = application().mediaPlayerActions();
        createStandardActions();

        playerMenuBar = new PlayerMenuBar();

        mediaMenu = new JMenu(resourceName("menu.media"));
        mediaMenu.setMnemonic(resourceMnemonic("menu.media"));
        mediaMenu.add(new JMenuItem(mediaOpenAction));
        mediaRecentMenu = new RecentMediaMenu(Resource.resource("menu.media.item.recent")).menu();
        mediaMenu.add(mediaRecentMenu);
        mediaMenu.add(new JSeparator());
        mediaMenu.add(new JMenuItem(mediaQuitAction));
        playerMenuBar.add(mediaMenu);

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
        playerMenuBar.add(playbackMenu);

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
        playerMenuBar.add(audioMenu);

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
        actionFactory.addActions(mediaPlayerActions
                .videoZoomActions(), videoZoomMenu/*, true*/); // FIXME how to handle zoom 1:1 and fit to window - also, probably should not use addActions to select
        videoMenu.add(videoZoomMenu);
        videoAspectRatioMenu = new JMenu(resourceName("menu.video.item.aspectRatio"));
        videoAspectRatioMenu.setMnemonic(resourceMnemonic("menu.video.item.aspectRatio"));
        actionFactory.addActions(mediaPlayerActions.videoAspectRatioActions(), videoAspectRatioMenu, true);
        videoMenu.add(videoAspectRatioMenu);
        videoCropMenu = new JMenu(resourceName("menu.video.item.crop"));
        videoCropMenu.setMnemonic(resourceMnemonic("menu.video.item.crop"));
        actionFactory.addActions(mediaPlayerActions.videoCropActions(), videoCropMenu, true);
        videoMenu.add(videoCropMenu);
        videoMenu.add(new JSeparator());
        videoMenu.add(new JMenuItem(mediaPlayerActions.videoSnapshotAction()));
        playerMenuBar.add(videoMenu);

        subtitleMenu = new JMenu(resourceName("menu.subtitle"));
        subtitleMenu.setMnemonic(resourceMnemonic("menu.subtitle"));
        subtitleMenu.add(new JMenuItem(addSubtitleFileAction));
        subtitleMenu.add(new JMenuItem(addSubtitleFileAction2));
        subtitleMenu.add(new JMenuItem(languagePackAction));

        subtitleTrackMenu = new SubtitleTrackMenu().menu();
        overlayMenu = new OverlayMenu().menu();
        subtitleMenu.add(subtitleTrackMenu);
        subtitleMenu.add(overlayMenu);
        playerMenuBar.add(subtitleMenu);

        toolsMenu = new JMenu(resourceName("menu.tools"));
        toolsMenu.setMnemonic(resourceMnemonic("menu.tools"));
        toolsMenu.add(new JMenuItem(toolsEffectsAction));
        toolsMenu.add(new JMenuItem(toolsMessagesAction));
        toolsMenu.add(new JSeparator());
        toolsMenu.add(new JMenuItem(toolsDebugAction));
        playerMenuBar.add(toolsMenu);

        viewMenu = new JMenu(resourceName("menu.view"));
        viewMenu.setMnemonic(resourceMnemonic("menu.view"));
        viewMenu.add(new JCheckBoxMenuItem(viewStatusBarAction));
        playerMenuBar.add(viewMenu);

        helpMenu = new JMenu(resourceName("menu.help"));
        helpMenu.setMnemonic(resourceMnemonic("menu.help"));
        helpMenu.add(new JMenuItem(helpAboutAction));
        playerMenuBar.add(helpMenu);

        setJMenuBar(playerMenuBar);

        videoContentPane = new VideoContentPane();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(videoContentPane, BorderLayout.CENTER);
        contentPane.setTransferHandler(new MediaTransferHandler() {
            @Override
            protected void onMediaDropped(String[] uris) {
                String uri = uris[0];
                if (uri.endsWith(".srt")) {
                    mediaPlayer.setSubTitleFile(uri);
                    translatorService.addSubtitleFile(new File(uri));
                    Application.application().post(SubtitleAddedEvent.INSTANCE);
                } else {
                    mediaPlayer.playMedia(uri);
                }
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
                SwingUtilities.invokeLater(translationOverlay::updateOverlay);
            }
        });

        translationOverlay = new TranslationOverlay(this, new HashMap<>());
        mediaPlayer.setOverlay(translationOverlay);
        mediaPlayerComponent.getVideoSurface().requestFocusInWindow();
        applyPreferences();
        translatorService = new TranslatorService();


        setMinimumSize(new Dimension(370, 240));
    }

    private Integer resourceMnemonic(String id) {
        return Resource.resource(id).mnemonic();
    }

    private String resourceName(String id) {
        return Resource.resource(id).name();
    }

    private void createStandardActions() {
        actionFactory.mediaOpenAction();
        actionFactory.mediaQuitAction();
        actionFactory.videoFullscreenAction();
        actionFactory.videoAlwaysOnTopAction();
        actionFactory.subtitleAddSubtitleFileAction();
        actionFactory.subtitleAddSubtitleFileAction2();
        actionFactory.languagePacksAction();
        actionFactory.toolsEffectsAction();
        actionFactory.toolsMessagesAction();
        actionFactory.toolsDebugAction();
        actionFactory.viewStatusBarAction();
        actionFactory.helpAboutAction();
        actionFactory.exitFullScreenAction();
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
                application().addRecentMedia(mrl);
            }
        }
        String languagePack = prefs.get("languagePack", "");
        if (languagePack != null && !languagePack.isEmpty()) {
            application().setLanguageEnum(LanguageEnum.fromString(languagePack));
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
            LanguageEnum languageEnum = application().languageEnum();
            if (languageEnum != null) {
                prefs.put("languagePack", languageEnum.getValue());
            }

            String recentMedia;
            List<String> mrls = application().recentMedia();
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
}
