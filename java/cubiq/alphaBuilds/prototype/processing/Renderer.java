package cubiq.alphaBuilds.prototype.processing;

import cubiq.alphaBuilds.prototype.io.InteractionHandler;
import cubiq.alphaBuilds.prototype.model.Model;
import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.javafx.NewtCanvasJFX;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import static com.jogamp.opengl.GL.*;

public class Renderer implements Observer {

    private Model model;
    private GLU glu;
    private GLWindow glWindow;
    private InteractionHandler interactionHandler;
    private final float[] rotation = new float[] {0f, 0f, 0f};
    private final int[] startRotation = new int[] {-90,-90, -90};
    private int frame;
    
    public Renderer() {
        createGLWindow();
    }
    
    private void createGLWindow() {
        Display jfxNewtDisplay = NewtFactory.createDisplay(null, false);
        Screen screen = NewtFactory.createScreen(jfxNewtDisplay, 0);
        GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));

        // Enable FSAA (full screen antialiasing)
        caps.setSampleBuffers(true);
        caps.setNumSamples(4);

        glWindow = GLWindow.create(screen, caps);
    }
    
    private void startRenderer() {

        Cubie[] cubies = new Cubie[27];

        int counter = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    cubies[counter] = new Cubie(x-1, y-1, z-1, new int[]{0, 0});
                    counter++;
                }
            }
        }
        // TODO TESTEN !!!!!!!!!!!!
        System.out.println(Arrays.toString(cubies[8].getPosition()));
        cubies[8].rotateCubie(2, 1);
        System.out.println(Arrays.toString(cubies[8].getPosition()));


        NewtCanvasJFX glCanvas = new NewtCanvasJFX(glWindow);
        glCanvas.setWidth(600);
        glCanvas.setHeight(700);
        model.getRendererPane().getChildren().add(glCanvas);

        FPSAnimator animator = new FPSAnimator(glWindow, 60, true);
        animator.start();

        interactionHandler = new InteractionHandler();

        glWindow.addMouseListener(interactionHandler);
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
                glu.gluPerspective(30f, aspectRatio, 0.1, 1000f);
                gl.glMatrixMode(gl.GL_MODELVIEW);
            }

            public void display(GLAutoDrawable drawable) {
                GL2 gl = drawable.getGL().getGL2();
                gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

                gl.glLoadIdentity();

                // Defines the position of the camera
                glu.gluLookAt(-9.5f, 6.1f, 9.5f, 0f, 0f, 0f, 0f, 1.0f, 0f);

                // Rotation button action
                for (int i = 0; i < 3; i++) {
                    if (rotation[i] < startRotation[i] + 90) {
                        rotation[i] = easeOut(frame, startRotation[i], 90, 30);
                        frame++;
                    }
                }

                gl.glRotatef(rotation[0], 1f, 0f, 0f);
                gl.glRotatef(rotation[1], 0f, 1f, 0f);
                gl.glRotatef(rotation[2], 0f, 0f, 1f);

                gl.glRotatef(interactionHandler.getAngleXaxis(), 1f, 0f, 0f);
                gl.glRotatef(interactionHandler.getAngleYaxis(), 0f, 1f, 0f);


                // Offset to center the cube in the scene
                gl.glTranslatef(-1f, -1f, -1f);

                int[][] colors = mirrorSide(model.getNormalizedColors());

                for (int z = 0; z < 3; z++) {
                    for (int y = 0; y < 3; y++) {
                        for (int x = 0; x < 3; x++) {

                            float hCW = 0.5f; // Half the width of the cubies (half cubie width)
                            float sCO = 0.52f; // The offset from the stickers to the cubie center (sticker center offset)
                            float hSO = 0.45f; // Half the width of the inner sticker (half sticker outer)
                            float hSI = 0.35f; // Half the width of the outer sticker (half sticker inner)

                            // Cubie shell
                            gl.glBegin(GL.GL_TRIANGLE_STRIP); // TODO QUAD STRIP
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

                            // Stickers
                            // X
                            if (x == 0) {
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3f(0f, 0.62f, 0.33f);
                                gl.glVertex3f(-sCO, -hSO, -hSO);
                                gl.glVertex3f(-sCO, -hSO, hSO);
                                gl.glVertex3f(-sCO, hSO, -hSO);
                                gl.glVertex3f(-sCO, hSO, hSO);
                                gl.glEnd();
                            }

                            if (x == 2) {
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3f(0.24f, 0.51f, 0.96f);
                                gl.glVertex3f(sCO, -hSO, hSO);
                                gl.glVertex3f(sCO, -hSO, -hSO);
                                gl.glVertex3f(sCO, hSO, hSO);
                                gl.glVertex3f(sCO, hSO, -hSO);
                                gl.glEnd();
                            }

                            // Y
                            if (y == 0) {
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3f(1f, 1f, 1f);
                                gl.glVertex3f(hSO, -sCO, hSO);
                                gl.glVertex3f(-hSO, -sCO, hSO);
                                gl.glVertex3f(hSO, -sCO, -hSO);
                                gl.glVertex3f(-hSO, -sCO, -hSO);
                                gl.glEnd();
                            }

                            if (y == 2) {
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3f(0.99f, 0.8f, 0.03f);
                                gl.glVertex3f(-hSO, sCO, hSO);
                                gl.glVertex3f(hSO, sCO, hSO);
                                gl.glVertex3f(-hSO, sCO, -hSO);
                                gl.glVertex3f(hSO, sCO, -hSO);
                                gl.glEnd();
                            }

                            // Z
                            if (z == 0) {
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3f(0.86f, 0.26f, 0.18f);
                                gl.glVertex3f(hSO, -hSO, -sCO);
                                gl.glVertex3f(-hSO, -hSO, -sCO);
                                gl.glVertex3f(hSO, hSO, -sCO);
                                gl.glVertex3f(-hSO, hSO, -sCO);
                                gl.glEnd();
                            }

                            if (z == 2) {
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3fv(convert2Rgb(colors[x][y]), 0);
                                gl.glVertex3f(-hSI, -hSI, sCO);
                                gl.glVertex3f(hSI, -hSI, sCO);
                                gl.glVertex3f(-hSI, hSI, sCO);
                                gl.glVertex3f(hSI, hSI, sCO);
                                gl.glEnd();

                                // LEFT ADDON
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3fv(convert2Rgb(colors[x][y]), 0);
                                gl.glVertex3f(-hSO, -hSI, sCO);
                                gl.glVertex3f(-hSI, -hSI, sCO);
                                gl.glVertex3f(-hSO, hSI, sCO);
                                gl.glVertex3f(-hSI, hSI, sCO);
                                gl.glEnd();

                                // RIGHT ADDON
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3fv(convert2Rgb(colors[x][y]), 0);
                                gl.glVertex3f(hSI, -hSI, sCO);
                                gl.glVertex3f(hSO, -hSI, sCO);
                                gl.glVertex3f(hSI, hSI, sCO);
                                gl.glVertex3f(hSO, hSI, sCO);
                                gl.glEnd();

                                // TOP ADDON
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3fv(convert2Rgb(colors[x][y]), 0);
                                gl.glVertex3f(-hSI, hSI, sCO);
                                gl.glVertex3f(hSI, hSI, sCO);
                                gl.glVertex3f(-hSI, hSO, sCO);
                                gl.glVertex3f(hSI, hSO, sCO);
                                gl.glEnd();

                                // BOTTOM ADDON
                                gl.glBegin(GL.GL_TRIANGLE_STRIP);
                                gl.glColor3fv(convert2Rgb(colors[x][y]), 0);
                                gl.glVertex3f(-hSI, -hSO, sCO);
                                gl.glVertex3f(hSI, -hSO, sCO);
                                gl.glVertex3f(-hSI, -hSI, sCO);
                                gl.glVertex3f(hSI, -hSI, sCO);
                                gl.glEnd();

                                float[] tFP = {0.35f, 0.45f, 0.38f, 0.45f, 0.40f, 0.44f, 0.42f, 0.43f}; // Triangle fan points

                                // TOP-LEFT ADDON
                                gl.glBegin(GL_TRIANGLE_FAN);
                                gl.glColor3fv(convert2Rgb(colors[x][y]), 0);
                                gl.glVertex3f(-0.35f, 0.35f, sCO);
                                gl.glVertex3f(-tFP[0], tFP[1], sCO);
                                gl.glVertex3f(-tFP[2], tFP[3], sCO);
                                gl.glVertex3f(-tFP[4], tFP[5], sCO);
                                gl.glVertex3f(-tFP[6], tFP[7], sCO);
                                gl.glVertex3f(-tFP[7], tFP[6], sCO);
                                gl.glVertex3f(-tFP[5], tFP[4], sCO);
                                gl.glVertex3f(-tFP[3], tFP[2], sCO);
                                gl.glVertex3f(-tFP[1], tFP[0], sCO);
                                gl.glEnd();

                                // TOP-RIGHT ADDON
                                gl.glBegin(GL_TRIANGLE_FAN);
                                gl.glColor3fv(convert2Rgb(colors[x][y]), 0);
                                gl.glVertex3f(0.35f, 0.35f, sCO);
                                gl.glVertex3f(tFP[1], tFP[0], sCO);
                                gl.glVertex3f(tFP[3], tFP[2], sCO);
                                gl.glVertex3f(tFP[5], tFP[4], sCO);
                                gl.glVertex3f(tFP[7], tFP[6], sCO);
                                gl.glVertex3f(tFP[6], tFP[7], sCO);
                                gl.glVertex3f(tFP[4], tFP[5], sCO);
                                gl.glVertex3f(tFP[2], tFP[3], sCO);
                                gl.glVertex3f(tFP[0], tFP[1], sCO);
                                gl.glEnd();

                                // BOTTOM-LEFT ADDON
                                gl.glBegin(GL_TRIANGLE_FAN);
                                gl.glColor3fv(convert2Rgb(colors[x][y]), 0);
                                gl.glVertex3f(-0.35f, -0.35f, sCO);
                                gl.glVertex3f(-tFP[1], -tFP[0], sCO);
                                gl.glVertex3f(-tFP[3], -tFP[2], sCO);
                                gl.glVertex3f(-tFP[5], -tFP[4], sCO);
                                gl.glVertex3f(-tFP[7], -tFP[6], sCO);
                                gl.glVertex3f(-tFP[6], -tFP[7], sCO);
                                gl.glVertex3f(-tFP[4], -tFP[5], sCO);
                                gl.glVertex3f(-tFP[2], -tFP[3], sCO);
                                gl.glVertex3f(-tFP[0], -tFP[1], sCO);
                                gl.glEnd();

                                // BOTTOM-RIGHT ADDON
                                gl.glBegin(GL_TRIANGLE_FAN);
                                gl.glColor3fv(convert2Rgb(colors[x][y]), 0);
                                gl.glVertex3f(0.35f, -0.35f, sCO);
                                gl.glVertex3f(tFP[0], -tFP[1], sCO);
                                gl.glVertex3f(tFP[2], -tFP[3], sCO);
                                gl.glVertex3f(tFP[4], -tFP[5], sCO);
                                gl.glVertex3f(tFP[6], -tFP[7], sCO);
                                gl.glVertex3f(tFP[7], -tFP[6], sCO);
                                gl.glVertex3f(tFP[5], -tFP[4], sCO);
                                gl.glVertex3f(tFP[3], -tFP[2], sCO);
                                gl.glVertex3f(tFP[1], -tFP[0], sCO);
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

    private void drawSticker(GL2 gl, int axis) { // X = 0, Y = 1, Z = 2
        /*
        if (z == 0) {
            gl.glBegin(GL.GL_TRIANGLE_STRIP);
            gl.glColor3f(0.86f, 0.26f, 0.18f);
            gl.glVertex3f(hSO, -hSO, -sCO);
            gl.glVertex3f(-hSO, -hSO, -sCO);
            gl.glVertex3f(hSO, hSO, -sCO);
            gl.glVertex3f(-hSO, hSO, -sCO);
            gl.glEnd();
         */

        
    }

    private int[][] mirrorSide(int[][] side) {
        int[][] mirroredSide = new int[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (y == 0) mirroredSide[x][y] = side[x][2];
                else if (y == 1) mirroredSide[x][y] = side[x][y];
                else mirroredSide[x][y] = side[x][0];
            }
        }
        return mirroredSide;
    }

    private float[] convert2Rgb(int normColor) {
        switch (normColor) {
            case 0:
                return new float[]{1.0f, 1.0f, 1.0f};
            case 1:
                return new float[]{0.0f, 0.62f, 0.33f};
            case 2:
                return new float[]{0.86f, 0.26f, 0.18f};
            case 3:
                return new float[]{1.0f, 0.42f, 0.0f};
            case 4:
                return new float[]{0.24f, 0.51f, 0.96f};
            case 5:
                return new float[]{0.99f, 0.8f, 0.03f};
            default:
                return null;
        }
    }

    /**
     * Function to calculate a ease out animation
     * Source: http://gizma.com/easing/
     * @param t current time
     * @param b cubiq.start value
     * @param c change in value
     * @param d duration
     * @return Eased value
     */
    private float easeOut(float t, float b, float c, float d) {
        t /= d;
        t--;
        return c*(t*t*t + 1) + b;
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "cubeFound":
                startRenderer();
                break;
            case "rotateCubeX":
                if (rotation[0] % 90 == 0) {
                    startRotation[0] = (int)rotation[0];
                    frame = 0;
                }
                break;
            case "rotateCubeY":
                if (rotation[1] % 90 == 0) {
                    startRotation[1] = (int)rotation[1];
                    frame = 0;
                }
                break;
            case "rotateCubeZ":
                if (rotation[2] % 90 == 0) {
                    startRotation[2] = (int)rotation[2];
                    frame = 0;
                }
                break;
        }
    }

    public void initModel(Model model) {
        this.model = model;
    }
}
