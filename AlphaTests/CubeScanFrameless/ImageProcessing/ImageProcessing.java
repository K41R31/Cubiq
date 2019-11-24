package AlphaTests.CubeScanFrameless.ImageProcessing;

import AlphaTests.CubeScanFrameless.Model.CubeScanFramelessModel;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.*;

public class ImageProcessing implements Observer {

        private CubeScanFramelessModel model;


    private void checkForCube() {
        Mat inputMat = model.getUnprocessedMat();

        //Convert hsv to gray
        List<Mat> splittedMat = new ArrayList<>();
        Core.split(inputMat, splittedMat);
        Mat processedMat = splittedMat.get(2);

        //Add gaussian blur
        Imgproc.GaussianBlur(processedMat, processedMat, new Size(2 * model.getBlurThreshold() + 1, 2 * model.getBlurThreshold() + 1), 0);

        //Add Canny
        Imgproc.Canny(processedMat, processedMat, model.getCannyThreshold1(), model.getCannyThreshold2());

        //Make the lines thicker
        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * model.getDilateKernel() + 1, 2 * model.getDilateKernel() + 1));
        Imgproc.dilate(processedMat, processedMat, dilateKernel);

        //Find contours
        List<MatOfPoint> foundContours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(processedMat, foundContours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        //Approximate the shape of the contours
        List<MatOfPoint2f> approximations = new ArrayList<>();
        for (int i = 0; i < foundContours.size(); i++) {
            approximations.add(new MatOfPoint2f());
            Imgproc.approxPolyDP(new MatOfPoint2f(foundContours.get(i).toArray()), approximations.get(i), 0.1 * Imgproc.arcLength(new MatOfPoint2f(foundContours.get(i).toArray()), true), true);
        }

        //TODO Bei ineinander liegenden Rechecken, das Aüßere aussortieren (moments?)

        //Draw the found contours on the unprocessed image
        Mat contourMat = drawContoursOnUnprocessedImage(approximations);

        //Convert mat to bgr
        Imgproc.cvtColor(contourMat, contourMat, Imgproc.COLOR_HSV2BGR); //TODO Das wäre woanders besser aufgehoben
        model.setProcessedMat(contourMat);
    }

    /**
     * Tests whether the given approximations form a square
     */
    private boolean isSquare(MatOfPoint2f cornerMat) {

        //All approximations that don't have four corners are filtered out
        if (cornerMat.rows() != 4) return false;

        //Sort the corners based on their location
        Point[] corners = sortCorners(cornerMat.toArray());

        //Calculate the length of all four sides
        double distanceTop = distanceBetweenTwoPoints(corners[0], corners[1]);
        double distanceRight = distanceBetweenTwoPoints(corners[1], corners[2]);
        double distanceBottom = distanceBetweenTwoPoints(corners[2], corners[3]);
        double distanceLeft = distanceBetweenTwoPoints(corners[3], corners[0]);
        double[] distances = new double[] {distanceTop, distanceRight, distanceBottom, distanceLeft};

        //Calculate the threshold for the rectangle
        double maxDistance = getMax(distances);
        double minDistance = maxDistance * model.getSideLenghtThreshold();

        //If any side is much smaller than the given threshold or is generally very short or long, return false
        for (double distance : distances) {
            //Check for great length differences
            if (distance < minDistance) {
                return false;
            }
            //Check for sides that are shorter than 2% of the image width
            if (distance < model.getUnprocessedMat().width() * 0.02)
                return false;
            //Check for sides that are longer than 15% of the image width
            if (distance > model.getUnprocessedMat().width() * 0.15)
                return false;
        }

        //The angles of all four corners must not be outside the threshold
        double minAngle = 90 - model.getAngleThreshold();
        double maxAngle = 90 + model.getAngleThreshold();

        double angleTopLeft = Math.toDegrees(getAngle(corners[3], corners[1], corners[0]));
        if (angleTopLeft < minAngle || angleTopLeft > maxAngle)
            return false;

        double angleTopRight = Math.toDegrees(getAngle(corners[0], corners[2], corners[1]));
        if (angleTopRight < minAngle || angleTopRight > maxAngle)
            return false;

        double angleBottomRight = Math.toDegrees(getAngle(corners[0], corners[2], corners[3]));
        if (angleBottomRight < minAngle || angleBottomRight > maxAngle)
            return false;

        double angleBottomLeft = Math.toDegrees(getAngle(corners[3], corners[1], corners[2]));
        if (angleBottomLeft < minAngle || angleBottomLeft > maxAngle)
            return false;

        //The rectangle must not be rotated further than the threshold
        double farLeft = getMin(new double[] {corners[0].x, corners[1].x, corners[2].x, corners[3].x});
        double farRight = getMax(new double[] {corners[0].x, corners[1].x, corners[2].x, corners[3].x});
        double farUp = getMin(new double[] {corners[0].y, corners[1].y, corners[2].y, corners[3].y});
        Point boundingRectTopLeft = new Point(farLeft, farUp);
        Point boundingRectTopRight = new Point(farRight, farUp);

        if (corners[0].y < corners[1].y) {
            double angleLeft = Math.toDegrees(getAngle(corners[1], boundingRectTopRight, corners[0]));
            if (angleLeft > model.getRotationThreshold())
                return false;
        }

        else {
            double angleRight = Math.toDegrees(getAngle(corners[0], boundingRectTopLeft, corners[1]));
            if (angleRight > model.getRotationThreshold())
                return false;
        }

        return true;
    }

    //Returns the angle at point2 for the triangle formed by the three given points
    private double getAngle(Point point0, Point point1, Point point2) {

        //Get the distance between the points (Side x -> opposite side of point x)
        double side0 = distanceBetweenTwoPoints(point2, point1);
        double side1 = distanceBetweenTwoPoints(point0, point2);
        double side2 = distanceBetweenTwoPoints(point0, point1);

        //Calculate the cosine angle at point2
        double cosAngle = (Math.pow(side0, 2) + Math.pow(side1, 2) - Math.pow(side2, 2)) / (2 * side0 * side1);

        //Limit the angle to 0 and 180
        if (cosAngle > 1) cosAngle = 1;
        else if (cosAngle < -1) cosAngle = -1;

        return Math.acos(cosAngle);
    }

    /**
     * Sort the corners -> topLeft: 0, topRight: 1, bottomRight: 2, bottomLeft: 3
     */
    private Point[] sortCorners(Point[] corners) {

        List<Point> results = new ArrayList<>();

        //Get the min and max values for x and y //TODO Könnte man auch mit boundingRect lösen
        double[] xValues = new double[] {corners[0].x, corners[1].x, corners[2].x, corners[3].x};
        double[] yValues = new double[] {corners[0].y, corners[1].y, corners[2].y, corners[3].y};
        double minX = getMin(xValues);
        double maxX = getMax(xValues);
        double minY = getMin(yValues);
        double maxY = getMax(yValues);

        //Top left
        Point topLeft = new Point(); //TODO Kann man in einer externen Methode zusammenfassen
        double topLeftDistance = 0;
        for (Point corner : corners) {
            if (results.contains(corner)) continue;
            double distance = distanceBetweenTwoPoints(new Point(minX, minY), corner);
            if (topLeftDistance == 0 || distance < topLeftDistance) {
                topLeft = corner;
                topLeftDistance = distance;
            }
        }
        results.add(topLeft);

        //Top left
        Point topRight = new Point();
        double topRightDistance = 0;
        for (Point corner : corners) {
            if (results.contains(corner)) continue;
            double distance = distanceBetweenTwoPoints(new Point(maxX, minY), corner);
            if (topRightDistance == 0 || distance < topRightDistance) {
                topRight = corner;
                topRightDistance = distance;
            }
        }
        results.add(topRight);

        //Bootom Right
        Point bottomRight = new Point();
        double bottomRightDistance = 0;
        for (Point corner : corners) {
            if (results.contains(corner)) continue;
            double distance = distanceBetweenTwoPoints(new Point(maxX, maxY), corner);
            if (bottomRightDistance== 0 || distance < bottomRightDistance) {
                bottomRight = corner;
                bottomRightDistance = distance;
            }
        }
        results.add(bottomRight);

        //Bootom Left
        Point bottomLeft = new Point();
        double bottomLeftDistance = 0;
        for (Point corner : corners) {
            if (results.contains(corner)) continue;
            double distance = distanceBetweenTwoPoints(new Point(minX, maxY), corner);
            if (bottomLeftDistance== 0 || distance < bottomLeftDistance) {
                bottomLeft = corner;
                bottomLeftDistance = distance;
            }
        }
        results.add(bottomLeft);

        return results.toArray(new Point[4]);
    }

    private double distanceBetweenTwoPoints(Point point0, Point point1) {
        double xDiff = point0.x - point1.x;
        double yDiff = point0.y - point1.y;
        return Math.sqrt(Math.pow(xDiff,2) + Math.pow(yDiff, 2));
    }

    private double getMin(double[] values) {
        double min = values[0];
        for (int i = 1; i < values.length; i++) {
            min = Math.min(min, values[i]);
        }
        return min;
    }

    private double getMax(double[] values) {
        double max = values[0];
        for (int i = 1; i < values.length; i++) {
            max = Math.max(max, values[i]);
        }
        return max;
    }

    private Mat drawContoursOnUnprocessedImage(List<MatOfPoint2f> approximations) {
        List<MatOfPoint> convertedApproximations = new ArrayList<>();
        for (MatOfPoint2f approximation : approximations) {
            if (isSquare(approximation)) {
                convertedApproximations.add(new MatOfPoint());
                approximation.convertTo(convertedApproximations.get(convertedApproximations.size() - 1), CvType.CV_32S);
            }
        }
        Mat outputMat = model.getUnprocessedMat().clone();
        for (int i = 0; i < convertedApproximations.size(); i++) {
            Imgproc.drawContours(outputMat, convertedApproximations, i, new Scalar(60, 255, 255), 2);
        }
        return outputMat;
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "processImage":
                checkForCube();
                break;
        }
    }

    public void initModel(CubeScanFramelessModel model) {
        this.model = model;
    }
}
