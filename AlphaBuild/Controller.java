package AlphaBuild;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {

    private Model model;


    @FXML
    private void extendView() {
        // Get the screen size
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        // Get the stage from the model
        Stage stage = model.getStage();

        // Set the size of the application
        stage.setWidth(1000);
        stage.setHeight(800);

        // Center the application
        stage.setX((dimension.getWidth() - stage.getWidth()) / 2);
        stage.setY((dimension.getHeight() - stage.getHeight()) / 2);
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
        }
    }

    public void initModel(Model model) {
        this.model = model;
    }
}
