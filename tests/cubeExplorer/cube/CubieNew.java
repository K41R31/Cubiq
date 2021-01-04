package cubeExplorer.cube;

import com.jogamp.opengl.math.Quaternion;

public class CubieNew {

    private final float CUBIE_SIZE = 1;
    private final float[] CUBIE_COLOR = {0.1f, 0.1f, 0.1f};
    private float[] verticesPosColor, verticesPos, translation, localPos;
    private int[] indices;
    private Quaternion rotation;


    public CubieNew(float x, float y, float z) {
        translation = new float[] {x, y, z};
        rotation = new Quaternion();
        createVertices();
        createIndices();
    }

    private void createVertices() {
        verticesPosColor = new float[48];
        float[] a = {CUBIE_SIZE / 2, CUBIE_SIZE / 2, CUBIE_SIZE / 2};
        for (int i = 0; i < 8; i++) {
            System.arraycopy(a, 0, verticesPosColor, i * 6, 3);
            System.arraycopy(a, 0, verticesPos, i * 3, 3);
            System.arraycopy(CUBIE_COLOR, 0, verticesPosColor, i * 6 + 3, 3);
            if (i % 2 != 0) a[0] *= -1;
            if (i == 3) a[1] *= -1;
            a[2] *= -1;
        }
    }

    public float[] getBoundingBox() {
        float[][] boundingBoxVertices = new float[36][3];
        for (int i = 0; i < indices.length; i++) {
            float[] pos = new float[3];
            System.arraycopy(verticesPos, indices[i] * 3, pos, 0, 3);
            verticesPos[]
        }
    }

    private void createIndices() {
        indices = new int[] {4, 6, 5, 7, 3, 6, 2, 4, 0, 5, 1, 3, 0, 2};
    }

    public void rotateToQuat(Quaternion rotation) {
        rotation.set(rotation);
    }

    public void updateLocalPos() {
        rotation.rotateVector(localPos, 0, localPos, 0);
    }

    public float[] getLocalPosition() {
        return localPos;
    }

    public Quaternion getRotationQuat() {
        return rotation;
    }

    public void setRotationQuat(Quaternion rotation) {
        this.rotation = rotation;
    }

    public float[] getTranslation() {
        return translation;
    }
}
