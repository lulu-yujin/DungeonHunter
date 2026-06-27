package animation;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Animator {

    private Map<String, Image[]> clips = new HashMap<>();

    private String currentClip;
    private Image[] currentFrames;

    private int frameIndex = 0;
    private int frameTick = 0;
    private int frameSpeed = 8;

    private boolean loop = true;
    private boolean finished = false;

    public void addClip(String name, String... paths) {

        Image[] frames = new Image[paths.length];

        for (int i = 0; i < paths.length; i++) {

            var stream = getClass().getResourceAsStream(paths[i]);

            if (stream != null) {
                frames[i] = new Image(stream);
            } else {
                System.err.println("[Animator] Missing image: " + paths[i]);
                frames[i] = null;
            }
        }

        clips.put(name, frames);
    }

    public void play(String clipName, boolean loop) {

        if (clipName == null) {
            return;
        }

        if (clipName.equals(currentClip) && !finished) {
            return;
        }

        Image[] frames = clips.get(clipName);

        if (frames == null || frames.length == 0) {
            System.err.println("[Animator] Missing clip: " + clipName);
            return;
        }

        currentClip = clipName;
        currentFrames = frames;
        frameIndex = 0;
        frameTick = 0;
        this.loop = loop;
        finished = false;
    }

    public void update() {

        if (currentFrames == null || finished) {
            return;
        }

        frameTick++;

        if (frameTick >= frameSpeed) {

            frameTick = 0;
            frameIndex++;

            if (frameIndex >= currentFrames.length) {

                if (loop) {
                    frameIndex = 0;
                } else {
                    frameIndex = currentFrames.length - 1;
                    finished = true;
                }
            }
        }
    }
    
    public void setClipFrame(String clipName, int index) {

        Image[] frames = clips.get(clipName);

        if (frames == null || frames.length == 0) {
            System.err.println("[Animator] Missing clip: " + clipName);
            return;
        }

        currentClip = clipName;
        currentFrames = frames;

        frameIndex = index % frames.length;

        if (frameIndex < 0) {
            frameIndex = 0;
        }

        frameTick = 0;
        loop = false;
        finished = false;
    }

    public Image getCurrentFrame() {

        if (currentFrames == null || currentFrames.length == 0) {
            return null;
        }

        return currentFrames[frameIndex];
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFrameSpeed(int frameSpeed) {
        this.frameSpeed = frameSpeed;
    }
}