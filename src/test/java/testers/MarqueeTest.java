/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2016 Caprica Software Limited.
 */

package testers;

import uk.co.caprica.vlcj.binding.internal.libvlc_marquee_position_e;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static uk.co.caprica.vlcj.player.Marquee.marquee;
/**
 * Test the marquee.
 * <p>
 * The enable/disable buttons simply toggle the marquee on/off, to actually change marquee settings
 * you must use the apply button.
 */
public class MarqueeTest extends VlcjTest {

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private final JFrame frame;

    private final JPanel jpanel;

    private final JPanel controlsPanel;

    public static void main(final String[] args) throws Exception {
      /*  if(args.length != 1) {
            System.err.println("Specify a single MRL");
            System.exit(1);
        }*/

        setLookAndFeel();

        SwingUtilities.invokeLater(() -> new MarqueeTest().start("D:\\Torrents\\Horrible.Bosses.2.2014.1080p.BluRay.x264-SPARKS\\Horrible.Bosses.2.2014.1080p.BluRay.x264-SPARKS.mkv"));
    }

    public MarqueeTest() {
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        jpanel = new JPanel();
        jpanel.setLayout(new BorderLayout());
        jpanel.add(mediaPlayerComponent, BorderLayout.CENTER);

        controlsPanel = new ControlsPanel();
        jpanel.add(controlsPanel, BorderLayout.SOUTH);

        frame = new JFrame("vlcj Marquee Test");
        frame.setContentPane(jpanel);
        frame.setSize(1300, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release(true);
            }
        });
    }

    public void start(String mrl) {
        frame.setVisible(true);
        mediaPlayerComponent.getMediaPlayer().playMedia(mrl);
    }

    @SuppressWarnings("serial")
    public class ControlsPanel extends JPanel {

        JTextField xTextField = new JTextField(4);

        JTextField yTextField = new JTextField(4);

        JComboBox positionCombo = new JComboBox();

        JSlider opacitySlider = new JSlider(0, 255);

        JTextField textTextField = new JTextField(20);

        JCheckBox enableCheckBox = new JCheckBox("Enable");

        JButton applyButton = new JButton("Apply");

        JButton enableButton = new JButton("Enable");

        JButton disableButton = new JButton("Disable");

        public ControlsPanel() {
            setBorder(new TitledBorder("Marquee Controls"));

            positionCombo.setModel(new PositionComboModel());

            JSeparator separator;

            add(new JLabel("X"));
            add(xTextField);
            add(new JLabel("Y"));
            add(yTextField);
            add(new JLabel("Position"));
            add(positionCombo);
            add(new JLabel("Opacity"));
            add(opacitySlider);
            add(new JLabel("Text"));
            add(textTextField);
            add(enableCheckBox);
            separator = new JSeparator(JSeparator.VERTICAL);
            separator.setPreferredSize(new Dimension(4, 20));
            add(separator);
            add(applyButton);
            separator = new JSeparator(JSeparator.VERTICAL);
            separator.setPreferredSize(new Dimension(4, 20));
            add(separator);
            add(enableButton);
            add(disableButton);

            opacitySlider.setPreferredSize(new Dimension(100, 16));

            opacitySlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                }
            });

            applyButton.addActionListener(evt -> {
                int x = -1;
                int y = -1;
                try {
                    x = Integer.parseInt(xTextField.getText());
                    y = Integer.parseInt(yTextField.getText());
                }
                catch(NumberFormatException e) {
                }

                marquee()
                        .text(textTextField.getText())
                        .location(x, y)
                        .position((libvlc_marquee_position_e)positionCombo.getSelectedItem())
                        .opacity(opacitySlider.getValue())
                        .enable(enableCheckBox.isSelected())
                        .apply(mediaPlayerComponent.getMediaPlayer());
            });

            enableButton.addActionListener(e -> mediaPlayerComponent.getMediaPlayer().enableMarquee(true));

            disableButton.addActionListener(e -> mediaPlayerComponent.getMediaPlayer().enableMarquee(false));
        }
    }

    @SuppressWarnings("serial")
    private class PositionComboModel extends DefaultComboBoxModel {

        private PositionComboModel() {
            super(libvlc_marquee_position_e.values());
            insertElementAt(null, 0);
            setSelectedItem(null);
        }
    }
}