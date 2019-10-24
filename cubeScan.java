import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class cubeScan {

    private Mat imageMat;
    private String filePath = "Resources/Assets/cubeTestImage.jpg";

    public void loadImage() {
        imageMat = Imgcodecs.imread(filePath);
    }
}
