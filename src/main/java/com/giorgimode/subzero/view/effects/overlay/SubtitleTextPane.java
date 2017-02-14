package com.giorgimode.subzero.view.effects.overlay;

import javax.swing.JTextPane;

/**
 * Created by modeg on 2/14/2017.
 */
public class SubtitleTextPane extends JTextPane {
    public void refresh() {
        setEditorKit(createDefaultEditorKit());
    }
}
