package AlphaTests.CubeScanFrameless.Model;

import org.opencv.core.Mat;

import java.util.Observable;

public class CubeScanFramelessModel extends Observable {

    private boolean debug = true;
    private Mat uprocessedMat, processedMat;
    private int cannyThreshold1 = 21, cannyThreshold2 = 19;
    private double sideLenghtThreshold = 0.6; //0.6
    private double angleThreshold = 50; //20
    private double rotationThreshold = 60; //60
    private double blurThreshold = 3;
    private double dilateKernel = 1;


    public void loadImage() {
        setChanged();
        notifyObservers("loadNewImage");
        setChanged();
        notifyObservers("processImage");
    }

    public Mat getUnprocessedMat() {
        return uprocessedMat;
    }
    public void setUprocessedMat(Mat uprocessedMat) {
        this.uprocessedMat = uprocessedMat;
    }

    public Mat getProcessedMat() {
        return processedMat;
    }
    public void setProcessedMat(Mat processedMat) {
        this.processedMat = processedMat;
        setChanged();
        notifyObservers("updateImageView");
    }

    public int getCannyThreshold1() {
        return cannyThreshold1;
    }
    public void setCannyThreshold1(int cannyThreshold1) {
        this.cannyThreshold1 = cannyThreshold1;
        setChanged();
        notifyObservers("processImage");
    }

    public int getCannyThreshold2() {
        return cannyThreshold2;
    }
    public void setCannyThreshold2(int cannyThreshold2) {
        this.cannyThreshold2 = cannyThreshold2;
        setChanged();
        notifyObservers("processImage");
    }

    public boolean isDebug() {
        return debug;
    }
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public double getSideLenghtThreshold() {
        return sideLenghtThreshold;
    }
    public void setSideLenghtThreshold(double sideLenghtThreshold) {
        this.sideLenghtThreshold = sideLenghtThreshold;
    }

    public double getAngleThreshold() {
        return angleThreshold;
    }
    public void setAngleThreshold(double angleThreshold) {
        this.angleThreshold = angleThreshold;
    }

    public double getRotationThreshold() {
        return rotationThreshold;
    }
    public void setRotationThreshold(double rotationThreshold) {
        this.rotationThreshold = rotationThreshold;
    }

    public double getBlurThreshold() {
        return blurThreshold;
    }
    public void setBlurThreshold(double blurThreshold) {
        this.blurThreshold = blurThreshold;
    }

    public double getDilateKernel() {
        return dilateKernel;
    }
    public void setDilateKernel(double dilateKernel) {
        this.dilateKernel = dilateKernel;
    }
}
