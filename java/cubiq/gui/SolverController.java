package cubiq.gui;

import cubiq.models.GuiModel;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import java.awt.*;
import java.security.Policy;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SolverController implements Observer {
     //TODO: Bilderbezeichnung noch ändern, aktuell 2R, ändern auf R2... usw.!!!!

    private String imagePath;
    private int currentCycle = 0;
    private float startOffset = 0;
    private float startValue;

    @FXML
    private HBox solveIconPane;
    @FXML
    private Button startButton;
    private float solvePaneOffset = 0;

    public SolverController() {
        imagePath = "/assets/solveIcons/";
    }

    private GuiModel guiModel;

    private List<String> solution = new ArrayList<>();

    private void solveStringConverter() {
        String solveString = guiModel.getSolveString();
        Polygon polygon = new Polygon();
        polygon.setViewOrder(0);
        int idx = 0;
        while (true) {
            int idxNew;
            idxNew = solveString.indexOf(",", idx);
            if (idxNew != -1) {
                solution.add((solveString.substring(idx, idxNew)));
                idx = idxNew + 1;
            } else {
                break;
            }
        }
    }

    private void loadCubeIcons() {
        for(int i = 0; i < solution.size(); i++) {
            solveIconPane.getChildren().add(new SolveIcon(solution.get(i)));
        }
    }

    @FXML
    private void startSolution() {
        solveIconPane.setVisible(true);

        Timeline innerTimeline = new Timeline();
        innerTimeline.getKeyFrames().addAll(
                new KeyFrame(new Duration(0), e -> {
                    solvePaneOffset = easeInOut(currentCycle, startValue, -1,149);
                    currentCycle++;
                }),
                new KeyFrame(new Duration(1), e -> solveIconPane.setPadding(new Insets(0, 0, 0, solvePaneOffset)))

        );
        innerTimeline.setCycleCount(149);

        Timeline outerTimeline = new Timeline();
        outerTimeline.getKeyFrames().addAll(
                new KeyFrame(new Duration(0), e -> {
                    startValue = solvePaneOffset;
                    innerTimeline.play();
                }),
                new KeyFrame(new Duration(1500))
        );
        outerTimeline.setCycleCount(3);
        outerTimeline.play();
    }

    class SolveIcon extends ImageView {
        public SolveIcon(String solveString) {
            Image image = new Image(getClass().getResourceAsStream(imagePath+solveString+".png"));
            this.setFitWidth(97);
            this.setFitHeight(144);
            this.setImage(image);
        }
    }

    /**
     * Function to calculate a ease in/out animation
     * Source: http://gizma.com/easing/
     * @param t current time
     * @param b start value
     * @param c change in value
     * @param d duration
     * @return Eased value
     */
    private float easeInOut(float t, float b, float c, float d) {
        t /= d/2;
        if (t < 1) return c/2*t*t*t + b;
        t -= 2;
        return c/2*(t*t*t + 2) + b;
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String) arg) {
            case "startSolver":
                solveStringConverter();
                loadCubeIcons();
                break;
        }
    }

    public void initModel(GuiModel guiModel) {
        this.guiModel = guiModel;
    }
}
