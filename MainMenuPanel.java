package Gui;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    JButton instructionsBtn;
    JButton exitBtn;
    JButton levelBtn;
    JButton playBtn;
    public MainMenuPanel( GameFrame frame) {

        ImageIcon icon = new ImageIcon("C:\\Users\\ahmed\\Downloads\\pngtree-snow-covering-the-ground-picture-image_12813784.jpg");
        JPanel bgPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));

        playBtn = new JButton("Play");
        styleImageButton(playBtn, "C:\\Users\\ahmed\\Downloads\\تنزيل (1).jpeg");


        levelBtn = new JButton("Select Level");
        styleImageButton(levelBtn, "C:\\Users\\ahmed\\Downloads\\تنزيل (1).jpeg");


        instructionsBtn = new JButton("Instructions");
        styleImageButton(instructionsBtn, "C:\\Users\\ahmed\\Downloads\\تنزيل (1).jpeg");

        styleImageButton(playBtn, "C:\\Users\\ahmed\\Downloads\\تنزيل (1).jpeg");
        exitBtn = new JButton("Exit");
        styleImageButton(exitBtn, "C:\\Users\\ahmed\\Downloads\\تنزيل (1).jpeg");



        playBtn.addActionListener(e -> frame.switchTo(new LevelSelectionPanel(frame)));
        instructionsBtn.addActionListener(e -> frame.switchTo(new InstructionsPanel(frame)));
        levelBtn.addActionListener(e -> frame.switchTo(new GamePanel(frame)));
        exitBtn.addActionListener(e -> System.exit(0));







        bgPanel.add(Box.createRigidArea(new Dimension(150, 200)));
        bgPanel.add(playBtn);

        bgPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        bgPanel.add(instructionsBtn);

        bgPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        bgPanel.add(exitBtn);

        setLayout(new BorderLayout());
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
