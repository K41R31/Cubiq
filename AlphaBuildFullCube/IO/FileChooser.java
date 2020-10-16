package AlphaBuildFullCube.IO;

import AlphaBuildFullCube.Model.Model;
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
        fileChooser.setTitle("Select cube images");

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
    private void loadImages() {
        String rootFile = fileChooser.showOpenDialog(model.getStage()).getParent();
        if (rootFile == null) return;
        Mat[] cubeImages = new Mat[6];

        cubeImages[0] = Imgcodecs.imread(rootFile + "/white.jpg");
        cubeImages[1] = Imgcodecs.imread(rootFile + "/yellow.jpg");
        cubeImages[2] = Imgcodecs.imread(rootFile + "/red.jpg");
        cubeImages[3] = Imgcodecs.imread(rootFile + "/orange.jpg");
        cubeImages[4] = Imgcodecs.imread(rootFile + "/green.jpg");
        cubeImages[5] = Imgcodecs.imread(rootFile + "/blue.jpg");

        for (Mat cubeImage : cubeImages) cubeImage.convertTo(cubeImage, CvType.CV_8UC3);
        model.setOriginalCubeImages(cubeImages);
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "loadImage":
                loadImages();
                break;
        }
    }

    public void initModel(Model model) {
        this.model = model;
    }
}
