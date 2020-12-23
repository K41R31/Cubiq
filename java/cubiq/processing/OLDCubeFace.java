package cubiq.processing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OLDCubeFace {

    private final List<int[][]> colorScheme;
    private final List<int[]> faceIndices;

    public OLDCubeFace(List<int[][]> colorScheme) {
        this.colorScheme = colorScheme;
        faceIndices = new ArrayList<>();
    }

    public float[] createColoredCubeFace(int index) {
        float[] fullFaceVertices = new float[0];
        int[] fullFaceIndices = new int[0];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                // vertices
                float[] singleFaceVertices = newSingleFace(colorScheme.get(index)[x][y], x, y);
                System.out.println(Arrays.toString(singleFaceVertices));
                float[] mergedVertices = new float[fullFaceVertices.length + singleFaceVertices.length];

                System.arraycopy(fullFaceVertices, 0, mergedVertices, 0, fullFaceVertices.length);
                System.arraycopy(singleFaceVertices, 0, mergedVertices, fullFaceVertices.length, singleFaceVertices.length);
                fullFaceVertices = mergedVertices;

                // indices
                int[] singleFaceIndices = newSingleFaceIndices(x, y);
                int[] mergedIndices = new int[fullFaceIndices.length + singleFaceIndices.length];

                System.arraycopy(fullFaceIndices, 0, mergedIndices, 0, fullFaceIndices.length);
                System.arraycopy(singleFaceIndices, 0, mergedIndices, fullFaceIndices.length, singleFaceIndices.length);
                fullFaceIndices = mergedIndices;
            }
        }
        faceIndices.add(fullFaceIndices);
        return fullFaceVertices;
    }

    private float[] newSingleFace(int color, int x, int y) {
        float offsetX, offsetY;
        if (x == 0) offsetX = -1;
        else if (x == 1) offsetX = 0;
        else offsetX = 1;
        if (y == 0) offsetY = 1;
        else if (y == 1) offsetY = 0;
        else offsetY = -1;

        float[] vertices = new float[] {
                -0.5f + offsetX,  0.5f + offsetY,    0,
                0,            0,                     0,
                0.5f  + offsetX,  0.5f + offsetY,    0,
                0,            0,                     0,
                -0.5f + offsetX,  -0.5f  + offsetY,  0,
                0,            0,                     0,
                0.5f  + offsetX,  -0.5f  + offsetY,  0,
                0,            0,                     0
        };

        return addColor(vertices, color);
    }

    private int[] newSingleFaceIndices(int x, int y) {
        int round = x + y * 3;
        return new int[] {
                0 + round, 1 + round, 2 + round,
                1 + round, 3 + round, 2 + round
        };
    }

    private float[] addColor(float[] vertices, int color) {
        float[] rgb = colorToRGB(color);
        for (int i = 3; i < vertices.length; i = i + 6) {
            for (int j = 0; j < 3; j++) {
                vertices[i + j] = rgb[j];
            }
        }
        return vertices;
    }

    private float[] colorToRGB(int color) {
        switch (color) {
            case 0:
                return new float[]{1, 1, 1};
            case 1:
                return new float[]{0, 1, 0};
            case 2:
                return new float[]{1, 0, 0};
            case 3:
                return new float[]{1, 0.2f, 0};
            case 4:
                return new float[]{0, 0, 1};
            case 5:
                return new float[]{1, 1, 0};
        }
        return new float[]{0, 0, 0};
    }

    public int[] getCubeFaceIndices(int index) {
        return faceIndices.get(index);
    }
}
