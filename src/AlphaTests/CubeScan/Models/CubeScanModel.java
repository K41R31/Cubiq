package AlphaTests.CubeScan.Models;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import java.util.Observable;

public class CubeScanModel extends Observable {

    private Point frameOrigin = new Point(432, 181);
    private int searchFrameSize = 350;
    private Point[][] searchPointGrid = new Point[][] {
            {new Point(507, 242), new Point(620, 242), new Point(731, 242)},
            {new Point(507, 359), new Point(620, 359), new Point(731, 359)},
            {new Point(507, 475), new Point(620, 475), new Point(731, 475)}
    };
    private Scalar[][] gridColors = new Scalar[3][3];
    private int huTh, saTh, vaTh;
    private int gaBl = 5, meBl = 5;
    private int[][] foundBlobs;
    private Mat originalImage;
    private Mat[][] blobImages, binaryImages;


    public void addSliderListener() {
        setChanged();
        notifyObservers("addSliderListener");
    }

    public void loadNewImage() {
        setChanged();
        notifyObservers("loadNewImage");
    }

    public void setHuTh(int value) {
        this.huTh = value;
    }
    public int getHuTh() {
        return this.huTh;
    }

    public void setSaTh(int value) {
        this.saTh = value;
    }
    public int getSaTh() {
        return this.saTh;
    }

    public void setVaTh(int value) {
        this.vaTh = value;
    }
    public int getVaTh() {
        return this.vaTh;
    }

    public void setGaBl(int value) {
        this.gaBl = value;
    }
    public int getGaBl() {
        return this.gaBl;
    }
    public void setMeBl(int value) {
        this.meBl = value;
    }
    public int getMeBl() {
        return this.meBl;
    }

    public void setFoundBlobs(int[][] array) {
        this.foundBlobs = array;
    }
    public int[][] getFoundBlobs() {
        return this.foundBlobs;
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
