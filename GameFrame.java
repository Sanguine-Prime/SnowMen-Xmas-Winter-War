package Gui;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("My Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new MainMenuPanel(this));
        setVisible(true);
    }

    public void switchTo(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
        panel.requestFocus();
    }
}
