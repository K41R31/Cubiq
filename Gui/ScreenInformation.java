package GUI;

import Models.ScreenInformationModel;
import Start.Start;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ScreenInformation implements Observer {

    private ScreenInformationModel screenInformationModel;

    public static void setStageSize(double width, double height) {
        Start.primaryStage.setWidth(width);
        Start.primaryStage.setHeight(height);
    }

    private void toggleFullScreen() {
        if (screenInformationModel.getIsFullscreen()) {
            Start.primaryStage.setX(screenInformationModel.getSavedSceneX());
            Start.primaryStage.setY(screenInformationModel.getSavedSceneY());
            Start.primaryStage.setWidth(screenInformationModel.getSavedSceneWidth());
            Start.primaryStage.setHeight(screenInformationModel.getSavedSceneHeight());
            screenInformationModel.setIsFullscreen(false);
        } else {
            screenInformationModel.setSavedSceneX(Start.primaryStage.getX());
            screenInformationModel.setSavedSceneY(Start.primaryStage.getY());
            screenInformationModel.setSavedSceneWidth(Start.primaryStage.getWidth());
            screenInformationModel.setSavedSceneHeight(Start.primaryStage.getHeight());
            Start.primaryStage.setX(0);
            Start.primaryStage.setY(0);
            Start.primaryStage.setWidth(screenInformationModel.getScreenWidth());
            Start.primaryStage.setHeight(screenInformationModel.getScreenHeight()
                    - screenInformationModel.getTaskbarHeight());
            screenInformationModel.setIsFullscreen(true);
        }
    }

    private void toggleDraggedFullScreen() {
        if (screenInformationModel.getIsFullscreen()) {
            double mousePosX = MouseInfo.getPointerInfo().getLocation().x;
            double screenWidthThird = screenInformationModel.getScreenWidth() / 3;
            if (mousePosX <= screenWidthThird) {
                Start.primaryStage.setX(1);
            } else if (mousePosX > screenWidthThird & mousePosX < screenWidthThird * 2) {
                Start.primaryStage.setX(screenInformationModel.getScreenWidth() / 2
                        - screenInformationModel.getSavedSceneWidth() / 2);
            } else {
                Start.primaryStage.setX(screenInformationModel.getScreenWidth()
                        - screenInformationModel.getSavedSceneWidth());
            }
            Start.primaryStage.setWidth(screenInformationModel.getSavedSceneWidth());
            Start.primaryStage.setHeight(screenInformationModel.getSavedSceneHeight());
            screenInformationModel.setIsFullscreen(false);
        } else {
            screenInformationModel.setSavedSceneWidth(Start.primaryStage.getWidth());
            screenInformationModel.setSavedSceneHeight(Start.primaryStage.getHeight());
            Start.primaryStage.setX(0);
            Start.primaryStage.setY(0);
            Start.primaryStage.setWidth(screenInformationModel.getScreenWidth());
            Start.primaryStage.setHeight(screenInformationModel.getScreenHeight()
                    - screenInformationModel.getTaskbarHeight());
            screenInformationModel.setIsFullscreen(true);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String) arg) {
            case "toggleFullScreen":
                toggleFullScreen();
                break;
            case "toggleDraggedFullScreen":
                toggleDraggedFullScreen();
                break;
        }
    }

    public void initModel(ScreenInformationModel model) {
        this.screenInformationModel = model;
    }
}
