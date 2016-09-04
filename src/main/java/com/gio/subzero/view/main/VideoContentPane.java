package com.gio.subzero.view.main;

import static com.gio.subzero.Application.application;

import java.awt.CardLayout;

import javax.swing.JPanel;

import com.gio.subzero.view.image.ImagePane;

final class VideoContentPane extends JPanel {

    private static final String NAME_DEFAULT = "default";

    private static final String NAME_VIDEO = "video";

    private final CardLayout cardLayout;

    VideoContentPane() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        add(new ImagePane(ImagePane.Mode.CENTER, getClass().getResource("/vlcj-logo.png"), 0.6f), NAME_DEFAULT);
        add(application().mediaPlayerComponent(), NAME_VIDEO);
    }

    public void showDefault() {
        cardLayout.show(this, NAME_DEFAULT);
    }

    public void showVideo() {
        cardLayout.show(this, NAME_VIDEO);
    }
}
