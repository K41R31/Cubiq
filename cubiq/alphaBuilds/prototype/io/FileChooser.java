package cubiq.alphaBuilds.prototype.io;

import cubiq.alphaBuilds.prototype.model.Model;
import javafx.scene.control.Alert;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class FileChooser implements Observer {

    private Model model;
    private javafx.stage.FileChooser fileChooser;


    public FileChooser() {
        fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Select an image");

        // Set the standard location, where the file chooser starts
        fileChooser.setInitialDirectory(new File("src/AlphaBuild/Resources/Images"));

        // Set the extensions that you can select with the file chooser
        fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new javafx.stage.FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new javafx.stage.FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    /**
     * Opens the file chooser, reads the selected image and loads it into the model
     */
    private void loadImage() {
        File file = fileChooser.showOpenDialog(model.getStage());
        if (file == null) return;
        Mat mat = Imgcodecs.imread(file.toString());
        if (mat.empty() && file.toString() != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Loading File");
            alert.setHeaderText("Could not load the image");
            alert.setContentText(file.toString().substring(file.toString().lastIndexOf("\\") + 1) + " is not a supported file");
            alert.showAndWait();
            loadImage();
            return;
        }
        mat.convertTo(mat, CvType.CV_8UC3);
        model.setOriginalImage(mat);
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "loadImage":
                loadImage();
                break;
        }
    }

    public void initModel(Model model) {
        this.model = model;
    }
}
