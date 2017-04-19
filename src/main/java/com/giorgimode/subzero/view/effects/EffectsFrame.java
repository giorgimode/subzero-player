package com.giorgimode.subzero.view.effects;

import com.giorgimode.subzero.Application;
import com.giorgimode.subzero.event.ShowEffectsEvent;
import com.giorgimode.subzero.view.BaseFrame;
import com.giorgimode.subzero.view.effects.audio.AudioEffectsPanel;
import com.giorgimode.subzero.view.effects.video.VideoEffectsPanel;
import com.google.common.eventbus.Subscribe;
import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.util.prefs.Preferences;

@SuppressWarnings("serial")
public class EffectsFrame extends BaseFrame {

    public EffectsFrame() {
        super(Application.resources().getString("dialog.effects"));

        JTabbedPane tabbedPane = new JTabbedPane();

        AudioEffectsPanel audioEffectsPanel = new AudioEffectsPanel();
        tabbedPane.addTab(Application.resources().getString("dialog.effects.tabs.audio"), audioEffectsPanel);

        VideoEffectsPanel videoEffectsPanel = new VideoEffectsPanel();
        tabbedPane.addTab(Application.resources().getString("dialog.effects.tabs.video"), videoEffectsPanel);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        contentPane.setLayout(new MigLayout("fill", "[grow]", "[grow]"));
        contentPane.add(tabbedPane, "grow");

        setContentPane(tabbedPane);

        applyPreferences();
    }

    private void applyPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(EffectsFrame.class);
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
            Preferences prefs = Preferences.userNodeForPackage(EffectsFrame.class);
            prefs.putInt("frameX", getX());
            prefs.putInt("frameY", getY());
            prefs.putInt("frameWidth", getWidth());
            prefs.putInt("frameHeight", getHeight());
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onShowEffects(ShowEffectsEvent event) {
        setVisible(true);
    }
}
