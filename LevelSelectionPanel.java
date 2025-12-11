package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelSelectionPanel extends JPanel {
    JButton level1;
    JButton level2;
    JButton back;

    public LevelSelectionPanel(GameFrame frame) {
        ImageIcon icon = new ImageIcon("D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bac.jpg");

        JPanel bgPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));

        level1 = new JButton("Level 1");
        styleImageButton(level1, "D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bacbo.jpeg");


        level2 = new JButton("Level 2");
        styleImageButton(level2, "D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bacbo.jpeg");


        back = new JButton("Back");
        styleImageButton(back, "D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\BackGrounds\\bacbo.jpeg");

        level1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.switchTo(new GamePanel(frame));
            }
        });

        level2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.switchTo(new GamePanel(frame));
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.switchTo(new MainMenuPanel(frame));
            }
        });





        bgPanel.add(Box.createRigidArea(new Dimension(150, 200)));

        bgPanel.add(level1);
        bgPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        bgPanel.add(level2);
        bgPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        bgPanel.add(back);
        bgPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        setLayout(new  BorderLayout());
        add(bgPanel, BorderLayout.CENTER);

    }


    private void styleImageButton(JButton button, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);

        // Resize image to fit the button size
        Image img = icon.getImage().getScaledInstance(200, 50, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        button.setIcon(icon);

        // Make the button transparent except for the image
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        // Text settings
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 25));

        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}

