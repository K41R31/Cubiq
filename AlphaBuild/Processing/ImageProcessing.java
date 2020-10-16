package AlphaBuild.Processing;

import AlphaBuild.Model.Model;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.*;

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

        // Apply the canny algorithm to the image (edge detection)
        Imgproc.Canny(greyImg, greyImg, 5.0, 17.0);
        debugOutput(greyImg, "2_canny");

        // Make the lines thicker
        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
        Imgproc.dilate(greyImg, greyImg, dilateKernel);

        // Find all contours in the image and store them into the list contours
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(greyImg, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        // Approximates the shape of the contours with forms
        // ArcLength calculates the length of a contour
        // Closed = true -> keep only the forms that are closed
        List<MatOfPoint2f> approximations = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++) {
            approximations.add(new MatOfPoint2f());
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), approximations.get(i),
                    0.1 * Imgproc.arcLength(new MatOfPoint2f(contours.get(i).toArray()),
                            true), true);
        }

        // Debug output for approximations
        List<MatOfPoint> drawableApproximations = convertMat2fToMat(approximations);
        Mat approximationMat = drawContoursOnMat(drawableApproximations);
        debugOutput(approximationMat, "3_approximations");

        MatOfPoint2f cubeBoundingRect = findCubeBoundingRect(approximations);

        // Draws the found rectangle
        Mat cubeBoundingRectImage = new Mat(1080, 1920, CvType.CV_8UC3);
        Point[] points = cubeBoundingRect.toArray();
        Imgproc.rectangle(cubeBoundingRectImage, points[0], points[2], new Scalar(255, 255, 255), 5);
        debugOutput(cubeBoundingRectImage, "4_cubeBoundingRect");

        /**
         * white    = 0.95f, 0.95f, 0.91f
         * green    = 0.17f, 0.80f, 0.16f
         * red      = 0.87f, 0.14f, 0.14f
         * orange   = 0.84f, 0.50f, 0.12f
         * blue     = 0.15f, 0.68f, 0.82f
         * yellow   = 0.87f, 0.86f, 0.14f
         */
        model.setCubeColors(new float[][][]
                {
                        {
                                {0.95f, 0.95f, 0.91f},
                                {0.17f, 0.80f, 0.16f},
                                {0.95f, 0.95f, 0.91f},
                                {0.15f, 0.68f, 0.82f},
                                {0.87f, 0.86f, 0.14f},
                                {0.84f, 0.50f, 0.12f},
                                {0.84f, 0.50f, 0.12f},
                                {0.15f, 0.68f, 0.82f},
                                {0.95f, 0.95f, 0.91f}
                        }
                });

        model.callObservers("cubeFound");
    }

    private MatOfPoint2f findCubeBoundingRect(List<MatOfPoint2f> approximations) {
        double largestEdge = 0;
        MatOfPoint2f cubeBoundingRect = new MatOfPoint2f();

        for (MatOfPoint2f approximation : approximations) {
            if (approximation.rows() != 4) continue;
            Point[] points = approximation.toArray();

            double[] yPositions = {points[0].y, points[1].y, points[2].y, points[3].y};
            Arrays.sort(yPositions);

            double yDifference0 = Math.abs(yPositions[0] - yPositions[1]);
            double yDifference1 = Math.abs(yPositions[2] - yPositions[3]);

            if (yDifference0 > 15 || yDifference1 > 15) continue;

            double height = Math.abs(yPositions[0] - yPositions[3]);
            if (largestEdge < height){
                largestEdge = height;
                cubeBoundingRect = approximation;
            }
        }
        return cubeBoundingRect;
    }

    private Mat drawContoursOnMat(List<MatOfPoint> contours) {
        Mat mat = new Mat(1080, 1920, CvType.CV_8UC3);
        for (int index = 0; index < contours.size(); index++)
            Imgproc.drawContours(mat, contours, index, new Scalar(255, 255, 255), 5);
        return mat;
    }

    private List<MatOfPoint> convertMat2fToMat(List<MatOfPoint2f> matOfPoint2f) {
        List<MatOfPoint> matOfPoint = new ArrayList<>();
        for (MatOfPoint2f approximation : matOfPoint2f) {
            MatOfPoint contour = new MatOfPoint();
            approximation.convertTo(contour, CvType.CV_32S);
            matOfPoint.add(contour);
        }
        return matOfPoint;
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