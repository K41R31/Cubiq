package cubiq.gui;

import cubiq.models.GuiModel;
import cubiq.models.ScreenInformationModel;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ScreenInformation implements Observer {

    private ScreenInformationModel screenInformationModel;
    private GuiModel guiModel;

    public void setStageSize(double width, double height) {
        guiModel.getStage().setWidth(width);
        guiModel.getStage().setHeight(height);
    }

    private void toggleFullScreen() {
        if (screenInformationModel.getIsFullscreen()) {
            guiModel.getStage().setX(screenInformationModel.getSavedSceneX());
            guiModel.getStage().setY(screenInformationModel.getSavedSceneY());
            guiModel.getStage().setWidth(screenInformationModel.getSavedSceneWidth());
            guiModel.getStage().setHeight(screenInformationModel.getSavedSceneHeight());
            screenInformationModel.setIsFullscreen(false);
        } else {
            screenInformationModel.setSavedSceneX(guiModel.getStage().getX());
            screenInformationModel.setSavedSceneY(guiModel.getStage().getY());
            screenInformationModel.setSavedSceneWidth(guiModel.getStage().getWidth());
            screenInformationModel.setSavedSceneHeight(guiModel.getStage().getHeight());
            guiModel.getStage().setX(0);
            guiModel.getStage().setY(0);
            guiModel.getStage().setWidth(screenInformationModel.getScreenWidth());
            guiModel.getStage().setHeight(screenInformationModel.getScreenHeight()
                    - screenInformationModel.getTaskbarHeight());
            screenInformationModel.setIsFullscreen(true);
        }
    }

    private void toggleDraggedFullScreen() {
        if (screenInformationModel.getIsFullscreen()) {
            double mousePosX = MouseInfo.getPointerInfo().getLocation().x;
            double screenWidthThird = screenInformationModel.getScreenWidth() / 3;
            if (mousePosX <= screenWidthThird) {
                guiModel.getStage().setX(1);
            } else if (mousePosX > screenWidthThird & mousePosX < screenWidthThird * 2) {
                guiModel.getStage().setX(screenInformationModel.getScreenWidth() / 2
                        - screenInformationModel.getSavedSceneWidth() / 2);
            } else {
                guiModel.getStage().setX(screenInformationModel.getScreenWidth()
                        - screenInformationModel.getSavedSceneWidth());
            }
            guiModel.getStage().setWidth(screenInformationModel.getSavedSceneWidth());
            guiModel.getStage().setHeight(screenInformationModel.getSavedSceneHeight());
            screenInformationModel.setIsFullscreen(false);
        } else {
            screenInformationModel.setSavedSceneWidth(guiModel.getStage().getWidth());
            screenInformationModel.setSavedSceneHeight(guiModel.getStage().getHeight());
            guiModel.getStage().setX(0);
            guiModel.getStage().setY(0);
            guiModel.getStage().setWidth(screenInformationModel.getScreenWidth());
            guiModel.getStage().setHeight(screenInformationModel.getScreenHeight()
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
            case "initStageSize":
                setStageSize(screenInformationModel.getScreenWidth() * 0.8, screenInformationModel.getScreenHeight() * 0.8);
        }
    }

    public void initScreenInformationModel(ScreenInformationModel screenInformationModel) {
        this.screenInformationModel = screenInformationModel;
    }

    public void initGuiModel(GuiModel guiModel) {
        this.guiModel = guiModel;
    }
}
