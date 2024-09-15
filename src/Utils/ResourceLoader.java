package Utils;


import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class ResourceLoader {
    public static Image loadImage(String path) throws IOException {
        URL url = ResourceLoader.class.getResource(path);
        if (url == null) {
            throw new IOException("Recurso não encontrado: " + path);
        }
        return new ImageIcon(url).getImage();
    }

    public static Clip loadAudio(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        URL url = ResourceLoader.class.getResource(path);
        if (url == null) {
            throw new IOException("Recurso não encontrado: " + path);
        }
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        return clip;
    }
}