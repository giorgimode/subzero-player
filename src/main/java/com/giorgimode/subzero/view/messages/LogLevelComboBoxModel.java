package com.giorgimode.subzero.view.messages;

import javax.swing.DefaultComboBoxModel;

import uk.co.caprica.vlcj.binding.internal.libvlc_log_level_e;

@SuppressWarnings("serial")
final class LogLevelComboBoxModel extends DefaultComboBoxModel<libvlc_log_level_e> {

    LogLevelComboBoxModel() {
        for(libvlc_log_level_e value : libvlc_log_level_e.values()) {
            addElement(value);
        }
    }
}
