package cubeExplorer.cube;

import com.jogamp.opengl.math.Quaternion;
import com.jogamp.opengl.math.VectorUtil;

import java.util.Arrays;

public class CubieNew {

    private final float CUBIE_SIZE = 1;
    private float[] CUBIE_COLOR = {0.01f, 0.01f, 0.01f};
    private float[] verticesPosColor, verticesPos, translation, localPos;
    private int[] indices;
    private Quaternion rotation;


    public CubieNew(float x, float y, float z) {
        translation = new float[] {x, y, z};
        localPos = new float[] {x, y, z};
        rotation = new Quaternion();
        verticesPos = new float[24];
        if (Arrays.equals(this.localPos, new float[]{-1, -1, -1}))
            CUBIE_COLOR = new float[] {1f, 1f, 1f};
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
        float[] boundingBoxVertices = new float[108];
        // Orient the vertices to match the actual translation of the cubie
        float[] orientedVertices = new float[24];
        for (int i = 0; i < 8; i++) {
            float[] vec = new float[3];
            rotation.rotateVector(vec, 0, verticesPos, i*3);
            VectorUtil.addVec3(vec, vec, translation);
            System.arraycopy(vec, 0, orientedVertices, i*3, 3);
        }
        // Triangle strip to triangles
        for (int i = 0, counter = 0; i < indices.length - 2; i++) {
            for (int j = 0; j < 3; j++, counter++) {
                System.arraycopy(orientedVertices, indices[i] + j, boundingBoxVertices, counter * 3, 3);
            }
        }
        return boundingBoxVertices;
    }


    private void createIndices() {
        indices = new int[] {4, 6, 5, 7, 3, 6, 2, 4, 0, 5, 1, 3, 0, 2};
    }

    public float[] getVerticesPosColor() {
        return verticesPosColor;
    }

    public int[] getIndices() {
        return indices;
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
