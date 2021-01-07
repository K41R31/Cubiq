package cubiq.gui;

import cubiq.models.GuiModel;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import java.security.Key;

import javafx.scene.shape.Polyline;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SolverController implements Observer {
     //TODO: Bilderbezeichnung noch ändern, aktuell 2R, ändern auf R2... usw.!!!!

    private String imagePath;
    private boolean isAnimating = false;

    private int currentCycle = 0;
    private float startOffset = 0;
    private float startValue;

    @FXML
    private HBox solveIconPane, buttonPane;
    @FXML
    private Button startButton;
    private float solvePaneOffset = 0;

    private GuiModel guiModel;

    private List<String> solution = new ArrayList<>();
    private Timeline innerTimeline, outerTimeline;

    private void initializeSolverController() {
        imagePath = "/assets/solveIcons/";
        currentCycle = solution.size() - 1;
        buttonPane.getChildren().add(new ControlPane());

        solveIconPane.setVisible(true);

        innerTimeline = new Timeline();
        innerTimeline.getKeyFrames().addAll(
                new KeyFrame(new Duration(0), e -> solvePaneOffset -= 0.5),
                new KeyFrame(new Duration(1), e -> solveIconPane.setPadding(new Insets(0, 0, 0, solvePaneOffset)))
        );
        innerTimeline.setCycleCount(298);

        outerTimeline = new Timeline();

        outerTimeline.getKeyFrames().addAll(
                new KeyFrame(new Duration(2000), e -> { innerTimeline.play(); currentCycle--; System.out.println(currentCycle); })
        );
        outerTimeline.setCycleCount(solution.size()-1);
    }

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

    private void openSpeedSlider() {
    }

    private void loadCubeIcons() {
        for(int i = 0; i < solution.size(); i++) {
            solveIconPane.getChildren().add(new SolveIcon(solution.get(i)));
        }
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

    class ControlPane extends AnchorPane {
        ImageView buttonLIcon, buttonMIcon, buttonRIcon;

        ControlPane() {
            initializeRootPane();
            Polygon polygonLeft = generatePolygones(new Double[]{0.0, 0.0, 63.0, 63.0, 193.0, 63.0, 130.0, 0.0}, 0);
            Polygon polygonMiddle = generatePolygones(new Double[]{0.0, 0.0, 63.0, 63.0, 158.0, 63.0, 220.0, 0.0}, 1);
            Polygon polygonRight = generatePolygones(new Double[]{0.0, 0.0, -63.0, 63.0, 70.0, 63.0, 130.0, 0.0}, 2);
            getChildren().addAll(polygonLeft, polygonMiddle, polygonRight);
            polygonMiddle.setViewOrder(-1);
        }

        private void initializeRootPane() {
            this.minWidth(USE_PREF_SIZE);
            this.minHeight(USE_PREF_SIZE);
            this.prefWidth(480);
            this.prefHeight(63);
            this.maxWidth(USE_PREF_SIZE);
            this.maxHeight(USE_PREF_SIZE);
        }

        private Polygon generatePolygones(Double[] polygonPoints, int id) {
            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(polygonPoints);
            polygon.setFill(Color.web("#3f464f"));
            polygon.setStrokeWidth(0);
            polygon.setEffect(new DropShadow());

            setTopAnchor(polygon, 0.0);
            setBottomAnchor(polygon, 0.0);

            polygon.setOnMouseEntered(e -> polygon.setFill(Color.web("#e4e4e4")));
            polygon.setOnMouseExited(e -> polygon.setFill(Color.web("#3f464f")));
            switch (id) {
                case 0: // Restart iteration
                    polygon.setOnMousePressed(e -> {
                        outerTimeline.stop();
                        innerTimeline.stop();
                        currentCycle = solution.size() - 1;
                        solvePaneOffset = 0;
                        solveIconPane.setPadding(new Insets(0));
                    });

                    setLeftAnchor(polygon, 0.0);
                    break;

                case 1: // Play/Pause
                    polygon.setOnMousePressed(e -> {
                        System.out.println(outerTimeline.getStatus());
                        if(outerTimeline.getStatus() != Animation.Status.RUNNING && currentCycle > 0)
                        outerTimeline.play();
                        else
                            outerTimeline.pause();
                    });
                    setLeftAnchor(polygon, 130.0);
                    break;

                case 2: // Set speed
                    polygon.setOnMousePressed(e -> openSpeedSlider());
                    setLeftAnchor(polygon, 288.0);
            }
            return polygon;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String) arg) {
            case "startSolver":
                solveStringConverter();
                initializeSolverController();
                loadCubeIcons();
                break;
        }
    }

    public void initModel(GuiModel guiModel) {
        this.guiModel = guiModel;
    }
}
