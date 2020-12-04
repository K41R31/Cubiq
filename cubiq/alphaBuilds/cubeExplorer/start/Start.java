package cubiq.alphaBuilds.cubeExplorer.start;

import cubiq.alphaBuilds.cubeExplorer.gui.Controller;
import cubiq.alphaBuilds.cubeExplorer.model.Model;
import cubiq.alphaBuilds.cubeExplorer.processing.RendererPP;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        FXMLLoader viewLoader = new FXMLLoader(getClass().getResource("../gui/View.fxml"));
        AnchorPane viewPane = viewLoader.load();

        // Init Classes
        RendererPP renderer = new RendererPP();

        // Init Model
        Model model = new Model();

        Controller controller = viewLoader.getController();

        controller.initModel(model);
        renderer.initModel(model);

        model.addObserver(controller);
        model.addObserver(renderer);

        model.setStage(stage);

        // Init Scene---------------------------------------------------------------------------------------------------
        Scene scene = new Scene(viewPane);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResource("../Resources/Assets/taskbarIcon.png").toExternalForm().replace("%20", " ")));
        stage.show();

        // Load Fonts
        Font kionaRegular = Font.loadFont(getClass().getResource("../Resources/Fonts/Kiona-Regular.ttf").toExternalForm().replace("%20", " "), 30);
        model.setKionaRegular(kionaRegular);

        Font kionaItalic = Font.loadFont(getClass().getResource("../Resources/Fonts/Kiona-Italic.ttf").toExternalForm().replace("%20", " "), 17);
        model.setKionaItalic(kionaItalic);

        // Tell all observers, that the GUI has been initialized
        model.callObservers("guiInitialized");
        model.callObservers("startRenderer");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
