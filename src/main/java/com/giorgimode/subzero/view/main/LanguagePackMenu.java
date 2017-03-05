package com.giorgimode.subzero.view.main;

import com.giorgimode.dictionary.impl.LanguageEnum;
import com.giorgimode.subzero.view.OnDemandMenu;
import com.giorgimode.subzero.view.action.mediaplayer.LanguagePackAction;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.giorgimode.subzero.Application.application;
import static com.giorgimode.subzero.view.action.Resource.resource;

final class LanguagePackMenu extends OnDemandMenu {

    LanguagePackMenu() {
        super(resource("menu.subtitle.item.language"), true);
    }

    @Override
    protected final void onPrepareMenu(JMenu menu) {
//        ButtonGroup buttonGroup = new ButtonGroup();
        LanguageEnum selectedLanguage = onGetSelectedLanguage();
        for (LanguageEnum language : onGetLanguages()) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(createAction(language));
//            menu.setLayout(new GridLayout(4, 4));
//            buttonGroup.add(menuItem);
            menu.add(Box.createVerticalGlue());
            menu.add(menuItem);
            if (selectedLanguage == language) {
                menuItem.setSelected(true);
            }
        }
    }


    private Action createAction(LanguageEnum lang) {
        return new LanguagePackAction(lang, application().mediaPlayerComponent().getMediaPlayer());
    }

    private List<LanguageEnum> onGetLanguages() {
        String parentDir = application().parentDir();

        File file = new File(parentDir);
        File[] directories = file.listFiles(File::isDirectory);
        if (ArrayUtils.isEmpty(directories) || ArrayUtils.isEmpty(directories[0].listFiles())) {
            return new ArrayList<>();
        }
        return Arrays.stream(directories)
                .map(File::getName)
                .map(LanguageEnum::fromString)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private LanguageEnum onGetSelectedLanguage() {
        return application().languageEnum();
    }

}
