package com.giorgimode.subzero.view.effects.overlay;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

public class SubtitlePanel extends JScrollPane {
    private boolean focused = false;
    private JTextPane jTextPane = new JTextPane();
    private Dimension panelDimension = new Dimension();
    private int maximumAllowedHeight = getHeight();
    private Map.Entry<String, Map<String, List<String>>> wordDefinitionEntryMap;
    private StyledDocument originalStyledDocument;


    public SubtitlePanel(Map.Entry<String, Map<String, List<String>>> wordDefinitionEntryMap, String preview) {
        getViewport().setView(jTextPane);
        this.wordDefinitionEntryMap = wordDefinitionEntryMap;

        jTextPane.setForeground(Color.white);
        panelDimension.setSize(getWidth(), getHeight());
        jTextPane.setText(preview);

        jTextPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setFocused(true);
                setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                jTextPane.setStyledDocument(SubtitlePanel.this.getOriginalStyledDocument());
                int newHeight = (int) SubtitlePanel.this.getPreferredSize().getHeight();
                jTextPane.setSize((int) panelDimension.getWidth(), Math.min(newHeight, maximumAllowedHeight));
                SubtitlePanel.this.setSize((int) panelDimension.getWidth(), Math.min(newHeight, maximumAllowedHeight));
                setOpaque(true);
                jTextPane.setForeground(null);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isFocused()) {
                    System.out.println("mouseEntered");
                    int maxSize = (int) Math.max(SubtitlePanel.this.getPreferredSize().getHeight(), panelDimension.getHeight());
                    SubtitlePanel.this.setSize((int) panelDimension.getWidth(), Math.min(2 * (int) panelDimension.getHeight(), maxSize));
                    jTextPane.setSize((int) panelDimension.getWidth(), Math.min(2 * (int) panelDimension.getHeight(), maxSize));
                    setOpaque(true);
                    jTextPane.setForeground(null);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isFocused()) {
                    System.out.println("mouseExited");
                    SubtitlePanel.this.setSize((int) panelDimension.getWidth(), (int) panelDimension.getHeight());
                    jTextPane.setSize((int) panelDimension.getWidth(), (int) panelDimension.getHeight());
                    setOpaque(false);
                    jTextPane.setForeground(Color.white);
                }
            }
        });
        jTextPane.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setFocused(false);
                setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                SubtitlePanel.this.setSize((int) panelDimension.getWidth(), (int) panelDimension.getHeight());
                jTextPane.setSize((int) panelDimension.getWidth(), (int) panelDimension.getHeight());
                setOpaque(false);
                jTextPane.setForeground(Color.white);
                jTextPane.setStyledDocument(new DefaultStyledDocument());
                jTextPane.setText(preview);
                jTextPane.setFont(new Font("Sansserif", Font.BOLD, 5));
            }
        });
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jTextPane.setEditable(false);
        setOpaque(false);
        jTextPane.setOpaque(false);
        getViewport().setOpaque(false);
        setBackground(Color.white);
        DefaultCaret caret = (DefaultCaret) jTextPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        jTextPane.setCaretPosition(0);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        g2.setPaint(new Color(255, 255, 255, 64));

        g2.fillRect(0, 0, getWidth(), getHeight());
    }


    private boolean isFocused() {
        return focused;
    }

    private void setFocused(boolean focused) {
        this.focused = focused;
    }

    public JTextPane getjTextPane() {
        return jTextPane;
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        panelDimension.setSize(d.getWidth(), d.getHeight());
    }

    public void setMaximumAllowedHeight(int windowHeight) {
        maximumAllowedHeight = windowHeight - getY() - windowHeight / 5;
    }

    public Map.Entry<String, Map<String, List<String>>> getWordDefinitionEntryMap() {
        return wordDefinitionEntryMap;
    }

    public StyledDocument getOriginalStyledDocument() {
        return originalStyledDocument;
    }

    public void setOriginalStyledDocument(StyledDocument originalStyledDocument) {
        this.originalStyledDocument = originalStyledDocument;
    }
}