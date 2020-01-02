package Models;

import javafx.scene.text.Font;

import java.util.Observable;

public class SettingsModel extends Observable {

    private Font kiona;
    private Font bender;

    // initFooter-------------------------------------------------------------------------------------------------------

    public void initFooter() {
        setChanged();
        notifyObservers("initFooter");
    }

    // switchActiveMenuItem

    public void setMenuItemSolveActive() {
        setChanged();
        notifyObservers("menuItemSolveActive");
    }

    public void setMenuItemLearnActive() {
        setChanged();
        notifyObservers("menuItemLearnActive");
    }

    public void setMenuItemTimerActive() {
        setChanged();
        notifyObservers("menuItemTimerActive");
    }

    // kiona------------------------------------------------------------------------------------------------------------

    public Font getKiona() {
        return this.kiona;
    }

    public void setKiona(Font font) {
        this.kiona = font;
    }

    // bender-----------------------------------------------------------------------------------------------------------

    public Font getBender() {
        return this.bender;
    }

    public void setBender(Font font) {
        this.bender = font;
    }
}
