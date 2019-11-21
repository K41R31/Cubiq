package AlphaTests.CubeScan.Models;

import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import java.util.List;
import java.util.Observable;

public class CubeScanModel extends Observable {

    private Point frameOrigin = new Point(466, 185);
    private int searchFrameSize = 350;
    private Point[][] searchPointGrid = new Point[][] {
            {new Point(524, 243), new Point(524, 359), new Point(524, 477)},
            {new Point(640, 243), new Point(640, 359), new Point(640, 477)},
            {new Point(757, 243), new Point(757, 359), new Point(757, 477)}
    };
    private Scalar[][] gridColors = new Scalar[3][3];
    private int loHu, hiHu, loSa, hiSa, loVa, hiVa;
    private int gaBl = 5, meBl = 5;
    private List<List<KeyPoint>> foundBlobsList;
    private Mat originalImage;
    private Mat[][] blobImages, binaryImages;
    private boolean useMeanColor = true;


    public void loadNewImage() {
        setChanged();
        notifyObservers("loadNewImage");
        setChanged();
        notifyObservers("readColorsFromGrid");
        setChanged();
        notifyObservers("addSlider");
        setChanged();
        notifyObservers("processImages");
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

    public Mat[][] getBlobImages() {
        return this.blobImages;
    }
    public void setBlobImages(Mat[][] matArray) {
        this.blobImages = matArray;
    }

    public Mat[][] getBinaryImages() {
        return this.binaryImages;
    }
    public void setBinaryImages(Mat[][] matArray) {
        this.binaryImages = matArray;
    }

    public int getSearchFrameSize() {
        return this.searchFrameSize;
    }

    public Point getFrameOrigin() {
        return this.frameOrigin;
    }

    public Point[][] getSearchPointGrid() {
        return this.searchPointGrid;
    }

    public Scalar[][] getGridColors() {
        return this.gridColors;
    }
    public void setGridColors(Scalar[][] colors) {
        this.gridColors = colors;
        setChanged();
        notifyObservers("updateSlider");
    }

    public List<List<KeyPoint>> getFoundBlobsList() {
        return foundBlobsList;
    }
    public void setFoundBlobsList(List<List<KeyPoint>> foundBlobsList) {
        this.foundBlobsList = foundBlobsList;
    }

    public boolean isUseMeanColor() {
        return useMeanColor;
    }
    public void setUseMeanColor(boolean useMeanColor) {
        this.useMeanColor = useMeanColor;
        setChanged();
        notifyObservers("readColorsFromGrid");
        setChanged();
        notifyObservers("processImages");
    }

    public void updateImageViews() {
        setChanged();
        notifyObservers("updateImageViews");
    }

    public void processImages() {
        setChanged();
        notifyObservers("processImages");
    }
}
