package com.giorgimode.subzero.view.action.mediaplayer;

import com.giorgimode.dictionary.impl.LanguageEnum;
import com.giorgimode.subzero.Application;
import uk.co.caprica.vlcj.player.MediaPlayer;

import java.awt.event.ActionEvent;

public final class LanguagePackAction extends MediaPlayerAction {

    private final LanguageEnum name;

    public LanguagePackAction(LanguageEnum name, MediaPlayer mediaPlayer) {
        super(name.getValue(), mediaPlayer);
        this.name = name;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Application.application().setLanguageEnum(name);
    }
}
