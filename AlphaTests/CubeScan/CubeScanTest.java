package AlphaTests.CubeScan;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.*;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_COLOR;
import static org.opencv.imgcodecs.Imgcodecs.imread;

public class CubeScanTest extends Application {

    //TODO BILD IST BGR!!!

    private int[] sliderValues = new int[6];
    private ImageView view;
    private Mat imgMat;
    boolean showingOriginalImage = true;

    /*
    public void blobDetector(Mat img) {

        //Imgproc.cvtColor(imgMat, cvtColorMat, Imgproc.COLOR_BGR2HSV);

        //SimpleBlobDetector detector = SimpleBlobDetector.create();
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        detector.detect(img, keypoints);
        List<KeyPoint> list = keypoints.toList();
        keypoints.release();
        Features2d.drawKeypoints(img, keypoints, cvtColorMat);
        System.out.println(list);
    }
    */

    private void showOriginalImage() {
        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".jpg", imgMat, byteMat);
        view.setImage(new Image(new ByteArrayInputStream(byteMat.toArray())));
        showingOriginalImage = true;
    }

    private void updateImage() {
        Mat processedImg = new Mat();
        Core.inRange(imgMat, new Scalar(sliderValues[0], sliderValues[1], sliderValues[2]), new Scalar(sliderValues[3], sliderValues[4], sliderValues[5]), processedImg);
        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".jpg", processedImg, byteMat);
        view.setImage(new Image(new ByteArrayInputStream(byteMat.toArray())));
        showingOriginalImage = false;
    }

    private HBox createControllers() {
        HBox hBox = new HBox();

        hBox.getChildren().addAll(
                new ColorRangeSlider(0),
                new ColorRangeSlider(1),
                new ColorRangeSlider(2),
                new ColorRangeSlider(3),
                new ColorRangeSlider(4),
                new ColorRangeSlider(5)
        );

        Button saveValuesButton = new Button();
        saveValuesButton.setText("Save Values");
        saveValuesButton.setOnAction(actionEvent -> {
            try (PrintWriter writer = new PrintWriter("src/AlphaTests/CubeScan/rangeValues.txt")) {
                for (int i = 0; i < 6; i++) writer.println(sliderValues[i]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        Button loadValuesButton = new Button();
        loadValuesButton.setText("Load Values");
        loadValuesButton.setOnAction(actionEvent -> {
            try {
                FileReader reader = new FileReader("src/AlphaTests/CubeScan/rangeValues.txt");
                BufferedReader bReader = new BufferedReader(reader);
                for (int i = 0; i < 6; i++) {
                    sliderValues[i] = Integer.parseInt(bReader.readLine());
                    updateImage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button showOriginalImageButton = new Button();
        showOriginalImageButton.setText("Show Processed Image");
        showOriginalImageButton.setOnAction(actionEvent -> {
            if (showingOriginalImage) {
                updateImage();
                showOriginalImageButton.setText("Show Original Image");
            } else {
                showOriginalImage();
                showOriginalImageButton.setText("Show Prodessed Image");
            }
        });

        hBox.getChildren().add(saveValuesButton);
        hBox.getChildren().add(loadValuesButton);
        hBox.getChildren().add(showOriginalImageButton);

        return hBox;
    }

    private class ColorRangeSlider extends Slider {
        ColorRangeSlider(int number) {
            setMin(0);
            setMax(255);
            switch (number) {
                case 0:
                    valueProperty().addListener((ov, old_val, new_val) -> {
                        sliderValues[0] = (int)Math.round(getValue());
                        updateImage();
                    });
                    break;
                case 1:
                    valueProperty().addListener((ov, old_val, new_val) -> {
                        sliderValues[1] = (int)Math.round(getValue());
                        updateImage();
                    });
                    break;
                case 2:
                    valueProperty().addListener((ov, old_val, new_val) -> {
                        sliderValues[2] = (int)Math.round(getValue());
                        updateImage();
                    });
                    break;
                case 3:
                    valueProperty().addListener((ov, old_val, new_val) -> {
                        sliderValues[3] = (int)Math.round(getValue());
                        updateImage();
                    });
                    break;
                case 4:
                    valueProperty().addListener((ov, old_val, new_val) -> {
                        sliderValues[4] = (int)Math.round(getValue());
                        updateImage();
                    });
                    break;
                case 5:
                    valueProperty().addListener((ov, old_val, new_val) -> {
                        sliderValues[5] = (int)Math.round(getValue());
                        updateImage();
                    });
                    break;
            }
        }
    }

    private void loadImage() {
        imgMat = imread("src/AlphaTests/CubeScan/cubeTestImage.jpg", IMREAD_COLOR);
        if (imgMat.empty()) System.out.println("Image was not read");
    }

    @Override
    public void start(Stage stage) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        AnchorPane rootPane = new AnchorPane();

        stage.setTitle("Cubiq");
        stage.setScene(new Scene(rootPane, 1280, 720));
        stage.show();

        loadImage();

        view = new ImageView();
        rootPane.getChildren().add(view);

        rootPane.getChildren().add(createControllers());

        showOriginalImage();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
