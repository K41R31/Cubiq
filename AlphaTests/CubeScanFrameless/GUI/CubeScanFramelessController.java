package AlphaTests.CubeScanFrameless.GUI;

import AlphaTests.CubeScanFrameless.Model.CubeScanFramelessModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import java.io.ByteArrayInputStream;
import java.util.Observable;
import java.util.Observer;

public class CubeScanFramelessController implements Observer {

    private CubeScanFramelessModel model;
    private boolean showUnprocessedImage = false;
    @FXML
    private ImageView imageView;

    private void updateImageView() {
        MatOfByte matOfByte = new MatOfByte();

        if (showUnprocessedImage) Imgcodecs.imencode(".jpg", model.getUnprocessedMat(), matOfByte);
        else Imgcodecs.imencode(".jpg", model.getProcessedMat(), matOfByte);

        Platform.runLater(() -> imageView.setImage(new Image(new ByteArrayInputStream(matOfByte.toArray()))));
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
