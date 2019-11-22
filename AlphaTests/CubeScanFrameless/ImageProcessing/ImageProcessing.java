package AlphaTests.CubeScanFrameless.ImageProcessing;

import AlphaTests.CubeScanFrameless.Model.CubeScanFramelessModel;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.*;

public class ImageProcessing implements Observer {

        private CubeScanFramelessModel model;
        private int blurThreshold = 3;
        private int dilateKernelSize = 2;


    private void checkForCube() {
        Mat inputMat = model.getOriginalMat();

        //Convert hsv to gray
        List<Mat> splittedMat = new ArrayList<>();
        Core.split(inputMat, splittedMat);
        Mat processedMat = splittedMat.get(2);

        //Add gaussian blur
        if (blurThreshold != 0) Imgproc.GaussianBlur(processedMat, processedMat, new Size(2 * blurThreshold + 1, 2 * blurThreshold + 1), 0);

        //Add Canny
        Imgproc.Canny(processedMat, processedMat, model.getCannyThreshold1(), model.getCannyThreshold2());

        //Make the lines thicker
        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * dilateKernelSize + 1, 2 * dilateKernelSize + 1));
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

        //Draw contours
        /*
        Mat contourMat = new Mat(model.getOriginalMat().size(), CvType.CV_8UC3);
        List<MatOfPoint> convertedApproximations = new ArrayList<>();
        for (int i = 0; i < approximations.size(); i++) {
            convertedApproximations.add(new MatOfPoint());
            approximations.get(i).convertTo(convertedApproximations.get(i), CvType.CV_32S);
        }
        //TODO Das Zeichnen von den Konturen dauert sehr lange
        for (int i = 0; i < approximations.size(); i++) {
            Imgproc.drawContours(contourMat, convertedApproximations, i, new Scalar(0, 0, 255));
        }
        */

        ArrayList<Double> areas = new ArrayList<>();
        for (int i = 0; i < approximations.size();  i++) {
            double area = Imgproc.contourArea(approximations.get(0));
            if (area > 200) areas.add(area);
        }
        System.out.println(areas.size()); //TODO Rechnet die Fläche einer (geschlossenen) Kontur aus (gibt einen double zurück)
        System.out.println(Imgproc.moments(approximations.get(100))); //TODO Rechnet die Mitte einer (geschlossenen) Kontur aus (gibt Moments Objekte zurück)

        model.setProcessedMat(processedMat);
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
