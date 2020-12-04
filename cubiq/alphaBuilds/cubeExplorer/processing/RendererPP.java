package cubiq.alphaBuilds.cubeExplorer.processing;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.javafx.NewtCanvasJFX;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.math.Quaternion;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.PMVMatrix;
import cubiq.alphaBuilds.cubeExplorer.io.InteractionHandlerPP;
import cubiq.alphaBuilds.cubeExplorer.model.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Observable;
import java.util.Observer;

import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_LESS;

public class RendererPP implements GLEventListener, Observer {

    private Model model;
    private GLWindow glWindow;
    private InteractionHandlerPP interactionHandler;
    private CubiePP cubie;

    private ShaderProgram shaderProgram;

    // Pointers (names) for data transfer and handling on GPU
    private int[] vaoName;  // Names of vertex array objects
    private int[] vboName;	// Names of vertex buffer objects
    private int[] iboName;	// Names of index buffer objects

    // Declaration for using the projection-model-view matrix tool
    PMVMatrix pmvMatrix;


    public RendererPP() {
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
        glCanvas.setWidth(700);
        glCanvas.setHeight(700);
        model.getRendererPane().getChildren().add(glCanvas);

        FPSAnimator animator = new FPSAnimator(glWindow, 60, true);
        animator.start();

        interactionHandler = new InteractionHandlerPP();

        glWindow.addMouseListener(interactionHandler);
        glWindow.addGLEventListener(this);
    }

    private void initCubie(GL3 gl) {
        // Create a cubie
        cubie = new CubiePP(1, new float[]{0, 0, 0});

        gl.glBindVertexArray(vaoName[0]);
        // Shader program
        shaderProgram = new ShaderProgram(gl);
        shaderProgram.loadShaderAndCreateProgram("resources\\shaders\\",
                "O0_Basic.vert", "O0_Basic.frag");

        // activate and initialize vertex buffer object (VBO)
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[0]);
        // floats use 4 bytes in Java
        gl.glBufferData(GL.GL_ARRAY_BUFFER, cubie.getVertices().length * 4,
                FloatBuffer.wrap(cubie.getVertices()), GL.GL_STATIC_DRAW);

        // activate and initialize index buffer object (IBO)
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, iboName[0]);
        // integers use 4 bytes in Java
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, cubie.getIndices().length * 4,
                IntBuffer.wrap(cubie.getIndices()), GL.GL_STATIC_DRAW);

        // Activate and order vertex buffer object data for the vertex shader
        // The vertex buffer contains: position (3), color (3)
        // Defining input for vertex shader
        // Pointer for the vertex shader to the position information per vertex
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6* Float.BYTES, 0);
        // Pointer for the vertex shader to the color information per vertex
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6* Float.BYTES, 3* Float.BYTES);
    }

    private void displayCubie(GL3 gl) {
        gl.glUseProgram(shaderProgram.getShaderProgramID());
        // Transfer the PVM-Matrix (model-view and projection matrix) to the vertex shader
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glBindVertexArray(vaoName[0]);
        // Draws the elements in the order defined by the index buffer object (IBO)
        gl.glDrawElements(GL.GL_TRIANGLE_STRIP, cubie.getIndices().length, GL.GL_UNSIGNED_INT, 0);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // Retrieve the OpenGL graphics context
        GL3 gl = drawable.getGL().getGL3();

        // BEGIN: Preparing scene
        // BEGIN: Allocating vertex array objects and buffers for each object
        int noOfObjects = 1;
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

        // Initialize objects to be drawn (see respective sub-methods)
        initCubie(gl);
        // END: Preparing scene

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
        gl.glClearColor(0.04f, 0.07f, 0.12f, 1.0f);
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
        interactionHandler.setWindowHeight(height);

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
//        interactionHandler.nextFrame();

        // Apply view transform using the PMV-Tool
        // Camera positioning is steered by the interaction handler
        pmvMatrix.glLoadIdentity();
        pmvMatrix.gluLookAt(-9.5f, 6.1f, 9.5f, 0f, 0f, 0f, 0f, 1.0f, 0f);

        Quaternion quaternion0 = new Quaternion();
        quaternion0.setFromEuler((float)Math.toRadians(45), (float)Math.toRadians(0), (float)Math.toRadians(0));

        Quaternion quaternion1 = new Quaternion();
        quaternion1.setFromEuler((float)Math.toRadians(0), (float)Math.toRadians(0), (float)Math.toRadians(0));

        Quaternion quaternion2;
        quaternion2 = quaternion0.mult(quaternion1);

        pmvMatrix.glPushMatrix();
        pmvMatrix.glRotate(quaternion2);
        displayCubie(gl);
        pmvMatrix.glPopMatrix();
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
