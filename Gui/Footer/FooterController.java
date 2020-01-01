package Gui.Footer;

import Models.SettingsModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class FooterController implements Initializable, Observer {

    private SettingsModel settingsModel;
    @FXML
    private AnchorPane ap_rootPane;
    private Polygon firstElement, secondElement, thirdElement, fourthElement;

    private Double[] firstElementPoints = new Double[] {
            0d, 0d,
            53d, 0d,
            105d, 52d,
            480d, 52d,
            510d, 83d,
            0d, 83d
    };

    private Double[] secondElementPoints = new Double[] {
            0d, 0d,
            480d, 0d,
            510d, 31d,
            30d, 31d
    };

    private void buildScene() {
        HBox menuItemContainer = new HBox();

        firstElement = new Polygon();
        secondElement = new Polygon();
        thirdElement = new Polygon();
        fourthElement = new Polygon();

        firstElement.getPoints().addAll(firstElementPoints);
        secondElement.getPoints().addAll(secondElementPoints);
        thirdElement.getPoints().addAll(secondElementPoints);
        fourthElement.getPoints().addAll(secondElementPoints);

        ap_rootPane.getChildren().addAll(firstElement, secondElement, thirdElement, fourthElement);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buildScene();
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String) arg) {
        }
    }

    public void initModel(SettingsModel model) {
        this.settingsModel = model;
    }
}
