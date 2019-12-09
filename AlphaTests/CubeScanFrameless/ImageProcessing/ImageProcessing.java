package AlphaTests.CubeScanFrameless.ImageProcessing;

import AlphaTests.CubeScanFrameless.Model.CubeScanFramelessModel;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ImageProcessing implements Observer {

    private CubeScanFramelessModel model;
    private VideoCapture videoCapture;

    private void startWebcamStream() {
        videoCapture = new VideoCapture(0);
        videoCapture.set(3, 1280);
        videoCapture.set(4, 720);

        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(this::checkForCube, 0, 40, TimeUnit.MILLISECONDS);
    }

    private void checkForCube() {
        if (!videoCapture.isOpened()) throw new CvException("Webcam could not be found");

        Mat frame = new Mat();
        videoCapture.read(frame);

        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);

        // Convert hsv to gray
        List<Mat> splittedMat = new ArrayList<>();
        Core.split(frame, splittedMat);
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
        Mat heirarchy = new Mat();
        Imgproc.findContours(processedMat, foundContours, heirarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

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
            if (!isSquare(approximation, false)) continue;
            // Save the found squares
            cubeSquares.add(approximation);
            // Get the centers
            Moments moments = Imgproc.moments(approximation);
            if (moments.m00 != 0) centers.add(new Point(moments.m10 / moments.m00, moments.m01 / moments.m00));
        }

        // If nothing was found
        if (cubeSquares.isEmpty()) {
            Mat convertedMat = new Mat();
            Imgproc.cvtColor(frame, convertedMat, Imgproc.COLOR_HSV2BGR);
            model.setProcessedMat(convertedMat);
            return;
        }

        // Remove overlapping squares TODO centerThreshold abhängig von der Quadratgröße machen/ immer das kleinere Quadrat nehmen
        int centerThreshold = 20;
        for (int i = 0; i < cubeSquares.size(); i++) {
            for (int c = 0; c < cubeSquares.size(); c ++) {
                if (c == i) continue;
                if (centers.get(c).x < centers.get(i).x - centerThreshold || centers.get(c).x > centers.get(i).x + centerThreshold) continue;
                if (centers.get(c).y < centers.get(i).y - centerThreshold || centers.get(c).y > centers.get(i).y + centerThreshold) continue;
                centers.remove(c);
                cubeSquares.remove(c);
            }
        }


        // Bounding rect TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //if (centers.size() < 8) return;
        //if (centers.size() > 12) return;

        // Get the bounding rect
        MatOfPoint matOfPoint = new MatOfPoint();
        matOfPoint.fromList(centers);
        Rect boundingRect = Imgproc.boundingRect(matOfPoint);

        // Get the top left corner
        Object[] centersArray = centers.toArray(); //TODO EXCEPTION
        //getBoundingCorner(centersArray, new ArrayList<>(), new Point(boundingRect.x, boundingRect.y));

        /* TODO
            Wenn BoundingRect == Suqare -> keine Ausreißer (rotation könnte Probleme machen (rotation ausrechen))
            Wenn alle centers auf dem Raster liegen -> 100% Würfel gefunden
            Wenn centers <= 9 -> nur Würfel Steine gefunden
            Fehlende Steine von Raster ableiten
            Rasterpositionen == Position von Stein
            Farben auslesen
         */


        // Get the upper-left square
        int upperLeftSquareIndex = -1;
        double[] xValues = new double[cubeSquares.size()];
        double[] yValues = new double[cubeSquares.size()];
        for (int i = 0; i < cubeSquares.size(); i++) {
            xValues[i] = centers.get(i).x;
            yValues[i] = centers.get(i).y;
        }
        double minX = getMin(xValues);
        double minY = getMin(yValues);

        double topLeftDistance = 0;
        for (int i = 0; i < centers.size(); i++) {
            Point center = centers.get(i);
            double distance = distanceBetweenTwoPoints(new Point(minX, minY), center);
            if (topLeftDistance == 0 || distance < topLeftDistance) {
                topLeftDistance = distance;
                upperLeftSquareIndex = i;
            }
        }

        // Draw the found contours in the unprocessed image
        List<MatOfPoint> convertedApproximations = new ArrayList<>();
        for (MatOfPoint2f cubeSquare : cubeSquares) {
            convertedApproximations.add(new MatOfPoint());
            cubeSquare.convertTo(convertedApproximations.get(convertedApproximations.size() - 1), CvType.CV_32S);
        }
        Mat contourMat = frame.clone();
        for (int i = 0; i < convertedApproximations.size(); i++) {
            Imgproc.drawContours(contourMat, convertedApproximations, i, new Scalar(60, 255, 255), 2);
        }

        //TODO DRAW BOUNDING RECT + GRID--------------------------------------------------------------------------------
        Point boundingPoint1 = new Point(boundingRect.x, boundingRect.y);
        Point boundingPoint2 = new Point(boundingRect.x + boundingRect.width, boundingRect.y + boundingRect.height);
        double differenceX = boundingPoint2.x - boundingPoint1.x;
        double differenceY = boundingPoint2.y - boundingPoint1.y;

        Imgproc.rectangle(contourMat, boundingPoint1, boundingPoint2, new Scalar(5, 255, 255), 5);
        Imgproc.line(contourMat,
                new Point(boundingPoint1.x + differenceX / 2, boundingPoint1.y),
                new Point(boundingPoint1.x + differenceX / 2, boundingPoint2.y),
                new Scalar(5, 255, 255), 5
        );
        Imgproc.line(contourMat,
                new Point(boundingPoint1.x, boundingPoint1.y + differenceY / 2),
                new Point(boundingPoint2.x, boundingPoint1.y + differenceY / 2),
                new Scalar(5, 255, 255), 5
        );
        //TODO END------------------------------------------------------------------------------------------------------

        // Convert mat to bgr
        Imgproc.cvtColor(contourMat, contourMat, Imgproc.COLOR_HSV2BGR);
        model.setProcessedMat(contourMat);
    }

    /**
     * Test whether the given approximations form a square
     * that is not too big or too small.
     * @param cornerMat The matrix, with the found approximations
     * @param ignoreAbsoluteWidth if true, the general width will be ignored
     * @return true if the approximation is a square
     */
    private boolean isSquare(MatOfPoint2f cornerMat, boolean ignoreAbsoluteWidth) {

        // All approximations that don't have four corners are filtered out
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
            // If ignoreAbsoluteWidth is true, the general width will be ignored
            if (ignoreAbsoluteWidth) continue;
            // Check for sides that are shorter than 2% of the image width
            if (distance < 1280 * 0.02) //TODO Später dynamisch mit width von frame
                return false;
            // Check for sides that are longer than 15% of the image width
            if (distance > 1280 * 0.15) //TODO Später dynamisch mit width von frame
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

    private boolean boundingRectIsSquare() {
        return false;
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

    /**
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
     * Calculate the bigest double value in the given array
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
            case "processImage":
                startWebcamStream();
                break;
        }
    }

    public void initModel(CubeScanFramelessModel model) {
        this.model = model;
    }
}
