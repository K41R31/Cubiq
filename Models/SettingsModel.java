package Models;

import javafx.scene.text.Font;

import java.util.Observable;

public class SettingsModel extends Observable {

    private Font kiona;


    // initFooter-------------------------------------------------------------------------------------------------------

    public void initFooter() {
        setChanged();
        notifyObservers("initFooter");
    }

    // kiona------------------------------------------------------------------------------------------------------------

    public Font getKiona() {
        return this.kiona;
    }

    public void setKiona(Font font) {
        this.kiona = font;
    }
}
