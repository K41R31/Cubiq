package cubiq.alphaBuilds.cubeExplorer.Cube;

import com.jogamp.opengl.math.Quaternion;

public class Cube {

    private final float CUBIE_SIZE = 1;
    private final float[] CUBIE_COLOR = {0, 0, 0};
    private final int cubeLayersCount;
    private Cubie[][][] cubies;
    private float[] cubieVertices;
    private int[] cubieIndices;

    public Cube(int cubeLayersCount) {
        this.cubeLayersCount = cubeLayersCount;
        createCubieVertices();
        createCubieIndices();
        initCubies();
    }

    private void initCubies() {
        cubies = new Cubie[cubeLayersCount][cubeLayersCount][cubeLayersCount];
        // Offset, to center the cube in the scene
        float cubePosOffset = (cubeLayersCount - 1) / 2f;
        for (int x = 0; x < cubeLayersCount; x++) {
            for (int y = 0; y < cubeLayersCount; y++) {
                for (int z = 0; z < cubeLayersCount; z++) {
                    cubies[x][y][z] = new Cubie(x - cubePosOffset, y - cubePosOffset, z - cubePosOffset);
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
    }

    private void createCubieIndices() {
        cubieIndices = new int[] {4, 6, 5, 7, 3, 6, 2, 4, 0, 5, 1, 3, 0, 2};
    }

    public void updateLocalPos() {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    cubies[x][y][z].updateLocalPos();
                }
            }
        }
    }

    public void rotateCubeTo(Quaternion rotation) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    cubies[x][y][z].rotateTo(rotation);
                }
            }
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

    public float[] getCubiePosition(int qbIndexX, int qbIndexY, int qbIndexZ) {
        return cubies[qbIndexX][qbIndexY][qbIndexZ].getPosition();
    }

    public Quaternion getCubieRotation(int qbIndexX, int qbIndexY, int qbIndexZ) {
        return cubies[qbIndexX][qbIndexY][qbIndexZ].getRotation();
    }
}
