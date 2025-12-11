package Gui;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private Map<String, Clip> soundClips;
    private Clip backgroundMusic;
    private float volume = 0.7f;
    private boolean muted = false;

    public AudioManager() {
        soundClips = new HashMap<>();
    }


    // Load a sound effect
    public void loadSound(String name, String filePath) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            soundClips.put(name, clip);
        } catch (Exception e) {
            System.err.println("Error loading sound: " + name);
            e.printStackTrace();
        }
    }
    // Try this in your mute toggle:
    public void toggleMute() {
        muted = !muted;

        if (muted) {
            if (backgroundMusic != null && backgroundMusic.isRunning()) {
                backgroundMusic.stop();
            }
        } else {
            if (backgroundMusic != null && !backgroundMusic.isRunning()) {
                backgroundMusic.setFramePosition(0);
                backgroundMusic.start();
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }

        System.out.println("Audio muted: " + muted);
    }
    public boolean isMuted() {
        return muted;
    }

    // Play a sound effect
    public void playSound(String name) {
        if (muted) return;

        Clip clip = soundClips.get(name);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            setClipVolume(clip, volume);
            clip.start();
        }
    }

    // Play background music
    public void playBackgroundMusic(String filePath) {
        try {
            if (backgroundMusic != null && backgroundMusic.isRunning()) {
                backgroundMusic.stop();
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);

            setClipVolume(backgroundMusic, volume * 0.8f);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            System.err.println("Error playing background music");
            e.printStackTrace();
        }
    }

    // Stop background music
    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    // Set volume
    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    // Mute/unmute
    public void setMuted(boolean muted) {
        this.muted = muted;
        if (muted) {
            stopBackgroundMusic();
        } else {
            // If we're unmuting and background music was loaded, restart it
            if (backgroundMusic != null && !backgroundMusic.isRunning()) {
                backgroundMusic.setFramePosition(0);
                backgroundMusic.start();
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
    }


    private void setClipVolume(Clip clip, float volume) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            dB = Math.max(-80.0f, Math.min(dB, 6.0206f));
            gainControl.setValue(dB);
        }
    }

    public void cleanup() {
        for (Clip clip : soundClips.values()) {
            clip.close();
        }
        if (backgroundMusic != null) {
            backgroundMusic.close();
        }
    }
}