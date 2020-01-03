package AlphaTests.GuiTest;

import AlphaTests.GuiTest.Renderer.StartRendererPP;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class SwingInFxTest extends Application {

    private static final int GLCANVAS_WIDTH = 1280;
    private static final int GLCANVAS_HEIGHT = 720;
    private static final int FRAME_RATE = 60;

    @Override
    public void start(Stage stage) throws IOException {
        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent();

        StackPane rootPane = new StackPane();

        FXMLLoader cubeScanLoader = new FXMLLoader(getClass().getResource("./Gui/View.fxml"));
        rootPane.getChildren().add(cubeScanLoader.load());

        rootPane.getChildren().add(swingNode);

        stage.setScene(new Scene(rootPane, 200, 200));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        stage.getIcons().add(new Image("AlphaTests/GuiTest/taskbarIcon.png"));
        stage.getIcons().clear();
    }

    private void createAndSetSwingContent() {
        JFrame frame = new JFrame();
        frame.setType(Window.Type.UTILITY);

        GLProfile profile = GLProfile.get(GLProfile.GL3);
        GLCapabilities capabilities = new GLCapabilities(profile);

        GLCanvas canvas = new StartRendererPP(capabilities);
        canvas.setPreferredSize(new Dimension(GLCANVAS_WIDTH, GLCANVAS_HEIGHT));
        frame.add(canvas);

        final FPSAnimator animator = new FPSAnimator(canvas, FRAME_RATE, true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread(() -> {
                    if (animator.isStarted()) animator.stop();
                    System.exit(0);
                }).start();
            }
        });
        frame.setSize(GLCANVAS_WIDTH, GLCANVAS_HEIGHT);
        frame.setUndecorated(true);
        frame.setVisible(true);
        animator.start();

        canvas.requestFocusInWindow();
        }

    public static void main(String[] args) {
        launch(args);
    }
}