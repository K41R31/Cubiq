package cubeExplorer.processing;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.javafx.NewtCanvasJFX;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.PMVMatrix;
import cubeExplorer.cube.Cube;
import cubeExplorer.io.InteractionHandler;
import cubeExplorer.model.Model;

import java.util.Observable;
import java.util.Observer;

import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_LESS;

public class Renderer implements GLEventListener, Observer {

    private Model model;
    private final GLWindow glWindow;
    private Cube cube;
    private InteractionHandler interactionHandler;
    private final int DEVICE_WIDTH = 700;
    private final int DEVICE_HEIGHT = 700;
    private final float[] CAM_POS = new float[] {-9.5f, 6.1f, 9.5f};

    private ShaderProgram shaderProgram;

    // Pointers (names) for data transfer and handling on GPU
    private int[] vaoName;  // Names of vertex array objects
    private int[] vboName;	// Names of vertex buffer objects
    private int[] iboName;	// Names of index buffer objects

    // Declaration for using the projection-model-view matrix tool
    PMVMatrix pmvMatrix;


    public Renderer() {
        Display jfxNewtDisplay = NewtFactory.createDisplay(null, false);
        Screen screen = NewtFactory.createScreen(jfxNewtDisplay, 0);
        GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));

        // Enable FSAA (full screen antialiasing)
        caps.setSampleBuffers(true);
        caps.setNumSamples(4);

        glWindow = GLWindow.create(screen, caps);
    }

    private void startRenderer() {
        NewtCanvasJFX glCanvas = new NewtCanvasJFX(glWindow);

        glCanvas.setWidth(DEVICE_WIDTH);
        glCanvas.setHeight(DEVICE_HEIGHT);
        model.getRendererPane().getChildren().add(glCanvas);

        FPSAnimator animator = new FPSAnimator(glWindow, 60, true);
        animator.start();

        interactionHandler = new InteractionHandler(CAM_POS, DEVICE_WIDTH, DEVICE_HEIGHT);

        glWindow.addMouseListener(interactionHandler);
        glWindow.addGLEventListener(this);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // Retrieve the OpenGL graphics context
        GL3 gl = drawable.getGL().getGL3();

        // BEGIN: Preparing scene
        // BEGIN: Allocating vertex array objects and buffers for each object
        int noOfObjects = 81;
        // create vertex array objects for noOfObjects objects (VAO)
        vaoName = new int[noOfObjects];
        gl.glGenVertexArrays(noOfObjects, vaoName, 0);
        if (vaoName[0] < 1)
            System.err.println("Error allocating vertex array object (VAO).");

        // create vertex buffer objects for noOfObjects objects (VBO)
        vboName = new int[noOfObjects];
        gl.glGenBuffers(noOfObjects, vboName, 0);
        if (vboName[0] < 1)
            System.err.println("Error allocating vertex buffer object (VBO).");

        // create index buffer objects for noOfObjects objects (IBO)
        iboName = new int[noOfObjects];
        gl.glGenBuffers(noOfObjects, iboName, 0);
        if (iboName[0] < 1)
            System.err.println("Error allocating index buffer object.");
        // END: Allocating vertex array objects and buffers for each object

        // Initialize cubie
        cube = new Cube(3);
        cube.initCubies(gl, vaoName, vboName, iboName);

        // Shader program
        shaderProgram = new ShaderProgram(gl);
        shaderProgram.loadShaderAndCreateProgram(
                getClass().getResource("/cubeExplorer/shaders/").getPath().replace("%20", " "),
                "O0_Basic.vert", "O0_Basic.frag");

        interactionHandler.setCube(cube);


        // Create object for projection-model-view matrix calculation.
        pmvMatrix = new PMVMatrix();

        // Switch on depth test
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);

        // Switch on back face culling
        gl.glEnable(GL.GL_CULL_FACE);
        gl.glCullFace(GL.GL_BACK);

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, gl.GL_FILL);

        // Set background color of the GLCanvas.
        gl.glClearColor(0.098f, 0.106f, 0.114f, 1.0f);
    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * called when the OpenGL window is resized.
     * @param drawable The OpenGL drawable
     * @param x x-coordinate of the viewport
     * @param y y-coordinate of the viewport
     * @param width width of the viewport
     * @param height height of the viewport
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        // Retrieve the OpenGL graphics context
        GL3 gl = drawable.getGL().getGL3();

        // Set the viewport to the entire window
        gl.glViewport(0, 0, width, height);
        // Set the window size in the interactionHandler
        interactionHandler.setWindowWidth(width);

        // Switch the pmv-tool to perspective projection
        pmvMatrix.glMatrixMode(PMVMatrix.GL_PROJECTION);
        // Reset projection matrix to identity
        pmvMatrix.glLoadIdentity();

        // Calculate projection matrix
        //      Parameters:
        //          fovy (field of view), aspect ratio,
        //          zNear (near clipping plane), zFar (far clipping plane)
        float aspectRatio = (float)width / (float)height;
        pmvMatrix.gluPerspective(30f, aspectRatio, 0.1f, 1000f);

        // Switch to model-view transform
        pmvMatrix.glMatrixMode(PMVMatrix.GL_MODELVIEW);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        // Retrieve the OpenGL graphics context
        GL3 gl = drawable.getGL().getGL3();
        // Clear color and depth buffer
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

        // Tell the interactionHandler that the program has run one frame further
        interactionHandler.nextFrame();

        // Apply view transform using the PMV-Tool
        // Camera positioning is steered by the interaction handler
        pmvMatrix.glLoadIdentity();
        pmvMatrix.gluLookAt(CAM_POS[0], CAM_POS[1], CAM_POS[2], 0f, 0f, 0f, 0f, 1.0f, 0f);

        // Set matrices in the interaction handler
        float[] pMatrix = new float[16];
        System.arraycopy(pmvMatrix.glGetPMvMatrixf().array(), 0, pMatrix, 0, 16);
        interactionHandler.setProjectionMatrix(pMatrix);

        float[] mvMatrix = new float[16];
        System.arraycopy(pmvMatrix.glGetPMvMatrixf().array(), 16, mvMatrix, 0, 16);
        interactionHandler.setModelviewMatrix(mvMatrix);

        displayCubies(gl);
    }

    private void displayCubies(GL3 gl) {
        for (int i = 0; i < cube.getTotalCubies(); i++) {
            pmvMatrix.glPushMatrix();
            // Cubie rotation
            pmvMatrix.glRotate(cube.getCubieRotation(i));
            // Cubie translation
            float[] qbTransl = cube.getCubieTranslation(i);
            pmvMatrix.glTranslatef(qbTransl[0], qbTransl[1], qbTransl[2]);

            gl.glUseProgram(shaderProgram.getShaderProgramID());
            // Transfer the PVM-Matrix (model-view and projection matrix) to the vertex shader
            gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
            gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
            gl.glBindVertexArray(vaoName[i]);

            // Draws the elements in the order defined by the index buffer object (IBO)
            gl.glDrawElements(GL.GL_TRIANGLE_STRIP, cube.getCubieIndices(i).length, GL.GL_UNSIGNED_INT, 0);

            pmvMatrix.glPopMatrix();
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Retrieve the OpenGL graphics context
        GL3 gl = drawable.getGL().getGL3();

        // Detach and delete shader program
        gl.glUseProgram(0);
        shaderProgram.deleteShaderProgram();

        // deactivate VAO and VBO
        gl.glBindVertexArray(0);
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);
        gl.glDeleteVertexArrays(1, vaoName,0);
        gl.glDeleteBuffers(1, vboName, 0);

        System.exit(0);
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
            case "startRenderer":
                startRenderer();
                break;
        }
    }

    public void initModel(Model model) {
        this.model = model;
    }
}
