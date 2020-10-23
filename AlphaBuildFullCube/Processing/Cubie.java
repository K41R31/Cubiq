package AlphaBuildFullCube.Processing;

public class Cubie {

    int cubeType = 0; // 0: Mittelstein, 1: Kantenstein, 2: Eckstein
    int[] position = new int[3];
    int[] colors; // Colors on the x, y and z axis; [0] -> x, [1] -> y, [2] -> z; If the value is negative the color is on the opposite side

    public Cubie(int x, int y, int z, int cubeType, int[] colors) {
        position[0] = x;
        position[1] = y;
        position[2] = z;
        this.cubeType = cubeType;
        this.colors = colors;
    }

    public Cubie(int[] position, int cubeType, int[] colors) {
        this.position = position;
        this.cubeType = cubeType;
        this.colors = colors;
    }

    public void rotateEdgeXClockwise() {
        // TODO Rotation X Uhrzeigersinn
    }

    public void rotateEdgeXCounterClockWise() {

    }

    public void rotateCornerXClockwise() {

    }

    public void rotateCornerXCounterClockWise() {

    }
}
