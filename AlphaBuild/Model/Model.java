package AlphaBuild.Model;

import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import java.util.Observable;

public class Model extends Observable {

    private Stage stage;
    private Font kionaRegular, kionaItalic;
    private Mat originalImage;
    private AnchorPane rendererPane;
    private double[][][] cubeColors;
    private final double sideLengthThreshold = 0.8;
    private final double angleThreshold = 50.0;
    private final double rotationThreshold = 20.0;
    private final double blurThreshold = 3.0;

    /**
     * Calls the update function in every observer class
     * @param arg A sting that will be submitted
     */
    public void callObservers(String arg) {
        setChanged();
        notifyObservers(arg);
    }

    // Getter and setter------------------------------------------------------------------------------------------------
    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Font getKionaRegular() {
        return this.kionaRegular;
    }

    public void setKionaRegular(Font kionaRegular) {
        this.kionaRegular = kionaRegular;
    }

    public Font getKionaItalic() {
        return this.kionaItalic;
    }

    public void setKionaItalic(Font kionaItalic) {
        this.kionaItalic = kionaItalic;
    }

    public Mat getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(Mat originalImage) {
        this.originalImage = originalImage;
    }

    public AnchorPane getRendererPane() {
        return rendererPane;
    }

    public void setRendererPane(AnchorPane rendererPane) {
        this.rendererPane = rendererPane;
    }

    public double[][][] getCubeColors() {
        return cubeColors;
    }

    public void setCubeColors(double[][][] cubeColors) {
        this.cubeColors = cubeColors;
    }
}
