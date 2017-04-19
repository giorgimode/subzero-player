package com.giorgimode.subzero.view.action;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import static com.giorgimode.subzero.Application.resources;

public final class Resource {

    private final String id;

    public static Resource resource(String id) {
        return new Resource(id);
    }

    private Resource(String id) {
        this.id = id;
    }

    public String name() {
        if (resources().containsKey(id)) {
            return resources().getString(id);
        } else {
            return null;
        }
    }

    public Integer mnemonic() {
        String key = id + ".mnemonic";
        if (resources().containsKey(key)) {
            return (int) resources().getString(key).charAt(0);
        } else {
            return null;
        }
    }

    public KeyStroke shortcut() {
        String key = id + ".shortcut";
        if (resources().containsKey(key)) {
            return KeyStroke.getKeyStroke(resources().getString(key));
        } else {
            return null;
        }
    }

    public String tooltip() {
        String key = id + ".tooltip";
        if (resources().containsKey(key)) {
            return resources().getString(key);
        } else {
            return null;
        }
    }

    public Icon menuIcon() {
        String key = id + ".menuIcon";
        if (resources().containsKey(key)) {
            return new ImageIcon(getClass().getResource("/icons/actions/" + resources().getString(key) + ".png"));
        } else {
            return null;
        }
    }

    public Icon buttonIcon() {
        String key = id + ".buttonIcon";
        if (resources().containsKey(key)) {
            return new ImageIcon(getClass().getResource("/icons/buttons/" + resources().getString(key) + ".png"));
        } else {
            return null;
        }
    }
}
