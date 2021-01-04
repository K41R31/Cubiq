package cubeExplorer.cube;

import com.jogamp.opengl.math.Quaternion;

public class Cubie {

    private float[] localPos;
    private final float[] translation;
    private Quaternion rot;
    private float[] indices;

    public Cubie(float x, float y, float z) {
        translation = new float[] {x, y, z};
        rot = new Quaternion();
    }

    public void rotateToQuat(Quaternion rotation) {
        rot.set(rotation);
    }

    public void updateLocalPos() {
        rot.rotateVector(localPos, 0, localPos, 0);
    }

    public float[] getLocalPosition() {
        return localPos;
    }

    public Quaternion getRotationQuat() {
        return rot;
    }

    public void setRotationQuat(Quaternion rotation) {
        this.rot = rotation;
    }

    public float[] getTranslation() {
        return translation;
    }
}
