package cubiq.models;

import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.opencv.core.Mat;

import java.util.List;
import java.util.Observable;

public class GuiModel extends Observable {

    /*
    Ist das hier, das GuiModel weil es vorher noch aus dem Debugmodus stammt? Eigentlich haben wir ja keine Gui mehr in dem Sinne. Ist mir nur aufgefallen.. (Tim)
    TODO Der Name passt nicht wirklich, das stimmt. Die meisten Variablen sind eher für den Cube scan. Das sollten wir vielleicht noch anders aufteilen.
     */

    private Stage stage;
    private Font kiona, kionaItalic;
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
    private boolean isFullscreen = false;
    private double screenWidth, screenHeight;
    private int headerHeight = 23;
    private int taskbarHeight = 40;
//    private int[] minWindowSize = new int[] {1484, 716};
    private int[] minWindowSize = new int[] {1368, 716};
    private double savedSceneX, savedSceneY, savedSceneWidth, savedSceneHeight;


    private String solveString = "R2,D,B,R',";//B2,D,R,U,F',L',U',F2,D,R2,D2,B2,L2,F2,U',L2,"; //TODO: Beim Export vom Kociemba ein Komma am Ende setzen

    public void callObservers(String arg) {
        setChanged();
        notifyObservers(arg);
    }

    // SolveString------------------------------------------------------------------------------------------------------
    public String getSolveString() {
        return solveString;
    }

    public void setSolveString(String solveString) {
        this.solveString = solveString;
    }



    // Kiona------------------------------------------------------------------------------------------------------------

    public Font getKiona() {
        return this.kiona;
    }

    public void setKiona(Font font) {
        this.kiona = font;
    }

    public Font getKionaItalic() {
        return this.kionaItalic;
    }

    public void setKionaItalic(Font font) {
        this.kionaItalic = font;
    }

    // Bender-----------------------------------------------------------------------------------------------------------

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

    public void setOriginalFrame(Mat frame) {
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
    }

    // IsFullscreen-----------------------------------------------------------------------------------------------------
    public void setIsFullscreen(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
    }

    public boolean getIsFullscreen() {
        return isFullscreen;
    }

    // ScreenSize-------------------------------------------------------------------------------------------------------
    public void setScreenWidth(double screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setScreenHeight(double screenHeight) {
        this.screenHeight = screenHeight;
    }

    public double getScreenWidth() {
        return screenWidth;
    }

    public double getScreenHeight() {
        return screenHeight;
    }

    // HeaderHeight-----------------------------------------------------------------------------------------------------

    public int getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    // TaskbarHeight----------------------------------------------------------------------------------------------------

    public int getTaskbarHeight() {
        return taskbarHeight;
    }

    public void setTaskbarHeight(int taskbarHeight) {
        this.taskbarHeight = taskbarHeight;
    }

    // MinWindowSize----------------------------------------------------------------------------------------------------

    public int[] getMinWindowSize() {
        return minWindowSize;
    }

    public void setMinWindowSize(int[] minWindowSize) {
        this.minWindowSize = minWindowSize;
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


    // SavedScenePosition-----------------------------------------------------------------------------------------------

    public double getSavedSceneX() {
        return savedSceneX;
    }

    public void setSavedSceneX(double savedSceneX) {
        this.savedSceneX = savedSceneX;
    }

    public double getSavedSceneY() {
        return savedSceneY;
    }

    public void setSavedSceneY(double savedSceneY) {
        this.savedSceneY = savedSceneY;
    }

    // SavedSceneSize---------------------------------------------------------------------------------------------------

    public double getSavedSceneWidth() {
        return savedSceneWidth;
    }

    public void setSavedSceneWidth(double savedSceneWidth) {
        this.savedSceneWidth = savedSceneWidth;
    }

    public double getSavedSceneHeight() {
        return savedSceneHeight;
    }

    public void setSavedSceneHeight(double savedSceneHeight) {
        this.savedSceneHeight = savedSceneHeight;
    }
}
