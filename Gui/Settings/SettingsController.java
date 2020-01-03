package GUI.Settings;

import Models.GuiModel;

import java.util.Observable;
import java.util.Observer;

public class SettingsController implements Observer {

    private GuiModel guiModel;

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
        }
    }

    public void initModel(GuiModel model) {
        this.guiModel = model;
    }
}
