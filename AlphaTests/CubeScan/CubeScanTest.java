package AlphaTests.CubeScan;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.io.*;
import java.util.List;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_COLOR;
import static org.opencv.imgcodecs.Imgcodecs.imread;

public class CubeScanTest extends Application {

    //TODO BILD IST BGR!!!

    private int[] sliderValues = new int[8];
    private ImageView view;
    private Mat imgMat;
    private boolean showingOriginalImage = true;
    private Slider[] sliders = new Slider[8];
    private boolean doBlobDetector = false;
    List<KeyPoint> list;

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
        if (doBlobDetector) {
            Mat blobMat = imgMat.clone();
            for (int i = 0; i < list.size(); i++) Imgproc.circle(blobMat, list.get(i).pt, (int)list.get(i).size/2, new Scalar(100, 100, 100), 2);
            MatOfByte byteMat = new MatOfByte();
            Imgcodecs.imencode(".jpg", blobMat, byteMat);
            view.setImage(new Image(new ByteArrayInputStream(byteMat.toArray())));
        } else {
            MatOfByte byteMat = new MatOfByte();
            Imgcodecs.imencode(".jpg", imgMat, byteMat);
            view.setImage(new Image(new ByteArrayInputStream(byteMat.toArray())));
        }
        showingOriginalImage = true;
    }

    private void updateImage() {
        Mat processedImg = new Mat();
        Imgproc.GaussianBlur(imgMat, processedImg, new Size(sliderValues[6], sliderValues[6]), sliderValues[6], sliderValues[6]);
        Imgproc.cvtColor(processedImg, processedImg, Imgproc.COLOR_BGR2HSV);
        Imgproc.medianBlur(processedImg, processedImg, sliderValues[7]);
        Core.inRange(processedImg, new Scalar(sliderValues[0], sliderValues[1], sliderValues[2]), new Scalar(sliderValues[3], sliderValues[4], sliderValues[5]), processedImg);
        if(doBlobDetector) {
            FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
            detector.read("src/AlphaTests/CubeScan/Resources/SavedData/blobdetectorparams1.xml");
            MatOfKeyPoint keypoints = new MatOfKeyPoint();
            detector.detect(processedImg, keypoints);
            list = keypoints.toList();
            System.out.println(list.size());
            keypoints.release();
        }
        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".jpg", processedImg, byteMat);
        view.setImage(new Image(new ByteArrayInputStream(byteMat.toArray())));
        showingOriginalImage = false;
    }

    private VBox createControllers() {
        VBox vBox = new VBox();
        HBox hBoxColorRange = new HBox();
        HBox hBoxGaussianBlur = new HBox();
        HBox hBoxMedianBlur = new HBox();

        hBoxColorRange.getChildren().add(new Text("Color Range"));

        sliders[0] = new ColorRangeSlider(0);
        sliders[1] = new ColorRangeSlider(1);
        sliders[2] = new ColorRangeSlider(2);
        sliders[3] = new ColorRangeSlider(3);
        sliders[4] = new ColorRangeSlider(4);
        sliders[5] = new ColorRangeSlider(5);


        hBoxColorRange.getChildren().addAll(
                sliders[0],
                sliders[1],
                sliders[2],
                sliders[3],
                sliders[4],
                sliders[5]
                );

        Button saveValuesButton = new Button();
        saveValuesButton.setText("Save Values");
        saveValuesButton.setOnAction(actionEvent -> {
            try (PrintWriter writer = new PrintWriter("src/AlphaTests/CubeScan/rangeValues.txt")) {
                for (int i = 0; i < 8; i++) writer.println(sliderValues[i]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        Button loadValuesButton = new Button();
        loadValuesButton.setText("Load Values");
        loadValuesButton.setOnAction(actionEvent -> {
            try {
                FileReader reader = new FileReader("src/AlphaTests/CubeScan/Resources/SavedData/rangeValues.txt");
                BufferedReader bReader = new BufferedReader(reader);
                for (int i = 0; i < 8; i++) {
                    sliderValues[i] = Integer.parseInt(bReader.readLine());
                    sliders[i].setValue(sliderValues[i]);
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

        Button doBlobDetectorButton = new Button();
        doBlobDetectorButton.setText("Blob Detector");
        doBlobDetectorButton.setOnAction(actionEvent -> doBlobDetector = true);

        hBoxColorRange.getChildren().addAll(saveValuesButton, loadValuesButton, showOriginalImageButton, doBlobDetectorButton);

        sliders[6] = new Slider();
        sliderValues[6] = 1;
        sliders[6].setMin(1);
        sliders[6].setMax(20);
        sliders[6].valueProperty().addListener((ov, old_val, new_val) -> {
            sliderValues[6] = (int)Math.round(sliders[6].getValue());
            if (sliderValues[6] % 2 == 0) sliderValues[6]++;
            updateImage();
        });

        sliders[7] = new Slider();
        sliderValues[7] = 1;
        sliders[7].setMin(1);
        sliders[7].setMax(20);
        sliders[7].valueProperty().addListener((ov, old_val, new_val) -> {
            sliderValues[7] = (int)Math.round(sliders[7].getValue());
            if (sliderValues[7] % 2 == 0) sliderValues[7]++;
            updateImage();
        });

        hBoxGaussianBlur.getChildren().addAll(new Text("Gaussian Blur Kernelsize"), sliders[6]);
        hBoxMedianBlur.getChildren().addAll(new Text("Median Blur Kernelsize"), sliders[7]);

        vBox.getChildren().addAll(hBoxColorRange, hBoxGaussianBlur, hBoxMedianBlur);
        return vBox;
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
        imgMat = imread("src/AlphaTests/CubeScan/Resources/Assets/WIN_20191108_11_57_09_Pro.jpg", IMREAD_COLOR);
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
