package AlphaTests.CubeScan.ImageProcessing;

import AlphaTests.CubeScan.Models.CubeScanModel;
import org.opencv.core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ImageProcessing implements Observer {

    private CubeScanModel model;


    private void processBinaryImage() {
        Mat processMat = model.getOriginalImage().clone();
        Mat maskedMat = doMask(processMat);
        Imgproc.GaussianBlur(maskedMat, processMat, new Size(model.getGaBl(), model.getGaBl()), model.getGaBl(), model.getGaBl());
        Imgproc.cvtColor(processMat, processMat, Imgproc.COLOR_BGR2HSV);
        Imgproc.medianBlur(processMat, processMat, model.getMeBl());
        Core.inRange(processMat, new Scalar(model.getLoHu(), model.getLoSa(), model.getLoVa()), new Scalar(model.getHiHu(), model.getHiSa(), model.getHiVa()), processMat);
        model.setBinaryImage(maskedMat);
    }

    private void blobDetector() {
        Mat binaryMat = model.getBinaryImage().clone();
        Mat blobMat = model.getOriginalImage().clone();
        binaryMat.convertTo(binaryMat, CvType.CV_8U);
        List<KeyPoint> foundBlobs;
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
        detector.read("src/AlphaTests/CubeScan/Resources/SavedData/blobdetectorparams1.xml");
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        detector.detect(binaryMat, keypoints);
        foundBlobs = keypoints.toList();
        model.setFoundBlobs(foundBlobs.size());
        keypoints.release();
        for (int i = 0; i < foundBlobs.size(); i++) System.out.println(foundBlobs.get(i).pt);
        for (int i = 0; i < foundBlobs.size(); i++) Imgproc.circle(blobMat, foundBlobs.get(i).pt, (int)foundBlobs.get(i).size/2, new Scalar(100, 100, 100), 2);
        model.setBlobImage(blobMat);
    }

    private Mat doMask(Mat inputMat) {
        Mat mask = new Mat(new Size(1280, 720), CvType.CV_8U, new Scalar(0, 0, 0));
        Imgproc.rectangle(mask, new Point(464, 184), new Point(464+354, 184+354), new Scalar(255, 255, 255), Core.FILLED);

        //Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2HSV);

        Mat outputMat = new Mat(new Size(1280, 720), CvType.CV_8U);
        outputMat.copyTo(inputMat, mask);
        return outputMat;
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "processImage":
                processBinaryImage();
                blobDetector();
                break;
        }
    }

    public void initModel(CubeScanModel model) {
        this.model = model;
    }
}
