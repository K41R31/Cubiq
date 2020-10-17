package AlphaBuild.Processing;

import AlphaBuild.Model.Model;
import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.javafx.NewtCanvasJFX;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Renderer implements Observer {

    private Model model;
    private GLU glu;
    private GLWindow glWindow;
    private float DISTANCE = 2.2f;
    
    public Renderer() {
        createGLWindow();
    }
    
    private void createGLWindow() {
        Display jfxNewtDisplay = NewtFactory.createDisplay(null, false);
        Screen screen = NewtFactory.createScreen(jfxNewtDisplay, 0);
        GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
        glWindow = GLWindow.create(screen, caps);
    }
    
    private void startRenderer() {
        NewtCanvasJFX glCanvas = new NewtCanvasJFX(glWindow);
        glCanvas.setWidth(600);
        glCanvas.setHeight(700);
        model.getRendererPane().getChildren().add(glCanvas);

        FPSAnimator animator = new FPSAnimator(glWindow, 60, true);
        animator.start();

        glWindow.addGLEventListener(new GLEventListener() {

            public void init(GLAutoDrawable drawable) {
                GL2 gl = glWindow.getGL().getGL2();

                glu = new GLU();

                gl.glClearColor(0.04f, 0.07f, 0.12f, 1.0f);
            }

            public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
                GL2 gl = drawable.getGL().getGL2();

                if (height == 0) height = 1;
                gl.glViewport(0, 0, width, height);
                gl.glMatrixMode(gl.GL_PROJECTION);
                gl.glLoadIdentity();
                float aspectRatio = (float)width / (float)height;
                glu.gluPerspective(50.0, aspectRatio, 0.1, 1000.0);
                gl.glMatrixMode(gl.GL_MODELVIEW);
            }

            public void display(GLAutoDrawable drawable) {
                GL2 gl = drawable.getGL().getGL2();
                gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

                gl.glLoadIdentity();

                glu.gluLookAt(0f, 0f, 2f,
                        0f, 0f, 0f,
                        0f, 1.0f, 0f);

                gl.glTranslatef(-2f, 2f, -12f);

                int[][] normColors = model.getNormalizedColors();
                List<float[]> rgbColors = convert2Rgb(normColors);

                for (int i = 0; i < 9; i++) {
                    gl.glBegin(GL2.GL_QUADS);
                    gl.glColor4f(rgbColors.get(i)[0], rgbColors.get(i)[1], rgbColors.get(i)[2], 1f);
                    gl.glVertex3f(-1.0f, 1.0f, 0.0f);      // Top left
                    gl.glVertex3f(1.0f, 1.0f, 0.0f);       // Top right
                    gl.glVertex3f(1.0f, -1.0f, 0.0f);      // Bottom right
                    gl.glVertex3f(-1.0f, -1.0f, 0.0f);     // Bottom left
                    gl.glEnd();

                    if (i == 2 || i == 5)
                        gl.glTranslatef(-(DISTANCE * 2), -DISTANCE, 0.0f);
                    else
                        gl.glTranslatef(DISTANCE, 0.0f, 0.0f);
                }
            }

            public void dispose(final GLAutoDrawable drawable) {
            }
        });
    }

    private List<float[]> convert2Rgb(int[][] normColors) {
        List<float[]> rgbColors = new ArrayList<>();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                switch (normColors[x][y]) {
                    case 0:
                        rgbColors.add(new float[]{0.95f, 0.95f, 0.91f});
                        break;
                    case 1:
                        rgbColors.add(new float[]{0.17f, 0.80f, 0.16f});
                        break;
                    case 2:
                        rgbColors.add(new float[]{0.87f, 0.14f, 0.14f});
                        break;
                    case 3:
                        rgbColors.add(new float[]{0.84f, 0.50f, 0.12f});
                        break;
                    case 4:
                        rgbColors.add(new float[]{0.15f, 0.68f, 0.82f});
                        break;
                    case 5:
                        rgbColors.add(new float[]{0.87f, 0.86f, 0.14f});
                }
            }
        }
        return rgbColors;
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "cubeFound":
                startRenderer();
                break;
        }
    }

    public void initModel(Model model) {
        this.model = model;
    }
}
