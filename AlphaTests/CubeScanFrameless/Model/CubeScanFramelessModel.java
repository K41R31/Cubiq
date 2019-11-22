package AlphaTests.CubeScanFrameless.Model;

import org.opencv.core.Mat;

import java.util.Observable;

public class CubeScanFramelessModel extends Observable {

    private Mat originalMat, processedMat;
    private int cannyThreshold1 = 20, cannyThreshold2 = 40;


    public void loadImage() {
        setChanged();
        notifyObservers("loadNewImage");
        setChanged();
        notifyObservers("processImage");
    }

    public Mat getOriginalMat() {
        return originalMat;
    }
    public void setOriginalMat(Mat originalMat) {
        this.originalMat = originalMat;
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
}
