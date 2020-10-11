package AlphaBuild.Renderer;

/**
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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;

/**
 * Performs the OpenGL rendering
 * Uses fixed function pipeline commands.
 * Does not use shaders.
 *
 * Rotation and translation of the camera is included.
 * 	    Use keyboard: left/right/up/down-keys and +/-Keys
 * 	    Alternatively use mouse movements:
 * 	        press left/right button and move and use mouse wheel
 *
 * Draws a simple triangle.
 * Serves as a template (start code) for setting up an OpenGL/Jogl application
 *
 *  * Based on a tutorial by Chua Hock-Chuan
 * http://www3.ntu.edu.sg/home/ehchua/programming/opengl/JOGL2.0.html
 *
 * and on an example by Xerxes Rånby
 * http://jogamp.org/git/?p=jogl-demos.git;a=blob;f=src/demos/es2/RawGL2ES2demo.java;hb=HEAD
 *
 * @author Karsten Lehn
 * @version 26.8.2015, 29.8.2015, 16.9.2015, 5.9.2017, 10.9.2017, 13.10.2017, 16.9.2018, 19.9.2018
 *
 */
public class StartRenderer extends GLCanvas implements GLEventListener {

    private static final long serialVersionUID = 1L;
    // Declaration of an object for handling keyboard and mouse interactions
    private InteractionHandler interactionHandler;
    // Declaration for the OpenGL Utility Library GLU
    private GLU glu;

    private float abstand = (float) 2.2;

    /**
     * Standard constructor for object creation.
     */
    public StartRenderer() {
        // Create the GLCanvas with default capabilities
        super();
        // Add this object as OpenGL event listener to the canvas
        this.addGLEventListener(this);
        createAndRegisterInteractionHandler();
    }

    /**
     * Create the GLCanvas with the requested OpenGL capabilities
     * @param capabilities The capabilities of the GLCanvas, including the OpenGL profile
     */
    public StartRenderer(GLCapabilities capabilities) {
        // Create the canvas with the requested OpenGL capabilities
        super(capabilities);
        // Add this object as event listener
        this.addGLEventListener(this);
        createAndRegisterInteractionHandler();
    }

    /**
     * Helper method for creating an interaction handler object and registering it
     * for key press and mouse interaction call backs.
     */
    private void createAndRegisterInteractionHandler() {
        // The constructor call of the interaction handler generates meaningful default values
        // The start parameters can also be set via setters
        // (see class definition of the interaction handler).
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
        // Retrieve the OpenGL graphics context
        GL2 gl = drawable.getGL().getGL2();
        // Creation of an GLU object, for using the OpenGL Utility Library
        glu = new GLU();
        // Outputs information about the available and chosen profile
        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
        System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));


        // A subroutine for light definition might be called here
        //setLight(gl);

        // Start parameter settings for the interaction handler might be called here
        // interactionHandler.setEyeZ(2);

        // Background color of the GLCanvas
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * called by the OpenGL animator for every frame.
     * @param drawable The OpenGL drawable
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        // Retrieve the OpenGL graphics context
        GL2 gl = drawable.getGL().getGL2();
        // Clear color and depth buffer
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // Switch on back face culling
        //gl.glEnable(GL.GL_CULL_FACE);
        //gl.glCullFace(GL.GL_BACK);

        // Reset matrix for geometric transformations
        gl.glLoadIdentity();
        // Apply view transform including camera positioning steered by the interaction handler
        glu.gluLookAt(0f, 0f, interactionHandler.getEyeZ(),
                0f, 0f, 0f,
                0f, 1.0f, 0f);
        gl.glTranslatef((float) (interactionHandler.getxPosition()-1.0), (float) (interactionHandler.getyPosition()+4.0), -12.0f);
        gl.glRotatef(interactionHandler.getAngleXaxis(), 1f, 0f, 0f);
        gl.glRotatef(interactionHandler.getAngleYaxis(), 0f, 1f, 0f);

        // Controlling the interaction settings
/*        System.out.println("Camera: z = " + interactionHandler.getEyeZ() + ", " +
                "x-Rot: " + interactionHandler.getAngleXaxis() +
                ", y-Rot: " + interactionHandler.getAngleYaxis() +
                ", x-Translation: " + interactionHandler.getxPosition()+
                ", y-Translation: " + interactionHandler.getyPosition());// definition of translation of model (Model/Object Coordinates --> World Coordinates)
*/
        // BEGIN: definition of scene content (i.e. objects, models)

        gl.glBegin(GL2.GL_QUADS);                  // Drawing using triangles
        gl.glColor4f(0.0f, 0.8431f, 1.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);      // Top left
        gl.glVertex3f(1.0f, 1.0f, 0.0f);       // Top right
        gl.glVertex3f(1.0f, -1.0f, 0.0f);      // Bottom right
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);     // Bottom left
        gl.glEnd();

        gl.glTranslatef(abstand, 0.0f, 0.0f);
        gl.glBegin(GL2.GL_QUADS);                  // Drawing using quad
        gl.glColor4f(1.0f, 0.8431f, 0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);      // Top left
        gl.glVertex3f(1.0f, 1.0f, 0.0f);       // Top right
        gl.glVertex3f(1.0f, -1.0f, 0.0f);      // Bottom right
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);     // Bottom left
        gl.glEnd();

        gl.glTranslatef(abstand, 0.0f, 0.0f);
        gl.glBegin(GL2.GL_QUADS);                  // Drawing using quad
        gl.glColor4f(1.0f, 0.0f, 0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);      // Top left
        gl.glVertex3f(1.0f, 1.0f, 0.0f);       // Top right
        gl.glVertex3f(1.0f, -1.0f, 0.0f);      // Bottom right
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);     // Bottom left
        gl.glEnd();

        gl.glTranslatef(-(abstand+abstand), -abstand, 0.0f);
        gl.glBegin(GL2.GL_QUADS);                  // Drawing using quad
        gl.glColor4f(1.0f, 0.8431f, 0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);      // Top left
        gl.glVertex3f(1.0f, 1.0f, 0.0f);       // Top right
        gl.glVertex3f(1.0f, -1.0f, 0.0f);      // Bottom right
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);     // Bottom left
        gl.glEnd();

        gl.glTranslatef(abstand, 0f, 0.0f);
        gl.glBegin(GL2.GL_QUADS);                  // Drawing using quad
        gl.glColor4f(1.0f, 0.8431f, 0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);      // Top left
        gl.glVertex3f(1.0f, 1.0f, 0.0f);       // Top right
        gl.glVertex3f(1.0f, -1.0f, 0.0f);      // Bottom right
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);     // Bottom left
        gl.glEnd();

        gl.glTranslatef(abstand, 0f, 0.0f);
        gl.glBegin(GL2.GL_QUADS);                  // Drawing using quad
        gl.glColor4f(1.0f, 0.8431f, 0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);      // Top left
        gl.glVertex3f(1.0f, 1.0f, 0.0f);       // Top right
        gl.glVertex3f(1.0f, -1.0f, 0.0f);      // Bottom right
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);     // Bottom left
        gl.glEnd();

        gl.glTranslatef(-(abstand+abstand), -abstand, 0.0f);
        gl.glBegin(GL2.GL_QUADS);                  // Drawing using quad
        gl.glColor4f(1.0f, 0.8431f, 0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);      // Top left
        gl.glVertex3f(1.0f, 1.0f, 0.0f);       // Top right
        gl.glVertex3f(1.0f, -1.0f, 0.0f);      // Bottom right
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);     // Bottom left
        gl.glEnd();

        gl.glTranslatef(abstand, 0f, 0.0f);
        gl.glBegin(GL2.GL_QUADS);                  // Drawing using quad
        gl.glColor4f(1.0f, 0.8431f, 0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);      // Top left
        gl.glVertex3f(1.0f, 1.0f, 0.0f);       // Top right
        gl.glVertex3f(1.0f, -1.0f, 0.0f);      // Bottom right
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);     // Bottom left
        gl.glEnd();

        gl.glTranslatef(abstand, 0f, 0.0f);
        gl.glBegin(GL2.GL_QUADS);                  // Drawing using quad
        gl.glColor4f(1.0f, 0.8431f, 0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 0.0f);      // Top left
        gl.glVertex3f(1.0f, 1.0f, 0.0f);       // Top right
        gl.glVertex3f(1.0f, -1.0f, 0.0f);      // Bottom right
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);     // Bottom left
        gl.glEnd();
        // END: definition of scene content
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
        GL2 gl = drawable.getGL().getGL2();

        // Avoiding division by zero
        if (height == 0)
            height = 1;
        // Set the viewport to the entire window
        gl.glViewport(0, 0, width, height);
        // Switch to perspective projection
        gl.glMatrixMode(gl.GL_PROJECTION);
        // Reset projection matrix to identity
        gl.glLoadIdentity();
        // Determine the aspect ratio of the viewport
        float aspectRatio = (float) width / (float) height;
        // Calculate projection matrix
        //      Parameters for  glu-call:
        //          fovy (field of view), aspect ratio,
        //          zNear (near clipping plane), zFar (far clipping plane)
        glu.gluPerspective(50.0, aspectRatio, 0.1, 1000.0);
        // Switch to model-view transform
        gl.glMatrixMode(gl.GL_MODELVIEW);
    }

    /**
     * Implementation of the OpenGL EventListener (GLEventListener) method
     * called when OpenGL canvas ist destroyed.
     * @param drawable
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Rarely used when using fixed function pipeline commands
    }
}
