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
    private int gaBl = 1, meBl = 1;
    private int foundBlobs;
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
        notifyObservers("processImage");
    }

    public Mat[][] getBlobImages() {
        return this.blobImages;
    }
    public void setBlobImages(Mat[][] matArray) {
        this.blobImages = matArray;
        setChanged();
        notifyObservers("updateBlobImage");
    }

    public Mat[][] getBinaryImages() {
        return this.binaryImages;
    }
    public void setBinaryImages(Mat[][] matArray) {
        this.binaryImages = matArray;
        setChanged();
        notifyObservers("updateBinaryImages");
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
}
