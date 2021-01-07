package cubiq.cube;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.math.Quaternion;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Cube {

    private final int cubeLayersCount;
    private Cubie[] cubies;
    private int totalCubies;

    public Cube(int cubeLayersCount) {
        this.cubeLayersCount = cubeLayersCount;
    }

    public void initCubies(GL3 gl, int[] vaoName, int[] vboName, int[] iboName) {
        totalCubies = (int)Math.pow(cubeLayersCount, 3);
        cubies = new Cubie[totalCubies];
        // Offset, to center the cube in the scene
        float cubePosOffset = (cubeLayersCount - 1) / 2f;
        for (int x = 0, c = 0; x < cubeLayersCount; x++) {
            for (int y = 0; y < cubeLayersCount; y++) {
                for (int z = 0; z < cubeLayersCount; z++, c++) {
                    cubies[c] = new Cubie(x - cubePosOffset, y - cubePosOffset, z - cubePosOffset);
                    gl.glBindVertexArray(vaoName[c]);
                    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboName[c]);
                    gl.glBufferData(GL.GL_ARRAY_BUFFER, cubies[c].getVerticesPosColor().length * 4L,
                            FloatBuffer.wrap(cubies[c].getVerticesPosColor()), GL.GL_STATIC_DRAW);
                    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, iboName[c]);
                    gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, cubies[c].getIndices().length * 4L,
                            IntBuffer.wrap(cubies[c].getIndices()), GL.GL_STATIC_DRAW);
                    gl.glEnableVertexAttribArray(0);
                    gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 0);
                    gl.glEnableVertexAttribArray(1);
                    gl.glVertexAttribPointer(1, 3, GL.GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
                }
            }
        }
    }

    public void updateLocalPos() {
        for (Cubie qb : cubies) {
            qb.updateLocalPos();
        }
    }

    public void rotateCubeTo(Quaternion rotation) {
        for (Cubie qb: cubies) {
            qb.rotateToQuat(rotation);
        }
    }

    public float[] getCubieBoundingBox(int index) {
        return cubies[index].getBoundingBox();
    }

    public int[] getCubieIndices(int index) {
        return cubies[index].getIndices();
    }

    public int getCubeLayersCount() {
        return cubeLayersCount;
    }

    public float[] getCubiePosition(int qbIndex) {
        return cubies[qbIndex].getLocalPosition();
    }

    public float[] getCubieTranslation(int qbIndex) {
        return cubies[qbIndex].getTranslation();
    }

    public Quaternion getCubieRotation(int qbIndex) {
        return cubies[qbIndex].getRotationQuat();
    }

    public int getTotalCubies() {
        return totalCubies;
    }
}
