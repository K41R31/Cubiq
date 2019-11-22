package AlphaTests.CubeScanFrameless.Start;

import AlphaTests.CubeScanFrameless.FileChooser.ImageLoader;
import AlphaTests.CubeScanFrameless.GUI.CubeScanFramelessController;
import AlphaTests.CubeScanFrameless.ImageProcessing.ImageProcessing;
import AlphaTests.CubeScanFrameless.Model.CubeScanFramelessModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.opencv.core.Core;

public class CubeScanFramelessAlpha extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //Init GUI------------------------------------------------------------------------------------------------------
        AnchorPane rootPane = new AnchorPane();

        FXMLLoader cubeScanLoader = new FXMLLoader(getClass().getResource("../GUI/CubeScanFramelessView.fxml"));
        rootPane.getChildren().add(cubeScanLoader.load());

        //Init Classes--------------------------------------------------------------------------------------------------
        ImageProcessing imageProcessing = new ImageProcessing();

        //Init Models---------------------------------------------------------------------------------------------------
        CubeScanFramelessModel model = new CubeScanFramelessModel();
        CubeScanFramelessController controller = cubeScanLoader.getController();
        controller.initModel(model);
        imageProcessing.initModel(model);

        model.addObserver(controller);
        model.addObserver(imageProcessing);

        //Init ImageLoader----------------------------------------------------------------------------------------------
        ImageLoader imageLoader = new ImageLoader(stage);
        imageLoader.initModel(model);
        model.addObserver(imageLoader);

        model.loadImage();

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
