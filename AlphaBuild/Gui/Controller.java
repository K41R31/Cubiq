package AlphaBuild.Gui;

import AlphaBuild.Model.Model;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {

    private Model model;
    @FXML
    private HBox menuItemsPane;
    @FXML
    private Text titel, buildInfo;


    private void generateMenu() {
        StackPane menuItem0 = new MenuItem("use webcam");
        StackPane menuItem1 = new MenuItem("load image");
        StackPane menuItem2 = new MenuItem("exit");

        titel.setFont(model.getKionaRegular());
        titel.setStyle("-fx-font-size: 30");
        buildInfo.setFont(model.getKionaRegular());
        buildInfo.setStyle("-fx-font-size: 20");

        menuItemsPane.getChildren().addAll(menuItem0, menuItem1, menuItem2);
    }

    @FXML
    private void extendView() {
        // Get the screen size
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        // Get the stage from the model
        Stage stage = model.getStage();

        // Set the size of the application
        stage.setWidth(1000);
        stage.setHeight(800);

        // Center the application
        stage.setX((dimension.getWidth() - stage.getWidth()) / 2);
        stage.setY((dimension.getHeight() - stage.getHeight()) / 2);
    }

    @FXML
    private void startRenderer() {
        model.callObservers("startRenderer");
        // TODO Renderer mit Farwerten starten
    }

    // Inner class, that generates custom styled buttons
    private class MenuItem extends StackPane {
        private String name;
        private int width;
        private int height = 50;
        private int skewValue = 10;

        /**
         * Generates a styled button with representative functions inside a stackpane
         * @param name The name of the button. It will appear at the button label and will help to choose the right
         *             button actions. The only valid values are: "use webcam", "load image" and "exit"
         */
        public MenuItem(String name) {
            this.name = name;
            if (!name.equals("exit")) width = 200;
            else width = 100;

            initRootPane();
            Text title = generateText();
            Polygon polyBorder = generatePolyBorder(title);

            this.getChildren().addAll(polyBorder, title);
        }

        private void initRootPane() {
            setMinWidth(USE_PREF_SIZE);
            setMinHeight(USE_PREF_SIZE);
            setPrefWidth(width);
            setPrefHeight(height);
            setMaxWidth(USE_PREF_SIZE);
            setMaxHeight(USE_PREF_SIZE);
            setAlignment(Pos.CENTER);
        }

        private Polygon generatePolyBorder(Text title) {
            Polygon polyBorder = new Polygon(0, 0, skewValue, -height, width, -height, width - skewValue, 0);
            polyBorder.setFill(Color.TRANSPARENT);
            polyBorder.setStroke(Color.web("#64c4c0"));

            polyBorder.setOnMouseEntered(event -> {
                polyBorder.setFill(Color.web("#64c4c0"));
                title.setFill(Color.web("#0b1320"));
            });

            polyBorder.setOnMouseExited(event -> {
                polyBorder.setFill(Color.TRANSPARENT);
                title.setFill(Color.web("#64c4c0"));
            });

            switch (name) {
                case "use webcam":
                    polyBorder.setOnMouseClicked(event -> System.out.println("noch nicht verfügbar."));
                    break;
                case "load image":
                    polyBorder.setOnMouseClicked(event -> {
                        model.callObservers("loadImage");
                        model.callObservers("image loaded");
                    });
                    break;
                case "exit":
                    polyBorder.setOnMouseClicked(event -> System.exit(0));
                    break;
            }

            return polyBorder;
        }

        private Text generateText() {
            Text title = new Text();
            title.setText(name);
            title.setFont(model.getKionaItalic());
            title.setStyle("-fx-font-size: 17");
            title.setFontSmoothingType(FontSmoothingType.GRAY);
            title.setFill(Color.web("#64c4c0"));
            title.setDisable(true);
            return title;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "guiInitialized":
                generateMenu();
                break;
        }
    }

    public void initModel(Model model) {
        this.model = model;
    }
}
