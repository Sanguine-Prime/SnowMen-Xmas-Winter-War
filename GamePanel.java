package Gui;

import Snowman.AnimGLEventListener3;
import Snowman.AnimListener;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {

    boolean paused = false;
    private GLCanvas glcanvas;
    private Animator animator;
    private GameFrame frame;

    public GamePanel(GameFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setFocusable(true);

    
        setupOpenGL();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
            }
        });
    }

    private void setupOpenGL() {
      
        glcanvas = new GLCanvas();
        glcanvas = new GLCanvas();
        AnimListener listener = new AnimGLEventListener3();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);

        glcanvas.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
            }
        });

        add(glcanvas, BorderLayout.CENTER);

       
        animator = new FPSAnimator(15);
        animator.add(glcanvas);
        animator.start();
    }

    private void togglePause() {
        paused = !paused;

        if (paused) {
            animator.stop();
            frame.switchTo(new PausePanel(frame, this));
        } else {
           
        }
    }

    public void resumeGame() {
        if (paused) {
            paused = false;
            if (!animator.isAnimating()) {
                animator.start();
            }
         
            requestFocus();
        }
    }

    public void stopAnimation() {
        if (animator != null && animator.isAnimating()) {
            animator.stop();
        }
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
        if (glcanvas != null) {
            glcanvas.requestFocus();
        }
    }
}

class PausePanel extends JPanel {

    public PausePanel(GameFrame frame, GamePanel gamePanel) {

        ImageIcon bgImage = new ImageIcon("C:\\Users\\ahmed\\Downloads\\pngtree-snow-covering-the-ground-picture-image_12813784.jpg");

        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(
                        bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        bgPanel.setLayout(new BorderLayout());

        // ----------------------------------------------------------
     \
        JLabel title = new JLabel("GAME PAUSED", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        bgPanel.add(title, BorderLayout.NORTH);

        // ----------------------------------------------------------
     
        JPanel centerButtons = new JPanel();
        centerButtons.setOpaque(false);
        centerButtons.setLayout(new BoxLayout(centerButtons, BoxLayout.Y_AXIS));

        JButton resumeBtn = new JButton("Resume");
        JButton menuBtn = new JButton("Main Menu");
        JButton exitBtn = new JButton("Exit");
        styleImageButton(exitBtn, "C:\\Users\\ahmed\\Downloads\\تنزيل (1).jpeg");

        styleImageButton(resumeBtn, "C:\\Users\\ahmed\\Downloads\\تنزيل (1).jpeg");
        styleImageButton(menuBtn, "C:\\Users\\ahmed\\Downloads\\تنزيل (1).jpeg");

        exitBtn.addActionListener(e -> System.exit(0));
        resumeBtn.addActionListener(e -> {
            gamePanel.resumeGame();
            frame.switchTo(gamePanel);
        });

        menuBtn.addActionListener(e -> {
            gamePanel.stopAnimation();
            frame.switchTo(new MainMenuPanel(frame));
        });

        centerButtons.add(Box.createRigidArea(new Dimension(0, 150)));
        centerButtons.add(resumeBtn);
        centerButtons.add(Box.createRigidArea(new Dimension(0, 20)));
        centerButtons.add(menuBtn);
        centerButtons.add(Box.createRigidArea(new Dimension(0, 20)));
        centerButtons.add(exitBtn);


        bgPanel.add(centerButtons, BorderLayout.CENTER);

        // ----------------------------------------------------------
       
        JLabel hint = new JLabel("Press ESC to pause/resume", SwingConstants.CENTER);
        hint.setForeground(Color.WHITE);
        hint.setFont(new Font("Arial", Font.ITALIC, 18));

        bgPanel.add(hint, BorderLayout.SOUTH);

        // ----------------------------------------------------------
      
        setLayout(new BorderLayout());
        add(bgPanel, BorderLayout.CENTER);
    }

    // ---------------------------------------------------------
    private void styleImageButton(JButton button, String imagePath) {

        ImageIcon icon = new ImageIcon(imagePath);
        Image resizedImg = icon.getImage().getScaledInstance(250, 60, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImg);

        button.setIcon(icon);

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 25));

        button.setPreferredSize(new Dimension(250, 60));
        button.setMaximumSize(new Dimension(250, 60));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

}

