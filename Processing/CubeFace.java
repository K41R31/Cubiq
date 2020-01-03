package Processing;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CubeFace {

    private List<int[][]> colorScheme;
    private List<int[]> faceIndices;

    public CubeFace(List<int[][]> colorScheme) {
        this.colorScheme = colorScheme;
        faceIndices = new ArrayList<>();
    }

    public float[] createColoredCubeFace(int index) {
        System.out.println(colorScheme.size());
        float[] fullFace = new float[0];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                float[] singleFace = newSingleFace(colorScheme.get(index)[x][y], x, y);
                float[] result = new float[fullFace.length + singleFace.length];

                System.arraycopy(fullFace, 0, result, 0, fullFace.length);
                System.arraycopy(singleFace, 0, result, fullFace.length, singleFace.length);
                fullFace = result;
            }
        }
        return fullFace;
    }

    private float[] newSingleFace(int color, int x, int y) {
        if (x == 0) x = -1;
        else if (x == 1) x = 0;
        else x = 1;
        if (y == 0) y = -1;
        else if (y == 1) y = 0;
        else y = 1;

        float[] vertices = new float[] {
                -0.5f + x,  -0.5f + y,   0,
                0,            0,         0,
                0.5f  + x,  -0.5f + y,   0,
                0,            0,         0,
                -0.5f + x,  0.5f  + y,   0,
                0,            0,         0,
                0.5f  + x,  0.5f  + y,   0
        };

        faceIndices.add(new int[] {
                0, 1, 2,
                1, 3, 2
        });

        return addColor(vertices, color);
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
