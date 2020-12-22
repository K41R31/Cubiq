package cubiq.alphaBuilds.cubeExplorer.Cube;

import com.jogamp.opengl.math.Quaternion;

public class Cube {

    private final float CUBIE_SIZE = 1;
    private final float[] CUBIE_COLOR = {0, 0, 0};
    private int cubeLayers;
    private Cubie[][][] cubies;
    private Quaternion cubeRotation;
    private float[] cubieVertices;
    private int[] cubieIndices;

    public Cube(int cubeLayers) {
        this.cubeLayers = cubeLayers;
        this.cubeRotation = new Quaternion();
        createCubieVertices();
        createCubieIndices();
        initCubies();
        initMousePickerGrid();
    }

    private void initCubies() {
        cubies = new Cubie[cubeLayers][cubeLayers][cubeLayers];
        // Offset, to center the cube in the scene
        float cubePosOffset = (cubeLayers - 1) / 2f;
        for (int x = 0; x < cubeLayers; x++) {
            for (int y = 0; y < cubeLayers; y++) {
                for (int z = 0; z < cubeLayers; z++) {
                    cubies[x][y][z] = new Cubie(x - cubePosOffset, y - cubePosOffset, z - cubePosOffset);
                }
            }
        }
    }

    private void initMousePickerGrid() {

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

    public float[] getCubieVertices() {
        return cubieVertices;
    }

    public int[] getCubieIndices() {
        return cubieIndices;
    }

    public int getCubeLayers() {
        return cubeLayers;
    }

    public float[] getCubiePosition(int qbIndexX, int qbIndexY, int qbIndexZ) {
        return cubies[qbIndexX][qbIndexY][qbIndexZ].getPosition();
    }

    public Quaternion getCubieRotation(int qbIndexX, int qbIndexY, int qbIndexZ) {
        return cubies[qbIndexX][qbIndexY][qbIndexZ].getRotation();
    }

    public Quaternion getCubeRotation() {
        return cubeRotation;
    }

    public void setCubeRotation(Quaternion cubeRotation) {
        this.cubeRotation = cubeRotation;
    }
}
