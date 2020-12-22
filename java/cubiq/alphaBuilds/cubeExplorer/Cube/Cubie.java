package cubiq.alphaBuilds.cubeExplorer.Cube;

import com.jogamp.opengl.math.Quaternion;
import com.jogamp.opengl.math.VectorUtil;

public class Cubie {

    private float[] pos;
    private Quaternion rot;

    public Cubie(float x, float y, float z) {
        this.pos = new float[] {x, y, z};
        rot = new Quaternion();
    }

    /**
     * Applies the transformation applied to this cubie to the given vector.
     * @return Transformed vector.
     */
    public float[] orientVector(float[] vec) {
        rot.rotateVector(vec, 0, vec, 0);
        VectorUtil.addVec3(vec, vec, pos);
        return vec;
    }

    public void rotateCubie(Quaternion rotation) {
        rot.mult(rotation);
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
