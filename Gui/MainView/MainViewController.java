package Gui.MainView;

import Models.SettingsModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Shear;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MainViewController implements Observer, Initializable {

    private SettingsModel settingsModel;
    @FXML
    private AnchorPane root;
    @FXML
    private ImageView iv_footerOverlay1, iv_footerOverlay2;
    @FXML
    private Polygon pg_footer1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Polygon overlayMask0 = new Polygon(0, 0, 30, 31, 511, 31, 480, 0);
        Polygon overlayMask1 = new Polygon(0, 0, 30, 31, 511, 31, 480, 0);
        //root.getChildren().add(overlayMask);
        iv_footerOverlay1.setClip(overlayMask0);
        iv_footerOverlay2.setClip(overlayMask1);
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
        }
    }

    public void initModel(SettingsModel model) {
        this.settingsModel = model;
    }
}
