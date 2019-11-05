package AlphaTests.CubeScan.ImageProcessing;

import AlphaTests.CubeScan.Models.CubeScanModel;
import org.opencv.core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;
import java.util.*;

public class ImageProcessing implements Observer {

    private CubeScanModel model;

    /*
    private void processBinaryImage() {
        Mat processMat = model.getOriginalImage().clone();
        Imgproc.GaussianBlur(doMask(processMat), processMat, new Size(model.getGaBl(), model.getGaBl()), model.getGaBl(), model.getGaBl());
        Imgproc.medianBlur(processMat, processMat, model.getMeBl());
        Core.inRange(processMat,
                new Scalar(model.getGridColors()[2][2].val[0] - 5, model.getGridColors()[2][2].val[1] - 30, model.getGridColors()[2][2].val[2] - 30),
                new Scalar(model.getGridColors()[2][2].val[0] + 5, model.getGridColors()[2][2].val[1] + 30, model.getGridColors()[2][2].val[2] + 30), processMat);
        model.setBinaryImage(processMat);
    }
    */

    private void checkForCube() {
        Mat frameOfWebcamStream = model.getOriginalImage();
        double[] color = new double[3];
        Scalar[][] colors = new Scalar[3][3];
        Mat[][] binaryMatArray = new Mat[3][3];
        Mat[][] blobMatArray = new Mat[3][3];
        int[][] totalFoundBlobs = new int[3][3];
        List<KeyPoint> foundBlobs;
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
        detector.read("src/AlphaTests/CubeScan/Resources/SavedData/blobdetectorparams1.xml");

        //Read colors from grid
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int i = 0; i < 3; i++) color[i] = frameOfWebcamStream.get((int)model.getSearchPointGrid()[x][y].y, (int)model.getSearchPointGrid()[x][y].x)[i];
                colors[x][y] = new Scalar(color[0], color[1], color[2]);
            }
        }

        model.setGridColors(colors);

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {

                //Apply mask to image
                Mat mask = new Mat(frameOfWebcamStream.size(), CvType.CV_8U, new Scalar(0));
                Imgproc.rectangle(mask, model.getFrameOrigin(), new Point(model.getFrameOrigin().x + model.getSearchFrameSize(),  model.getFrameOrigin().y + model.getSearchFrameSize()), new Scalar(255), Core.FILLED);
                Mat processedFrame = new Mat(frameOfWebcamStream.size(), CvType.CV_8U, new Scalar(0));
                Imgproc.cvtColor(processedFrame, processedFrame, Imgproc.COLOR_GRAY2BGR);
                frameOfWebcamStream.copyTo(processedFrame, mask);

                //Image Operations
                if (model.getGaBl() != 0) Imgproc.GaussianBlur(processedFrame, processedFrame, new Size(model.getGaBl(), model.getGaBl()), model.getGaBl(), model.getGaBl());
                if (model.getMeBl() != 0) Imgproc.medianBlur(processedFrame, processedFrame, model.getMeBl()); //TODO Median Blur dauert lange
                Core.inRange(processedFrame,
                        new Scalar(model.getLoHu(), model.getLoSa(), model.getLoVa()),
                        new Scalar(model.getHiHu(), model.getHiSa(), model.getHiVa()), processedFrame);
                binaryMatArray[x][y] = processedFrame;

                System.out.println("loHu: " + model.getLoHu() + ", loSa: " + model.getLoSa() + ", loVa: " + model.getLoVa());
                System.out.println("hiHu: " + model.getHiHu() + ", hiSa: " + model.getHiSa() + ", hiVa: " + model.getHiVa());
                System.out.println(" ");

                //Detect Blobs
                detector.detect(processedFrame, keypoints);
                foundBlobs = keypoints.toList();
                Mat blobMat = model.getOriginalImage().clone();
                for (KeyPoint foundBlob : foundBlobs) Imgproc.circle(blobMat, foundBlob.pt, (int) foundBlob.size / 2, new Scalar(0, 0, 255), 1);
                totalFoundBlobs[x][y] = foundBlobs.size();
                Imgproc.cvtColor(blobMat, blobMat, Imgproc.COLOR_HSV2BGR);
                blobMatArray[x][y] = blobMat;
            }
        }
        model.setBinaryImages(binaryMatArray);
        model.setBlobImages(blobMatArray);
        model.setFoundBlobs(totalFoundBlobs);
        model.updateImageViews();
    }

    /*
    private void blobDetector() {
        Mat binaryMat = model.getBinaryImage().clone();
        Mat blobMat = model.getOriginalImage().clone();
        List<KeyPoint> foundBlobs;
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
        detector.read("src/AlphaTests/CubeScan/Resources/SavedData/blobdetectorparams1.xml");
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        detector.detect(binaryMat, keypoints);
        foundBlobs = keypoints.toList();
        model.setFoundBlobs(foundBlobs.size());
        keypoints.release();
        for (int i = 0; i < foundBlobs.size(); i++) Imgproc.circle(blobMat, foundBlobs.get(i).pt, (int)foundBlobs.get(i).size/2, new Scalar(100, 100, 100), 2);
        model.setBlobImage(blobMat);
    }
    */

    /*
    private Mat doMask(Mat inputMat) {
        Mat mask = new Mat(inputMat.size(), CvType.CV_8U, new Scalar(0));
        Imgproc.rectangle(mask, model.getFrameOrigin(), new Point(model.getFrameOrigin().x + 350,  model.getFrameOrigin().y + 350), new Scalar(255), Core.FILLED);

        Mat outputMat = new Mat(inputMat.size(), CvType.CV_8U, new Scalar(0));

        mask.convertTo(mask, CvType.CV_8U);
        inputMat.convertTo(inputMat, CvType.CV_8U);
        Imgproc.cvtColor(outputMat, outputMat, COLOR_GRAY2BGR);

        inputMat.copyTo(outputMat, mask);
        return outputMat;
    }
    */

    /*
    private void getGridColors() {
        Mat mat = model.getOriginalImage().clone();
        Scalar[][] colors = new Scalar[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                double[] color = new double[3];
                for (int i = 0; i < 3; i++) {
                    color[i] = mat.get((int)model.getSearchPointGrid()[x][y].y, (int)model.getSearchPointGrid()[x][y].x)[i];
                }
                colors[x][y] = new Scalar(color[0], color[1], color[2]);
            }
        }
        model.setGridColors(colors);
    }
    */

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "processImages":
                checkForCube();
                break;
        }
    }

    public void initModel(CubeScanModel model) {
        this.model = model;
    }
}
