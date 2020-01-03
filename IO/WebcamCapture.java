package IO;

import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class WebcamCapture {

    private VideoCapture videoCapture;
    private int framerate;

    public WebcamCapture(int webcamIndex) {
        createWebcamCapture(webcamIndex);
    }

    public void createWebcamCapture(int webcamIndex) {
        videoCapture = new VideoCapture(webcamIndex);
        if (!videoCapture.isOpened()) throw new CvException("Webcam could not be found");

        // Track the framerate
        framerate = calculateFramerate();

        // Set width and height TODO width/ height von webcam auslesen
        videoCapture.set(3, 1920);
        videoCapture.set(4, 1080);
        // Set the framerate
        videoCapture.set(5, framerate);
    }

    private int calculateFramerate() {
        long before, after;
        before = System.nanoTime();
        for (int i = 0; i < 50; i++)
            videoCapture.read(new Mat());
        after = System.nanoTime();

        double nanosPerFrame = (after - before) / 50d;
        double secPerFrame = nanosPerFrame / 1e9d;
        int framerate = (int) Math.round(1 / secPerFrame);

        // Normalize Framerate to typical framerates
        if (framerate < 10) return 0;
        if (framerate < 20) return 15;
        if (framerate < 27) return 25;
        if (framerate < 40) return 30;
        if (framerate < 100) return 60;
        return 120;
    }

    public int getFramerate() {
        return this.framerate;
    }

    public VideoCapture getVideoCapture() {
        return this.videoCapture;
    }
}
