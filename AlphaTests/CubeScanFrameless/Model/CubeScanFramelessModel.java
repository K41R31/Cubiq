package AlphaTests.CubeScanFrameless.Model;

import org.opencv.core.Mat;

import java.util.Observable;

public class CubeScanFramelessModel extends Observable {

    private boolean debug = true;
    private Mat uprocessedMat, processedMat;
    private int cannyThreshold1 = 17, cannyThreshold2 = 23; //17, 23
    private double sideLengthThreshold = 0.8; //0.8
    private double angleThreshold = 360; //50
    private double rotationThreshold = 360; //20
    private double blurThreshold = 3; //3
    private double dilateKernel = 1; //1


    public void loadImage() { //TODO
        //setChanged();
        //notifyObservers("loadNewImage");
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

    public double getSideLengthThreshold() {
        return sideLengthThreshold;
    }
    public void setSideLengthThreshold(double sideLengthThreshold) {
        this.sideLengthThreshold = sideLengthThreshold;
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
