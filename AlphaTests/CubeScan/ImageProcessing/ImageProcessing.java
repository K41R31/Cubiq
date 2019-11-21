package AlphaTests.CubeScan.ImageProcessing;

import AlphaTests.CubeScan.Models.CubeScanModel;
import org.opencv.core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;
import java.util.*;

public class ImageProcessing implements Observer {

    private CubeScanModel model;

    private double meanColorRectSize = 60; //TODO Ins Model, meanColorRectSize % 2 != 0
    private boolean debug = true;
    private int hueThreshold = 5;
    private int satThreshold = 40;
    private int valThreshold = 40;


    private void checkForCubeNew() {
        Mat inputMat = model.getOriginalImage();

        //Convert hsv to gray
        List<Mat> splittedMat = new ArrayList<>();
        Core.split(inputMat, splittedMat);
        Mat grayMat = splittedMat.get(2);

        //Add gaussian blur
        Mat blurredMat = new Mat();
        Imgproc.GaussianBlur(grayMat, blurredMat, new Size(3, 3), 0);

        //Add Canny
        Mat cannyMat = new Mat();
        Imgproc.Canny(blurredMat, cannyMat, 20, 40);

        Mat[][] matList = new Mat[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                matList[x][y] = cannyMat;
            }
        }
        model.setBinaryImages(matList);
        model.updateImageViews();
    }

    private void readColorsFromGrid() {
        //Clones the unprocessed input Mat
        Mat frameOfWebcamStream = model.getOriginalImage().clone();
        Scalar[][] colors = new Scalar[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                //Executes getMeanHSVColor with the current point from the search grid and writes the value into colors
                colors[x][y] = getMeanHSVColor(frameOfWebcamStream, new Point(
                        model.getSearchPointGrid()[x][y].x - meanColorRectSize / 2,
                        model.getSearchPointGrid()[x][y].y - meanColorRectSize / 2)
                );
            }
        }
        //Passes the found average colors to the model
        model.setGridColors(colors);
    }

    private void checkForCube() {
        Mat frameOfWebcamStream = model.getOriginalImage();
        Mat[][] binaryMatArray = new Mat[3][3];
        Mat[][] blobMatArray = new Mat[3][3];
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        List<KeyPoint>[][] totalBlobList = new List[3][3];
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
        detector.read("src/AlphaTests/CubeScan/Resources/SavedData/blobdetectorparams1.xml");

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {

                List<KeyPoint> blobList;

                //Apply mask to image
                Mat mask = new Mat(frameOfWebcamStream.size(), CvType.CV_8U, new Scalar(0));
                Imgproc.rectangle(mask, model.getFrameOrigin(), new Point(model.getFrameOrigin().x + model.getSearchFrameSize(), model.getFrameOrigin().y + model.getSearchFrameSize()), new Scalar(255), Core.FILLED);
                Mat processMat = new Mat(frameOfWebcamStream.size(), CvType.CV_8U, new Scalar(0));
                Imgproc.cvtColor(processMat, processMat, Imgproc.COLOR_GRAY2BGR);
                frameOfWebcamStream.copyTo(processMat, mask);

                //TODO Gaussian-Blur Produziert Grüne Artefakte
                //Image Operations
                //if (model.getGaBl() != 0) Imgproc.GaussianBlur(processMat, processMat, new Size(model.getGaBl(), model.getGaBl()), model.getGaBl(), model.getGaBl());
                if (model.getMeBl() != 0) Imgproc.medianBlur(processMat, processMat, model.getMeBl());

                //TODO Range Slider Werte
                //new Scalar(model.getLoHu(), model.getLoSa(), model.getLoVa()),
                //new Scalar(model.getHiHu(), model.getHiSa(), model.getHiVa()), processMat);

                processMat = processInRange(x, y, processMat, model.getGridColors()[x][y]);
                binaryMatArray[x][y] = processMat;

                //Detect Blobs
                detector.detect(processMat, keypoints);
                blobList = keypoints.toList();
                Mat blobMat = model.getOriginalImage().clone();
                for (KeyPoint foundBlob : blobList) Imgproc.circle(blobMat, foundBlob.pt, (int) foundBlob.size / 2, new Scalar(0, 0, 255), 1);
                Imgproc.cvtColor(blobMat, blobMat, Imgproc.COLOR_HSV2BGR);
                blobMatArray[x][y] = blobMat;

                totalBlobList[x][y] = blobList;
            }
        }

        int counter = 0;

        //Calculates how many frames found a cube
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                for (int i = 0; i < totalBlobList[x][y].size(); i++) {
                    Point BlobPosition = totalBlobList[x][y].get(i).pt;
                    Point girdPos = model.getSearchPointGrid()[x][y];

                    if (BlobPosition.x < girdPos.x - meanColorRectSize / 2) continue;
                    if (BlobPosition.x > girdPos.x + meanColorRectSize / 2) continue;
                    if (BlobPosition.y < girdPos.y - meanColorRectSize / 2) continue;
                    if (BlobPosition.y > girdPos.y + meanColorRectSize / 2) continue;
                    counter++;
                }
            }
        }
        //If a cube is found in more than 6 images, there is a cube in the window
        if (debug) {
            if (counter > 6) {
                System.out.println("-------------------------------");
                System.out.println("Cube found: " + counter + " / 9");
                System.out.print("\n");
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        System.out.print(colorDetection(model.getGridColors()[x][y]));
                        if (x < 2) System.out.print(", ");
                    }
                    System.out.print("\n");
                }
                System.out.println("-------------------------------");
            } else System.out.println("No Cube found: " + counter + " / 9");
        }
        model.setBinaryImages(binaryMatArray);
        model.setBlobImages(blobMatArray);
        model.updateImageViews();
    }

    private Scalar getMeanHSVColor(Mat frameOfWebcamStream, Point searchGridPoint) {
        double hue, sat = 0, val = 0;
        double[] readColor;
        double unitVectorX = 0, unitVectorY = 0;
        for (int col = 0; col < meanColorRectSize; col++) {
            for (int row = 0; row < meanColorRectSize; row++) {
                //Reads the Color from a pixel in the given rectangle
                readColor = frameOfWebcamStream.get((int)Math.round(row + searchGridPoint.y), (int)Math.round(col + searchGridPoint.x));
                //Convert the hue in unit vectors
                unitVectorX += Math.cos(Math.toRadians(readColor[0]*2));
                unitVectorY += Math.sin(Math.toRadians(readColor[0]*2));
                sat += readColor[1];
                val += readColor[2];
            }
        }
        //Get the mean of all unit vectors
        unitVectorX /= Math.pow(meanColorRectSize, 2);
        unitVectorY /= Math.pow(meanColorRectSize, 2);
        //Convert the calculated unit vector to an angle that can be used as a hue value
        hue = Math.toDegrees(Math.atan2(unitVectorY, unitVectorX));
        //Because the hue value is a circle, negative values should be added with 360°
        if (hue < 0) hue += 360;
        //Open Cv uses hue values between 0 - 179
        hue /= 2;
        sat /= Math.pow(meanColorRectSize, 2);
        val /= Math.pow(meanColorRectSize, 2);
        return new Scalar(Math.round(hue), Math.round(sat), Math.round(val));
    }

    private Mat processInRange(int x, int y, Mat processMat, Scalar scalar) {
        double[] color = scalar.val;

        //If the hue - threshold is less than 0, two in range operations must be performed, which are then added to each other
        if (color[0] - hueThreshold < 0) {
            Mat lowerRedMat = new Mat(), upperRedMat = new Mat();
            Core.inRange(processMat,
                    new Scalar(0, color[1] - satThreshold, color[2] - valThreshold),
                    new Scalar(color[0] + hueThreshold, color[1] + satThreshold, color[2] + valThreshold), lowerRedMat);
            Core.inRange(processMat,
                    new Scalar(179 + color[0] - hueThreshold, color[1] - satThreshold, color[2] - valThreshold),
                    new Scalar(179, color[1] + satThreshold, color[2] + valThreshold), upperRedMat);
            Core.add(lowerRedMat, upperRedMat, processMat);

            if (debug) System.out.println("Image " + x + ", " + y + " ->" +
                    "lowHue: " + 0 + "-" + Math.round(color[0] + hueThreshold) + ", HighHue: " + Math.round(179 + color[0] - hueThreshold) + "-" + 179 +
                    "/ saturation: " + (color[1] - satThreshold) + " - " + (color[1] + satThreshold) +
                    "/ value: " + (color[2] - valThreshold) + " - " + (color[2] + valThreshold));
        }
        //If the hue + threshold is greater than 179, two in range operations must be performed, which are then added to each other
        else if (color[0] + hueThreshold > 179) {
            Mat lowerRedMat = new Mat(), upperRedMat = new Mat();
            Core.inRange(processMat,
                    new Scalar(0, color[1] - satThreshold, color[2] - valThreshold),
                    new Scalar(color[0] + hueThreshold - 179, color[1] + satThreshold, color[2] + valThreshold), lowerRedMat);
            Core.inRange(processMat,
                    new Scalar(color[0] - hueThreshold, color[1] - satThreshold, color[2] - valThreshold),
                    new Scalar(179, color[1] + satThreshold, color[2] + valThreshold), upperRedMat);
            Core.add(lowerRedMat, upperRedMat, processMat);

            if (debug) System.out.println("Image " + x + ", " + y + " ->" +
                    "lowHue: " + 0 + "-" + Math.round(color[0] + hueThreshold - 179) + ", Highue: " + Math.round(color[0] - hueThreshold) + "-" + 179 +
                    "/ saturation: " + (color[1] - satThreshold) + " - " + (color[1] + satThreshold) +
                    "/ value: " + (color[2] - valThreshold) + " - " + (color[2] + valThreshold));
        }
        //Performs a Range operation with the found hue +- Threshold
        else {
            Core.inRange(processMat,
                    new Scalar(color[0] - hueThreshold, color[1] - satThreshold, color[2] - valThreshold),
                    new Scalar(color[0] + hueThreshold, color[1] + satThreshold, color[2] + valThreshold), processMat);

            if (debug) System.out.println("Image " + x + ", " + y + " ->" +
                    "hue: " + Math.round(color[0] - hueThreshold) + " - " + Math.round(color[0] + hueThreshold) +
                    "/ saturation: " + (color[1] - satThreshold) + " - " + (color[1] + satThreshold) +
                    "/ value: " + (color[2] - valThreshold) + " - " + (color[2] + valThreshold));
        }
        return processMat;
    }

    private String colorDetection(Scalar color) {
        //Calculates the respective color from the color values
        if (color.val[1] < 100 && color.val[2] > 110) return "WHITE";
        else if (color.val[0] < 5) return "RED";
        else if (color.val[0] < 18) return "ORANGE";
        else if (color.val[0] < 36) return "YELLOW";
        else if (color.val[0] < 100) return "GREEN";
        else if (color.val[0] < 128) return "BLUE";
        else if (color.val[0] < 181) return "RED";
        return "NO COLOR DETECTED";
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "processImages":
                checkForCubeNew();
                break;
            case "readColorsFromGrid":
                readColorsFromGrid();
                break;
        }
    }

    public void initModel(CubeScanModel model) {
        this.model = model;
    }
}


/*
    private void RGBtoHSV() {
        double[] bgr = new double[] {139, 192, 120};
        double h, s, v;
        double min, max, delta;
        //Get the min, max value of rgb
        min = bgr[0];
        max = bgr[0];
        for (int i = 1; i < bgr.length; i++) {
            min = Math.min(min, bgr[i]);
            max = Math.max(max, bgr[i]);
        }
        v = max;                        // v
        delta = max - min;
        if (max != 0) s = delta / max * 255; //s
        else {                           // r = g = b = 0
            s = 0;
            h = -1;
            System.out.println("hsv: " + h + ", " + s + ", " + v);
            return;
        }
        if (max == min) {                // Alles Grau
            h = 0;
            s = 0;
            System.out.println("hsv: " + h + ", " + s + ", " + v);
            return;
        }
        if (bgr[2] == max) h = (bgr[1] - bgr[0]) / delta;       // Zwischen Gelb und Magenta
        else if (bgr[1] == max) h = 2 + (bgr[0] - bgr[2]) / delta;   // Zwischen Cyan und Gelb
        else h = 4 + (bgr[2] - bgr[1]) / delta;   // Zwischen Magenta und Zyan
        h *= 60;                     // Degrees
        if (h < 0) h += 360;
        h /= 2;
        System.out.println("hsv: " + h + ", " + s + ", " + v);
    }

    void HSVtoRGB( float r, float g, float b, float h, float s, float v) { //rgb -> [0,1] hsv -> [0,255; 0,1; 0,1]
        int i;
        float f, p, q, t;
        if(s == 0) { // achromatisch (Grau)
            r = g = b = v;
            return;
        }
        h /= 60;           // sector 0 to 5
        i = Math.round(h);
        f = h - i;         // factorial part of h
        p = v * (1 - s);
        q = v * (1 - s * f);
        t = v * (1 - s * (1 - f));
        switch(i) {
            case 0: r = v; g = t; b = p; break;
            case 1: r = q; g = v; b = p; break;
            case 2: r = p; g = v; b = t; break;
            case 3: r = p; g = q; b = v; break;
            case 4: r = t; g = p; b = v; break;
            default: r = v; g = p; b = q; //case 5
            break;
        }
    }
 */
