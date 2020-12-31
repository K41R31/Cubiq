package prototype.processing;

public class Cubie {

    int cubieType;
    int[] position = new int[3]; // [0] -> x, [1] -> y, [2] -> z
    int[] colors; // Colors on the x, y and z axis; [0] -> x, [1] -> y, [2] -> z

    public Cubie(int x, int y, int z, int[] colors) {
        position[0] = x;
        position[1] = y;
        position[2] = z;
        this.colors = colors;
        cubieType = calculateCubieType();
    }

    public Cubie(int[] position, int[] colors) {
        for (int i = 0; i < 3; i++)
            this.position[i] = position[i];
        this.colors = colors;
        cubieType = calculateCubieType();
    }

    /**
     * Rotate the cubie around the given axis
     * @param axis 0: x, 1: y, 2: z
     * @param direction 1: clockwise, -1: counterclockwise
     */
    public void rotateCubie(int axis, int direction) {
        int[] rotatedLayer;
            if (axis == 0) {
                rotatedLayer = rotateLayer(position[2], position[1], direction);
                position[2] = rotatedLayer[0];
                position[1] = rotatedLayer[1];
            }
            else if (axis == 1) {
                rotatedLayer = rotateLayer(position[0], position[2], direction);
                position[0] = rotatedLayer[0];
                position[2] = rotatedLayer[1];
            }
            else if (axis == 2) {
                rotatedLayer = rotateLayer(position[0], position[1], direction);
                position[0] = rotatedLayer[0];
                position[1] = rotatedLayer[1];
            }
    }

    private int[] rotateLayer(int val0, int val1, int direction) {
        // Clockwise
        if (direction == 1) {
            // Corner rotation
            if (cubieType == 0) {
                if (val0 == val1)
                    val1 *= -1;
                else
                    val0 *= -1;
            }
            // Edge rotation
            else if (cubieType == 1) {
                if (val0 == 0) {
                    val0 = val1;
                    val1 = 0;
                } else {
                    val1 = val1 * -1;
                    val0 = 0;
                }
            }
        }
        // Counterclockwise
        else {
            // Corner rotation
            if (cubieType == 0) {
                if (val0 == val1)
                    val0 *= -1;
                else
                    val1 *= -1;
            }
            // Edge rotation
            else if (cubieType == 1) {
                if (val0 == 0) {
                    val0 = val1 * -1;
                    val1 = 0;
                } else {
                    val1 = val0;
                    val0 = 0;
                }
            }
        }
        return new int[]{val0, val1};
    }

    private int calculateCubieType() {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            if (position[i] == 0)
                counter++;
        }
        return counter;
    }

    public int[] getPosition() {
        return position;
    }

    /**
     * Get the type of the cubie (center, edge or corner)
     * The value of the returned integer corresponds to the
     * number of middle axes the cubie lays on
     * @return 0: corner
     *         1: edge
     *         2: center
     *         3: absolute center
     */
    public int getCubieType() {
        return cubieType;
    }
}
