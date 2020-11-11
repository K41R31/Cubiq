package cubiq.start;

import cubiq.gui.footer.Footer;
import cubiq.gui.header.HeaderController;
import cubiq.gui.mainView.MainViewController;
import cubiq.gui.resizeFrame.ResizeFrame;
import cubiq.gui.ScreenInformation;
import cubiq.gui.settings.SettingsController;
import cubiq.io.FileChooser;
import cubiq.models.ScreenInformationModel;
import cubiq.models.GuiModel;
import cubiq.processing.ScanCube;
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

public class Start extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Load library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Init Gui-----------------------------------------------------------------------------------------------------
        ScreenInformation screenInformation = new ScreenInformation();
        AnchorPane generalRoot = new AnchorPane();

        // Settings
        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("../gui/settings/SettingsView.fxml"));
        AnchorPane settingsPane = settingsLoader.load();
        settingsPane.getStylesheets().add("cubiq/gui/settings/SettingsStyle.css");
        generalRoot.getChildren().add(settingsPane);
        AnchorPane.setTopAnchor(settingsPane, 23d);
        AnchorPane.setLeftAnchor(settingsPane, 0d);
        AnchorPane.setRightAnchor(settingsPane, 0d);
        AnchorPane.setBottomAnchor(settingsPane, 0d);

        // MainView
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("../gui/mainView/MainViewView.fxml"));
        AnchorPane mainViewPane = mainViewLoader.load();
        mainViewPane.getStylesheets().add("cubiq/gui/mainView/MainViewStyle.css");
        generalRoot.getChildren().add(mainViewPane);
        AnchorPane.setTopAnchor(mainViewPane, 23d);
        AnchorPane.setLeftAnchor(mainViewPane, 0d);
        AnchorPane.setRightAnchor(mainViewPane, 0d);
        AnchorPane.setBottomAnchor(mainViewPane, 0d);

        // Header
        FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("../gui/header/HeaderView.fxml"));
        AnchorPane headerPane = headerLoader.load();
        headerPane.getStylesheets().add("cubiq/gui/header/HeaderStyle.css");
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

        // Init FileChooser
        cubiq.io.FileChooser fileChooser = new FileChooser();

        // Init Models--------------------------------------------------------------------------------------------------
        ScreenInformationModel screenInformationModel = new ScreenInformationModel();
        GuiModel guiModel = new GuiModel();

        guiModel.setStage(stage);

        HeaderController headerController = headerLoader.getController();
        SettingsController settingsController = settingsLoader.getController();
        MainViewController mainViewController = mainViewLoader.getController();

        dragFrame.initScreenInformationModel(screenInformationModel);
        dragFrame.initGuiModel(guiModel);
        headerController.initModels(screenInformationModel, guiModel);
        footer.initModel(guiModel);
        scanCube.initModel(guiModel);
        settingsController.initModel(guiModel);
        mainViewController.initModel(guiModel);
        fileChooser.initModel(guiModel);
        screenInformation.initGuiModel(guiModel);

        screenInformationModel.addObserver(dragFrame);
        screenInformationModel.addObserver(headerController);
        screenInformationModel.addObserver(screenInformation);
        guiModel.addObserver(scanCube);
        guiModel.addObserver(footer);
        guiModel.addObserver(fileChooser);
        guiModel.addObserver(settingsController);
        guiModel.addObserver(mainViewController);
        guiModel.addObserver(headerController);
        guiModel.addObserver(dragFrame);

        screenInformation.initScreenInformationModel(screenInformationModel);

        // Init ScreenInformation---------------------------------------------------------------------------------------
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        screenInformationModel.setScreenWidth(dimension.width);
        screenInformationModel.setScreenHeight(dimension.height);

        // Init Scene---------------------------------------------------------------------------------------------------
        Scene scene = new Scene(generalRoot, Color.TRANSPARENT);
        screenInformationModel.callObservers("initStageSize");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

        // Load Fonts
        Font bender = Font.loadFont(getClass().getResource("../Resources/Fonts/Bender-Light.ttf").toExternalForm().replace("%20", " "), 20);
        guiModel.setBender(bender);

        screenInformationModel.callObservers("toggleFullScreen");
        guiModel.callObservers("initializeHeaderController");
        guiModel.callObservers("initFooter");
        guiModel.callObservers("initResizeLines");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("../Resources/Assets/taskbarIcon.png")));

        /*
        if (result.contains("Error"))
            switch (result.charAt(result.length() - 1)) {
                case '1':
                    result = "There are not exactly nine facelets of each color!";
                    break;
                case '2':
                    result = "Not all 12 edges exist exactly once!";
                    break;
                case '3':
                    result = "Flip error: One edge has to be flipped!";
                    break;
                case '4':
                    result = "Not all 8 corners exist exactly once!";
                    break;
                case '5':
                    result = "Twist error: One corner has to be twisted!";
                    break;
                case '6':
                    result = "Parity error: Two corners or two edges have to be exchanged!";
                    break;
                case '7':
                    result = "No solution exists for the given maximum move number!";
                    break;
                case '8':
                    result = "Timeout, no solution found within given maximum time!";
                    break;
            }
        System.out.println("RESULT: " + result);
         */


        guiModel.callObservers("startWebcamLoop");
        //guiModel.callObservers("startLoadedImagesLoop");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
