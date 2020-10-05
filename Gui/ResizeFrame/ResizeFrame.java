package Gui.ResizeFrame;

import Models.ScreenInformationModel;
import Start.Start;
import javafx.beans.value.ChangeListener;
import javafx.scene.Cursor;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ResizeFrame implements Observer {

    private ScreenInformationModel screenInformationModel;
    private AnchorPane dragFramePane;
    private Line[] lines;
    private Point[] alignPoints;
    private double windowCursorPosX, windowCursorPosY;
    private double scenePosX, scenePosY;
    private double sceneWidth, sceneHeight;
    private double offsetX, offsetY;


    public ResizeFrame() {
        alignPoints = new Point[13];
        lines = new Line[12];
        dragFramePane = new AnchorPane();
        dragFramePane.setPickOnBounds(false);
        initResizeLines();
        addResizePointsUpdater();
    }

    private void addResizePointsUpdater() {
        ChangeListener sizeChangeListener = (ChangeListener<Double>) (observable, oldValue, newValue) -> alignResizeLines();
        Start.primaryStage.widthProperty().addListener(sizeChangeListener);
        Start.primaryStage.heightProperty().addListener(sizeChangeListener);
    }

    private void initResizeLines() {
        for (int i = 0; i < 13; i++) {
            alignPoints[i] = new Point();
        }
        for (int i = 0; i < 12; i++) {
            lines[i] = new Line();
            lines[i].setStrokeWidth(4);
            lines[i].setStroke(Color.TRANSPARENT);
            dragFramePane.getChildren().add(lines[i]);
            switch (i) {
                case 0:
                    lines[i].setCursor(Cursor.N_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosY = event.getScreenY();
                        scenePosY = Start.primaryStage.getY();
                        sceneHeight = Start.primaryStage.getHeight();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetY = event.getScreenY() - windowCursorPosY;
                        if (sceneHeight - offsetY > screenInformationModel.getMinWindowSize()[1]) {
                            Start.primaryStage.setY(scenePosY + offsetY);
                            Start.primaryStage.setHeight(sceneHeight - offsetY);
                        }
                    });
                    continue;
                case 1:
                case 2:
                    lines[i].setCursor(Cursor.NE_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosX = event.getScreenX();
                        windowCursorPosY = event.getScreenY();
                        scenePosY = Start.primaryStage.getY();
                        sceneWidth = Start.primaryStage.getWidth();
                        sceneHeight = Start.primaryStage.getHeight();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetX = event.getScreenX() - windowCursorPosX;
                        offsetY = event.getScreenY() - windowCursorPosY;
                        if (sceneWidth + offsetX > screenInformationModel.getMinWindowSize()[0])
                            Start.primaryStage.setWidth(sceneWidth + offsetX);
                        if (sceneHeight - offsetY > screenInformationModel.getMinWindowSize()[1]) {
                            Start.primaryStage.setHeight(sceneHeight - offsetY);
                            Start.primaryStage.setY(scenePosY + offsetY);
                        }
                    });
                    continue;
                case 3:
                    lines[i].setCursor(Cursor.E_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosX = event.getScreenX();
                        sceneWidth = Start.primaryStage.getWidth();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetX = event.getScreenX() - windowCursorPosX;
                        if (sceneWidth + offsetX > screenInformationModel.getMinWindowSize()[0])
                            Start.primaryStage.setWidth(sceneWidth + offsetX);
                    });
                    continue;
                case 4:
                case 5:
                    lines[i].setCursor(Cursor.SE_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosX = event.getScreenX();
                        windowCursorPosY = event.getScreenY();
                        sceneWidth = Start.primaryStage.getWidth();
                        sceneHeight = Start.primaryStage.getHeight();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetX = event.getScreenX() - windowCursorPosX;
                        offsetY = event.getScreenY() - windowCursorPosY;
                        if (sceneWidth + offsetX > screenInformationModel.getMinWindowSize()[0])
                            Start.primaryStage.setWidth(sceneWidth + offsetX);
                        if (sceneHeight + offsetY > screenInformationModel.getMinWindowSize()[1])
                            Start.primaryStage.setHeight(sceneHeight + offsetY);
                    });
                    continue;
                case 6:
                    lines[i].setCursor(Cursor.S_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosY = event.getScreenY();
                        sceneHeight = Start.primaryStage.getHeight();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetY = event.getScreenY() - windowCursorPosY;
                        if (sceneHeight + offsetY > screenInformationModel.getMinWindowSize()[1])
                            Start.primaryStage.setHeight(sceneHeight + offsetY);
                    });
                    continue;
                case 7:
                case 8:
                    lines[i].setCursor(Cursor.SW_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosX = event.getScreenX();
                        windowCursorPosY = event.getScreenY();
                        scenePosX = Start.primaryStage.getX();
                        sceneWidth = Start.primaryStage.getWidth();
                        sceneHeight = Start.primaryStage.getHeight();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetX = event.getScreenX() - windowCursorPosX;
                        offsetY = event.getScreenY() - windowCursorPosY;
                        if (sceneWidth - offsetX > screenInformationModel.getMinWindowSize()[0]) {
                            Start.primaryStage.setX(scenePosX + offsetX);
                            Start.primaryStage.setWidth(sceneWidth - offsetX);
                        }
                        if (sceneHeight + offsetY > screenInformationModel.getMinWindowSize()[1])
                            Start.primaryStage.setHeight(sceneHeight + offsetY);
                    });
                    continue;
                case 9:
                    lines[i].setCursor(Cursor.W_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosX = event.getScreenX();
                        scenePosX = Start.primaryStage.getX();
                        sceneWidth = Start.primaryStage.getWidth();
                    });
                    lines[i].setOnMouseDragged(event -> {
                    offsetX = event.getScreenX() - windowCursorPosX;
                    if (sceneWidth - offsetX > screenInformationModel.getMinWindowSize()[0]) {
                        Start.primaryStage.setX(scenePosX + offsetX);
                        Start.primaryStage.setWidth(sceneWidth - offsetX);
                    }
                });
                    continue;
                case 10:
                case 11:
                    lines[i].setCursor(Cursor.NW_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosX = event.getScreenX();
                        windowCursorPosY = event.getScreenY();
                        scenePosX = Start.primaryStage.getX();
                        scenePosY = Start.primaryStage.getY();
                        sceneWidth = Start.primaryStage.getWidth();
                        sceneHeight = Start.primaryStage.getHeight();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetX = event.getScreenX() - windowCursorPosX;
                        offsetY = event.getScreenY() - windowCursorPosY;
                        if (sceneWidth - offsetX > screenInformationModel.getMinWindowSize()[0]) {
                            Start.primaryStage.setX(scenePosX + offsetX);
                            Start.primaryStage.setWidth(sceneWidth - offsetX);
                        }
                        if (sceneHeight - offsetY > screenInformationModel.getMinWindowSize()[1]) {
                            Start.primaryStage.setY(scenePosY + offsetY);
                            Start.primaryStage.setHeight(sceneHeight - offsetY);
                        }
                    });
                    lines[i].setOnMouseReleased(event -> {
                        if (event.getScreenY() == 0 || event.getScreenY() == screenInformationModel.getScreenHeight() - 1) {
                            Start.primaryStage.setHeight(screenInformationModel.getScreenHeight() - 40);
                            Start.primaryStage.setY(0);
                        }
                    });
            }
        }
    }

    private void alignResizeLines() {
        alignPoints[0] = new Point(10, 2);
        alignPoints[1] = new Point((int) Start.primaryStage.getWidth() -10, 2);
        alignPoints[2] = new Point((int) Start.primaryStage.getWidth() - 2, 2);
        alignPoints[3] = new Point((int) Start.primaryStage.getWidth() - 2, 10);
        alignPoints[4] = new Point((int) Start.primaryStage.getWidth() - 2, (int) Start.primaryStage.getHeight() - 10);
        alignPoints[5] = new Point((int) Start.primaryStage.getWidth() - 2, (int) Start.primaryStage.getHeight() - 2);
        alignPoints[6] = new Point((int) Start.primaryStage.getWidth() - 10, (int) Start.primaryStage.getHeight() - 2);
        alignPoints[7] = new Point(10, (int) Start.primaryStage.getHeight() - 2);
        alignPoints[8] = new Point(2, (int) Start.primaryStage.getHeight() - 2);
        alignPoints[9] = new Point(2, (int) Start.primaryStage.getHeight() - 10);
        alignPoints[10] = new Point(2, 10);
        alignPoints[11] = new Point(2, 2);
        alignPoints[12] = new Point(10, 2);

        for (int i = 0; i < 12; i++) {
            lines[i].setStartX(alignPoints[i].getX());
            lines[i].setEndX(alignPoints[i + 1].getX());
            lines[i].setStartY(alignPoints[i].getY());
            lines[i].setEndY(alignPoints[i + 1].getY());
        }
    }

    private void maximizeHeight() {

    }

    public AnchorPane getDragFramePane() {
        return dragFramePane;
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String) arg) {
            case "initResizeLines":
                initResizeLines();
            case "alignLines":
                alignResizeLines();
                break;
            case "toggleDraggedFullScreen":
            case "toggleFullScreen":
                dragFramePane.setDisable(screenInformationModel.getIsFullscreen());
                break;
        }
    }

    public void initModel(ScreenInformationModel model) {
        this.screenInformationModel = model;
    }
}
