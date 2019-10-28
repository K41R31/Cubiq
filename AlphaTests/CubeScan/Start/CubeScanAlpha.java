package AlphaTests.CubeScan.Start;

import AlphaTests.CubeScan.FileChooser.ImageLoader;
import AlphaTests.CubeScan.GUI.CubeScanController;
import AlphaTests.CubeScan.ImageProcessing.ImageProcessing;
import AlphaTests.CubeScan.Models.CubeScanModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.opencv.core.Core;

public class CubeScanAlpha extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //Init GUI------------------------------------------------------------------------------------------------------
        AnchorPane rootPane = new AnchorPane();

        FXMLLoader cubeScanLoader = new FXMLLoader(getClass().getResource("../GUI/CubeScanView.fxml"));
        rootPane.getChildren().add(cubeScanLoader.load());

        //Init Classes--------------------------------------------------------------------------------------------------
        ImageProcessing imageProcessing = new ImageProcessing();

        //Init Models---------------------------------------------------------------------------------------------------
        CubeScanModel cubeScanModel = new CubeScanModel();
        CubeScanController cubeScanController = cubeScanLoader.getController();
        cubeScanController.initModel(cubeScanModel);
        imageProcessing.initModel(cubeScanModel);

        cubeScanModel.addObserver(cubeScanController);
        cubeScanModel.addObserver(imageProcessing);

        cubeScanModel.addSliderListener();

        ImageLoader imageLoader = new ImageLoader(stage);
        imageLoader.initModel(cubeScanModel);
        imageLoader.loadImage();

        //Init Scene----------------------------------------------------------------------------------------------------
        stage.setTitle("Cubiq");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(rootPane, 1280, 720));
        stage.show();

        stage.getIcons().add(new Image("Resources/Assets/taskbarIcon.png"));
        stage.getIcons().size();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
