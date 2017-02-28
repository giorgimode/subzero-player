package com.giorgimode.subzero.view.main;

import com.giorgimode.dictionary.impl.LanguageEnum;
import com.giorgimode.subzero.view.OnDemandMenu;
import com.giorgimode.subzero.view.action.Resource;
import com.giorgimode.subzero.view.action.mediaplayer.LanguagePackAction;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.giorgimode.subzero.Application.application;
import static com.giorgimode.subzero.view.action.Resource.resource;

final class LanguagePackMenu extends OnDemandMenu {

    LanguagePackMenu() {
        super(resource("menu.subtitle.item.language"));
    }

    @Override
    protected final void onPrepareMenu(JMenu menu) {
        ButtonGroup buttonGroup = new ButtonGroup();
        LanguageEnum selectedLanguage = onGetSelectedLanguage();
        for (LanguageEnum language : onGetLanguages()) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(createAction(language));
//            menuItem.putClientProperty(KEY_TRACK_DESCRIPTION, language);
            buttonGroup.add(menuItem);
            menu.add(menuItem);
            if (selectedLanguage == language) {
                menuItem.setSelected(true);
            }
        }
    }


    protected Action createAction(LanguageEnum lang) {
        return new LanguagePackAction(lang.getValue(), application().mediaPlayerComponent().getMediaPlayer());
    }

    protected List<LanguageEnum> onGetLanguages() {
        String parentDir = getAppDir();

        File file = new File(parentDir);
        File[] directories = file.listFiles(File::isDirectory);
        if (ArrayUtils.isEmpty(directories)) {
            return new ArrayList<>();
        }
        return Arrays.stream(directories)
                .map(File::getName)
                .map(LanguageEnum::fromString)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private String getAppDir() {
        String parentDir;
        try {
            return new File(".").getCanonicalFile().getParent() + "/lang";
        } catch (IOException e) {
            return "";
        }
    }

    protected LanguageEnum onGetSelectedLanguage() {
        return application().languageEnum();
    }

}
