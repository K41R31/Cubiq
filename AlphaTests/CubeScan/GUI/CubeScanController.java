package AlphaTests.CubeScan.GUI;

import AlphaTests.CubeScan.Models.CubeScanModel;
import com.sun.javafx.scene.paint.MaterialHelper;
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
    private ImageView iv_binary0, iv_binary1, iv_binary2, iv_binary3, iv_binary4, iv_binary5, iv_binary6, iv_binary7, iv_binary8;
    private ImageView[][] binaryImageViews;
    @FXML
    private ImageView iv_blob0, iv_blob1, iv_bloby2, iv_blob3, iv_blob4, iv_blob5, iv_blob6, iv_blob7, iv_blob8;
    private ImageView[][] blobImageViews;
    @FXML
    private GridPane gp_binaryGrid, gp_blobGrid;
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
        //Init Arrays
        binaryImageViews = new ImageView[][] {
                {iv_binary0, iv_binary1, iv_binary2},
                {iv_binary3, iv_binary4, iv_binary5},
                {iv_binary6, iv_binary7, iv_binary8}
        };
        blobImageViews = new ImageView[][] {
                {iv_blob0, iv_blob1, iv_bloby2},
                {iv_blob3, iv_blob4, iv_blob5},
                {iv_blob6, iv_blob7, iv_blob8}
        };
    }

    private void addSliderListener() {
        RangeSlider rangeSliderHue = new RangeSlider(0, 179, 0, 179, 170);
        sliderPaneHue.getChildren().add(rangeSliderHue);
        tx_lowHue.setText(String.valueOf(Math.round(rangeSliderHue.getLowValue())));
        tx_highHue.setText(String.valueOf(Math.round(rangeSliderHue.getHighValue())));
        rangeSliderHue.lowValueProperty().addListener((ov, old_val, new_val) -> {
            tx_lowHue.setText(String.valueOf(Math.round(new_val.doubleValue())));
            model.setLoHu((int)Math.round(new_val.doubleValue()));
            model.processImages();
        });
        rangeSliderHue.highValueProperty().addListener((ov, old_val, new_val) -> {
            tx_highHue.setText(String.valueOf(Math.round(new_val.doubleValue())));
            model.setHiHu((int)Math.round(new_val.doubleValue()));
            model.processImages();
        });

        RangeSlider rangeSliderSat = new RangeSlider(0, 255, 0, 255, 170);
        sliderPaneSat.getChildren().add(rangeSliderSat);
        tx_lowSat.setText(String.valueOf(Math.round(rangeSliderSat.getLowValue())));
        tx_highSat.setText(String.valueOf(Math.round(rangeSliderSat.getHighValue())));
        rangeSliderSat.lowValueProperty().addListener((ov, old_val, new_val) -> {
            tx_lowSat.setText(String.valueOf(Math.round(new_val.doubleValue())));
            model.setLoSa((int)Math.round(new_val.doubleValue()));
            model.processImages();
        });
        rangeSliderSat.highValueProperty().addListener((ov, old_val, new_val) -> {
            tx_highSat.setText(String.valueOf(Math.round(new_val.doubleValue())));
            model.setHiSa((int)Math.round(new_val.doubleValue()));
            model.processImages();
        });

        RangeSlider rangeSliderVal = new RangeSlider(0, 255, 0, 255, 170);
        sliderPaneVal.getChildren().add(rangeSliderVal);
        tx_lowVal.setText(String.valueOf(Math.round(rangeSliderVal.getLowValue())));
        tx_highVal.setText(String.valueOf(Math.round(rangeSliderVal.getHighValue())));
        rangeSliderVal.lowValueProperty().addListener((ov, old_val, new_val) -> {
            tx_lowVal.setText(String.valueOf(Math.round(new_val.doubleValue())));
            model.setLoVa((int)Math.round(new_val.doubleValue()));
            model.processImages();
        });
        rangeSliderVal.highValueProperty().addListener((ov, old_val, new_val) -> {
            tx_highVal.setText(String.valueOf(Math.round(new_val.doubleValue())));
            model.setHiVa((int)Math.round(new_val.doubleValue()));
            model.processImages();
        });

        sl_gaussKernel.valueProperty().addListener((ov, old_val, new_val) -> {
            double roundedVal = (int)Math.round(new_val.doubleValue());
            if (roundedVal % 2 == 0 && roundedVal != 0) roundedVal++;
            pb_gaussKernel.setProgress(roundedVal / 20);
            tx_gaussKernel.setText(String.valueOf((int)roundedVal));
            model.setGaBl((int)roundedVal);
            model.processImages();
        });
        sl_medianKernel.valueProperty().addListener((ov, old_val, new_val) -> {
            double roundedVal = (int)Math.round(new_val.doubleValue());
            if (roundedVal % 2 == 0 && roundedVal != 0) roundedVal++;
            pb_medianKernel.setProgress(roundedVal / 20);
            tx_medianKernel.setText(String.valueOf((int)roundedVal));
            model.setMeBl((int)roundedVal);
            model.processImages();
        });
    }

    private void updateImageViews() {
        MatOfByte matOfByte = new MatOfByte();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                setImage(model.getBinaryImages()[x][y], binaryImageViews[x][y], matOfByte);
                setImage(model.getBlobImages()[x][y], blobImageViews[x][y], matOfByte);
            }
        }
    }

    private void setImage(Mat image, ImageView iv, MatOfByte matOfByte) {
        Imgcodecs.imencode(".jpg", image, matOfByte);
        iv.setImage(new Image(new ByteArrayInputStream(matOfByte.toArray())));
        iv.setViewport(new Rectangle2D(390, 165, iv.getFitWidth() * 1.58, iv.getFitHeight() * 1.58));
    }

    @FXML
    private void toggleImageViews() {
        if (gp_binaryGrid.isVisible()) {
            gp_binaryGrid.setVisible(false);
            gp_blobGrid.setVisible(true);
            btn_toggleImageViews.setText("show binarys");
        } else {
            gp_binaryGrid.setVisible(true);
            gp_blobGrid.setVisible(false);
            btn_toggleImageViews.setText("show blobs");
        }
    }

    @FXML
    private void loadImage() {
        model.loadNewImage();
    }


    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "addSliderListener":
                addSliderListener();
                break;
            case "updateImageViews":
                updateImageViews();
                break;
            case "updateSlider":
                break;
        }
    }

    public void initModel(CubeScanModel model) {
        this.model = model;
    }
}
