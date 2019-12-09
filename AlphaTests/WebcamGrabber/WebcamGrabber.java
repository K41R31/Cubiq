package AlphaTests.WebcamGrabber;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebcamGrabber extends Application {

    private ImageView imageView;
    private VideoCapture videoCapture;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Init GUI
        AnchorPane anchorPane = new AnchorPane();
        imageView = new ImageView();

        anchorPane.getChildren().add(imageView);

        primaryStage.setScene(new Scene(anchorPane, 1280, 720));
        primaryStage.show();

        // Process Video stream
        videoCapture = new VideoCapture(0);

        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(this::getFrame, 0, 40, TimeUnit.MILLISECONDS);
    }

    private void getFrame() {
        if (videoCapture.isOpened()) {
            Mat frame = new Mat();
            videoCapture.read(frame);

            convertToImageView(frame);
        }
    }

    private void convertToImageView(Mat frame){
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", frame, buffer);
        Image imageToShow = new Image(new ByteArrayInputStream(buffer.toArray()));

        Platform.runLater(() -> imageView.setImage(imageToShow));
    }
}
