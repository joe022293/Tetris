import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioConverter {

    public static Clip loadCompatibleClip(String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream originalStream = AudioSystem.getAudioInputStream(file);
            AudioFormat originalFormat = originalStream.getFormat();

            // 目標格式：16-bit PCM, 44100Hz, Stereo（與原始相同除了 sample size）
            AudioFormat targetFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    originalFormat.getSampleRate(),
                    16, // <<--- 轉成 16-bit
                    originalFormat.getChannels(),
                    originalFormat.getChannels() * 2,
                    originalFormat.getSampleRate(),
                    false // 小端序（一般為 false）
            );

            // 轉換格式
            AudioInputStream convertedStream = AudioSystem.getAudioInputStream(targetFormat, originalStream);

            // 載入 Clip
            Clip clip = AudioSystem.getClip();
            clip.open(convertedStream);
            return clip;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }
}