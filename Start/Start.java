package Start;

import Gui.Footer.Footer;
import Gui.Header.HeaderController;
import Gui.MainView.MainViewController;
import Gui.ResizeFrame.ResizeFrame;
import Gui.ScreenInformation;
import Gui.Settings.SettingsController;
import Models.ScreenInformationModel;
import Models.SettingsModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

public class Start extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        Start.primaryStage = stage;

        // Init Gui-----------------------------------------------------------------------------------------------------
        ScreenInformation screenInformation = new ScreenInformation();
        AnchorPane generalRoot = new AnchorPane();

        // Settings
        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("../Gui/Settings/SettingsView.fxml"));
        AnchorPane settingsPane = settingsLoader.load();
        settingsPane.getStylesheets().add("Gui/Settings/SettingsStyle.css");
        generalRoot.getChildren().add(settingsPane);
        AnchorPane.setTopAnchor(settingsPane, 23d);
        AnchorPane.setLeftAnchor(settingsPane, 0d);
        AnchorPane.setRightAnchor(settingsPane, 0d);
        AnchorPane.setBottomAnchor(settingsPane, 0d);

        // MainView
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("../Gui/MainView/MainViewView.fxml"));
        AnchorPane mainViewPane = mainViewLoader.load();
        mainViewPane.getStylesheets().add("Gui/MainView/MainViewStyle.css");
        generalRoot.getChildren().add(mainViewPane);
        AnchorPane.setTopAnchor(mainViewPane, 23d);
        AnchorPane.setLeftAnchor(mainViewPane, 0d);
        AnchorPane.setRightAnchor(mainViewPane, 0d);
        AnchorPane.setBottomAnchor(mainViewPane, 0d);

        // Header
        FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("../Gui/Header/HeaderView.fxml"));
        AnchorPane headerPane = headerLoader.load();
        headerPane.getStylesheets().add("Gui/Header/HeaderStyle.css");
        generalRoot.getChildren().add(headerPane);
        AnchorPane.setTopAnchor(headerPane, 0d);
        AnchorPane.setLeftAnchor(headerPane, 0d);
        AnchorPane.setRightAnchor(headerPane, 0d);

        // Footer
        Footer footer = new Footer();
        generalRoot.getChildren().add(footer);
        AnchorPane.setBottomAnchor(footer, 0d);
        AnchorPane.setLeftAnchor(footer, 0d);
        AnchorPane.setRightAnchor(footer, 0d);

        // DragFrame
        ResizeFrame dragFrame = new ResizeFrame();
        generalRoot.getChildren().add(dragFrame.getDragFramePane());


        // Init Models--------------------------------------------------------------------------------------------------
        ScreenInformationModel screenInformationModel = new ScreenInformationModel();
        SettingsModel settingsModel = new SettingsModel();

        HeaderController headerController = headerLoader.getController();
        SettingsController settingsController = settingsLoader.getController();
        MainViewController mainViewController = mainViewLoader.getController();

        dragFrame.initModel(screenInformationModel);
        headerController.initModel(screenInformationModel);
        footer.initModel(settingsModel);
        settingsController.initModel(settingsModel);
        mainViewController.initModel(settingsModel);

        screenInformationModel.addObserver(dragFrame);
        screenInformationModel.addObserver(headerController);
        screenInformationModel.addObserver(screenInformation);
        settingsModel.addObserver(footer);
        settingsModel.addObserver(settingsController);
        settingsModel.addObserver(mainViewController);

        screenInformation.initModel(screenInformationModel);

        // Init ScreenInformation---------------------------------------------------------------------------------------
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        screenInformationModel.setScreenWidth(dimension.width);
        screenInformationModel.setScreenHeight(dimension.height);

        // Init Scene---------------------------------------------------------------------------------------------------
        Scene scene = new Scene(generalRoot, Color.TRANSPARENT);
        ScreenInformation.setStageSize(1640, 860);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load Fonts
        final Font kiona = Font.loadFont(getClass().getResource("../Resources/Fonts/Kiona-Regular.ttf").toExternalForm(), 20);
        settingsModel.setKiona(kiona);

        screenInformationModel.toggleFullScreen();
        screenInformationModel.alignResizeLines();
        settingsModel.initFooter();
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(("../Resources/Assets/taskbarIcon.png"))));
        primaryStage.getIcons().size();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
