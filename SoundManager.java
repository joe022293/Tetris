import java.io.File;
import java.util.HashMap;
import javax.sound.sampled.*;

public class SoundManager {
    private Clip bgmClip;
    private HashMap<String, Clip> soundEffects = new HashMap<>();

    public void playBGM(String filePath) {
        stopBGM();
        bgmClip = loadClip(filePath);
        if (bgmClip != null) {
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
        }
    }

    public void playSFX(String name) {
        Clip clip = soundEffects.get(name);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loadSFX(String name, String filePath) {
        Clip clip = loadClip(filePath);
        if (clip != null) {
            soundEffects.put(name, clip);
        }
    }

    private Clip loadClip(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (Exception e) {
            System.err.println("Error loading sound: " + filePath);
            e.printStackTrace();
            return null;
        }
    }
}
