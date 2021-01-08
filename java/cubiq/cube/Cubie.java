package cubiq.cube;

import com.jogamp.opengl.math.VectorUtil;
import cubiq.processing.MathUtils;

import java.util.Arrays;

public class Cubie {

    private final float CUBIE_SIZE = 1;
    private float[] CUBIE_COLOR = {0.01f, 0.01f, 0.01f};
    private float[] verticesPosColor, verticesPos, localPos;
    private int[] indices;


    public Cubie(float x, float y, float z) {
        localPos = new float[] {x, y, z};
        verticesPos = new float[24];
        createVertices();
        translateVertices(new float[] {x, y, z});
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

    private void updateVerticesPosColor() {
        for (int i = 0; i < 8; i++) {
            System.arraycopy(verticesPos, i*3, verticesPosColor, i * 6, 3);
        }
    }

    public float[] getBoundingBox() {
        float[] boundingBoxVertices = new float[108];
        // Triangle strip to triangles
        for (int i = 0, counter = 0; i < indices.length - 2; i++) {
            for (int j = 0; j < 3; j++, counter++) {
                System.arraycopy(verticesPos, indices[i] + j, boundingBoxVertices, counter * 3, 3);
            }
        }
        return boundingBoxVertices;
    }

    public void rotateAroundAxis(float amount, float[] axis) {
        System.out.println(amount);
        float[] vertex = new float[3];
        float[] rotatedVertex;
        // Update local position
        MathUtils.rotateVector(localPos, axis, amount);
        // Rotate all vertexes
        for (int i = 0; i < 8; i++) {
            System.arraycopy(verticesPos, i*3, vertex, 0, 3);
            rotatedVertex = MathUtils.rotateVector(vertex, axis, amount);
            System.arraycopy(rotatedVertex, 0, verticesPos, i*3, 3);
        }
        updateVerticesPosColor();
    }

    private void translateVertices(float[] translation) {
        VectorUtil.addVec3(verticesPos, verticesPos, translation);
        updateVerticesPosColor();
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

    public float[] getLocalPosition() {
        return localPos;
    }
}
