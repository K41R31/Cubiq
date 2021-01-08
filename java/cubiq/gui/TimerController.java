package cubiq.gui;

import cubiq.models.GuiModel;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;


public class TimerController implements Observer {

    @FXML
    private Text mm, ss, ms, sepA, sepB, bestTimeText, bestTime;

    @FXML
    private ProgressBar progBarGauss, progBarBlur, progBar;

    private GuiModel guiModel;

    private long startTime;
    private long stopTime;
    private long elapsedTime;
    private boolean isRunning = false;

    private void initializeTimerController() {

        mm.setFont(guiModel.getBender());
        mm.setStyle("-fx-font-size: 100");
        mm.setFontSmoothingType(FontSmoothingType.LCD);
        ss.setFont(guiModel.getBender());
        ss.setStyle("-fx-font-size: 100");
        ss.setFontSmoothingType(FontSmoothingType.LCD);
        ms.setFont(guiModel.getBender());
        ms.setStyle("-fx-font-size: 100");
        ms.setFontSmoothingType(FontSmoothingType.LCD);
        sepA.setFont(guiModel.getBender());
        sepA.setStyle("-fx-font-size: 100");
        sepA.setFontSmoothingType(FontSmoothingType.LCD);
        sepB.setFont(guiModel.getBender());
        sepB.setStyle("-fx-font-size: 100");
        sepB.setFontSmoothingType(FontSmoothingType.LCD);

        bestTimeText.setFont(guiModel.getBender());
        bestTimeText.setStyle("-fx-font-size: 15");
        bestTimeText.setFontSmoothingType(FontSmoothingType.LCD);
        bestTime.setFont(guiModel.getBender());
        bestTime.setStyle("-fx-font-size: 15");
        bestTime.setFontSmoothingType(FontSmoothingType.LCD);

        double bT = 60.0;
        progBar.setProgress(elapsedTime-(bT / 100));
        progBarBlur = progBar;
        progBarGauss = progBar;
    }

    private void start() {
        this.startTime = System.nanoTime();
        this.isRunning = true;
    }

    private void stop() {
        this.stopTime = System.nanoTime();
        this.isRunning = false;
    }

    private long getElapsedTimeMs() {
        if (isRunning) {
            elapsedTime = ((System.nanoTime() - startTime) / 1000);
        } else {
            elapsedTime = ((stopTime - startTime) / 1000);
        }
        return elapsedTime;
    }



    @Override
    public void update(Observable o, Object arg) {
        switch ((String) arg) {
            case "initGuiElements":
                initializeTimerController();
                break;
        }
    }

    public void initModel(GuiModel guiModel) {
        this.guiModel = guiModel;
    }
}
