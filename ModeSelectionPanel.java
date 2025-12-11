package Gui;

import javax.swing.*;
import java.awt.*;

public class ModeSelectionPanel extends JPanel {

    JButton singlePlayerBtn;
    JButton multiPlayerBtn;
    JButton backBtn;

    public ModeSelectionPanel(GameFrame frame) {

        ImageIcon bgImage = new ImageIcon("D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bac.jpg");

        JPanel bgPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));


        singlePlayerBtn = new JButton("Single Player");
        styleImageButton(singlePlayerBtn, "D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bacbo.jpeg");

        multiPlayerBtn = new JButton("Multiplayer");
        styleImageButton(multiPlayerBtn, "D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bacbo.jpeg");

        backBtn = new JButton("Back");
        styleImageButton(backBtn, "D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bacbo.jpeg");


        singlePlayerBtn.addActionListener(e -> frame.switchTo(new LevelSelectionPanel(frame)));

        multiPlayerBtn.addActionListener(e -> {
            frame.switchTo(new GamePanel(frame));
        });

        backBtn.addActionListener(e -> frame.switchTo(new MainMenuPanel(frame)));


        bgPanel.add(Box.createRigidArea(new Dimension(150, 200)));

        bgPanel.add(singlePlayerBtn);
        bgPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        bgPanel.add(multiPlayerBtn);
        bgPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        bgPanel.add(backBtn);
        bgPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        setLayout(new BorderLayout());
        add(bgPanel, BorderLayout.CENTER);
    }


    private void styleImageButton(JButton button, String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(200, 50, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        button.setIcon(icon);
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
