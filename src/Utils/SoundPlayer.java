package Utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {

    public void playSound(String path) {
        try {
            Clip clip = loadAudio(path);
            clip.start();
        } catch (Exception e) {
            System.err.println("Erro ao tocar o efeito sonoro: " + e.getMessage());
        }
    }

    private Clip loadAudio(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        URL url = getClass().getResource(path);
        if (url == null) {
            throw new IOException("Recurso não encontrado: " + path);
        }
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        return clip;
    }
}
