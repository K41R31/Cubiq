package AlphaTests.CubeScanFrameless.ImageProcessing;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class FramerateTracker {

    private VideoCapture videoCapture;

    public FramerateTracker(VideoCapture videoCapture) {
        this.videoCapture = videoCapture;
    }

    public int getFramerate() {
        long before, after;
        before = System.nanoTime();
        for (int i = 0; i < 50; i++)
            videoCapture.read(new Mat());
        after = System.nanoTime();

        double nanosPerFrame = (after - before) / 50d;
        double secPerFrame = nanosPerFrame / 1e9d;
        int framerate = (int)Math.round(1 / secPerFrame);

        return normalizedFramerate(framerate);
    }

    private int normalizedFramerate(int framerate) {
        if (framerate < 10) return 0;
        if (framerate < 20) return 15;
        if (framerate < 27) return 25;
        if (framerate < 40) return 30;
        if (framerate < 100) return 60;
        return 120;
    }
}
