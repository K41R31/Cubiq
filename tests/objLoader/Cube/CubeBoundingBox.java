package objLoader.Cube;

public class CubeBoundingBox {

    public CubeBoundingBox() {
        // axis > x > y
//        SelectionFaces[][][] selectionFaces = new SelectionFaces[3][3][3];

        for (int axis = 0; axis < 3; axis++) {
            if (axis == 0) {
                // x -> 1.5f
                for (int y = 0; y < 3; y++) {
                    for (int z = 0; z < 3; z++) {

                    }
                }
            }
            else if (axis == 1) {
                // y -> 1.5f
                for (int x = 0; x < 3; x++) {
                    for (int z = 0; z < 3; z++) {

                    }
                }
            }
            else {
                // z -> 1.5f
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {

                    }
                }
            }
        }
    }

    private float[] createTriangles(float x, float y, float z) {
        float[][] vertices = new float[6][3];
        vertices[0] = new float[] {x - 0.5f, 0, 0};
        for (int localx = 0; localx < 3; localx++) {
            for (int localy = 0; localy < 3; localy++) {

            }
        }
        return new float[3];
    }
}
