package AlphaTests.CubeScan.GUI;

import AlphaTests.CubeScan.Models.CubeScanModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class CubeScanController implements Observer, Initializable {

    private CubeScanModel model;

    @FXML
    private ImageView imageView;
    @FXML
    private Slider sl_gaussKernel, sl_medianKernel;
    @FXML
    private ProgressBar pb_gaussKernel, pb_medianKernel;
    @FXML
    private Text tx_lowHue, tx_highHue, tx_lowSat, tx_highSat, tx_lowVal, tx_highVal, tx_gaussKernel, tx_medianKernel;
    @FXML
    private Button btn_toggleImageViews;
    @FXML
    private AnchorPane sliderPaneHue, sliderPaneSat, sliderPaneVal;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private void addSlider() {
        sl_gaussKernel.valueProperty().addListener((ov, old_val, new_val) -> {
            double roundedVal = (int)Math.round(new_val.doubleValue());
            if (roundedVal % 2 == 0 && roundedVal != 0) roundedVal++;
            pb_gaussKernel.setProgress(roundedVal / 20);
            tx_gaussKernel.setText(String.valueOf((int)roundedVal));
            model.setGaBl((int)roundedVal);
            model.processImage();
        });
        sl_medianKernel.valueProperty().addListener((ov, old_val, new_val) -> {
            double roundedVal = (int)Math.round(new_val.doubleValue());
            if (roundedVal % 2 == 0 && roundedVal != 0) roundedVal++;
            pb_medianKernel.setProgress(roundedVal / 20);
            tx_medianKernel.setText(String.valueOf((int)roundedVal));
            model.setMeBl((int)roundedVal);
            model.processImage();
        });
    }

    private void updateImageView() {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", model.getBinaryImage(), matOfByte);
        imageView.setImage(new Image(new ByteArrayInputStream(matOfByte.toArray())));
        //iv.setViewport(new Rectangle2D(390, 165, iv.getFitWidth() * 1.58, iv.getFitHeight() * 1.58));
    }

    @FXML
    private void toggleImageViews() {
        System.out.println("Toggle Image Views");
    }

    @FXML
    private void loadImage() {
        model.loadNewImage();
    }


    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "addSlider":
                addSlider();
                break;
            case "updateImageView":
                updateImageView();
                break;
        }
    }

    public void initModel(CubeScanModel model) {
        this.model = model;
    }
}
