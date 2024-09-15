package Utils;

import javax.sound.sampled.*;
import static Utils.ResourceLoader.loadAudio;

public class SoundPlayer {
    Clip clip;
    Boolean loop;

    public Clip playSound(String path, boolean loop) {
        try {
            this.clip = loadAudio(path);
            this.loop = loop;

            if (this.loop) {
                this.clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            clip.start();
        } catch (Exception e) {
            System.err.println("Erro ao tocar o efeito sonoro: " + e.getMessage());
        }
        return clip;
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    public Clip getClip() {
        return clip;
    }
}
