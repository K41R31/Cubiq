package Processing;

import IO.WebcamCapture;
import Models.GuiModel;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.opencv.core.CvType.CV_8UC3;

public class ScanCube implements Observer {

    private GuiModel model;
    private WebcamCapture webcamCapture;
    private ScheduledExecutorService timer;
    private List<int[][]> scannedCubeSides = new ArrayList<>();
    private List<Double> centerColorSaturations = new ArrayList<>();

    public ScanCube() {
        webcamCapture = new WebcamCapture(0);
    }

    private void startLoop() {
        // Create a loop, that repeats "process" at the same speed as the framerate of the webcam
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(this::processFrames, 0, 1000 / webcamCapture.getFramerate(), TimeUnit.MILLISECONDS);
    }

    private void processFrames() {
        //Mat frame = new Mat(new Size(1920, 1080), CV_8UC3, new Scalar(255, 255, 255)); TODO Webcamless mode
        Mat frame = new Mat();
        webcamCapture.getVideoCapture().read(frame);
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);
        model.setWebcamframe(frame);

        // Get the position of the stickers
        List<Point> centers = cubeBoundarys(frame);

        // If no or less than 8 stickers were found, show the unprocessed image
        if (centers == null || centers.size() < 8) {
            model.updateImageView();
            return;
        }

        // Get the boundaries of the cube
        RotatedRect boundingRect = Imgproc.minAreaRect(new MatOfPoint2f(centers.toArray(new Point[0])));

        // Check if the boundingRect is a square. If not, show the unprocessed image
        if (!boundingRectIsSquare(boundingRect)) {
            model.updateImageView();
            return;
        }

        // Get a 3x3 grid (scanpoints) based on the bounding rectangle
        Point[][] scanpoints = gridBasedOnRect(boundingRect);

        // Get the mean HSV color at the scanpoints
        Scalar[][] meanHSVColorMatrix = meanHSVAtScanpoints(scanpoints, frame);

        // Get the colors at the scanpoints
        int[][] colorMatrix = normalizedColors(meanHSVColorMatrix);

        // Check if the scanned color matrix is already stored. If not, store it in the list differentColorMatrices
        if (isNewCubeSide(colorMatrix)) { // TODO Delay (Unscharfe Bilder) entweder mit Zeit oder mit 10 mal das gleiche erkannt
            scannedCubeSides.add(colorMatrix);
            centerColorSaturations.add(meanHSVColorMatrix[1][1].val[1]);
            model.setTotalCubeSideFound(scannedCubeSides.size());
        }

        // If all 6 sides were scanned, stop the loop
        if (scannedCubeSides.size() == 6) { // TODO WÜRFEL FERTIG--------------------------------------------------------------------------------------------------------------------------------
            System.out.println("FERTIG");
            logoCorrection();

            // Build the cube with the given color faces // TODO--------------------------------------------------------
            BuildCube buildCube = new BuildCube(scannedCubeSides);
            model.shutdown();
        }

        // Draw the found contours in the unprocessed image
        Mat contourMat = frame.clone();
//        Mat scanpointOverlay = Imgcodecs.imread("C://scanPointOverlay.png", Imgcodecs.IMREAD_UNCHANGED);

//        Imgproc.cvtColor(contourMat, contourMat, Imgproc.COLOR_HSV2BGR);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                Imgproc.circle(contourMat, scanpoints[x][y], 5, new Scalar(0, 0, 255), 10);
                //Rect roi = new Rect((int)Math.round(scanpoints[x][y].x), (int)Math.round(scanpoints[x][y].y), scanpointOverlay.width(),scanpointOverlay.height());
            }
        }

//        List<Mat> splitted = new ArrayList<>();
//        Core.split(scanpointOverlay, splitted);
//        alphaBlend(scanpointOverlay, contourMat, splitted.get());

        //Imgproc.cvtColor(contourMat, contourMat, Imgproc.COLOR_BGR2HSV);

        // Show processedMat
        model.setWebcamframe(contourMat);
        model.updateImageView();
    }

    private Mat alphaBlend(Mat foreground, Mat background, Mat alpha, Mat outImage) {
        // Find number of pixels.
        int numberOfPixels = foreground.rows() * foreground.cols() * foreground.channels();

        // Get floating point pointers to the data matrices
        /*
        float fptr = reinterpret_cast<float>(foreground.dataAddr());
        float bptr = reinterpret_cast<float>(background.data);
        float aptr = reinterpret_cast<float>(alpha.data);
        float outImagePtr = reinterpret_cast<float>(outImage.data);

        // Loop over all pixesl ONCE
        for(int i = 0; i < numberOfPixels; i++, outImagePtr++, fptr++, aptr++, bptr++) {
            *outImagePtr = (*fptr)*(*aptr) + (*bptr)*(1 - *aptr);
        }
        */
        return null;
    }

    private List<Point> cubeBoundarys(Mat frame) {
        // Convert hsv to gray
        List<Mat> splittedMat = new ArrayList<>();
        Core.split(frame, splittedMat);
        // Get the value
        Mat processedMat = splittedMat.get(2);

        // Add gaussian blur
        Imgproc.GaussianBlur(processedMat, processedMat, new Size(2 * model.getBlurThreshold() + 1, 2 * model.getBlurThreshold() + 1), 0);

        // Add Canny
        Imgproc.Canny(processedMat, processedMat, model.getCannyThreshold1(), model.getCannyThreshold2());

        // Make the lines thicker
        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * model.getDilateKernel() + 1, 2 * model.getDilateKernel() + 1));
        Imgproc.dilate(processedMat, processedMat, dilateKernel);

        // Find contours
        List<MatOfPoint> foundContours = new ArrayList<>();
        Imgproc.findContours(processedMat, foundContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        // Approximate the shape of the contours
        List<MatOfPoint2f> approximations = new ArrayList<>();
        for (int i = 0; i < foundContours.size(); i++) {
            approximations.add(new MatOfPoint2f());
            Imgproc.approxPolyDP(new MatOfPoint2f(foundContours.get(i).toArray()), approximations.get(i), 0.1 * Imgproc.arcLength(new MatOfPoint2f(foundContours.get(i).toArray()), true), true);
        }
        // Find the cube squares and their centers
        List<Point> centers = new ArrayList<>();
        List<MatOfPoint2f> cubeSquares = new ArrayList<>();
        for (MatOfPoint2f approximation : approximations) {
            // Proceed with the approximations that are squares
            if (!isSquare(approximation)) continue;
            // Save the found squares
            cubeSquares.add(approximation);
            // Get the centers
            Moments moments = Imgproc.moments(approximation);
            if (moments.m00 != 0) centers.add(new Point(moments.m10 / moments.m00, moments.m01 / moments.m00));
        }

        //TODO Bedinung bei der alle squares ungefähr gleich groß sein müssen

        // If nothing was found
        if (cubeSquares.isEmpty()) return null;

        // Remove overlapping squares TODO centerThreshold abhängig von der Quadratgröße machen -> immer das kleinere Quadrat nehmen
        int centerThreshold = 20;
        for (int i = 0; i < cubeSquares.size(); i++) {
            for (int c = 0; c < cubeSquares.size(); c++) {
                if (c == i) continue;
                if (centers.get(c).x < centers.get(i).x - centerThreshold || centers.get(c).x > centers.get(i).x + centerThreshold) continue;
                if (centers.get(c).y < centers.get(i).y - centerThreshold || centers.get(c).y > centers.get(i).y + centerThreshold) continue;
                centers.remove(c);
                cubeSquares.remove(c);
            }
        }
        return centers;
    }

    /**
     * Test whether the given approximations form a square
     * that is not too big or too small.
     * @param cornerMat The matrix, with the found approximations
     * @return true if the approximation is a square
     */
    private boolean isSquare(MatOfPoint2f cornerMat) {
        // All approximations that don't have four corners get filtered out
        if (cornerMat.rows() != 4) return false;

        // Sort the corners based on their location
        Point[] corners = sortCorners(cornerMat.toArray());

        // Calculate the length of all four sides
        double distanceTop = distanceBetweenTwoPoints(corners[0], corners[1]);
        double distanceRight = distanceBetweenTwoPoints(corners[1], corners[2]);
        double distanceBottom = distanceBetweenTwoPoints(corners[2], corners[3]);
        double distanceLeft = distanceBetweenTwoPoints(corners[3], corners[0]);
        double[] distances = new double[] {distanceTop, distanceRight, distanceBottom, distanceLeft};

        // Calculate the threshold for the rectangle
        double maxDistance = getMax(distances);
        double minDistance = maxDistance * model.getSideLengthThreshold();

        // If any side is much smaller than the given threshold or is generally very short or long, return false
        for (double distance : distances) {
            // Check for great length differences
            if (distance < minDistance) {
                return false;
            }
            // Check for sides that are shorter than 2% of the image width
            if (distance < model.getWebcamframe().width() * 0.02)
                return false;
            // Check for sides that are longer than 15% of the image width
            if (distance > model.getWebcamframe().width() * 0.15)
                return false;
        }

        // The angles of all four corners must not be outside the threshold
        double minAngle = 90 - model.getAngleThreshold();
        double maxAngle = 90 + model.getAngleThreshold();

        double angleTopLeft = getAngle(corners[3], corners[1], corners[0]);
        if (angleTopLeft < minAngle || angleTopLeft > maxAngle)
            return false;

        double angleTopRight = getAngle(corners[0], corners[2], corners[1]);
        if (angleTopRight < minAngle || angleTopRight > maxAngle)
            return false;

        double angleBottomRight = getAngle(corners[0], corners[2], corners[3]);
        if (angleBottomRight < minAngle || angleBottomRight > maxAngle)
            return false;

        double angleBottomLeft = getAngle(corners[3], corners[1], corners[2]);
        if (angleBottomLeft < minAngle || angleBottomLeft > maxAngle)
            return false;

        // The rectangle must not be rotated further than the threshold
        double farLeft = getMin(new double[] {corners[0].x, corners[1].x, corners[2].x, corners[3].x});
        double farRight = getMax(new double[] {corners[0].x, corners[1].x, corners[2].x, corners[3].x});
        double farUp = getMin(new double[] {corners[0].y, corners[1].y, corners[2].y, corners[3].y});
        Point boundingRectTopLeft = new Point(farLeft, farUp);
        Point boundingRectTopRight = new Point(farRight, farUp);


        if (corners[0].y > corners[1].y) {
            double angleRight = getAngle(corners[0], boundingRectTopLeft, corners[1]);
            if (angleRight > model.getRotationThreshold())
                return false;
        }

        else {
            double angleLeft = getAngle(corners[1], boundingRectTopRight, corners[0]);
            if (angleLeft > model.getRotationThreshold())
                return false;
        }
        return true;
    }

    private void logoCorrection() {
        int doubleColorIndex0 = -1;
        int doubleColorIndex1 = -1;
        List<Integer> centerColors = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int color = scannedCubeSides.get(i)[1][1];
            for (int j = 0; j < centerColors.size(); j++) {
                if (centerColors.get(j) == color && doubleColorIndex0 == -1) {
                    doubleColorIndex0 = i;
                    doubleColorIndex1 = j;
                }
            }
            centerColors.add(color);
        }
        if (doubleColorIndex0 == -1) return; // Kein Center doppelt. Keine Korrektur nötig
        if (centerColors.contains(0)) return; // Error. Weiß ist vorhanden. Eine andere Farbe ist doppelt. Cube neu einscannen

        // Ab hier ist klar, dass das Logo das Ergebnis abgefälscht hat
        double sat0 = centerColorSaturations.get(doubleColorIndex0);
        double sat1 = centerColorSaturations.get(doubleColorIndex1);

        int whiteIndex;
        if (sat0 < sat1) whiteIndex = doubleColorIndex0;
        else whiteIndex = doubleColorIndex1;

        int[][] singleCubeSide = scannedCubeSides.get(whiteIndex);
        singleCubeSide[1][1] = 0;
        scannedCubeSides.set(whiteIndex, singleCubeSide);
    }

    /**
     * Returns the angle at the point anglePoint for the triangle
     * formed by the three given points.
     * @param point0 The first point
     * @param point1 The second point
     * @param anglePoint The point at which the angle is calculated
     * @return The angle in degrees
     */
    private double getAngle(Point point0, Point point1, Point anglePoint) {

        // Get the distance between the points (Side x -> opposite side of point x)
        double side0 = distanceBetweenTwoPoints(anglePoint, point1);
        double side1 = distanceBetweenTwoPoints(point0, anglePoint);
        double side2 = distanceBetweenTwoPoints(point0, point1);

        // Calculate the cosine angle at anglePoint
        double cosAngle = (Math.pow(side0, 2) + Math.pow(side1, 2) - Math.pow(side2, 2)) / (2 * side0 * side1);

        // Limit the angle to 0 and 180
        if (cosAngle > 1) cosAngle = 1;
        else if (cosAngle < -1) cosAngle = -1;

        // Acos
        cosAngle = Math.acos(cosAngle);

        // Change to degrees
        cosAngle = Math.toDegrees(cosAngle);

        return cosAngle;
    }

    /**
     * Sorts the points so that they have the following order:
     * -> topLeft: 0, topRight: 1, bottomRight: 2, bottomLeft: 3
     * @param corners The points that are to be checked
     * @return The sorted points in an array
     */
    private Point[] sortCorners(Point[] corners) {

        List<Point> results = new ArrayList<>();

        // Get the min and max values for x and y
        double[] xValues = new double[] {corners[0].x, corners[1].x, corners[2].x, corners[3].x};
        double[] yValues = new double[] {corners[0].y, corners[1].y, corners[2].y, corners[3].y};
        double minX = getMin(xValues);
        double maxX = getMax(xValues);
        double minY = getMin(yValues);
        double maxY = getMax(yValues);

        // Top left
        results.add(getBoundingCorner(corners, results, new Point(minX, minY)));

        // Top right
        results.add(getBoundingCorner(corners, results, new Point(maxX, minY)));

        // Bootom Right
        results.add(getBoundingCorner(corners, results, new Point(maxX, maxY)));

        // Bootom Left
        results.add(getBoundingCorner(corners, results, new Point(minX, maxY)));

        return results.toArray(new Point[4]);
    }

    private Point getBoundingCorner(Point[] corners, List<Point> results, Point processCorner) {
        Point outputCorner = new Point();
        double topRightDistance = -1;
        for (Point corner : corners) {
            if (results.contains(corner)) continue;
            double distance = distanceBetweenTwoPoints(processCorner, corner);
            if (topRightDistance == -1 || distance < topRightDistance) {
                outputCorner = corner;
                topRightDistance = distance;
            }
        }
        return outputCorner;
    }

    private boolean boundingRectIsSquare(RotatedRect rotatedRect) {
        double thresholdPercentage = 20;
        // Checks if the width is outside of the threshold
        if (rotatedRect.size.width < rotatedRect.size.height * (100 - thresholdPercentage) / 100) return false;
        if (rotatedRect.size.width > rotatedRect.size.height * (100 + thresholdPercentage) / 100) return false;

        // If the rectangle is rotated further than allowed
        if (rotatedRect.angle < -90 || rotatedRect.angle > 0) return false;
        if (rotatedRect.angle > -60 && rotatedRect.angle < -30) return false;
        return true;
    }

    private Point centerBetweenTwoPoints(Point point0, Point point1) {
        double x, y;
        x = (point0.x + point1.x) / 2;
        y = (point0.y + point1.y) / 2;

        return new Point(x, y);
    }

    /**
     * Calculates the mean color from a rectangle with the size scanAreaSize and the center,
     * given by the two dimensional array scanpoints.
     * @param scanpoints A 3x3 array of points, where the colors should be scanned
     * @param frame The mat where the colors should be read from
     * @return The calculated colors, stored as a two dimensional int array:
     * 0 = white,
     * 1 = green,
     * 2 = red,
     * 3 = orange,
     * 4 = blue,
     * 5 = yellow
     */
    private Scalar[][] meanHSVAtScanpoints(Point[][] scanpoints, Mat frame) {
        double scanAreaHalf = model.getScanAreaSize() / 2;
        Scalar[][] colors = new Scalar[3][3];
        double hue, sat, val;
        double[] readColor;
        double unitVectorX, unitVectorY;

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                sat = 0;
                val = 0;
                unitVectorX = 0;
                unitVectorY = 0;

                // The start and endpoints, where the colors should get read from
                int startX = (int)Math.round(scanpoints[x][y].x - scanAreaHalf);
                int startY = (int)Math.round(scanpoints[x][y].y - scanAreaHalf);
                int endX = (int)Math.round(scanpoints[x][y].x + scanAreaHalf);
                int endY = (int)Math.round(scanpoints[x][y].y + scanAreaHalf);

                // Read all colors inside the scanRect
                for (int row = startY; row < endY; row++) {
                    for (int col = startX; col < endX; col++) {
                        // Read the Color from a pixel in the given rectangle
                        readColor = frame.get(row, col);
                        // Convert the hue into unit vectors
                        unitVectorX += Math.cos(Math.toRadians(readColor[0]*2));
                        unitVectorY += Math.sin(Math.toRadians(readColor[0]*2));
                        sat += readColor[1];
                        val += readColor[2];
                    }
                }
                // Get the mean of both unit vectors
                unitVectorX /= Math.pow(model.getScanAreaSize(), 2);
                unitVectorY /= Math.pow(model.getScanAreaSize(), 2);
                // Convert the calculated unit vector to an angle that can be used as a hue value
                hue = Math.toDegrees(Math.atan2(unitVectorY, unitVectorX));
                // Because the hue value is a circle, negative values should be added with 360°
                if (hue < 0) hue += 360;
                // Normalise the hue to the Open Cv range uses hue values between 0 - 179
                hue /= 2;
                sat /= Math.pow(model.getScanAreaSize(), 2);
                val /= Math.pow(model.getScanAreaSize(), 2);

                colors[x][y] = new Scalar(hue, sat, val);
            }
        }
        return colors;
    }

    private int[][] normalizedColors(Scalar[][] scalars) {
        int[][] normalizedColors = new int[3][3];
        for (int y  = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                double hue = scalars[x][y].val[0];
                double sat = scalars[x][y].val[1];
                double val = scalars[x][y].val[2];

                if (!(hue > 20 && hue < 70) && sat < 102 && val > 100) normalizedColors[x][y] = 0; // white
                else if (hue < 5) normalizedColors[x][y] = 2; // red
                else if (hue < 20) normalizedColors[x][y] = 3; // orange
                else if (hue < 45 || hue < 60 && sat < 155) normalizedColors[x][y] = 5; // yellow
                else if (hue < 90) normalizedColors[x][y] = 1; // green
                else if (hue < 140) normalizedColors[x][y] = 4; // blue
                else if (hue <= 180) normalizedColors[x][y] = 2; // red
            }
        }
        return normalizedColors;
    }

    private boolean isNewCubeSide(int[][] matrix) {
        if (scannedCubeSides.size() == 0) return true;
        int sameValuesCounter;
        for (int[][] scannedCubeSide : scannedCubeSides) {
            for (int rotation = 0; rotation < 4; rotation++) {
                sameValuesCounter = 0;
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        // Test if a color of the given matrix exists in the same place at scannedCubeSide
                        if (matrix[x][y] == scannedCubeSide[x][y]) {
                            // Raise the counter up
                            sameValuesCounter++;
                        }
                    }
                }
                // Sides are the same if 8 or more colors are at the same place
                if (sameValuesCounter >= 8) return false;
                if (rotation < 3) scannedCubeSide = rotateClockwise(scannedCubeSide);
            }
        }
        return true;
    }

    private int[][] rotateClockwise(int[][] array) {
        int[][] newArray = new int[3][3];
        for (int y = 0; y < 3; ++y)
            for (int x = 0; x < 3; ++x)
                newArray[y][x] = array[2 - x][y];
        return newArray;
    }

    private Point[][] gridBasedOnRect(RotatedRect boundingRect) {
        Point[][] scanpoints = new Point[3][3];
        Point[] boundingRectCorners = new Point[4];

        boundingRect.points(boundingRectCorners);

        // Creates a grid based on the centers of the stickers on the cube
        scanpoints[0][0] = boundingRectCorners[1];
        scanpoints[1][0] = centerBetweenTwoPoints(boundingRectCorners[1], boundingRectCorners[2]);
        scanpoints[2][0] = boundingRectCorners[2];
        scanpoints[0][1] = centerBetweenTwoPoints(boundingRectCorners[1], boundingRectCorners[0]);
        scanpoints[1][1] = centerBetweenTwoPoints(boundingRectCorners[1], boundingRectCorners[3]);
        scanpoints[2][1] = centerBetweenTwoPoints(boundingRectCorners[2], boundingRectCorners[3]);
        scanpoints[0][2] = boundingRectCorners[0];
        scanpoints[1][2] = centerBetweenTwoPoints(boundingRectCorners[0], boundingRectCorners[3]);
        scanpoints[2][2] = boundingRectCorners[3];
        return scanpoints;
    }

    /**Get both outer centers
     * Calculate the euclidean distance between two points
     * @param point0 The first point
     * @param point1 The second point
     * @return The euclidean distance between the two given points
     */
    private double distanceBetweenTwoPoints(Point point0, Point point1) {
        double xDiff = point0.x - point1.x;
        double yDiff = point0.y - point1.y;
        return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
    }

    /**
     * Calculate the smallest double value in the given array
     * @param values An array of doubles to be examined
     * @return The smallest double found
     */
    private double getMin(double[] values) {
        double min = values[0];
        for (int i = 1; i < values.length; i++) {
            min = Math.min(min, values[i]);
        }
        return min;
    }

    /**
     * Calculate the biggest double value in the given array
     * @param values An array of doubles to be examined
     * @return The biggest double found
     */
    private double getMax(double[] values) {
        double max = values[0];
        for (int i = 1; i < values.length; i++) {
            max = Math.max(max, values[i]);
        }
        return max;
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "startCubeScan":
                startLoop();
                break;
            case "shutdown":
                webcamCapture.getVideoCapture().release();
                timer.shutdownNow();
                System.exit(0);
                break;
        }
    }

    public void initModel(GuiModel model) {
        this.model = model;
    }
}
