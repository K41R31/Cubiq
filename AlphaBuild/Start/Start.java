package AlphaBuild.Start;

import AlphaBuild.Gui.Controller;
import AlphaBuild.Processing.ImageProcessing;
import AlphaBuild.Model.Model;
import AlphaBuild.Processing.Renderer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.opencv.core.Core;

public class Start extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        // Load library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Init View
        FXMLLoader viewLoader = new FXMLLoader(getClass().getResource("../Gui/View.fxml"));
        AnchorPane viewPane = viewLoader.load();

        // Init Image Processing
        ImageProcessing imageProcessing = new ImageProcessing();

        // Init Renderer
        Renderer renderer = new Renderer(viewPane);

        // Init Model
        Model model = new Model();

        Controller controller = viewLoader.getController();

        controller.initModel(model);
        imageProcessing.initModel(model);
        renderer.initModel(model);

        model.addObserver(controller);
        model.addObserver(imageProcessing);
        model.addObserver(renderer);

        model.setStage(stage);

        // Init Scene---------------------------------------------------------------------------------------------------
        Scene scene = new Scene(viewPane);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

        // Load Fonts
        Font kionaRegular = Font.loadFont(getClass().getResource("../Resources/Fonts/Kiona-Regular.ttf").toExternalForm().replace("%20", " "), 30);
        model.setKionaRegular(kionaRegular);

        Font kionaItalic = Font.loadFont(getClass().getResource("../Resources/Fonts/Kiona-Italic.ttf").toExternalForm().replace("%20", " "), 17);
        model.setKionaItalic(kionaItalic);

        model.callObservers("guiInitialized");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
