package Gui;


import javax.swing.*;
import java.awt.*;

public class InstructionsPanel extends JPanel {
    JTextArea text;
    JButton backBtn ;

    public InstructionsPanel(GameFrame frame) {

        ImageIcon icon = new ImageIcon("C:\\Users\\ahmed\\Downloads\\pngtree-snow-covering-the-ground-picture-image_12813784.jpg");

        JPanel bgPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));

        // ---------- Text Area ----------
        text = new JTextArea();
        text.setEditable(false);   // خليها read only
        text.setOpaque(false);     // خليها شفافة فوق الخلفية
        text.setForeground(new Color(4, 0, 98, 180));
        text.setFont(new Font("Arial", Font.BOLD, 17));
        text.setText(
                "Welcome to Penguin Snow Battle!\n" +
                        "\n" +
                        "You control a brave little penguin\n" +
                        "living in the cold, snowy lands\n" +
                        "of the Arctic.\n" +
                        "\n" +
                        "Your mission is simple:\n" +
                        "Throw snowballs at the enemy penguins\n" +
                        "before they hit you.\n" +
                        "\n" +
                        "Move fast… aim carefully…\n" +
                        "and survive the freezing battle!\n" +
                        "\n" +
                        "Every level becomes harder,\n" +
                        "with faster enemies and stronger attacks.\n" +
                        "\n" +
                        "Remember:\n" +
                        "• Dodge the incoming snowballs\n" +
                        "• Hit enemies to score points\n" +
                        "• Stay alive as long as you can\n" +
                        "\n" +
                        "Good luck, warrior penguin!\n"

        );

        text.setMaximumSize(new Dimension(600, 500));   // حجم مناسب
        text.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ---------- Back Button ----------
         backBtn = new JButton("Back");
        styleImageButton(backBtn, "C:\\Users\\ahmed\\Downloads\\تنزيل (1).jpeg");

        backBtn.addActionListener(e ->
                frame.switchTo(new MainMenuPanel(frame))
        );

        backBtn.setMaximumSize(new Dimension(200, 50));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ---------- Add Components ----------


        bgPanel.add(Box.createRigidArea(new Dimension(60, 10)));
        bgPanel.add(text);
        bgPanel.add(backBtn);
        bgPanel.add(Box.createRigidArea(new Dimension(0, 70)));

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
