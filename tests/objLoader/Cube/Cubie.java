package objLoader.Cube;

import com.jogamp.opengl.math.Quaternion;

public class Cubie {

    private float[] localPos;
    private final float[] translation;
    private Quaternion rot;
    private float[] indices;

    public Cubie(float x, float y, float z) {
        translation = new float[] {x, y, z};
        rot = new Quaternion();
        indices = new float[] {
                -0.5f,
        };
    }

    public void rotateTo(Quaternion rotation) {
        rot.set(rotation);
    }

    public void updateLocalPos() {
        rot.rotateVector(localPos, 0, localPos, 0);
    }

    public float[] getPosition() {
        return localPos;
    }

    public Quaternion getRotation() {
        return rot;
    }

    public void setRotation(Quaternion rotation) {
        this.rot = rotation;
    }

    public float[] getTranslation() {
        return translation;
    }
}
