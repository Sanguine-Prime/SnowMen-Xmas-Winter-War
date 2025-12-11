package Gui;

import javax.swing.*;

public class GameFrame extends JFrame {
    private static AudioManager audioManager;

    public GameFrame() {
        setTitle("SnowMan Battle");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize audio manager
        audioManager = new AudioManager();

        // Load sounds
        loadGameSounds();

        // Start with background music
        audioManager.playBackgroundMusic("D:\\Java Projects\\Xmas Winter War02\\Xmas Winter War\\PocketTanksPenguins\\src\\Assets\\Sounds\\background.wav");

        setContentPane(new MainMenuPanel(this));
        setVisible(true);
    }

    private void loadGameSounds() {
        // Load button click sounds
        audioManager.loadSound("button_click", "Assets/sounds/click.wav");
        audioManager.loadSound("menu_select", "Assets/sounds/menu_select.wav");
        audioManager.loadSound("snowball_throw", "Assets/sounds/throw.wav");
        audioManager.loadSound("hit", "Assets/sounds/hit.wav");
        audioManager.loadSound("hurt", "Assets/sounds/hurt.wav");
        audioManager.loadSound("win", "Assets/sounds/win.wav");
        audioManager.loadSound("lose", "Assets/sounds/lose.wav");
    }

    public void switchTo(JPanel panel) {
        // Play menu navigation sound
        playSound("menu_select");

        setContentPane(panel);
        revalidate();
        repaint();
        panel.requestFocus();
    }

    // Static methods to access audio from anywhere
    public static void playSound(String name) {
        if (audioManager != null) {
            audioManager.playSound(name);
        }
    }

    public static void stopBackgroundMusic() {
        if (audioManager != null) {
            audioManager.stopBackgroundMusic();
        }
    }

    public static void setMuted(boolean muted) {
        if (audioManager != null) {
            audioManager.setMuted(muted);
        }
    }
    public static boolean isAudioMuted() {
        if (audioManager != null) {
            return audioManager.isMuted();
        }
        return false;
    }

    public static void setVolume(float volume) {
        if (audioManager != null) {
            audioManager.setVolume(volume);
        }
    }

    @Override
    public void dispose() {
        if (audioManager != null) {
            audioManager.cleanup();
        }
        super.dispose();
    }
}