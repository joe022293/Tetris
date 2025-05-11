import java.io.File;
import java.util.HashMap;
import javax.sound.sampled.*;

public class SoundManager {
    private Clip bgmClip;
    private HashMap<String, Clip> soundEffects = new HashMap<>();
    private Piano comboSoundPlayer = new Piano();
    // private int comboVolume = 50;
    private int BGMVolume = 50;
    private int SFXVolume = 50;

    public void playCombo(int Combo){
        comboSoundPlayer.playPiano(Combo);
    }
    public void setComboVolume(int v){
        comboSoundPlayer.setPianoVolume(v);
    }
    public int getComboVolume(){
        return comboSoundPlayer.getPianoVolume();
    }
    public void loadBGM(String filePath){
        bgmClip = loadClip(filePath);
    }
    public void playBGM() {
        // stopBGM();
        if (bgmClip != null) {
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    public void setBGMVolume(int volume) {
        BGMVolume = volume;
        // volume: 0.0（靜音）到 1.0（最大聲）
        FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * (float)(volume * 0.01)) + gainControl.getMinimum();
        gainControl.setValue(gain);  // 設定音量
    }
    public int getBGMVolume(){
        return BGMVolume;
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
    public void setSFXVolume(int volume ,String name) {
        Clip clip = soundEffects.get(name);
        SFXVolume = volume;
        // volume: 0.0（靜音）到 1.0（最大聲）
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * (float)(volume * 0.01)) + gainControl.getMinimum();
        gainControl.setValue(gain);  // 設定音量
    }
    public int getSFXVolume(){
        return SFXVolume;
    }

    private Clip loadClip(String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream originalStream = AudioSystem.getAudioInputStream(file);
            AudioFormat originalFormat = originalStream.getFormat();

            // 如果是 24-bit PCM，就轉成 16-bit PCM
            AudioFormat targetFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    originalFormat.getSampleRate(),
                    16, // ← 強制轉為 16-bit
                    originalFormat.getChannels(),
                    originalFormat.getChannels() * 2,
                    originalFormat.getSampleRate(),
                    false // little endian（一般為 false）
            );

            // 如果不是 16-bit，就轉換；否則直接用原始音訊
            AudioInputStream finalStream = 
                originalFormat.getSampleSizeInBits() != 16 ?
                AudioSystem.getAudioInputStream(targetFormat, originalStream) :
                originalStream;

            Clip clip = AudioSystem.getClip();
            clip.open(finalStream);
            return clip;

        } catch (Exception e) {
            System.err.println("Error loading sound: " + filePath);
            e.printStackTrace();
            return null;
        }
    }
}
