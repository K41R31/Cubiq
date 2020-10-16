package AlphaBuildFullCube.Processing;

import AlphaBuildFullCube.Model.Model;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.*;

public class ImageProcessing implements Observer {

    private Model model;
    private Mat processMat;
    private List<MatOfPoint> contours;
    private List<MatOfPoint2f> approximations;
    private MatOfPoint2f cubeBoundingRect;
    private int side = 0;

    /**
     * Searches for a cube in a image
     */
    public void findCube() {
        Timeline processTimeline = new Timeline(
                new KeyFrame(
                        Duration.millis(0),
                        event -> {
                            // Get a image form the model and clone it
                            processMat = model.getOriginalCubeImages()[side].clone();
                            model.setProcessMat(processMat);
                            model.callObservers("processMatUpdated");

                            // Convert the image from brg to hsv
                            Imgproc.cvtColor(processMat, processMat, Imgproc.COLOR_BGR2HSV);

                            // Split the hvs image into three individual channels (h, s and v) and store them into a array list
                            // -> 0 = hue (color); 1 = saturation; 2 = value (brightness)
                            List<Mat> splittedMat = new ArrayList<>();
                            Core.split(processMat, splittedMat);

                            // Keep the image with the value channel (brightness map = grayscale image)
                            processMat = splittedMat.get(2);

                            // Blur the image with the kernel size 15
                            Imgproc.GaussianBlur(processMat, processMat, new Size(15, 15), 0);

                            // Apply the canny algorithm to the image (edge detection)
                            Imgproc.Canny(processMat, processMat, 5.0, 17.0);

                            // Make the lines thicker
                            Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
                            Imgproc.dilate(processMat, processMat, dilateKernel);

                            // Find all contours in the image and store them into the list contours
                            contours = new ArrayList<>();
                            Imgproc.findContours(processMat, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

                            // Approximates the shape of the contours with forms
                            // ArcLength calculates the length of a contour
                            // Closed = true -> keep only the forms that are closed
                            approximations = new ArrayList<>();
                            for (int j = 0; j < contours.size(); j++) {
                                approximations.add(new MatOfPoint2f());
                                Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(j).toArray()), approximations.get(j),
                                        0.1 * Imgproc.arcLength(new MatOfPoint2f(contours.get(j).toArray()),
                                                true), true);
                            }
                        }),
                new KeyFrame(
                        Duration.millis(200),
                        event -> {
                            // Finds the cube
                            cubeBoundingRect = findCubeBoundingRect(approximations);

                            // Draws the found rectangle
                            Point[] points = cubeBoundingRect.toArray();
                            processMat = model.getOriginalCubeImages()[side].clone();
                            Imgproc.rectangle(processMat, points[0], points[2], new Scalar(255, 255, 255), 5);
                            model.setProcessMat(processMat);
                            model.callObservers("processMatUpdated");
                            side++;
                        }),
                new KeyFrame(
                        Duration.millis(400),
                        event -> {
                            // TODO READ COLORS
/*
                            float[][] foundColors = new float[3][3];
                            float[][][] cubeSides = model.getCubeColors();
                            cubeSides[side] = foundColors;
                            model.setCubeColors(cubeSides);
*/

                            model.callObservers("newCubeSideFound");
                        })
        );

        processTimeline.setCycleCount(6);
        processTimeline.play();

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
                        },
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
                        },
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
                        },
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
                        },
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
                        },
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
        Mat mat = new Mat(model.getOriginalCubeImages()[0].rows(), model.getOriginalCubeImages()[0].cols(), CvType.CV_8UC3);
        for (int i = 0; i < contours.size(); i++)
            Imgproc.drawContours(mat, contours, i, new Scalar(255, 255, 255), 5);
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