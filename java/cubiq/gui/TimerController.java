package cubiq.gui;

import cubiq.models.GuiModel;

import java.util.Observable;
import java.util.Observer;

public class TimerController implements Observer {

    private GuiModel guiModel;

    private void initializeTimerController() {

    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String) arg) {
            case "initGuiElements":
                initializeTimerController();
                break;
        }
    }

    public void initModel(GuiModel guiModel) {
        this.guiModel = guiModel;
    }
}
