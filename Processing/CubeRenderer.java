package Processing; /**
 * Copyright 2012-2013 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.PMVMatrix;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

/**
 * Performs the OpenGL graphics processing using the Programmable Pipeline and the
 * OpenGL Core profile
 *
 * Starts an animation loop.
 * Zooming and rotation of the Camera is included (see AlphaTests.JoglShapesPP.InteractionHandler).
 * 	Use: left/right/up/down-keys and +/-Keys and mouse
 * Enables drawing of simple shapes: box, sphere, cone (frustum) and roof
 * Serves as a template (start code) for setting up an OpenGL/Jogl application
 * using a vertex and fragment shader.
 *
 * Please make sure setting the file path and names of the shader correctly (see below).
 *
 * Core code is based on a tutorial by Chua Hock-Chuan
 * http://www3.ntu.edu.sg/home/ehchua/programming/opengl/JOGL2.0.html
 *
 * and on an example by Xerxes Rånby
 * http://jogamp.org/git/?p=jogl-demos.git;a=blob;f=src/demos/es2/RawGL2ES2demo.java;hb=HEAD
 *
 * @author Karsten Lehn
 * @version 22.10.2017
 *
 */
public class CubeRenderer extends GLCanvas implements GLEventListener {

    private static final long serialVersionUID = 1L;

    private List<int[][]> colorScheme;

    // taking shader source code files from relative path
    private final String shaderPath = "src/Resources/Shader/";
    // Shader for object 0
    private final String vertexShader0FileName = "O0_Basic.vert";
    private final String fragmentShader0FileName = "O0_Basic.frag";

    private ShaderProgram shaderProgram0;

    // Pointers (names) for data transfer and handling on GPU
    private int[] vaoName;  // Names of vertex array objects
    private int[] vboName;	// Names of vertex buffer objects
    private int[] iboName;	// Names of index buffer objects

    // Create objects for the scene
    private CubeFace cubeFace;

    // Object for handling keyboard and mouse interaction
    private InteractionHandler interactionHandler;
    // Projection model view matrix tool
    private PMVMatrix pmvMatrix;

    /**
     * Standard constructor for object creation.
     */
    public CubeRenderer() {
        // Create the canvas with default capabilities
        super();
        // Add this object as OpenGL event listener
        this.addGLEventListener(this);
        createAndRegisterInteractionHandler();
    }

    /**
     * Create the canvas with the requested OpenGL capabilities
     * @param capabilities The capabilities of the canvas, including the OpenGL profile
     */
    public CubeRenderer(GLCapabilities capabilities, List<int[][]> colorScheme) {
        // Create the canvas with the requested OpenGL capabilities
        super(capabilities);
        this.colorScheme = colorScheme;
        // Add this object as an OpenGL event listener
        this.addGLEventListener(this);
        createAndRegisterInteractionHandler();
    }

    /**
     * Helper method for creating an interaction handler object and registering it
     * for key press and mouse interaction callbacks.
     */
    private void createAndRegisterInteractionHandler() {
        // The constructor call of the interaction handler generates meaningful default values
        // Nevertheless the start parameters can be set via setters
        // (see class definition of the interaction handler)
        interactionHandler = new InteractionHandler();
        this.addKeyListener(interactionHandler);
        this.addMouseListener(interactionHandler);
        this.addMouseMotionListener(interactionHandler);
        this.addMouseWheelListener(interactionHandler);
    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * that is called when the OpenGL renderer is started for the first time.
     * @param drawable The OpenGL drawable
     */
    @Override
    public void init(GLAutoDrawable drawable) {
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

        cubeFace = new CubeFace(colorScheme);

        // Initialize objects to be drawn (see respective sub-methods)
        initCubeFace0(gl);
        // END: Preparing scene

        // Switch on depth test
        gl.glEnable(GL.GL_DEPTH_TEST);

        // defining polygon drawing mode
        gl.glPolygonMode(GL.GL_FRONT, gl.GL_FILL);
//        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, gl.GL_LINE);

        // Create projection-model-view matrix
        pmvMatrix = new PMVMatrix();

        // Start parameter settings for the interaction handler might be called here
        interactionHandler.setEyeZ(5.5f);
        // END: Preparing scene
    }

    /**
     * Initializes the GPU for drawing object0
     * @param gl OpenGL context
     */
    private void initCubeFace0(GL3 gl) {
        // BEGIN: Prepare a sphere for drawing (object 0)
        // create sphere data for rendering a sphere using an index array into a vertex array
        gl.glBindVertexArray(vaoName[0]);
        // Shader program for object 0
        shaderProgram0 = new ShaderProgram(gl);
        shaderProgram0.loadShaderAndCreateProgram(shaderPath,
                vertexShader0FileName, fragmentShader0FileName);

        float[] cubeFace0Vertices = cubeFace.createColoredCubeFace(0);
        int[] cubeFace0Indices = cubeFace.getCubeFaceIndices(0);

        // activate and initialize vertex buffer object (VBO)
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[0]);
        // floats use 4 bytes in Java
        gl.glBufferData(GL.GL_ARRAY_BUFFER, cubeFace0Vertices.length * Float.BYTES,
                FloatBuffer.wrap(cubeFace0Vertices), GL.GL_STATIC_DRAW);

        // activate and initialize index buffer object (IBO)
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, iboName[0]);
        // integers use 4 bytes in Java
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, cubeFace0Indices.length * Float.BYTES,
                IntBuffer.wrap(cubeFace0Indices), GL.GL_STATIC_DRAW);

        // Activate and order vertex buffer object data for the vertex shader
        // Defining input variables for vertex shader
        // Pointer for the vertex shader to the position information per vertex
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6*Float.BYTES, 0);
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6*Float.BYTES, 3*Float.BYTES);
        // END: Prepare sphere for drawing
    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * called by the OpenGL animator for every frame.
     * @param drawable The OpenGL drawable
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

        // Background color of the canvas
        gl.glClearColor(0.08f, 0.10f, 0.15f, 1.0f);

        // For monitoring the interaction settings
/*        System.out.println("Camera: z = " + interactionHandler.getEyeZ() + ", " +
                "x-Rot: " + interactionHandler.getAngleXaxis() +
                ", y-Rot: " + interactionHandler.getAngleYaxis() +
                ", x-Translation: " + interactionHandler.getxPosition()+
                ", y-Translation: " + interactionHandler.getyPosition());// definition of translation of model (Model/Object Coordinates --> World Coordinates)
*/
        // Using the PMV-Tool for geometric transforms
        pmvMatrix.glMatrixMode(PMVMatrix.GL_MODELVIEW);
        pmvMatrix.glLoadIdentity();
        // Setting the camera position, based on user input
        pmvMatrix.gluLookAt(0f, 0f, interactionHandler.getEyeZ(),
                            0f, 0f, 0f,
                            0f, 1.0f, 0f);
        pmvMatrix.glTranslatef(interactionHandler.getxPosition(), interactionHandler.getyPosition(), 0f);
        pmvMatrix.glRotatef(interactionHandler.getAngleXaxis(), 1f, 0f, 0f);
        pmvMatrix.glRotatef(interactionHandler.getAngleYaxis(), 0f, 1f, 0f);

        // Transform for the complete scene
        pmvMatrix.glTranslatef(1f, 0.2f, 0f);

        pmvMatrix.glPushMatrix();
//        pmvMatrix.glTranslatef(-1.5f, 0f, 0f);
        displayObject0(gl);
        pmvMatrix.glPopMatrix();
    }

    private void displayObject0(GL3 gl) {
        gl.glUseProgram(shaderProgram0.getShaderProgramID());
        // Transfer the PVM-Matrix (model-view and projection matrix)
        // to the vertex shader
        gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
        gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
        gl.glBindVertexArray(vaoName[0]);
        // Draws the elements in the order defined by the index buffer object (IBO)
//        gl.glDrawArrays(GL.GL_TRIANGLES, cubeFace.getCubeFaceIndices(0).length, GL.GL_UNSIGNED_INT);
        gl.glDrawElements(GL.GL_TRIANGLES, cubeFace.getCubeFaceIndices(0).length, GL.GL_UNSIGNED_INT, 0);
    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * called when the OpenGL window is resized.
     * @param drawable The OpenGL drawable
     * @param x
     * @param y
     * @param width
     * @param height
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();

        pmvMatrix.glMatrixMode(PMVMatrix.GL_PROJECTION);
        pmvMatrix.glLoadIdentity();
        pmvMatrix.gluPerspective(45f, (float) width/ (float) height, 0.01f, 10000f);
    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * called when OpenGL canvas ist destroyed.
     * @param drawable
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
        System.out.println("Deleting allocated objects, incl. shader program.");
        GL3 gl = drawable.getGL().getGL3();

        // Detach and delete shader program
        gl.glUseProgram(0);
        shaderProgram0.deleteShaderProgram();

        // deactivate VAO and VBO
        gl.glBindVertexArray(0);
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);

        gl.glDisable(GL.GL_CULL_FACE);
        gl.glDisable(GL.GL_DEPTH_TEST);

        System.exit(0);
    }
}
