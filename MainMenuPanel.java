package Gui;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;


public class MainMenuPanel extends JPanel {
    JButton instructionsBtn;
    JButton exitBtn;
    JButton levelBtn;
    JButton playBtn;
    JButton audioBtn; // Add audio toggle button

    public MainMenuPanel(GameFrame frame) {
        // Background
        ImageIcon icon = new ImageIcon("D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bac.jpg"); // Use relative path
        JPanel bgPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));

        playBtn = new JButton("Play");
        styleImageButton(playBtn, "D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bacbo.jpeg");

        levelBtn = new JButton("Select Level");
        styleImageButton(levelBtn, "D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bacbo.jpeg");

        instructionsBtn = new JButton("Instructions");
        styleImageButton(instructionsBtn, "D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bacbo.jpeg");

        exitBtn = new JButton("Exit");
        styleImageButton(exitBtn, "D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bacbo.jpeg");

        // Audio toggle button
        boolean isMuted = false; // Default to not muted
        audioBtn = new JButton(isMuted ? "Audio: OFF" : "Audio: ON");
        styleImageButton(audioBtn, "D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bacbo.jpeg");

        // Add audio to button clicks
        playBtn.addActionListener(e -> {
            GameFrame.playSound("D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bacbo.jpeg");
            frame.switchTo(new ModeSelectionPanel(frame));
        });

        instructionsBtn.addActionListener(e -> {
            GameFrame.playSound("button_click");
            frame.switchTo(new InstructionsPanel(frame));
        });

        levelBtn.addActionListener(e -> {
            GameFrame.playSound("button_click");
            frame.switchTo(new LevelSelectionPanel(frame));
        });

        exitBtn.addActionListener(e -> {
            GameFrame.playSound("button_click");
            System.exit(0);
        });

        audioBtn.addActionListener(e -> {
            GameFrame.playSound("button_click");
            toggleAudio();
        });

        bgPanel.add(Box.createRigidArea(new Dimension(150, 200)));
        bgPanel.add(playBtn);
//        bgPanel.add(Box.createRigidArea(new Dimension(0, 20)));
//        bgPanel.add(levelBtn);
        bgPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        bgPanel.add(instructionsBtn);
        bgPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        bgPanel.add(audioBtn);
        bgPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        bgPanel.add(exitBtn);

        setLayout(new BorderLayout());
        add(bgPanel, BorderLayout.CENTER);
    }

    private void toggleAudio() {
        boolean isMuted = audioBtn.getText().contains("OFF");

        // Toggle the mute state
        GameFrame.setMuted(!isMuted);

        // Update button text
        if (isMuted) {
            audioBtn.setText("Audio: ON");
        } else {
            audioBtn.setText("Audio: OFF");
        }

        // Optional: Force a repaint to update immediately
        audioBtn.repaint();
    }


    private void styleImageButton(JButton button, String imagePath) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(200, 50, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            button.setIcon(icon);
        } catch (Exception e) {
            // If image not found, use text button
            System.err.println("Image not found: " + imagePath);
        }

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 25));

        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}