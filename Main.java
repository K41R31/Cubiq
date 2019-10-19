package Cubiq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

//https://openjfx.io/openjfx-docs/#install-javafx

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Cubiq");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        primaryStage.getIcons().add(new Image("Cubiq/Resources/Assets/taskbarIcon.png"));
        primaryStage.getIcons().size();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
