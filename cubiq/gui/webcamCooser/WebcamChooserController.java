package cubiq.gui.webcamCooser;

import cubiq.models.GuiModel;
import java.util.Observable;
import java.util.Observer;

public class WebcamChooserController implements Observer {

    private GuiModel model;




    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "menuItemSolveActive":
                break;
        }
    }

    public void initModel(GuiModel model) {
        this.model = model;
    }
}
