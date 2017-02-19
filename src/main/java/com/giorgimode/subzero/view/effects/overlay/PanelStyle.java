package com.giorgimode.subzero.view.effects.overlay;

import java.util.List;
import java.util.Map;

/**
 *  Created by modeg on 1/22/2017.
 */
public interface PanelStyle {
    void createOnClickStyle(SubtitlePanel subtitlePanel);

    void createPreviewStyle(SubtitlePanel subtitlePanel, Map.Entry<String, Map<String, List<String>>> wordDefinitionEntryMap);
}
