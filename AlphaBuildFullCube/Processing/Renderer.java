package AlphaBuildFullCube.Processing;

import AlphaBuildFullCube.Model.Model;
import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.javafx.NewtCanvasJFX;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import java.util.Observable;
import java.util.Observer;

import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_LESS;

public class Renderer implements Observer {

    private Model model;
    private GLU glu;
    private GLWindow glWindow;
    private float DISTANCE = 2.2f;
    private float rotateValue = 0.0f;
    private float rotation = 0f;
    
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

                // Switch on depth test
                gl.glEnable(GL_DEPTH_TEST);
                gl.glDepthFunc(GL_LESS);

                // Switch on back face culling
                gl.glEnable(GL.GL_CULL_FACE);
                gl.glCullFace(GL.GL_BACK);

                gl.glClearColor(0.04f, 0.07f, 0.12f, 1.0f);
            }

            public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
                GL2 gl = drawable.getGL().getGL2();

                if (height == 0) height = 1;
                gl.glViewport(0, 0, width, height);
                gl.glMatrixMode(gl.GL_PROJECTION);
                gl.glLoadIdentity();
                float aspectRatio = (float)width / (float)height;
                glu.gluPerspective(45f, aspectRatio, 0.1, 1000f);
                gl.glMatrixMode(gl.GL_MODELVIEW);
            }

            public void display(GLAutoDrawable drawable) {
                GL2 gl = drawable.getGL().getGL2();
                gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

                gl.glLoadIdentity();

                // Defines the position of the camera
                glu.gluLookAt(-5f, 5f, 5f, 0f, 0f, 0f, 0f, 1.0f, 0f);

                gl.glRotatef(rotation, 0f, 1f, 0f);
                rotation += 0.5f;

                gl.glTranslatef(0f, -2f, 0f);
                // Offset to center the cube in the scene
                gl.glTranslatef(-1f, 1f, -1f);


                for (int z = 0; z < 3; z++) {
                    for (int y = 0; y < 3; y++) {
                        for (int x = 0; x < 3; x++) {

                            float hCW = 0.5f; // Half the width of the cubies (half cubie width)
                            float sCO = 0.51f; // The offset from the stickers to the cubie center (sticker center offset)
                            float hSW = 0.45f; // Half the width of the stickers (half sticker width)

                            gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3f(0f, 0f, 0f);
                                gl.glVertex3f(hCW, -hCW, hCW);
                                gl.glVertex3f(-hCW, -hCW, hCW);
                                gl.glVertex3f(hCW, -hCW, -hCW);
                                gl.glVertex3f(-hCW, -hCW, -hCW);
                                gl.glVertex3f(-hCW, hCW, -hCW);
                                gl.glVertex3f(-hCW, -hCW, hCW);
                                gl.glVertex3f(-hCW, hCW, hCW);
                                gl.glVertex3f(hCW, -hCW, hCW);
                                gl.glVertex3f(hCW, hCW, hCW);
                                gl.glVertex3f(hCW, -hCW, -hCW);
                                gl.glVertex3f(hCW, hCW, -hCW);
                                gl.glVertex3f(-hCW, hCW, -hCW);
                                gl.glVertex3f(hCW, hCW, hCW);
                                gl.glVertex3f(-hCW, hCW, hCW);
                            gl.glEnd();

                            // X
                            if (x == 0) {
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                    gl.glColor3f(0f, 0.62f, 0.33f);
                                    gl.glVertex3f(-sCO, -hSW, -hSW);
                                    gl.glVertex3f(-sCO, -hSW, hSW);
                                    gl.glVertex3f(-sCO, hSW, -hSW);
                                    gl.glVertex3f(-sCO, hSW, hSW);
                                gl.glEnd();
                            }

                            if (x == 2) {
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3f(0.24f, 0.51f, 0.96f);
                                gl.glVertex3f(sCO, -hSW, hSW);
                                gl.glVertex3f(sCO, -hSW, -hSW);
                                gl.glVertex3f(sCO, hSW, hSW);
                                gl.glVertex3f(sCO, hSW, -hSW);
                                gl.glEnd();
                            }

                            // Y
                            if (y == 0) {
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3f(1f, 1f, 1f);
                                gl.glVertex3f(hSW, -sCO, hSW);
                                gl.glVertex3f(-hSW, -sCO, hSW);
                                gl.glVertex3f(hSW, -sCO, -hSW);
                                gl.glVertex3f(-hSW, -sCO, -hSW);
                                gl.glEnd();
                            }

                            if (y == 2) {
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3f(0.99f, 0.8f, 0.03f);
                                gl.glVertex3f(-hSW, sCO, hSW);
                                gl.glVertex3f(hSW, sCO, hSW);
                                gl.glVertex3f(-hSW, sCO, -hSW);
                                gl.glVertex3f(hSW, sCO, -hSW);
                                gl.glEnd();
                            }

                            // Z
                            if (z == 0) {
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3f(0.86f, 0.26f, 0.18f);
                                gl.glVertex3f(hSW, -hSW, -sCO);
                                gl.glVertex3f(-hSW, -hSW, -sCO);
                                gl.glVertex3f(hSW, hSW, -sCO);
                                gl.glVertex3f(-hSW, hSW, -sCO);
                                gl.glEnd();
                            }

                            if (z == 2) {
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3f(1f, 0.42f, 0f);
                                gl.glVertex3f(-hSW, -hSW, sCO);
                                gl.glVertex3f(hSW, -hSW, sCO);
                                gl.glVertex3f(-hSW, hSW, sCO);
                                gl.glVertex3f(hSW, hSW, sCO);
                                gl.glEnd();
                            }

                            gl.glTranslatef(1f, 0f, 0f);
                        }
                        gl.glTranslatef(-3f, 1f, 0f);
                    }
                    gl.glTranslatef(0f, -3f, 1f);
                }
            }

            public void dispose(final GLAutoDrawable drawable) {
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "cubeFound":
                startRenderer();
                break;
            case "newCubeSideFound":
                break;
        }
    }

    public void initModel(Model model) {
        this.model = model;
    }
}
