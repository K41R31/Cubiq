package cubeExplorer.cube;

import com.jogamp.opengl.math.Quaternion;

public class Cube {

    private final int cubeLayersCount;
    private CubieNew[] cubies;
    private int totalCubies;

    public Cube(int cubeLayersCount) {
        this.cubeLayersCount = cubeLayersCount;
        initCubies();
    }

    private void initCubies() {
        totalCubies = (int)Math.pow(cubeLayersCount, 3);
        cubies = new CubieNew[totalCubies];
        // Offset, to center the cube in the scene
        float cubePosOffset = (cubeLayersCount - 1) / 2f;
        for (int x = 0, c = 0; x < cubeLayersCount; x++) {
            for (int y = 0; y < cubeLayersCount; y++) {
                for (int z = 0; z < cubeLayersCount; z++, c++) {
                    cubies[c] = new CubieNew(x - cubePosOffset, y - cubePosOffset, z - cubePosOffset);
                }
            }
        }
    }

    public void updateLocalPos() {
        for (CubieNew qb : cubies) {
            qb.updateLocalPos();
        }
    }

    public void rotateCubeTo(Quaternion rotation) {
        for (CubieNew qb: cubies) {
            qb.rotateToQuat(rotation);
        }
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
