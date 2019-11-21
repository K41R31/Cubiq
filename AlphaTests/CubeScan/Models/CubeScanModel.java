package AlphaTests.CubeScan.Models;

import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import java.util.List;
import java.util.Observable;

public class CubeScanModel extends Observable {

    private Point frameOrigin = new Point(466, 185);
    private int loHu, hiHu, loSa, hiSa, loVa, hiVa;
    private int gaBl = 5, meBl = 5;
    private Mat originalImage, binaryImages;
    private boolean useMeanColor = true;


    public void loadNewImage() {
        setChanged();
        notifyObservers("loadNewImage");
        setChanged();
        notifyObservers("addSlider");
        setChanged();
        notifyObservers("processImage");
    }

    public int getLoHu() {
        return this.loHu;
    }
    public void setLoHu(int value) {
        this.loHu = value;
    }
    public int getHiHu() {
        return hiHu;
    }
    public void setHiHu(int hiHu) {
        this.hiHu = hiHu;
    }

    public int getLoSa() {
        return this.loSa;
    }
    public void setLoSa(int value) {
        this.loSa = value;
    }
    public int getHiSa() {
        return hiSa;
    }
    public void setHiSa(int hiSa) {
        this.hiSa = hiSa;
    }

    public int getLoVa() {
        return this.loVa;
    }
    public void setLoVa(int value) {
        this.loVa = value;
    }
    public int getHiVa() {
        return hiVa;
    }
    public void setHiVa(int hiVa) {
        this.hiVa = hiVa;
    }

    public int getGaBl() {
        return this.gaBl;
    }
    public void setGaBl(int value) {
        this.gaBl = value;
    }
    public int getMeBl() {
        return this.meBl;
    }
    public void setMeBl(int value) {
        this.meBl = value;
    }

    public Mat getOriginalImage() {
        return this.originalImage;
    }
    public void setOriginalImage(Mat mat) {
        this.originalImage = mat;
    }

    public Mat getBinaryImage() {
        return this.binaryImages;
    }
    public void setBinaryImage(Mat matArray) {
        this.binaryImages = matArray;
    }

    public Point getFrameOrigin() {
        return this.frameOrigin;
    }

    public boolean isUseMeanColor() {
        return useMeanColor;
    }
    public void setUseMeanColor(boolean useMeanColor) {
        this.useMeanColor = useMeanColor;
        setChanged();
        notifyObservers("readColorsFromGrid");
        setChanged();
        notifyObservers("processImage");
    }

    public void updateImageView() {
        setChanged();
        notifyObservers("updateImageView");
    }

    public void processImage() {
        setChanged();
        notifyObservers("processImage");
    }
}
