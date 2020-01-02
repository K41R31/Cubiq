package Gui.Footer;

import Models.SettingsModel;
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

    private SettingsModel settingsModel;

    private void initFooter() {
        HBox menuItemContainer = new HBox();
        menuItemContainer.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        menuItemContainer.setAlignment(Pos.BOTTOM_RIGHT);
        menuItemContainer.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        AnchorPane.setLeftAnchor(menuItemContainer, 0d);
        AnchorPane.setRightAnchor(menuItemContainer, 0d);
        AnchorPane.setBottomAnchor(menuItemContainer, 0d);

        MenuItem item0 = new MenuItem(new Double[] {0d, 0d, 53d, 0d, 105d, 50d, 480d, 50d, 510d, 81d, 0d, 81d});
        MenuItem item1 = new MenuItem(new Double[] {0d, 0d, 480d, 0d, 510d, 31d, 30d, 31d});
        MenuItem item2 = new MenuItem(new Double[] {0d, 0d, 480d, 0d, 510d, 31d, 30d, 31d});
        MenuItem item3 = new MenuItem(new Double[] {0d, 0d, 480d, 0d, 510d, 31d, 30d, 31d});

        item1.setText("S   o   l   v   e");

        menuItemContainer.getChildren().addAll(item3, item2, item1, item0);

        this.getChildren().addAll(menuItemContainer);
    }

    private class MenuItem extends StackPane {
        Polygon mainShape = new Polygon();
        Double[] points;

        MenuItem(Double[] points) {
            this.points = points;
            setPadding(new Insets(0, -33, 0, 0));
            setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            setPrefWidth(USE_COMPUTED_SIZE);
            setAlignment(Pos.BOTTOM_CENTER);
            StackPane.setMargin(mainShape, new Insets(0, 0, 0, -4));
            mainShape.getPoints().addAll(points);
            mainShape.setFill(Color.web("#3d444d"));
            mainShape.setStrokeWidth(0);

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

            DropShadow dropShadow = new DropShadow();
            dropShadow.setWidth(50);
            dropShadow.setHeight(50);
            mainShape.setEffect(dropShadow);

            getChildren().add(mainShape);
            addOverlay();
            addGlowLine();
        }

        private void addOverlay() {
            int height;
            if (mainShape.getPoints().size() == 8) height = 31;
            else height = 81;

            ImageView overlay = new ImageView();
            overlay.setImage(new Image(getClass().getResourceAsStream(("../../Resources/Assets/combOverlay.png"))));
            overlay.setFitWidth(514);
            overlay.setFitHeight(height);
            overlay.setViewport(new Rectangle2D(0, 0, 514, height));

            Polygon mask = new Polygon();
            mask.getPoints().addAll(points);
            overlay.setClip(mask);
            overlay.setMouseTransparent(true);
            getChildren().add(overlay);
        }

        private void addGlowLine() {
            Polyline mainLine = newGlowLine();
            Polyline blurredLine = newGlowLine();

            mainLine.setEffect(new Glow(0.5));
            blurredLine.setEffect(new GaussianBlur(15));

            StackPane.setAlignment(mainLine, Pos.TOP_CENTER);
            StackPane.setAlignment(blurredLine, Pos.TOP_CENTER);

            getChildren().addAll(mainLine, blurredLine);
        }

        private Polyline newGlowLine() {
            Polyline polyLine = new Polyline();
            Double[] linePoints;
            System.out.println(Arrays.toString(points));
            if (mainShape.getPoints().size() == 8) linePoints = Arrays.copyOfRange(points, 0, 6);
            else linePoints = Arrays.copyOfRange(points, 0, 10);
            polyLine.getPoints().addAll(linePoints);

            polyLine.setFill(Color.TRANSPARENT);
            polyLine.setStrokeType(StrokeType.CENTERED);
            polyLine.setStroke(Color.web("#2bccbd"));
            polyLine.setStrokeWidth(2);

            return polyLine;
        }

        private void setText(String string) {
            Text text = new Text(string.toUpperCase());
            text.setFont(settingsModel.getKiona());
            text.setStyle("-fx-font-size: 16");
            text.setFill(Color.web("#2bccbd"));
            text.setFontSmoothingType(FontSmoothingType.GRAY);
            StackPane.setAlignment(text, Pos.CENTER_LEFT);
            StackPane.setMargin(text, new Insets(3, 0, 0, 60));

            getChildren().add(text);
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

    public void initModel(SettingsModel model) {
        this.settingsModel = model;
    }
}
