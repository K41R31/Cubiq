package cubiq.gui;

import cubiq.models.GuiModel;
import java.util.Observable;
import java.util.Observer;

public class SolverController implements Observer {

    GuiModel guiModel;

    @Override
    public void update(Observable o, Object arg) {
        switch ((String) arg) {
            case "":
                break;
        }
    }

    public void initModel(GuiModel guiModel) {
        this.guiModel = guiModel;
    }
}
