package com.giorgimode.subzero.view.snapshot;

import com.giorgimode.subzero.view.image.ImagePane;
import com.giorgimode.subzero.view.image.ImagePane.Mode;
import com.google.common.io.Files;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import static com.giorgimode.subzero.Application.resources;

/**
 * Simple frame implementation that shows a buffered image.
 */
public class SnapshotView extends JFrame {

    private static final String DEFAULT_FILE_EXTENSION = "png";

    private final JFileChooser fileChooser = new JFileChooser();

    private final BufferedImage image;

    public SnapshotView(BufferedImage image) {
        this.image = image;
        setTitle("vlcj-player snapshot");
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new ImagePane(Mode.DEFAULT, image, 1.0f), BorderLayout.CENTER);
        contentPane.add(new ActionPane(), BorderLayout.SOUTH);
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationByPlatform(true);
        setVisible(true);
    }

    private void onSave() {
        if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(this)) {
            File file = fileChooser.getSelectedFile();
            String ext = Files.getFileExtension(file.getName()).toLowerCase();
            if (!DEFAULT_FILE_EXTENSION.equals(ext)) {
                file = new File(fileChooser.getSelectedFile().getAbsolutePath() + "." + DEFAULT_FILE_EXTENSION);
            }

            try {
                if (file.exists()) {
                    int input = JOptionPane.showConfirmDialog(this, resources().getString("dialog.saveImageWarning"),
                            resources().getString("dialog.saveImageWarningTitle"), JOptionPane.YES_NO_OPTION);
                    if (input == JOptionPane.NO_OPTION) {
                        onSave();
                    } else {
                        saveFile(file);
                    }
                } else {
                    saveFile(file);
                }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, MessageFormat.format(resources().getString("error.saveImage"), file.toString(),
                        e.getMessage()), resources().getString("dialog.saveImage"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile(File file) throws IOException {
        boolean wrote = ImageIO.write(image, DEFAULT_FILE_EXTENSION, file);
        if (!wrote) {
            JOptionPane.showMessageDialog(this, MessageFormat.format(resources().getString("error.saveImage"), file.toString(),
                    MessageFormat.format(resources().getString("error.saveImageFormat"), DEFAULT_FILE_EXTENSION)),
                    resources().getString("dialog.saveImage"), JOptionPane.ERROR_MESSAGE);
        } else {
            dispose();
        }
    }

    private final class ActionPane extends JPanel {

        private ActionPane() {
            setLayout(new MigLayout("fillx", "push[]", "[]"));
            JButton saveButton = new JButton("Save");
            add(saveButton);
            saveButton.addActionListener(e -> onSave());
        }
    }
}
