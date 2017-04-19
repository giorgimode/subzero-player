package com.giorgimode.subzero.view.main;

import com.giorgimode.subzero.Application;
import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.Info;
import uk.co.caprica.vlcj.version.LibVlcVersion;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Properties;

public final class AboutDialog extends JDialog {

    public AboutDialog(Window owner) {
        super(owner, Application.resources().getString("dialog.about"), Dialog.ModalityType.DOCUMENT_MODAL);

        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load build.properties", e);
        }

        setLayout(new MigLayout("insets 30, fillx", "[shrink]30[shrink][grow]", "[]30[]10[]10[]30[]10[]10[]0[]"));
        getContentPane().setBackground(Color.white);

        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(new ImageIcon(getClass().getResource("/vlcj-logo.png")));

        JLabel applicationLabel = new JLabel();
        applicationLabel.setFont(applicationLabel.getFont().deriveFont(30.0f));
        applicationLabel.setText(Application.resources().getString("dialog.about.application"));

        JLabel blurb1Label = new JLabel();
        blurb1Label.setText(Application.resources().getString("dialog.about.blurb1"));

        JLabel blurb2Label = new JLabel();
        blurb2Label.setText(Application.resources().getString("dialog.about.blurb2"));

        JLabel attribution1Label = new JLabel();
        attribution1Label.setText(Application.resources().getString("dialog.about.attribution1"));

        JLabel applicationVersionLabel = new JLabel();
        applicationVersionLabel.setText(Application.resources().getString("dialog.about.applicationVersion"));

        JLabel applicationVersionValueLabel = new ValueLabel();
        applicationVersionValueLabel.setText(properties.getProperty("application.version"));

        JLabel vlcjVersionLabel = new JLabel();
        vlcjVersionLabel.setText(Application.resources().getString("dialog.about.vlcjVersion"));

        JLabel vlcjVersionValueLabel = new ValueLabel();
        vlcjVersionValueLabel.setText(Info.getInstance().version().toString());

        JLabel vlcVersionLabel = new JLabel();
        vlcVersionLabel.setText(Application.resources().getString("dialog.about.vlcVersion"));

        JLabel vlcVersionValueLabel = new ValueLabel();
        vlcVersionValueLabel.setText(LibVlcVersion.getVersion().toString());

        JLabel vlcChangesetValueLabel = new ValueLabel();
        vlcChangesetValueLabel.setText(LibVlcVersion.getChangeset());

        add(logoLabel, "shrink, top, spany 7");
        add(applicationLabel, "grow, spanx 2, wrap");
        add(blurb1Label, "grow, spanx 2, wrap");
        add(blurb2Label, "grow, spanx 2, wrap");
        add(attribution1Label, "grow, spanx 2, wrap");
        add(applicationVersionLabel, "");
        add(applicationVersionValueLabel, "wrap");
        add(vlcjVersionLabel);
        add(vlcjVersionValueLabel, "wrap");
        add(vlcVersionLabel);
        add(vlcVersionValueLabel, "wrap");
        add(vlcChangesetValueLabel, "skip 2");

        getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        pack();
        setResizable(false);
    }

    private class ValueLabel extends JLabel {

        ValueLabel() {
            setFont(getFont().deriveFont(Font.BOLD));
        }
    }
}
