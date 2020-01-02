package Gui.MainView;

import Models.SettingsModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MainViewController implements Observer, Initializable {

    private SettingsModel settingsModel;
    @FXML
    private StackPane sp_solvePane, sp_learnPane, sp_timerPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "menuItemSolveActive":
                sp_solvePane.setVisible(true);
                sp_learnPane.setVisible(false);
                sp_timerPane.setVisible(false);
                break;
            case "menuItemLearnActive":
                sp_solvePane.setVisible(false);
                sp_learnPane.setVisible(true);
                sp_timerPane.setVisible(false);
                break;
            case "menuItemTimerActive":
                sp_solvePane.setVisible(false);
                sp_learnPane.setVisible(false);
                sp_timerPane.setVisible(true);
                break;
        }
    }

    public void initModel(SettingsModel model) {
        this.settingsModel = model;
    }
}
