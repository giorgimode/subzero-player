package com.giorgimode.subzero.view.main;

import static com.giorgimode.subzero.Application.application;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import com.giorgimode.subzero.view.OnDemandMenu;
import com.giorgimode.subzero.view.action.Resource;
import com.giorgimode.subzero.view.action.StandardAction;

final class RecentMediaMenu extends OnDemandMenu {

    RecentMediaMenu(Resource resource) {
        super(resource, true);
    }

    @Override
    protected final void onPrepareMenu(JMenu menu) {
        List<String> mrls = application().recentMedia();
        if (!mrls.isEmpty()) {
            int i = 1;
            for (String mrl : mrls) {
                menu.add(new PlayRecentAction(i++, mrl));
            }
            menu.add(new JSeparator());
        }
        menu.add(new ClearRecentMediaAction());
    }

    private class PlayRecentAction extends AbstractAction {

        private final String mrl;

        public PlayRecentAction(int number, String mrl) {
            super(String.format("%d: %s", number, mrl));
            putValue(Action.MNEMONIC_KEY, number < 10 ? number : 0);
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(String.format("control %d", number < 10 ? number : 0)));
            this.mrl = mrl;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            application().mediaPlayerComponent().getMediaPlayer().playMedia(mrl);
        }
    }

    private class ClearRecentMediaAction extends StandardAction {

        public ClearRecentMediaAction() {
            super(Resource.resource("menu.media.item.recent.item.clear"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            application().clearRecentMedia();
        }
    }
}
