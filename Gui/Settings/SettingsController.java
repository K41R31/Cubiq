package Gui.Settings;

import Models.SettingsModel;

import java.util.Observable;
import java.util.Observer;

public class SettingsController implements Observer {

    private SettingsModel settingsModel;

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
        }
    }

    public void initModel(SettingsModel model) {
        this.settingsModel = model;
    }
}
