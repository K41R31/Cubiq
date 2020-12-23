package cubiq.alphaBuilds.cubeExplorer.Cube;

import com.jogamp.opengl.math.Quaternion;

public class Cubie {

    private float[] pos;
    private Quaternion rot;

    public Cubie(float x, float y, float z) {
        this.pos = new float[] {x, y, z};
        rot = new Quaternion();
    }

    public void rotateTo(Quaternion rotation) {
        rot.set(rotation);
    }

    public void updateLocalPos() {
        rot.rotateVector(pos, 0, pos, 0);
    }

    public float[] getPosition() {
        return pos;
    }

    public Quaternion getRotation() {
        return rot;
    }

    public void setRotation(Quaternion rotation) {
        this.rot = rotation;
    }
}
