package cubeExplorer.Cube;

import com.jogamp.opengl.math.Quaternion;

import java.util.Arrays;

public class Cube {

    private final float CUBIE_SIZE = 1;
    private final float[] CUBIE_COLOR = {0, 0, 0};
    private final int cubeLayersCount;
    private Cubie[] cubies;
    private float[] cubieVertices;
    private int[] cubieIndices;
    private int totalCubies;

    public Cube(int cubeLayersCount) {
        this.cubeLayersCount = cubeLayersCount;
        createCubieVertices();
        createCubieIndices();
        initCubies();
    }

    private void initCubies() {
        totalCubies = (int)Math.pow(cubeLayersCount, 3);
        cubies = new Cubie[totalCubies];
        // Offset, to center the cube in the scene
        float cubePosOffset = (cubeLayersCount - 1) / 2f;
        for (int x = 0, c = 0; x < cubeLayersCount; x++) {
            for (int y = 0; y < cubeLayersCount; y++) {
                for (int z = 0; z < cubeLayersCount; z++, c++) {
                    cubies[c] = new Cubie(x - cubePosOffset, y - cubePosOffset, z - cubePosOffset);
                }
            }
        }
    }

    private void createCubieVertices() {
        cubieVertices = new float[48];
        float[] a = {CUBIE_SIZE / 2, CUBIE_SIZE / 2, CUBIE_SIZE / 2};
        for (int i = 0; i < 8; i++) {
            System.arraycopy(a, 0, cubieVertices, i * 6, 3);
            System.arraycopy(CUBIE_COLOR, 0, cubieVertices, i * 6 + 3, 3);
            if (i % 2 != 0) a[0] *= -1;
            if (i == 3) a[1] *= -1;
            a[2] *= -1;
        }
        System.out.println(Arrays.toString(cubieVertices));
    }

    private void createCubieIndices() {
        cubieIndices = new int[] {4, 6, 5, 7, 3, 6, 2, 4, 0, 5, 1, 3, 0, 2};
    }

    public void updateLocalPos() {
        for (Cubie qb : cubies) {
            qb.updateLocalPos();
        }
    }

    public void rotateCubeTo(Quaternion rotation) {
        for (Cubie qb: cubies) {
            qb.rotateTo(rotation);
        }
    }

    public float[] getCubieVertices() {
        return cubieVertices;
    }

    public int[] getCubieIndices() {
        return cubieIndices;
    }

    public int getCubeLayersCount() {
        return cubeLayersCount;
    }

    public float[] getCubiePosition(int qbIndex) {
        return cubies[qbIndex].getPosition();
    }

    public float[] getCubieTranslation(int qbIndex) {
        return cubies[qbIndex].getTranslation();
    }

    public Quaternion getCubieRotation(int qbIndex) {
        return cubies[qbIndex].getRotation();
    }

    public int getTotalCubies() {
        return totalCubies;
    }
}
