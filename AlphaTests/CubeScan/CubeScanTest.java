package AlphaTests.CubeScan;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_COLOR;
import static org.opencv.imgcodecs.Imgcodecs.imread;

public class CubeScanTest extends Application {

    //TODO BILD IST BGR!!!

    private int lower0, lower1, lower2, upper0, upper1, upper2;
    private ImageView view;
    private Mat imgMat;

    public void inRange(Mat img) {

        //Imgproc.cvtColor(imgMat, cvtColorMat, Imgproc.COLOR_BGR2HSV);

        //SimpleBlobDetector detector = SimpleBlobDetector.create();
        /*FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        detector.detect(img, keypoints);
        List<KeyPoint> list = keypoints.toList();
        keypoints.release();
        Features2d.drawKeypoints(img, keypoints, cvtColorMat);
        System.out.println(list);
        */
    }

    private Image updateImage() {
        Mat processedImg = new Mat();
        Core.inRange(imgMat, new Scalar(lower0, lower1, lower2), new Scalar(upper0, upper1, upper2), processedImg);
        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".jpg", processedImg, byteMat); //imgMat = Mat die gezeichnet werden soll
        return new Image(new ByteArrayInputStream(byteMat.toArray()));
    }

    private HBox createSliders() {
        HBox hBox = new HBox();

        ColorRangeSlider sliderLower0 = new ColorRangeSlider(0);
        ColorRangeSlider sliderLower1 = new ColorRangeSlider(1);
        ColorRangeSlider sliderLower2 = new ColorRangeSlider(2);
        ColorRangeSlider sliderUpper0 = new ColorRangeSlider(3);
        ColorRangeSlider sliderUpper1 = new ColorRangeSlider(4);
        ColorRangeSlider sliderUpper2 = new ColorRangeSlider(5);

        hBox.getChildren().addAll(sliderLower0, sliderLower1, sliderLower2, sliderUpper0, sliderUpper1, sliderUpper2);

        return hBox;
    }

    private class ColorRangeSlider extends Slider {
        ColorRangeSlider(int number) {
            setMin(0);
            setMax(255);
            switch (number) {
                case 0:
                    valueProperty().addListener((ov, old_val, new_val) -> {
                        lower0 = (int)Math.round(getValue());
                        view.setImage(updateImage());
                    });
                    break;
                case 1:
                    valueProperty().addListener((ov, old_val, new_val) -> {
                        lower1 = (int)Math.round(getValue());
                        view.setImage(updateImage());
                    });
                    break;
                case 2:
                    valueProperty().addListener((ov, old_val, new_val) -> {
                        lower2 = (int)Math.round(getValue());
                        view.setImage(updateImage());
                    });
                    break;
                case 3:
                    valueProperty().addListener((ov, old_val, new_val) -> {
                        upper0 = (int)Math.round(getValue());
                        view.setImage(updateImage());
                    });
                    break;
                case 4:
                    valueProperty().addListener((ov, old_val, new_val) -> {
                        upper1 = (int)Math.round(getValue());
                        view.setImage(updateImage());
                    });
                    break;
                case 5:
                    valueProperty().addListener((ov, old_val, new_val) -> {
                        upper2 = (int)Math.round(getValue());
                        view.setImage(updateImage());
                    });
                    break;
            }
        }
    }

    @Override
    public void start(Stage stage) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        AnchorPane rootPane = new AnchorPane();

        stage.setTitle("Cubiq");
        stage.setScene(new Scene(rootPane, 1280, 720));
        stage.show();

        imgMat = imread("src/AlphaTests/CubeScan/cubeTestImage.jpg", IMREAD_COLOR);
        if (imgMat.empty()) System.out.println("Image was not read");

        view = new ImageView();
        rootPane.getChildren().add(view);

        rootPane.getChildren().add(createSliders());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
