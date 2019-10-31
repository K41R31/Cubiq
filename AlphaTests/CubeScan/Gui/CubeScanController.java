package AlphaTests.CubeScan.GUI;

import AlphaTests.CubeScan.Models.CubeScanModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
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
    private Slider sl_hueRangeThreshold, sl_saturationRangeThreshold, sl_valueRangeThreshold, sl_gaussKernel, sl_medianKernel;
    @FXML
    private ProgressBar pb_hueRangeThreshold, pb_saturationRangeThreshold, pb_valueRangeThreshold, pb_gaussKernel, pb_medianKernel;
    @FXML
    private Text tx_hueThreshold, tx_satThreshold, tx_valThreshold, tx_gaussKernel, tx_medianKernel;
    @FXML
    private Button btn_toggleImageViews;


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
        sl_hueRangeThreshold.valueProperty().addListener((ov, old_val, new_val) -> {
            pb_hueRangeThreshold.setProgress(new_val.doubleValue() / 50);
            tx_hueThreshold.setText(String.valueOf((int)Math.round(new_val.doubleValue())));
        });
        sl_saturationRangeThreshold.valueProperty().addListener((ov, old_val, new_val) -> {
            pb_saturationRangeThreshold.setProgress(new_val.doubleValue() / 100);
            tx_satThreshold.setText(String.valueOf((int)Math.round(new_val.doubleValue())));
        });
        sl_valueRangeThreshold.valueProperty().addListener((ov, old_val, new_val) -> {
            pb_valueRangeThreshold.setProgress(new_val.doubleValue() / 100);
            tx_valThreshold.setText(String.valueOf((int)Math.round(new_val.doubleValue())));
        });

        sl_gaussKernel.valueProperty().addListener((ov, old_val, new_val) -> {
            pb_gaussKernel.setProgress(new_val.doubleValue() / 20);
            tx_gaussKernel.setText(String.valueOf((int)Math.round(new_val.doubleValue())));
        });
        sl_medianKernel.valueProperty().addListener((ov, old_val, new_val) -> {
            pb_medianKernel.setProgress(new_val.doubleValue() / 20);
            tx_medianKernel.setText(String.valueOf((int)Math.round(new_val.doubleValue())));
        });
    }


    private void updateBinaryImages() {
        if (model.getBinaryImages() == null) {
            return;
        }
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                MatOfByte matOfByte = new MatOfByte();
                Imgcodecs.imencode(".jpg", model.getBinaryImages()[x][y], matOfByte);
                binaryImageViews[x][y].setImage(new Image(new ByteArrayInputStream(matOfByte.toArray())));
            }
        }
    }

    private void updateBlobImages() {
        if (model.getBlobImages() == null) return;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                MatOfByte matOfByte = new MatOfByte();
                Imgcodecs.imencode(".jpg", model.getBlobImages()[x][y], matOfByte);
                blobImageViews[x][y].setImage(new Image(new ByteArrayInputStream(matOfByte.toArray())));
            }
        }
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
            case "updateBinaryImages":
                updateBinaryImages();
                break;
            case"updateBlobImages":
                updateBlobImages();
                break;
        }
    }

    public void initModel(CubeScanModel model) {
        this.model = model;
    }
}
