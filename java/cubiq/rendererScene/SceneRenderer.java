package cubiq.rendererScene;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.javafx.NewtCanvasJFX;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.PMVMatrix;
import cubiq.cube.Cube;
import cubiq.io.InteractionHandler;
import cubiq.models.GuiModel;
import de.hshl.obj.loader.OBJLoader;
import de.hshl.obj.loader.Resource;
import de.hshl.obj.loader.objects.Surface;
import de.hshl.obj.loader.objects.SurfaceObject;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_LESS;

public class SceneRenderer implements GLEventListener, Observer {

    // taking shader source code files from relative path
    private final String shaderPath = ".\\resources\\";
    // Shader for object 0
//    final String vertexShader0FileName = "O0_Basic.vert";
//    final String fragmentShader0FileName = "O0_Basic.frag";
//    private final String vertexShader0FileName = "BlinnPhongPoint.vert";
//    private final String fragmentShader0FileName = "BlinnPhongPoint.frag";
    private final String vertexShader0FileName = "BlinnPhongPoint.vert";
    private final String fragmentShader0FileName = "BlinnPhongPoint.frag";
    private static final Path objFile = Paths.get("./resources/models/scene.obj");

    private GuiModel guiModel;
    private final GLWindow glWindow;

    // taking texture files from relative path
    private final String texturePath = ".\\resources\\";
//    final String textureFileName = "GelbGruenPalette.png";
//    final String textureFileName = "HSHLLogo2.jpg";

    private ShaderProgram shaderProgram;

    int noOfObjects;

    // Pointers (names) for data transfer and handling on GPU
    private int[] vaoName;  // Name of vertex array object
    private int[] vboName;	// Name of vertex buffer object


    // Lists for the color and mesh data of the object
    List<Material> objectMaterials = new ArrayList<>();
    List<float[]> objectVertices = new ArrayList<>();

    // Define light sources
    private LightSource light0;

    // Object for handling keyboard and mouse interaction
    private cubiq.rendererScene.InteractionHandler interactionHandler;
    // Projection model view matrix tool
    private PMVMatrix pmvMatrix;


    public SceneRenderer() {
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

        glCanvas.setWidth(1850);
        glCanvas.setHeight(700);

//        interactionHandler = new InteractionHandler(camPos, deviceWidth, deviceHeight);

//        glWindow.addMouseListener(interactionHandler);
        glWindow.addGLEventListener(this);
    }

    @Override
    public void init(GLAutoDrawable drawable) {

        GL3 gl = drawable.getGL().getGL3();

        // Verify if VBO-Support is available
        if(!gl.isExtensionAvailable("GL_ARB_vertex_buffer_object"))
            System.out.println("Error: VBO support is missing");
        else
            System.out.println("VBO support is available");

        try {
            List<SurfaceObject> objects = new OBJLoader()
                    .setLoadNormals(true) // tell the loader to also load normal data
                    .loadSurfaceObjects(Resource.file(objFile)); // load multiple objects with materials

            // A Surface contains geometry and a material
            for (SurfaceObject object : objects) {
                Surface surface = object.getSurfaces().get(0);
                objectVertices.add(surface.getMesh().getVertices());

                // Load material
                float[] matEmission = surface.getMaterial().getEmissionColor().toFloatArray();
                float[] matAmbient = surface.getMaterial().getAmbientColor().toFloatArray();
                float[] matDiffuse = surface.getMaterial().getDiffuseColor().toFloatArray();
                float[] matSpecular = surface.getMaterial().getSpecularColor().toFloatArray();
                float matShininess = surface.getMaterial().getSpecularExponent().floatValue();

                objectMaterials.add(new Material(matEmission, matAmbient, matDiffuse, matSpecular, matShininess));
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        // BEGIN: Preparing scene
        // BEGIN: Allocating vertex array objects and buffers for each object
        noOfObjects = objectVertices.size();
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

        // Initialize objects to be drawn (see respective sub-methods)
        initObjects(gl);

        // Specify light parameters
        float[] lightPosition = {0.0f, 3.0f, 3.0f, 1.0f};
        float[] lightAmbientColor = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] lightDiffuseColor = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] lightSpecularColor = {1.0f, 1.0f, 1.0f, 1.0f};
        light0 = new LightSource(lightPosition, lightAmbientColor,
                lightDiffuseColor, lightSpecularColor);
        // END: Preparing scene

        // Switch on back face culling
//        gl.glEnable(GL.GL_CULL_FACE);
        gl.glCullFace(GL.GL_BACK);
//        gl.glCullFace(GL.GL_FRONT);
        // Switch on depth test
        gl.glEnable(GL.GL_DEPTH_TEST);

        // defining polygon drawing mode
//        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, gl.GL_FILL);
//        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, gl.GL_LINE);

        // Create projection-model-view matrix
        pmvMatrix = new PMVMatrix();

        // Start parameter settings for the interaction handler might be called here
        interactionHandler.setEyeZ(4);
        // END: Preparing scene

        // Set background color of the GLCanvas.
        gl.glClearColor(0.098f, 0.106f, 0.114f, 1.0f);
    }

    /**
     * Initializes the GPU for drawing object1
     * @param gl OpenGL context
     */
    private void initObjects(GL3 gl) {
        for (int i = 0; i < noOfObjects; i++) {
            // BEGIN: Prepare cube for drawing (object 1)
            gl.glBindVertexArray(vaoName[i]);
            shaderProgram = new ShaderProgram(gl);
            shaderProgram.loadShaderAndCreateProgram(shaderPath,
                    vertexShader0FileName, fragmentShader0FileName);

            // activate and initialize vertex buffer object (VBO)
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[i]);
            // floats use 4 bytes in Java
            gl.glBufferData(GL.GL_ARRAY_BUFFER, objectVertices.get(i).length * 4,
                    FloatBuffer.wrap(objectVertices.get(i)), GL.GL_STATIC_DRAW);

            gl.glEnableVertexAttribArray(0);
            gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 0);
            // Pointer for the vertex shader to the color information per vertex
            gl.glEnableVertexAttribArray(1);
            gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        }
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
        System.out.println("Reshape called.");
        System.out.println("x = " + x + ", y = " + y + ", width = " + width + ", height = " + height);

        pmvMatrix.glMatrixMode(PMVMatrix.GL_PROJECTION);
        pmvMatrix.glLoadIdentity();
        pmvMatrix.gluPerspective(30f, (float) width/ (float) height, 0.01f, 10000f);
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
        gl.glClearColor(0.97f, 0.97f, 0.97f, 1.0f);

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

        pmvMatrix.glPushMatrix();
        displayObjects(gl);
        pmvMatrix.glPopMatrix();
    }

    private void displayObjects(GL3 gl) {
        for (int i = 0; i < noOfObjects; i++) {
            // BEGIN: Draw the second object (object 1)
            gl.glUseProgram(shaderProgram.getShaderProgramID());
            // Transfer the PVM-Matrix (model-view and projection matrix) to the vertex shader
            gl.glUniformMatrix4fv(0, 1, false, pmvMatrix.glGetPMatrixf());
            gl.glUniformMatrix4fv(1, 1, false, pmvMatrix.glGetMvMatrixf());
            gl.glUniformMatrix4fv(2, 1, false, pmvMatrix.glGetMvitMatrixf());
            // transfer parameters of light source
            gl.glUniform4fv(3, 1, light0.getPosition(), 0);
            gl.glUniform4fv(4, 1, light0.getAmbient(), 0);
            gl.glUniform4fv(5, 1, light0.getDiffuse(), 0);
            gl.glUniform4fv(6, 1, light0.getSpecular(), 0);
            // transfer material parameters
            gl.glUniform4fv(7, 1, objectMaterials.get(i).getEmission(), 0);
            gl.glUniform4fv(8, 1, objectMaterials.get(i).getAmbient(), 0);
            gl.glUniform4fv(9, 1, objectMaterials.get(i).getDiffuse(), 0);
            gl.glUniform4fv(10, 1, objectMaterials.get(i).getSpecular(), 0);
            gl.glUniform1f(11, objectMaterials.get(i).getShininess());

            gl.glBindVertexArray(vaoName[i]);
            // Draws the elements in the order defined by the index buffer object (IBO)
            gl.glDrawArrays(GL.GL_TRIANGLES, 0, objectVertices.get(i).length);
        }
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
        shaderProgram.deleteShaderProgram();

        // deactivate VAO and VBO
        gl.glBindVertexArray(0);
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);

        gl.glDisable(GL.GL_CULL_FACE);
        gl.glDisable(GL.GL_DEPTH_TEST);

        System.exit(0);
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String)arg) {
        }
    }

    public void initModel(GuiModel guiModel) {
        this.guiModel = guiModel;
    }
}
