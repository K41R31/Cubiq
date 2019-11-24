package AlphaTests.CubeScanFrameless.GUI;

import AlphaTests.CubeScanFrameless.Model.CubeScanFramelessModel;
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
    @FXML
    private Text txt_canny1, txt_canny2;
    @FXML
    private Slider sl_canny1, sl_canny2;


    private void initSlider() {
        sl_canny1.valueProperty().addListener((ov, old_val, new_val) -> {
            double roundedVal = (int)Math.round(new_val.doubleValue());
            if (roundedVal % 2 == 0 && roundedVal != 0) roundedVal++;
            txt_canny1.setText(String.valueOf((int)roundedVal));
            model.setCannyThreshold1((int)roundedVal);
        });
        sl_canny2.valueProperty().addListener((ov, old_val, new_val) -> {
            double roundedVal = (int)Math.round(new_val.doubleValue());
            if (roundedVal % 2 == 0 && roundedVal != 0) roundedVal++;
            txt_canny2.setText(String.valueOf((int)roundedVal));
            model.setCannyThreshold2((int)roundedVal);
        });
    }

    private void updateImageView() {
        MatOfByte matOfByte = new MatOfByte();

        if (showUnprocessedImage) Imgcodecs.imencode(".jpg", model.getUnprocessedMat(), matOfByte);
        else Imgcodecs.imencode(".jpg", model.getProcessedMat(), matOfByte);

        imageView.setImage(new Image(new ByteArrayInputStream(matOfByte.toArray())));
    }

    @FXML
    private void toggleView() {
        showUnprocessedImage = !showUnprocessedImage;
        updateImageView();
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "loadNewImage":
                initSlider();
                break;
            case "updateImageView":
                updateImageView();
                break;
        }
    }

    public void initModel(CubeScanFramelessModel model) {
        this.model = model;
    }
}
