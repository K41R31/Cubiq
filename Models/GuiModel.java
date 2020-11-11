package Models;

import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.opencv.core.Mat;

import java.util.List;
import java.util.Observable;

public class GuiModel extends Observable {

    /*
    Ist das hier, das GuiModel weil es vorher noch aus dem Debugmodus stammt? Eigentlich haben wir ja keine Gui mehr in dem Sinne. Ist mir nur aufgefallen.. (Tim)
     */

    private Stage stage;
    private Font kiona;
    private Font bender;
    private Mat originalFrame;
    private Mat[] loadedImages;
    private boolean debug = false;
    private boolean mirrorWebcam = false;
    private int cannyThreshold1 = 17, cannyThreshold2 = 23; //17, 23
    private double sideLengthThreshold = 0.8; //0.8
    private double angleThreshold = 50; //50
    private double rotationThreshold = 20; //30
    private double blurThreshold = 3; //3
    private double dilateKernel = 1; //1
    private double scanAreaSize = 20;
    private List<int[][]> colorScheme;
    private int totalCubeSideFound = 0;


    public void callObservers(String arg) {
        setChanged();
        notifyObservers(arg);
    }

    // kiona------------------------------------------------------------------------------------------------------------

    public Font getKiona() {
        return this.kiona;
    }

    public void setKiona(Font font) {
        this.kiona = font;
    }

    // bender-----------------------------------------------------------------------------------------------------------

    public Font getBender() {
        return this.bender;
    }

    public void setBender(Font font) {
        this.bender = font;
    }

    // Webcam frame-----------------------------------------------------------------------------------------------------

    public Mat getOriginalFrame() {
        return this.originalFrame;
    }

    public void setWebcamFrame(Mat frame) {
        this.originalFrame = frame;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isMirrorWebcam() {
        return mirrorWebcam;
    }

    public void setMirrorWebcam(boolean mirrorWebcam) {
        this.mirrorWebcam = mirrorWebcam;
    }

    public int getCannyThreshold1() {
        return cannyThreshold1;
    }

    public void setCannyThreshold1(int cannyThreshold1) {
        this.cannyThreshold1 = cannyThreshold1;
    }

    public int getCannyThreshold2() {
        return cannyThreshold2;
    }

    public void setCannyThreshold2(int cannyThreshold2) {
        this.cannyThreshold2 = cannyThreshold2;
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

    public double getScanAreaSize() {
        return scanAreaSize;
    }

    public void setScanAreaSize(double scanAreaSize) {
        this.scanAreaSize = scanAreaSize;
    }

    public List<int[][]> getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(List<int[][]> colorScheme) {
        this.colorScheme = colorScheme;
    }

    public int getTotalCubeSideFound() {
        return totalCubeSideFound;
    }

    public void setTotalCubeSideFound(int totalCubeSideFound) {
        this.totalCubeSideFound = totalCubeSideFound;
        setChanged();
        notifyObservers("newCubeSideFound");
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Mat[] getLoadedImages() {
        return loadedImages;
    }

    public void setLoadedImages(Mat[] loadedImages) {
        this.loadedImages = loadedImages;
    }
}
