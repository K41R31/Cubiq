package cubiq.cube;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.math.Quaternion;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public class Cube {

    private final int cubeLayersCount;
    private Cubie[] cubies;
    private int totalCubies;
    private List<int[][]> colorScheme;


    public Cube(int cubeLayersCount, List<int[][]> colorScheme) {
        /*
        for (int i = 0; i < colorScheme.size(); i++) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    System.out.print(colorScheme.get(i)[x][y]);
                    if (x < 2) System.out.print(", ");
                }
                if (y < 2) System.out.print("\n");
            }
            if (i < 5) System.out.print("\n\n");
        }
        System.out.println();

         */
        this.cubeLayersCount = cubeLayersCount;
        this.colorScheme = colorScheme;
    }

    public void initCubies(GL3 gl, int[] vaoName, int[] vboName, int[] iboName) {
        if (colorScheme == null) {
        }
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

    private List<int[][]> generateDefaultScheme() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

            }
        }
        return null;
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

    /**
     * Float array values:
     *              x  y  z
     * [0] -> axis (0, 1, 2)
     *
     *               -1  1
     * [1] -> layer (-1, 1)
     *
     * @param layer
     * @param rotationFactor
     */
    public void rotateLayerWith(int[] layer, Quaternion rotationFactor) {
        for (int i = 0; i < totalCubies; i++) {
            Cubie cubie = cubies[i];
            float[] localPos = cubie.getLocalPosition();
            if (localPos[layer[0]] == layer[1])
                cubie.rotateWithQuat(rotationFactor);
        }
    }

    /**
     * Float array values:
     *              x  y  z
     * [0] -> axis (0, 1, 2)
     *
     *               -1  1
     * [1] -> layer (-1, 1)
     *
     * @param layer
     * @param rotation
     */
    public void setLayerTo(int[] layer, Quaternion rotation) {
        for (int i = 0; i < totalCubies; i++) {
            Cubie cubie = cubies[i];
            float[] localPos = cubie.getLocalPosition();
            if (localPos[layer[0]] == layer[1])
                cubie.setRotationQuat(rotation);
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
