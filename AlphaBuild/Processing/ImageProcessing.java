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
        List<Mat> splitMat = new ArrayList<>();
        Core.split(image, splitMat);

        // Keep the image with the value channel (brightness map = grayscale image)
        Mat greyImg = splitMat.get(2);
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

        // Get a bounding rectangle of the cube
        MatOfPoint2f cubeBoundingRect = findCubeBoundingRect(approximations);

        // Draws the found rectangle
        Mat cubeBoundingRectImage = new Mat(1080, 1920, CvType.CV_8UC3);
        Point[] points = cubeBoundingRect.toArray();
        Imgproc.rectangle(cubeBoundingRectImage, points[0], points[2], new Scalar(255, 255, 255), 5);
        debugOutput(cubeBoundingRectImage, "4_cubeBoundingRect");

        // Get the cube grid
        Point[][] cubeGrid = getCubeGrid(points);

        // Debug output for the cube grid
        Mat debugCircleMat = model.getOriginalImage().clone();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                Imgproc.circle(debugCircleMat, cubeGrid[x][y], 2, new Scalar(255, 255, 255), 5);
            }
        }
        debugOutput(debugCircleMat, "5_circles");

        int[][] normalizedColors = new int[3][3];

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                // Get the hsv color from a point in the cube grid
                double[] hsvColor = image.get((int)cubeGrid[x][y].y, (int)cubeGrid[x][y].x);

                // Normalize the color to an int value and store it in the array colors
                normalizedColors[x][y] = normalizeColors(hsvColor);
            }
        }

        model.setNormalizedColors(normalizedColors);
        model.callObservers("cubeFound");
    }

    /**
     * 0 white
     * 1 green
     * 2 red
     * 3 orange
     * 4 blue
     * 5 yellow
     * @param color
     * @return
     */
    private int normalizeColors(double[] color) {
        double hue = color[0];
        double sat = color[1];
        double val = color[2];

        if (!(hue > 20 && hue < 70) && sat < 102 && val > 100) return 0; // white
        if (hue < 5) return 2; // red
        if (hue < 20) return 3; // orange
        if (hue < 45 || hue < 60 && sat < 155) return 5; // yellow
        if (hue < 90) return 1; // green
        if (hue < 140) return 4; // blue
        if (hue <= 180) return 2; // red
        return -1;
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

    /**
     * Returns the location of all nine stickers, based on the corners of the cube
     * @param corners Four bounding corners of a cube
     * @return 2D array of all nine stickers
     */
    private Point[][] getCubeGrid(Point[] corners) {
        Point[][] cubeGrid = new Point[3][3];
        Point[] sortedCorners = sortCorners(corners);

        double cubieDistance = (sortedCorners[1].x - sortedCorners[0].x) / 3;
        double cubieOffset = cubieDistance / 2;
        double cubieMinY = sortedCorners[0].y + cubieOffset;

        for (int y = 0; y < 3; y++) {
            double cubieMinX = sortedCorners[0].x + cubieOffset;
            for (int x = 0; x < 3; x++) {
                cubeGrid[x][y] = new Point(cubieMinX + x * cubieDistance, cubieMinY + y * cubieDistance);
            }
        }
        return cubeGrid;
    }

    /**
     * Sorts the corners in the following order:
     * -> topLeft: 0, topRight: 1, bottomLeft: 2, bottomRight: 3
     * @param corners Unsorted corners
     */
    private Point[] sortCorners(Point[] corners) {
        Point[] sortedCorners = new Point[corners.length];
        Point center = getCenter(corners);

        for (Point corner : corners) {
            if (corner.x < center.x) {
                if (corner.y < center.y) {
                    sortedCorners[0] = corner;
                } else {
                    sortedCorners[2] = corner;
                }
            } else {
                if (corner.y < center.y) {
                    sortedCorners[1] = corner;
                } else {
                    sortedCorners[3] = corner;
                }
            }
        }
        return sortedCorners;
    }

    private Point getCenter(Point[] corners) {
        // Get min and max values
        double minX = corners[0].x;
        double maxX = corners[0].x;
        double minY = corners[0].y;
        double maxY = corners[0].y;

        for (int i = 1; i < corners.length; i++) {
            if (corners[i].x < minX) minX = corners[i].x;
            if (corners[i].x > maxX) maxX = corners[i].x;
            if (corners[i].y < minY) minY = corners[i].y;
            if (corners[i].y > maxY) maxY = corners[i].y;
        }

        return new Point((maxX - minX) / 2 + minX, (maxY - minY) / 2 + minY);
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