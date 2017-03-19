package com.giorgimode.subzero.view.effects.overlay;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import static com.giorgimode.subzero.Application.application;

public class TranslationPanel extends JScrollPane {
    private boolean focused = false;
    private final JTextPane jTextPane;
    private final Dimension panelDimension;
    private int maximumAllowedHeight;
    private Map.Entry<String, Map<String, List<String>>> wordDefinitionEntryMap;
    private StyledDocument originalStyledDocument;
    private StyledDocument previewStyledDocument;

    TranslationPanel(Map.Entry<String, Map<String, List<String>>> wordDefinitionEntryMap) {
        jTextPane = new JTextPane();
        panelDimension = new Dimension();
        getViewport().setView(jTextPane);
        maximumAllowedHeight = getHeight();
        this.wordDefinitionEntryMap = wordDefinitionEntryMap;
        panelDimension.setSize(getWidth(), getHeight());
        jTextPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setFocused(true);
                setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                jTextPane.setStyledDocument(TranslationPanel.this.getOriginalStyledDocument());

                int newHeight = (int) TranslationPanel.this.getPreferredSize().getHeight();
                updateOnEvent((int) panelDimension.getWidth(), Math.min(newHeight, maximumAllowedHeight), Color.BLACK);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isFocused()) {
                    int maxSize = (int) Math.max(TranslationPanel.this.getPreferredSize().getHeight(), panelDimension.getHeight());
                    updateOnEvent((int) panelDimension.getWidth(), Math.min(2 * (int) panelDimension.getHeight(), maxSize), Color.BLUE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isFocused()) {
                    TranslationPanel.this.updateOnEvent((int) panelDimension.getWidth(), (int) panelDimension.getHeight(), Color.BLACK);
                }
            }
        });
        jTextPane.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setFocused(false);
                setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                updateOnEvent((int) panelDimension.getWidth(), (int) panelDimension.getHeight(), Color.BLACK);
                applyPreview();
            }
        });

        jTextPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                application().mediaPlayerComponent().getVideoSurface().dispatchEvent(e);
            }
        });

        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        setOpaque(true);
        getViewport().setOpaque(false);
        applyPreview();
    }

    private void applyPreview() {
        if (previewStyledDocument != null) {
            jTextPane.setStyledDocument(previewStyledDocument);
        }
        jTextPane.setEditable(false);
        DefaultCaret caret = (DefaultCaret) jTextPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        jTextPane.setCaretPosition(0);
    }

    private void updateOnEvent(int width, int height, Color color) {
        setSize(width, height);
        jTextPane.setSize(width, height);
        jTextPane.setForeground(color);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    }


    private boolean isFocused() {
        return focused;
    }

    private void setFocused(boolean focused) {
        this.focused = focused;
    }

    JTextPane getjTextPane() {
        return jTextPane;
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        panelDimension.setSize(d.getWidth(), d.getHeight());
    }

    void setMaximumAllowedHeight(int windowHeight) {
        maximumAllowedHeight = windowHeight - getY() - windowHeight / 5;
    }

    Map.Entry<String, Map<String, List<String>>> getWordDefinitionEntryMap() {
        return wordDefinitionEntryMap;
    }

    private StyledDocument getOriginalStyledDocument() {
        return originalStyledDocument;
    }

    void setOriginalStyledDocument(StyledDocument originalStyledDocument) {
        this.originalStyledDocument = originalStyledDocument;
    }

    void setPreviewStyledDocument(StyledDocument previewStyledDocument) {
        this.previewStyledDocument = previewStyledDocument;
    }
}