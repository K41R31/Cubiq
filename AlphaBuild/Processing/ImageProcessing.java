package AlphaBuild.Processing;

import AlphaBuild.Model.Model;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ImageProcessing implements Observer {

    private Model model;


    /**
     * Searches for a cube in a image
     */
    public void findCube() {
        // Get a image form the model and clone it
        Mat image = model.getOriginalImage().clone();

        // Skips the processing if no image was selected
        if (image == null) return;

        // Convert the image from brg to hsv
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);

        // Split the hvs image into three individual channels (h, s and v) and store them into a array list
        // -> 0 = hue (color); 1 = saturation; 2 = value (brightness)
        List<Mat> splittedMat = new ArrayList<>();
        Core.split(image, splittedMat);

        // Keep the image with the value channel (brightness map = grayscale image)
        Mat greyImg = splittedMat.get(2);
        debugOutput(greyImg, "1_greyscale");

        // Blur the image with the kernel size 15
        Imgproc.GaussianBlur(greyImg, greyImg, new Size(15, 15), 0);
        debugOutput(greyImg, "2_blur");

        // Apply the canny algorithm to the image (edge detection)
        Imgproc.Canny(greyImg, greyImg, 5.0, 17.0);
        debugOutput(greyImg, "3_canny");

        model.callObservers("cubeFound");
/*
        System.out.println("Images stored at \"AlphaBuild/Resources/Images/Processed\"");
        System.exit(0);
*/
    }

    /**
     * Print an debug image
     * @param mat The image to be saved
     * @param name The name of the output
     */
    public void debugOutput(Mat mat, String name) {
        Imgcodecs.imwrite("src/AlphaBuild/Resources/Images/Processed/" + name + ".jpg", mat);
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "image loaded":
                findCube();
                break;
        }
    }

    public void initModel(Model model) {
        this.model = model;
    }
}