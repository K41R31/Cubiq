package AlphaBuild;

import javafx.application.Application;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class Start extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Load library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


    }

    public static void main(String[] args) {
        launch(args);
    }
}
