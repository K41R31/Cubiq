package AlphaTests.CubeScanFrameless.ImageProcessing;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.util.concurrent.TimeUnit;

public class FramerateTracker {

    private VideoCapture videoCapture;

    public FramerateTracker(VideoCapture videoCapture) {
        this.videoCapture = videoCapture;
    }

    public int getFramerate() {
        long before, after;
        before = System.nanoTime();
        videoCapture.read(new Mat());
        after = System.nanoTime();
        double seconds  = TimeUnit.NANOSECONDS.toMicros(after - before) / 1e6;
        System.out.println("seconds: " + seconds);
        return (int)(seconds);
    }
}
