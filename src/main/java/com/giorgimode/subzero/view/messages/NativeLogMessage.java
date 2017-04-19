package com.giorgimode.subzero.view.messages;

import uk.co.caprica.vlcj.binding.internal.libvlc_log_level_e;

final class NativeLogMessage {

    private final String             module;
    private final String             name;
    private final libvlc_log_level_e level;
    private final String             message;

    NativeLogMessage(String module, String name, libvlc_log_level_e level, String message) {
        this.module = module;
        this.name = name;
        this.level = level;
        this.message = message;
    }

    public String getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public libvlc_log_level_e getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }
}
