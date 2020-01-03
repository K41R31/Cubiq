package Gui.MainView;

import Models.GuiModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayInputStream;
import java.util.Observable;
import java.util.Observer;

public class MainViewController implements Observer {

    private GuiModel model;
    @FXML
    private StackPane sp_solvePane, sp_learnPane, sp_timerPane;
    @FXML
    private ImageView iv_imageView;


    private void updateImageView() {
        Mat convertedMat = model.getWebcamframe().clone();
        MatOfByte matOfByte = new MatOfByte();

        Imgproc.cvtColor(convertedMat, convertedMat, Imgproc.COLOR_HSV2BGR);

        //if (model.isMirrorWebcam()) Core.flip(convertedMat, convertedMat, 1);

        Imgcodecs.imencode(".jpg", convertedMat, matOfByte);
        Platform.runLater(() -> iv_imageView.setImage(new Image(new ByteArrayInputStream(matOfByte.toArray()))));
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "menuItemSolveActive":
                sp_solvePane.setVisible(true);
                sp_learnPane.setVisible(false);
                sp_timerPane.setVisible(false);
                break;
            case "menuItemLearnActive":
                sp_solvePane.setVisible(false);
                sp_learnPane.setVisible(true);
                sp_timerPane.setVisible(false);
                break;
            case "menuItemTimerActive":
                sp_solvePane.setVisible(false);
                sp_learnPane.setVisible(false);
                sp_timerPane.setVisible(true);
                break;
            case "updateImageView":
                updateImageView();
                break;
        }
    }

    public void initModel(GuiModel model) {
        this.model = model;
    }
}
