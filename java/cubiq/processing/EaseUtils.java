package cubiq.processing;

public class EaseUtils {

    /**
     * Function to calculate a ease in/out animation
     * Source: http://gizma.com/easing
     * @param t current time
     * @param b start value
     * @param c change in value
     * @param d duration
     * @return Eased value
     */
    public static float easeInOut(float t, float b, float c, float d) {
        t /= d/2;
        if (t < 1) return c/2*t*t*t + b;
        t -= 2;
        return c/2*(t*t*t + 2) + b;
    }

    /**
     * Function to calculate a ease out animation
     * Source: http://gizma.com/easing/
     * @param t current time
     * @param b start value
     * @param c change in value
     * @param d duration
     * @return Eased value
     */
    public static float easeOut(float t, float b, float c, float d) {
        t /= d;
        t--;
        return c*(t*t*t + 1) + b;
    }
}