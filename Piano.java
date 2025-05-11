import javax.sound.midi.*;

public class Piano {
    private Synthesizer synthesizer;
    private MidiChannel channel;
    private int setPianoVolume = 50;
    public Piano() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            channel = synthesizer.getChannels()[0]; // 使用第一個音軌
            synthesizer.loadAllInstruments(synthesizer.getDefaultSoundbank());
            channel.programChange(11); // 0 是 Acoustic Grand Piano
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playPiano(int c) {
        int[] midiNotes = {
            60, // C4
            62, // D4
            64, // E4
            65, // F4
            67, // G4
            69, // A4
            71, // B4
            72  // C5
        };
        int index = Math.min(c - 1, midiNotes.length - 1);
        int note = midiNotes[index];
        channel.noteOn(note, setPianoVolume); // 音量 100
        new Thread(() -> {
            try {
                Thread.sleep(300); // 持續時間
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            channel.noteOff(note);
        }).start();
    }

    public void close() {
        if (synthesizer != null && synthesizer.isOpen()) {
            synthesizer.close();
        }
    }
    public void setPianoVolume(int v){
        setPianoVolume = v;
    }
    public int getPianoVolume(){
        return setPianoVolume;
    }
}