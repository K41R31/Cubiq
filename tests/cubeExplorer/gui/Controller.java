package cubeExplorer.gui;

import cubeExplorer.model.Model;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {

    private Model model;
    @FXML
    private AnchorPane rendererPane;

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "guiInitialized":
                model.setRendererPane(rendererPane);
                break;
        }
    }

    public void initModel(Model model) {
        this.model = model;
    }
}
