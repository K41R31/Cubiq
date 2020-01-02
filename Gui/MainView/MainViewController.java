package Gui.MainView;

import Models.SettingsModel;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MainViewController implements Observer, Initializable {

    private SettingsModel settingsModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
