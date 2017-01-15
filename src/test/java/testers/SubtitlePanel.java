package testers;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;
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

public class SubtitlePanel extends JScrollPane {
    private boolean focused = false;
    private JTextPane textArea = new JTextPane();

    public SubtitlePanel(String text) {
        getViewport().setView(textArea);

        String shortText = text.substring(0, text.length() / 3) + "...";
        textArea.setText(shortText);
        textArea.setFont(new Font("Sansserif", Font.BOLD, 18));
        textArea.setForeground(Color.WHITE);

        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setFocused(true);
                setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                System.out.println("mouseClicked");
                textArea.setText(text);
                textArea.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isFocused()) {
                    System.out.println("mouseEntered");
                    textArea.setText(text.substring(0, text.length() / 2) + "...");
                    textArea.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isFocused()) {
                    System.out.println("mouseExited");
                    textArea.setText(shortText);
                    textArea.repaint();
                }
            }
        });
        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setFocused(false);
                setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                textArea.setText(shortText);
            }
        });
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        setOpaque(false);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        getViewport().setOpaque(false);
        setBackground(Color.cyan);
        setMaximumSize(new Dimension(1000, 1000));
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
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

    public JTextPane getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextPane textArea) {
        this.textArea = textArea;
    }
}