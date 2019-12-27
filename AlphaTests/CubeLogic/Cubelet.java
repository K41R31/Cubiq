package AlphaTests.CubeLogic;

public class Cubelet {

    private int color;
    private int rotation;
    private int[] colors;
    private int[] location;

    /** Middle Cubelet
     * Creates a new instance of a Cubelet, which is located on a middle position.
     * @param color one int value, which contains information about the color.
     * @param location the location which contains information about the X, Y, Z coordinates.
     * @param rotation the rotation of the cubelet in degrees.
     */

    public Cubelet(int color, int[] location, int rotation) {
        setColor(color);
        setLocation(location);
        setRotation(rotation);
    }

    /** Edge and Corner Cubelet
     * Creates a new instance of a Cubelet, which is located on an edge position.
     * @param colors depends on the type of Cubelet. Two int values for the Edge and three for the Corner Cubelets which
     * contain informations about the color of the specific sides of the Cubelet.
     * @param location the location which contains information about the X, Y, Z coordinates.
     * @param rotation the rotation of the cubelet in degrees.
     */

    public Cubelet(int[] colors, int[] location, int rotation) {
        setColors(colors);
        setLocation(location);
        setRotation(rotation);
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] location) {
        this.location = location;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }



}
