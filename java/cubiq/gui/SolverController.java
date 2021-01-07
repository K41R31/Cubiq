package cubiq.gui;

import cubiq.models.GuiModel;
import cubiq.processing.MathUtils;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SolverController implements Observer {
     //TODO: Bilderbezeichnung noch ändern, aktuell 2R, ändern auf R2... usw.!!!!

    private final float ANIMATION_JUMP_SPEED = 1f;
    private GuiModel guiModel;
    private String imagePath;
    private int currentCycle, animationResetCycles, cycleCounter;
    private float startOffset, solvePaneOffset;
    private List<String> solution;
    private List<SolveIcon> solveIcons;
    private Timeline innerTimeline, outerTimeline, resetAnimationTimeline;

    @FXML
    private HBox solveIconPane, buttonPane;
    @FXML
    private VBox speedSliderPane;
    @FXML
    private Slider speedSlider;
    @FXML
    private Text typoSlow, typoFast;
    @FXML
    private ProgressBar speedProgressBar;


    private void initializeSolverController() {
        solution = new ArrayList<>();
        solveIcons = new ArrayList<>();
        imagePath = "/assets/solveIcons/";
        buttonPane.getChildren().add(new ControlPane());

        solveStringConverter();

        solveIconPane.setVisible(true);
        cycleCounter = solution.size() - 1;
        speedSliderPane.setViewOrder(-1);
        speedSlider.setViewOrder(-2);
        typoSlow.setFont(guiModel.getKiona());
        typoSlow.setStyle("-fx-font-size: 12");
        typoSlow.setFontSmoothingType(FontSmoothingType.GRAY);
        typoFast.setFont(guiModel.getKiona());
        typoFast.setStyle("-fx-font-size: 12");
        typoSlow.setFontSmoothingType(FontSmoothingType.LCD);

        innerTimeline = new Timeline();
        innerTimeline.getKeyFrames().add(
                new KeyFrame(new Duration(1), e -> {
                    solvePaneOffset = MathUtils.easeInOut(currentCycle, startOffset, -149, Math.round(400 / ANIMATION_JUMP_SPEED));
                    currentCycle++;
                    solveIconPane.setPadding(new Insets(0, 0, 0, solvePaneOffset));
                })
        );
        innerTimeline.setCycleCount(Math.round(400 / ANIMATION_JUMP_SPEED));

        outerTimeline = new Timeline();
        outerTimeline.setRate(1);
        outerTimeline.getKeyFrames().add(
                new KeyFrame(new Duration(1800), e -> {
                    currentCycle = 0;
                    solvePaneOffset = Math.round(solvePaneOffset/ 149) * 149;
                    startOffset = solvePaneOffset;
                    cycleCounter--;
                    innerTimeline.play();
                })
        );
        outerTimeline.setCycleCount(solution.size()-1);

        resetAnimationTimeline = new Timeline();
        resetAnimationTimeline.getKeyFrames().add(
                new KeyFrame(new Duration(1), e -> {
                    solvePaneOffset = MathUtils.easeInOut(currentCycle, startOffset, startOffset*-1, animationResetCycles);
                    currentCycle++;
                    solveIconPane.setPadding(new Insets(0, 0, 0, solvePaneOffset));
                })
        );

        // Load cube icons
        for (String s : solution) {
            SolveIcon solveIcon = new SolveIcon(s);
            solveIcons.add(solveIcon);
            solveIconPane.getChildren().add(solveIcon);
        }
            speedSlider.valueProperty().addListener((ov, oldVl, newVl) -> {
            float value = newVl.floatValue();
            speedProgressBar.setProgress((value) * (1f / 3.2f));
            outerTimeline.setRate(value + 0.8f);
        });
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

    class SolveIcon extends ImageView {
        public SolveIcon(String solveString) {
            Image image = new Image(getClass().getResourceAsStream(imagePath+solveString+".png"));
            this.setFitWidth(97);
            this.setFitHeight(144);
            this.setImage(image);
        }
    }

    class ControlPane extends AnchorPane {
        ImageView buttonLIcon, buttonMIcon, buttonRIcon;

        ControlPane() {
            initializeRootPane();
            Polygon polygonLeft = generatePolygons(new Double[]{0.0, 0.0, 63.0, 63.0, 193.0, 63.0, 130.0, 0.0}, 0);
            Polygon polygonMiddle = generatePolygons(new Double[]{0.0, 0.0, 63.0, 63.0, 158.0, 63.0, 220.0, 0.0}, 1);
            Polygon polygonRight = generatePolygons(new Double[]{0.0, 0.0, -63.0, 63.0, 70.0, 63.0, 130.0, 0.0}, 2);
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

        private Polygon generatePolygons(Double[] polygonPoints, int id) {
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
                        cycleCounter = solution.size()-1;
                        currentCycle = 0;
                        startOffset = solvePaneOffset;
                        animationResetCycles = Math.round(Math.abs(solvePaneOffset / 3));
                        resetAnimationTimeline.setCycleCount(animationResetCycles);
                        resetAnimationTimeline.play();
                    });

                    setLeftAnchor(polygon, 0.0);
                    break;

                case 1: // Play/Pause
                    polygon.setOnMousePressed(e -> {
                        if (outerTimeline.getStatus() != Animation.Status.RUNNING && cycleCounter > 0)
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
            case "showSolver":
                initializeSolverController();
                break;
        }
    }

    public void initModel(GuiModel guiModel) {
        this.guiModel = guiModel;
    }
}
