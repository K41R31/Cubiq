package cubeExplorer.cube;

import com.jogamp.opengl.math.Quaternion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CubieNew {

    private final float CUBIE_SIZE = 1;
    private final float[] CUBIE_COLOR = {0.1f, 0.1f, 0.1f};
    private float[] verticesPosColor, verticesPos, translation, localPos;
    private int[] indices;
    private Quaternion rotation;


    public CubieNew(float x, float y, float z) {
        translation = new float[] {x, y, z};
        rotation = new Quaternion();
        verticesPos = new float[42];
        createVertices();
        createIndices();
        getBoundingBox();
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
        for (int i = 0, counter = 0; i < indices.length - 2; i++) {
            for (int j = 0; j < 3; j++, counter++) {
                System.out.println(indices[i + j]);
                System.arraycopy(verticesPos, indices[i + j], boundingBoxVertices, counter * 3, 3);
            }
        }
        return boundingBoxVertices;
    }
/*
    public float[] getBoundingBox() {
//        float[][] boundingBoxVertices = new float[36][3];
        List<float[]> boundingBoxVertices = new ArrayList<>();
        for (int i = 0; i < indices.length; i++) {
            // First triangle
            float[] triangle = new float[3];
            for (int j = 0; j < 3; j++) {
                triangle[j] =
            }
            boundingBoxVertices.add(indices[i + j]);
//            System.arraycopy(verticesPos, i * 3, pos, indices[i] * 3, 3);
        }
        System.out.println(Arrays.toString(verticesPos));
        return verticesPos;
    }
*/

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