package Models;

import java.util.Observable;

public class ScreenInformationModel extends Observable {

    //TODO Später unbenutzte Methoden rauslöschen

    private boolean isFullscreen;
    private double screenWidth, screenHeight, savedSceneX, savedSceneY, savedSceneWidth, savedSceneHeight;
    private int headerHeight = 23;
    private int taskbarHeight = 40;

    //toggleFullScreen--------------------------------------------------------------------------------------------------
    public void toggleFullScreen() {
        setChanged();
        notifyObservers("toggleFullScreen");
    }

    public void toggleDraggedFullScreen() {
        setChanged();
        notifyObservers("toggleDraggedFullScreen");
    }

    public void alignResizeLines() {
        setChanged();
        notifyObservers("alignLines");
    }

    //isFullscreen------------------------------------------------------------------------------------------------------
    public void setIsFullscreen(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
    }

    public boolean getIsFullscreen() {
        return isFullscreen;
    }

    //screenSize--------------------------------------------------------------------------------------------------------
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

    //savedSceneSize----------------------------------------------------------------------------------------------------

    public void setSavedSceneWidth(double width) {
        this.savedSceneWidth = width;
    }

    public void setSavedSceneHeight(double height) {
        this.savedSceneHeight = height;
    }

    public double getSavedSceneWidth() {
        return savedSceneWidth;
    }

    public double getSavedSceneHeight() {
        return savedSceneHeight;
    }

    //savedScenePosition------------------------------------------------------------------------------------------------

    public void setSavedSceneX(double savedSceneX) {
        this.savedSceneX = savedSceneX;
    }

    public void setSavedSceneY(double savedSceneY) {
        this.savedSceneY = savedSceneY;
    }

    public double getSavedSceneX() {
        return savedSceneX;
    }

    public double getSavedSceneY() {
        return savedSceneY;
    }

    //headerHeight------------------------------------------------------------------------------------------------------

    public int getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    //taskbarHeight-----------------------------------------------------------------------------------------------------

    public int getTaskbarHeight() {
        return taskbarHeight;
    }

    public void setTaskbarHeight(int taskbarHeight) {
        this.taskbarHeight = taskbarHeight;
    }
}
