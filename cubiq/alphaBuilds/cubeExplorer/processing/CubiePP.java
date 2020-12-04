package cubiq.alphaBuilds.cubeExplorer.processing;

public class CubiePP {

    private float[] position;
    private float[] rotation;
    private float cubeWidth;
    private float[] vertices = new float[48], color;
    private int[] indices;


    public CubiePP(float cubeWidth, float[] color) {
        this.color = color;
        this.cubeWidth = cubeWidth;
        createCubieVertices();
        createCubieIndices();
    }

    private void createCubieVertices() {
        float[] a = {cubeWidth, cubeWidth, cubeWidth};
        for (int i = 0; i < 8; i++) {
            System.arraycopy(a, 0, vertices, i * 6, 3);
            System.arraycopy(color, 0, vertices, i * 6 + 3, 3);
            if (i % 2 != 0) a[0] *= -1;
            if (i == 3) a[1] *= -1;
            a[2] *= -1;
        }
    }

    private void createCubieIndices() {
        indices = new int[]{4, 6, 5, 7, 3, 6, 2, 4, 0, 5, 1, 3, 0, 2};
    }

    public float[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public float[] getRotation() {
        return rotation;
    }

    public void setRotation(float[] rotation) {
        this.rotation = rotation;
    }

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }
}
