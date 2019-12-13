package AlphaTests.CubeScanFrameless.GUI;

import AlphaTests.CubeScanFrameless.Model.CubeScanFramelessModel;
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
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class CubeScanFramelessController implements Observer {

    private CubeScanFramelessModel model;
    @FXML
    private ImageView iv_imageView;
    @FXML
    private StackPane sp_buttonPane;

    private void updateImageView() {
        Mat convertedMat = new Mat();
        MatOfByte matOfByte = new MatOfByte();
        Imgproc.cvtColor(model.getProcessedMat(), convertedMat, Imgproc.COLOR_HSV2BGR);
        Imgcodecs.imencode(".jpg", convertedMat, matOfByte);
        Platform.runLater(() -> iv_imageView.setImage(new Image(new ByteArrayInputStream(matOfByte.toArray()))));
    }

    @FXML
    private void webcamClicked() {
        model.startWebcamStream();
        sp_buttonPane.setVisible(false);
    }

    @FXML
    private void fileChooserClicked() {
        model.loadImage();
        sp_buttonPane.setVisible(false);
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "updateImageView":
                updateImageView();
                break;
        }
    }

    public void initModel(CubeScanFramelessModel model) {
        this.model = model;
    }
}
