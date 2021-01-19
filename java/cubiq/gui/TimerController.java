package cubiq.gui;

import cubiq.models.GuiModel;
import cubiq.processing.MathUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.opencv.core.Mat;

import javax.swing.*;
import java.sql.Time;
import java.util.Observable;
import java.util.Observer;


public class TimerController implements Observer {

    private GuiModel guiModel;

    private long before, after;
    private boolean isRunning = false;
    private Timeline stopTimeline;

    @FXML
    private Text mm, ss, ms, sepA, sepB, bestTimeText, bestTime;

    @FXML
    private ProgressBar progBarGauss, progBarBlur, progBar;

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
       // progBar.setProgress(elapsedTime-(bT / 100));
        progBarBlur = progBar;
        progBarGauss = progBar;
    }

    private void initStopwatchAnimation() {
        stopTimeline = new Timeline();
        stopTimeline.setCycleCount(Timeline.INDEFINITE);
        stopTimeline.getKeyFrames().addAll(
                new KeyFrame(new Duration(0), e -> {
                    before = System.nanoTime();
                }),
                new KeyFrame(new Duration(30), e -> {
                    double msFrame, ssFrame, mFrame;
                    after = System.nanoTime();
                    Math.floor(msFrame = (after - before) / 1e6d);
                    ssFrame = (msFrame / 1000);
                    mFrame = (ssFrame / 60);

                    int milli = (int) msFrame;
                    int sec = (int) ssFrame;
                    int min = (int) mFrame;

                    ms.setText(String.valueOf(milli));
                    ss.setText(String.valueOf(sec));
                    mm.setText(String.valueOf(min));
                    })
        );
        stopTimeline.play();
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String) arg) {
            case "initGuiElements":
                initializeTimerController();
                initStopwatchAnimation();
                break;
        }
    }

    public void initModel(GuiModel guiModel) {
        this.guiModel = guiModel;
    }
}
