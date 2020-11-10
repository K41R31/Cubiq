package cubiq.alphaBuilds.cubeExplorer.gui;

import cubiq.alphaBuilds.cubeExplorer.model.Model;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {

    private Model model;
    @FXML
    private AnchorPane rendererPane;


    @FXML
    private void rotateCubeX() {
        model.callObservers("rotateCubeX");
    }

    @FXML
    private void rotateCubeY() {
        model.callObservers("rotateCubeY");
    }

    @FXML
    private void rotateCubeZ() {
        model.callObservers("rotateCubeZ");
    }

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
