package com.brydiam.javabioscripts;
import java.util.HashMap;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
    private static SoundManager instance;
    private HashMap<String, Long> lastPlayedTimes;
    private HashMap<String, Sound> sounds;

    private SoundManager() {
        lastPlayedTimes = new HashMap<>();
        sounds = new HashMap<>();
    }

    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void loadSound(String key, Sound sound) {
        sounds.put(key, sound);
    }

    public void playSound(String key, float volume, int speed) {
        if (sounds.containsKey(key)) {
            Long lastPlayedTime = lastPlayedTimes.get(key);
            if (lastPlayedTime == null || System.currentTimeMillis() - lastPlayedTime > speed) {
                sounds.get(key).play(volume, 1.2f, 0);
                lastPlayedTimes.put(key, System.currentTimeMillis());
            }
        }
    }

    public void dispose() {
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
        lastPlayedTimes.clear();
    }
}

