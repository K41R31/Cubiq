package AlphaBuild.Model;

import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Observable;

public class Model extends Observable {

    private Stage stage;
    private Dimension dimension;
    private Font kionaRegular, kionaItalic;


    public void callObservers(String arg) {
        setChanged();
        notifyObservers(arg);
    }

    // Getter and setter
    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Font getKionaRegular() {
        return this.kionaRegular;
    }

    public void setKionaRegular(Font kionaRegular) {
        this.kionaRegular = kionaRegular;
    }

    public Font getKionaItalic() {
        return this.kionaItalic;
    }

    public void setKionaItalic(Font kionaItalic) {
        this.kionaItalic = kionaItalic;
    }
}
