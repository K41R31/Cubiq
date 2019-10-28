package AlphaTests.CubeScan.Models;

import org.opencv.core.Mat;

import java.util.Observable;

public class CubeScanModel extends Observable {

    private int loHu, hiHu, loSa, hiSa, loVa, hiVa;
    private int gaBl = 1, meBl = 1;
    private int foundBlobs;
    private Mat originalImage, binaryImage, blobImage;


    public void addSliderListener() {
        setChanged();
        notifyObservers("addSliderListener");
    }

    public void setLoHu(int value) {
        this.loHu = value;
        setChanged();
        notifyObservers("processImage");
    }
    public int getLoHu() {
        return this.loHu;
    }
    public void setHiHu(int value) {
        this.hiHu = value;
        setChanged();
        notifyObservers("processImage");
    }
    public int getHiHu() {
        return this.hiHu;
    }
    public void setLoSa(int value) {
        this.loSa = value;
        setChanged();
        notifyObservers("processImage");
    }
    public int getLoSa() {
        return this.loSa;
    }
    public void setHiSa(int value) {
        this.hiSa = value;
        setChanged();
        notifyObservers("processImage");
    }
    public int getHiSa() {
        return this.hiSa;
    }
    public void setLoVa(int value) {
        this.loVa = value;
        setChanged();
        notifyObservers("processImage");
    }
    public int getLoVa() {
        return this.loVa;
    }
    public void setHiVa(int value) {
        this.hiVa = value;
        setChanged();
        notifyObservers("processImage");
    }
    public int getHiVa() {
        return this.hiVa;
    }

    public void setGaBl(int value) {
        this.gaBl = value;
        setChanged();
        notifyObservers("processImage");
    }
    public int getGaBl() {
        return this.gaBl;
    }
    public void setMeBl(int value) {
        this.meBl = value;
        setChanged();
        notifyObservers("processImage");
    }
    public int getMeBl() {
        return this.meBl;
    }

    public void setFoundBlobs(int value) {
        this.foundBlobs = value;
        setChanged();
        notifyObservers("foundBlobsUpdated");
    }
    public int getFoundBlobs() {
        return this.foundBlobs;
    }

    public Mat getOriginalImage() {
        return this.originalImage;
    }
    public void setOriginalImage(Mat mat) {
        this.originalImage = mat;
        setChanged();
        notifyObservers("updateOriginalImage");
    }

    public Mat getBlobImage() {
        return this.blobImage;
    }
    public void setBlobImage(Mat mat) {
        this.blobImage = mat;
        setChanged();
        notifyObservers("updateBlobImage");
    }

    public Mat getBinaryImage() {
        return this.binaryImage;
    }
    public void setBinaryImage(Mat mat) {
        this.binaryImage = mat;
        setChanged();
        notifyObservers("updateBinaryImage");
    }
}
