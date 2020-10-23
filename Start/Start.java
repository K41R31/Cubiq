package Start;

import Gui.Footer.Footer;
import Gui.Header.HeaderController;
import Gui.MainView.MainViewController;
import Gui.ResizeFrame.ResizeFrame;
import Gui.ScreenInformation;
import Gui.Settings.SettingsController;
import Models.ScreenInformationModel;
import Models.GuiModel;
import Processing.ScanCube;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.opencv.core.Core;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Start extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        Start.primaryStage = stage;

        // Load library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

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

        // Init ScanCube
        ScanCube scanCube = new ScanCube();

        // Init Models--------------------------------------------------------------------------------------------------
        ScreenInformationModel screenInformationModel = new ScreenInformationModel();
        GuiModel guiModel = new GuiModel();

        HeaderController headerController = headerLoader.getController();
        SettingsController settingsController = settingsLoader.getController();
        MainViewController mainViewController = mainViewLoader.getController();

        dragFrame.initModel(screenInformationModel);
        headerController.initModels(screenInformationModel, guiModel);
        footer.initModel(guiModel);
        scanCube.initModel(guiModel);
        settingsController.initModel(guiModel);
        mainViewController.initModel(guiModel);

        screenInformationModel.addObserver(dragFrame);
        screenInformationModel.addObserver(headerController);
        screenInformationModel.addObserver(screenInformation);
        guiModel.addObserver(scanCube);
        guiModel.addObserver(footer);
        guiModel.addObserver(settingsController);
        guiModel.addObserver(mainViewController);

        screenInformation.initModel(screenInformationModel);

        // Init ScreenInformation---------------------------------------------------------------------------------------
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        screenInformationModel.setScreenWidth(dimension.width);
        screenInformationModel.setScreenHeight(dimension.height);

        /*

        //TODO
        // 0 white;
        // 1 green,
        // 2 red;
        // 3 orange;
        // 4 blue;
        // 5 yellow
        List<int[][]> colorScheme = new ArrayList<>();
        colorScheme.add(new int[][] {
                {2, 5, 3},
                {4, 0, 1},
                {0, 5, 4}
        });
        colorScheme.add(new int[][] {
                {5, 1, 3},
                {2, 4, 3},
                {2, 0, 1}
        });
        colorScheme.add(new int[][] {
                {1, 4, 2},
                {4, 5, 0},
                {3, 2, 1}
        });
        colorScheme.add(new int[][] {
                {5, 0, 4},
                {4, 2, 3},
                {1, 2, 0}
        });
        colorScheme.add(new int[][] {
                {0, 5, 4},
                {3, 1, 5},
                {5, 1, 3}
        });
        colorScheme.add(new int[][] {
                {0, 0, 2},
                {2, 3, 3},
                {5, 1, 4}
        });

        BuildCube buildCube = new BuildCube(colorScheme);

        guiModel.setColorScheme(colorScheme);

        // Init cube renderer
        CubeRendererWindow cubeRendererWindow = new CubeRendererWindow(guiModel);
         */

        // Init Scene---------------------------------------------------------------------------------------------------
        Scene scene = new Scene(generalRoot, Color.TRANSPARENT);
        // Set the size of the window when toggling full screen
        ScreenInformation.setStageSize(screenInformationModel.getScreenWidth() * 0.8, screenInformationModel.getScreenHeight() * 0.8);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load Fonts
        Font bender = Font.loadFont(getClass().getResource("../Resources/Fonts/Bender-Light.ttf").toExternalForm().replace("%20", " "), 20);
        guiModel.setBender(bender);

        screenInformationModel.toggleFullScreen();
        screenInformationModel.alignResizeLines();
        guiModel.initFooter();
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../Resources/Assets/taskbarIcon.png")));

        guiModel.startCubeScan();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
