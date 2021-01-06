package cubiq.gui;

import cubiq.models.GuiModel;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SolverController implements Observer {
     //TODO: Bilderbezeichnung noch ändern, aktuell 2R, ändern auf R2... usw.!!!!

    private String imagePath;
    private int currentCycle = 0;
    private float startOffset = 0;

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

//  TODO Generell:
//          Die Duration gibt nicht an wie lange ein Keyframe dauert, sondern wann er in der Timeline getriggert werden soll.
//       Problem:
//          Der Interpolator kann die insets anscheinend nicht interpolieren, da sie keine direkten numerischen Werte sind.
//          Deswegen springt die Timeline von einem zum anderem Keyframe ohne zwischen ihnen zu interpolieren.
//       Spoiler:
//          Eine Lösung wäre, die Werte manuell zu Interpolieren. Unten bin ich pro Timeline Durchlauf immer einen Pixel weiter gegangen.

    @FXML
    private void startSolution() {
        // Inner
        Timeline startSolutionAnimation = new Timeline();
        Timeline sleepTimer = new Timeline();
        startSolutionAnimation.getKeyFrames().addAll(
               new KeyFrame(new Duration(0), e -> {
                   easeOut();
                   solveIconPane.setPadding(new Insets(0, 0, 0, solvePaneOffset));
                   currentCycle++;
               }),
               new KeyFrame(new Duration(2.5), e -> solvePaneOffset -= 0.5)
        );
        startSolutionAnimation.setCycleCount(298);
                new KeyFrame(new Duration(0), e -> solveIconPane.setPadding(new Insets(0, 0, 0, solvePaneOffset))),
                new KeyFrame(new Duration(13), e -> solvePaneOffset -= 1)
        );
        startSolutionAnimation.setCycleCount(149);
        startSolutionAnimation.setRate(1);
        solveIconPane.setVisible(true);
        startSolutionAnimation.setOnFinished(e -> {
            sleepTimer.play();
        });
        startSolutionAnimation.play();

        // Outer
        Timeline outerTimeline = new Timeline();
        outerTimeline.getKeyFrames().addAll(
                new KeyFrame(new Duration(200), e -> startSolutionAnimation.play())
        );
        outerTimeline.setRate(1);
        solveIconPane.setVisible(true);
        startSolutionAnimation.setOnFinished(e -> {
            System.out.println("Penis");
        });
        startSolutionAnimation.play();
        sleepTimer.getKeyFrames().addAll(
                new KeyFrame(new Duration(0)),
                new KeyFrame(new Duration(1500), e -> startSolutionAnimation.play())
                );
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
     * Function to calculate a ease out animation
     * Source: http://gizma.com/easing/
     * @param t current time
     * @param b start value
     * @param c change in value
     * @param d duration
     * @return Eased value
     */
    private float easeOut(float t, float b, float c, float d) {
        t /= d;
        t--;
        return c*(t*t*t + 1) + b;
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
