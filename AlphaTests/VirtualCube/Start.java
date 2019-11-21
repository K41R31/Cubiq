package AlphaTests.VirtualCube;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Start extends Application {

    @Override
    public void start(Stage stage) {
        AnchorPane rootPane = new AnchorPane();
        stage.setTitle("Cubiq");
        stage.setScene(new Scene(rootPane, 1280, 720));
        stage.show();

        new StartCodeMainWindowPP();
    }
}
