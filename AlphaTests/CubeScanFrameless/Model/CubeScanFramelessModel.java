package AlphaTests.CubeScanFrameless.Model;

import org.opencv.core.Mat;

import java.util.Observable;

public class CubeScanFramelessModel extends Observable {

    private boolean debug = true;
    private boolean mirrorWebcam = false;
    private Mat loadedMat, processedMat;
    private int cannyThreshold1 = 17, cannyThreshold2 = 23; //17, 23
    private double sideLengthThreshold = 0.8; //0.8
    private double angleThreshold = 50; //50
    private double rotationThreshold = 20; //30
    private double blurThreshold = 3; //3
    private double dilateKernel = 1; //1
    private double scanAreaSize = 20;


    public void loadImage() {
        setChanged();
        notifyObservers("loadNewImage");
        setChanged();
        notifyObservers("processImage");
    }

    public void startWebcamStream() {
        setChanged();
        notifyObservers("startWebcamStream");
    }

    public void updateImageView() {
        setChanged();
        notifyObservers("updateImageView");
    }

    public Mat getLoadedMat() {
        return loadedMat;
    }
    public void setLoadedMat(Mat loadedMat) {
        this.loadedMat = loadedMat;
    }

    public Mat getProcessedMat() {
        return processedMat;
    }
    public void setProcessedMat(Mat processedMat) {
        this.processedMat = processedMat;
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

    public boolean isMirrorWebcam() {
        return mirrorWebcam;
    }
    public void setMirrorWebcam(boolean mirrorWebcam) {
        this.mirrorWebcam = mirrorWebcam;
    }

    public double getScanAreaSize() {
        return scanAreaSize;
    }
    public void setScanAreaSize(double scanAreaSize) {
        this.scanAreaSize = scanAreaSize;
    }
}
