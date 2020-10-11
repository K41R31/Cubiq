package AlphaBuild;

import java.util.Observable;
import java.util.Observer;

public class ImageProcessing implements Observer {

    private Model model;


    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
        }
    }

    public void initModel(Model model) {
        this.model = model;
    }
}
