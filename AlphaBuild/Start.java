package AlphaBuild;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.javafx.NewtCanvasJFX;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.Animator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.opencv.core.Core;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;

public class Start extends Application {

    private Animator animator;

    @Override
    public void start(Stage stage) throws Exception {
        // Load library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Init View
        FXMLLoader viewLoader = new FXMLLoader(getClass().getResource("View.fxml"));
        AnchorPane viewPane = viewLoader.load();

        // Init Image Processing
        ImageProcessing imageProcessing = new ImageProcessing();

        // Init Model
        Model model = new Model();

        Controller controller = viewLoader.getController();

        controller.initModel(model);
        imageProcessing.initModel(model);

        model.addObserver(controller);
        model.addObserver(imageProcessing);

        model.setStage(stage);

        // Init Scene---------------------------------------------------------------------------------------------------
        Scene scene = new Scene(viewPane);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

        createNewtDisplay(viewPane);
    }

    private void createNewtDisplay(AnchorPane viewPane) {
        Platform.runLater(() -> {
            Display jfxNewtDisplay = NewtFactory.createDisplay(null, false);
            Screen screen = NewtFactory.createScreen(jfxNewtDisplay, 0);
            GLCapabilities caps = new GLCapabilities(GLProfile.getMaxFixedFunc(true));
            GLWindow glWindow1 = GLWindow.create(screen, caps);

            NewtCanvasJFX glCanvas = new NewtCanvasJFX(glWindow1);
            glCanvas.setWidth(800);
            glCanvas.setHeight(600);
            viewPane.getChildren().add(glCanvas);
            animator = new Animator(glWindow1);
            animator.start();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
