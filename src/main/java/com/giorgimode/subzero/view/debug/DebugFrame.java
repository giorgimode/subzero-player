package com.giorgimode.subzero.view.debug;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import com.giorgimode.subzero.event.ShowDebugEvent;
import com.giorgimode.subzero.view.BaseFrame;
import com.google.common.eventbus.Subscribe;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.prefs.Preferences;

import static com.giorgimode.subzero.Application.application;

@SuppressWarnings("serial")
public final class DebugFrame extends BaseFrame {
    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private final EventList<DebugMessage>      eventList;
    private final JTable                       table;
    private final MouseAdapter                 mouseEventHandler;

    public DebugFrame() {
        super("Debug Messages");
        this.mediaPlayerComponent = application().mediaPlayerComponent();
        this.mouseEventHandler = new MouseEventHandler();
        this.mediaPlayerComponent.getVideoSurface().addMouseListener(mouseEventHandler);
        this.mediaPlayerComponent.getVideoSurface().addMouseMotionListener(mouseEventHandler);
        this.mediaPlayerComponent.getVideoSurface().addMouseWheelListener(mouseEventHandler);

        this.mediaPlayerComponent.getVideoSurface().addKeyListener(new KeyEventHandler());

        Action clearAction = new AbstractAction("Clear") {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventList.clear();
            }
        };

        JButton clearButton = new JButton();
        clearButton.setAction(clearAction);

        JPanel controlsPane = new JPanel();
        controlsPane.setLayout(new BoxLayout(controlsPane, BoxLayout.X_AXIS));
        controlsPane.add(Box.createHorizontalGlue());
        controlsPane.add(clearButton);

        this.eventList = new BasicEventList<>();
        AdvancedTableModel<DebugMessage> eventTableModel =
                GlazedListsSwing.eventTableModelWithThreadProxyList(eventList, new DebugMessageTableFormat());

        table = new JTable();
        table.setModel(eventTableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(table);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(controlsPane, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        applyPreferences();
    }

    @Override
    public void dispose() {
        this.mediaPlayerComponent.getVideoSurface().removeMouseListener(mouseEventHandler);
        this.mediaPlayerComponent.getVideoSurface().removeMouseMotionListener(mouseEventHandler);
        this.mediaPlayerComponent.getVideoSurface().removeMouseWheelListener(mouseEventHandler);
    }

    private class MouseEventHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            addMessage(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //    addMessage(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //     addMessage(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //      addMessage(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //      addMessage(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            addMessage(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            addMessage(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //     addMessage(e);
        }
    }

    private class KeyEventHandler extends KeyAdapter {

        @Override
        public void keyTyped(KeyEvent e) {
            addMessage(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            addMessage(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            addMessage(e);
        }
    }

    private void addMessage(InputEvent message) {
        addMessage(message != null ? message.toString() : "EVENT WAS NULL");
    }

    private void addMessage(String message) {
        int bra = message.indexOf('[');
        int ket = message.indexOf(']');
        if (bra != -1 && ket != -1) {
            message = message.substring(bra + 1, ket);
        }
        eventList.add(new DebugMessage(message));
        int lastRow = table.convertRowIndexToView(table.getModel().getRowCount() - 1);
        table.scrollRectToVisible(table.getCellRect(lastRow, 0, true));
    }

    private void applyPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(DebugFrame.class);
        setBounds(
                prefs.getInt("frameX", 300),
                prefs.getInt("frameY", 300),
                prefs.getInt("frameWidth", 500),
                prefs.getInt("frameHeight", 300)
                 );
    }

    @Override
    protected void onShutdown() {
        if (wasShown()) {
            Preferences prefs = Preferences.userNodeForPackage(DebugFrame.class);
            prefs.putInt("frameX", getX());
            prefs.putInt("frameY", getY());
            prefs.putInt("frameWidth", getWidth());
            prefs.putInt("frameHeight", getHeight());
        }
    }

    @Subscribe
    public void onShowMessages(ShowDebugEvent event) {
        setVisible(true);
    }
}
