package AlphaTests.CubeScan.GUI;

import AlphaTests.CubeScan.Models.CubeScanModel;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;
import java.util.Observable;
import java.util.Observer;

public class CubeScanController implements Observer {

    private CubeScanModel cubeScanModel;

    @FXML
    private ImageView imageViewOriginal, imageViewBinary;
    @FXML
    private Slider sliderLoHu, sliderHiHu, sliderLoSa, sliderHiSa, sliderLoVa, sliderHiVa, sliderGaBl, sliderMeBl;
    @FXML
    private StackPane paneLoadImageBtn, paneBinaryBtn, paneOriginalBtn;
    @FXML
    private Text textTotalBlobs, textLoadImageBtn, textBinaryBtn, textOriginalBtn;


    private void addSliderListener() {
        sliderLoHu.valueProperty().addListener((ov, old_val, new_val) -> cubeScanModel.setLoHu((int)Math.round(sliderLoHu.getValue())));
        sliderHiHu.valueProperty().addListener((ov, old_val, new_val) -> cubeScanModel.setHiHu((int)Math.round(sliderHiHu.getValue())));
        sliderLoSa.valueProperty().addListener((ov, old_val, new_val) -> cubeScanModel.setLoSa((int)Math.round(sliderLoSa.getValue())));
        sliderHiSa.valueProperty().addListener((ov, old_val, new_val) -> cubeScanModel.setHiSa((int)Math.round(sliderHiSa.getValue())));
        sliderLoVa.valueProperty().addListener((ov, old_val, new_val) -> cubeScanModel.setLoVa((int)Math.round(sliderLoVa.getValue())));
        sliderHiVa.valueProperty().addListener((ov, old_val, new_val) -> cubeScanModel.setHiVa((int)Math.round(sliderHiVa.getValue())));

        sliderGaBl.valueProperty().addListener((ov, old_val, new_val) -> {
            if ((int)Math.round(sliderGaBl.getValue()) % 2 == 0) cubeScanModel.setGaBl((int)Math.round(sliderGaBl.getValue()) + 1);
            else cubeScanModel.setGaBl((int)Math.round(sliderGaBl.getValue()));
        });
        sliderMeBl.valueProperty().addListener((ov, old_val, new_val) -> {
            if ((int)Math.round(sliderMeBl.getValue()) % 2 == 0) cubeScanModel.setMeBl((int)Math.round(sliderMeBl.getValue()) + 1);
            else cubeScanModel.setMeBl((int)Math.round(sliderMeBl.getValue()));
        });
    }

    private void updateBlobImage() {
        if (cubeScanModel.getBlobImage() != null) {
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".jpg", cubeScanModel.getBlobImage(), matOfByte);
            imageViewOriginal.setImage(new Image(new ByteArrayInputStream(matOfByte.toArray())));
        }
    }

    private void updateBinaryImage() {
        if (cubeScanModel.getBinaryImage() != null) {
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".jpg", cubeScanModel.getBinaryImage(), matOfByte);
            imageViewBinary.setImage(new Image(new ByteArrayInputStream(matOfByte.toArray())));
        }
    }

    private void updateFoundBlobs() {
        textTotalBlobs.setText(String.valueOf(cubeScanModel.getFoundBlobs()));
    }

    @FXML
    private void originalButtonClicked() {
        imageViewOriginal.setVisible(true);
        imageViewBinary.setVisible(false);
    }

    @FXML
    private void binaryButtonClicked() {
        imageViewOriginal.setVisible(false);
        imageViewBinary.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "addSliderListener":
                addSliderListener();
                break;
            case "updateBinaryImage":
                updateBinaryImage();
                break;
            case"updateBlobImage":
                updateBlobImage();
            case "foundBlobsUpdated":
                updateFoundBlobs();
                break;
        }
    }

    public void initModel(CubeScanModel model) {
        this.cubeScanModel = model;
    }
}
