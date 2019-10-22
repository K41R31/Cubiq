package Cubiq;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageProcessing {

    private Mat imageMat;

    public ImageProcessing(String file) {
        imageMat = Imgcodecs.imread(file);
    }
}
