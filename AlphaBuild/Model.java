package AlphaBuild;

import javafx.stage.Stage;
import java.util.Observable;

public class Model extends Observable {

    private Stage stage;


    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
