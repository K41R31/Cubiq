package AlphaTests.CubeScan.FileChooser;

import AlphaTests.CubeScan.Models.CubeScanModel;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ImageLoader implements Observer {

    private CubeScanModel model;
    private Stage stage;
    private FileChooser fileChooser;

    public ImageLoader(Stage stage) {
        this.stage = stage;
        initFileChooser();
    }

    private void initFileChooser() {
        fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Select an image");

        fileChooser.setInitialDirectory(new File("C:/Users/"));

        fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new javafx.stage.FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new javafx.stage.FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    public void loadImage() {
        File file = null;
        try {
            file = fileChooser.showOpenDialog(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file == null) return;
        Mat imageMat = Imgcodecs.imread(file.toString());
        if (imageMat.empty() && file.toString() != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Loading File");
            alert.setHeaderText("Could not load the image");
            alert.setContentText(file.toString().substring(file.toString().lastIndexOf("\\") + 1) + " is not a supported file");
            alert.showAndWait();
            loadImage();
            return;
        }
        imageMat.convertTo(imageMat, CvType.CV_8U);
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2HSV);
        model.setOriginalImage(imageMat);
        model.processImages();
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "loadNewImage":
                loadImage();
                break;
        }
    }

    public void initModel(CubeScanModel model) {
        this.model = model;
    }
}
