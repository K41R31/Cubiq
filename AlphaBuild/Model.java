package AlphaBuild;

import javafx.stage.Stage;

import java.awt.*;
import java.util.Observable;

public class Model extends Observable {

    private Stage stage;
    private Dimension dimension;


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
}
