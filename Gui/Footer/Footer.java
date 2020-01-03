package Gui.Footer;

import Models.GuiModel;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class Footer extends AnchorPane implements Observer {

    private GuiModel guiModel;

    private void initFooter() {
        HBox menuItemContainer = new HBox();
        menuItemContainer.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        menuItemContainer.setAlignment(Pos.BOTTOM_RIGHT);
        menuItemContainer.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        AnchorPane.setLeftAnchor(menuItemContainer, 0d);
        AnchorPane.setRightAnchor(menuItemContainer, 0d);
        AnchorPane.setBottomAnchor(menuItemContainer, 0d);

        MenuItem menuItemScan = new MenuItem(new Double[] {0d, 0d, 53d, 0d, 105d, 50d, 480d, 50d, 510d, 81d, 0d, 81d}, "test");
        MenuItem menuItemSolve = new MenuItem(new Double[] {0d, 0d, 480d, 0d, 510d, 31d, 30d, 31d}, "solve");
        MenuItem menuItemLearn = new MenuItem(new Double[] {0d, 0d, 480d, 0d, 510d, 31d, 30d, 31d}, "learn");
        MenuItem menuItemTimer = new MenuItem(new Double[] {0d, 0d, 480d, 0d, 510d, 31d, 30d, 31d}, "timer");

        menuItemSolve.setOnMouseClicked(event -> guiModel.setMenuItemSolveActive());
        menuItemLearn.setOnMouseClicked(event -> guiModel.setMenuItemLearnActive());
        menuItemTimer.setOnMouseClicked(event -> guiModel.setMenuItemTimerActive());

        menuItemContainer.getChildren().addAll(menuItemTimer, menuItemLearn, menuItemSolve, menuItemScan);

        ChangeListener sizeChangeListener = (ChangeListener<Double>) (observable, oldValue, newValue) -> {
            double width = menuItemContainer.getWidth() / 4;
            menuItemScan.updateWidth(width);
            menuItemSolve.updateWidth(width);
            menuItemLearn.updateWidth(width);
            menuItemTimer.updateWidth(width);
        };
        menuItemContainer.widthProperty().addListener(sizeChangeListener);

        this.getChildren().addAll(menuItemContainer);
    }

    private class MenuItem extends StackPane {
        Polygon mainShape = new Polygon();
        ImageView overlay;
        Polyline glowLine, blurredLine;
        Double[] points;

        MenuItem(Double[] points, String title) {
            this.points = points;
            setPadding(new Insets(0, -33, 0, 0));
            setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            setMinWidth(USE_PREF_SIZE);
            setPrefWidth(USE_COMPUTED_SIZE);
            setMaxWidth(USE_PREF_SIZE);
            setAlignment(Pos.BOTTOM_LEFT);
            mainShape.getPoints().addAll(points);
            mainShape.setFill(Color.web("#3d444d"));
            mainShape.setStrokeWidth(0);

            DropShadow dropShadow = new DropShadow();
            dropShadow.setWidth(50);
            dropShadow.setHeight(50);
            mainShape.setEffect(dropShadow);

            overlay = createOverlay();
            glowLine = createGlowLine();
            blurredLine = createBlurredLine();

            if (mainShape.getPoints().size() == 8) {
                setMinHeight(USE_PREF_SIZE);
                setPrefHeight(31);
                setMaxHeight(USE_PREF_SIZE);
            }
            else {
                setMinHeight(USE_PREF_SIZE);
                setPrefHeight(81);
                setMaxHeight(USE_PREF_SIZE);
            }

            getChildren().add(mainShape);
            getChildren().add(overlay);
            getChildren().add(glowLine);
            getChildren().add(blurredLine);

            if (mainShape.getPoints().size() == 8) {
                Text captionText = createCaption(title);

                mainShape.setOnMouseEntered(event -> {
                    mainShape.setFill(Color.web("#2bccbd"));
                    captionText.setFill(Color.web("#3d444d"));
                });

                mainShape.setOnMouseExited(event -> {
                    mainShape.setFill(Color.web("#3d444d"));
                    captionText.setFill(Color.web("#2bccbd"));
                });
                getChildren().add(captionText);
            } else {
                Text captionText = createScanCaption();

            }
        }

        private ImageView createOverlay() {
            int height;
            if (mainShape.getPoints().size() == 8) height = 31;
            else height = 81;

            ImageView overlay = new ImageView();
            overlay.setImage(new Image(getClass().getResourceAsStream(("../../Resources/Assets/combOverlay.png"))));
            overlay.setFitWidth(510);
            overlay.setFitHeight(height);
            overlay.setViewport(new Rectangle2D(0, 0, 510, height));

            Polygon mask = new Polygon();
            mask.getPoints().addAll(points);
            overlay.setClip(mask);
            overlay.setMouseTransparent(true);
            return overlay;
        }

        private Polyline createGlowLine() {
            Polyline line = newGlowLine();
            line.setEffect(new Glow(0.5));
            StackPane.setAlignment(line, Pos.TOP_CENTER);

            return line;
        }

        private Polyline createBlurredLine() {
            Polyline line = newGlowLine();
            line.setEffect(new GaussianBlur(15));
            StackPane.setAlignment(line, Pos.TOP_CENTER);

            return line;
        }

        private Polyline newGlowLine() {
            Polyline polyLine = new Polyline();
            Double[] linePoints;
            if (mainShape.getPoints().size() == 8) linePoints = Arrays.copyOfRange(points, 0, 6);
            else linePoints = Arrays.copyOfRange(points, 0, 10);
            polyLine.getPoints().addAll(linePoints);

            polyLine.setFill(Color.TRANSPARENT);
            polyLine.setStrokeType(StrokeType.CENTERED);
            polyLine.setStroke(Color.web("#2bccbd"));
            polyLine.setStrokeWidth(2);

            polyLine.setMouseTransparent(true);

            return polyLine;
        }

        private Text createCaption(String title) {
            // Create the effect of a spaced font
            String spacedTitle = "";
            for (int i = 0; i < title.length(); i++) {
                spacedTitle = spacedTitle.concat(title.substring(i, i + 1));
                if (i != title.length() - 1)
                    spacedTitle = spacedTitle.concat("   ");
            }
            Text text = new Text(spacedTitle.toUpperCase());
            text.setFont(guiModel.getBender());
            text.setStyle("-fx-font-size: 19");
            text.setFontSmoothingType(FontSmoothingType.LCD);
            text.setFill(Color.web("#2bccbd"));
            StackPane.setAlignment(text, Pos.CENTER_LEFT);
            StackPane.setMargin(text, new Insets(3, 0, 0, 60));

            text.setMouseTransparent(true);

            return text;
        }

        private Text createScanCaption() {
            Text text = new Text("S   C   A   N");
            text.setFont(guiModel.getBender());
            text.setStyle("-fx-font-size: 20pt");
            text.setFill(Color.web("#2bccbd"));
            text.setFontSmoothingType(FontSmoothingType.GRAY);

            return text;
        }

        private void updateWidth(double width) {
            // vals -> [0] + [1] the indexes of the outer right points of the polygon; [2] the height
            int[] vals = new int[] {2, 4, 31};
            if (mainShape.getPoints().size() != 8) vals = new int[] {6, 8, 81};
            // rootPane
            setPrefWidth(width + 1);
            // mainShape
            mainShape.getPoints().set(vals[0], width);
            mainShape.getPoints().set(vals[1], width + 30d);
            // overlay
            overlay.setFitWidth(width + 30d);
            overlay.setViewport(new Rectangle2D(0, 0, width + 30d, vals[2]));
            Polygon polygon = (Polygon)overlay.getClip();
            polygon.getPoints().set(vals[0], width);
            polygon.getPoints().set(vals[1], width + 30d);
            // lines
            glowLine.getPoints().set(vals[0], width);
            glowLine.getPoints().set(vals[1], width + 30d);
            blurredLine.getPoints().set(vals[0], width);
            blurredLine.getPoints().set(vals[1], width + 30d);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String) arg) {
            case "initFooter":
                initFooter();
                break;
        }
    }

    public void initModel(GuiModel model) {
        this.guiModel = model;
    }
}
