package cubiq.alphaBuilds.cubeExplorer.io;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.math.Quaternion;

public class InteractionHandlerPP implements MouseListener {

    private final int MOVE_DISTANCE_THRESHOLD = 5;
    private final int FAST_ROTATION_CLICK_SPEED = 7;
    private final int CUBE_SNAP_BACK_SPEED = 23;
    private final float CUBE_ROTATION_SPEED = 0.15f;
    private Quaternion actualQuat, pressedQuat, snapToQuat, releasedQuat;
    private int mousePressedX, mousePressedY, windowWidth, actualFrame, pressedFrame;
    private float rotatedSincePress, snapBackDiff;
    private boolean swingingBack, mousePressed;
    private int snapBackFrameCount, direction; // -1 -> no direction; 0 -> x; 1 -> y; 2 -> z


    public InteractionHandlerPP() {
        mousePressed = false;
        swingingBack = false;
        actualQuat = new Quaternion();
        pressedQuat = new Quaternion();
        snapToQuat = new Quaternion();
        releasedQuat = new Quaternion();
        snapBackFrameCount = 0;
        direction = -1;
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        /* TODO Wenn die Maus neben dem Würfel gedrückt wurde -> frage die Rotation des Würfels ab und gebe das rotierte Quaternion zurück
                Wenn die Maus auf dem Würfel gedrückt wurde -> frage die Rotation des aktuellen Steins ab
         */
        if (mouseEvent.getButton() == MouseEvent.BUTTON1 && !swingingBack) {
            mousePressed = true;
            pressedFrame = actualFrame;

            // Store the position of the mouse when it was pressed
            mousePressedX = mouseEvent.getX();
            mousePressedY = mouseEvent.getY();

            // Store the angles of the cube when the mouse was pressed in an quaternion
            pressedQuat.set(actualQuat);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        mousePressed = false;
        // If the mouse travelled enough to trigger a direction
        if (direction != -1) {
            float nextSnapAngle;
            float[] directionAxis = new float[3];
            directionAxis[direction] = 1f;

            // Reset the animation frame counter
            snapBackFrameCount = 0;

            // Save the released rotation quaternion
            releasedQuat.set(actualQuat);

            // Round the angle that the cube has been rotated since the mouse was pressed to 90°
            nextSnapAngle = (float)(Math.round(rotatedSincePress / (Math.PI/2)) * (Math.PI/2));

            // If the mouse was released shortly after it was pressed, increase or decrease the rotation by 90°
            if (actualFrame < pressedFrame + FAST_ROTATION_CLICK_SPEED) {
                // Increase or decrease the rotation by 90°
                if (rotatedSincePress > 0)
                    nextSnapAngle += Math.PI/2;
                else
                    nextSnapAngle -= Math.PI/2;
            }

            // Create a quaternion with the rounded angle
            snapToQuat.setFromAngleNormalAxis(nextSnapAngle, directionAxis);

            snapToQuat.mult(pressedQuat);

            // Calculate the angle the cube must rotate to reach the next step
            snapBackDiff = rotatedSincePress - nextSnapAngle;

            // Reset the direction
            direction = -1;
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (mousePressed) {
            int mouseMovedX = Math.abs(mousePressedX - mouseEvent.getX());
            int mouseMovedY = Math.abs(mousePressedY - mouseEvent.getY());
            Quaternion stepRotQuat = new Quaternion();

            // Process in which direction the mouse moved
            if (direction == -1) {
                if (mouseMovedX > MOVE_DISTANCE_THRESHOLD || mouseMovedY > MOVE_DISTANCE_THRESHOLD) {
                    if (mouseMovedX > mouseMovedY)
                        direction = 1;
                    else {
                        if (mousePressedX < windowWidth / 2)
                            direction = 2;
                        else
                            direction = 0;
                    }
                }
            }
            else {
                // Differentiation whether the X or Y axis is used for the calculation
                if (direction == 1) {
                    float mouseMoved = (float)Math.toRadians(mouseEvent.getX() - mousePressedX);
                    rotatedSincePress = mouseMoved * CUBE_ROTATION_SPEED;
                    stepRotQuat.setFromAngleNormalAxis(rotatedSincePress, new float[]{0, 1, 0});
                }
                else {
                    float mouseMoved = (float)Math.toRadians(mouseEvent.getY() - mousePressedY);
                    rotatedSincePress = mouseMoved * CUBE_ROTATION_SPEED;
                    if (direction == 2)
                        stepRotQuat.setFromAngleNormalAxis(rotatedSincePress, new float[]{0, 0, 1});
                    else
                        stepRotQuat.setFromAngleNormalAxis(rotatedSincePress, new float[]{1, 0, 0});
                }
                actualQuat = stepRotQuat.mult(pressedQuat);
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseEvent mouseEvent) {
    }

    public void nextFrame() {
        actualFrame++;
        if (!mousePressed && !actualQuat.equals(snapToQuat))
            snapBack();
    }

    private void snapBack() {
        swingingBack = true;
        snapBackFrameCount++;

        float animationLength = Math.round(Math.abs(snapBackDiff * CUBE_SNAP_BACK_SPEED));

        if (animationLength != 0 && snapBackFrameCount < animationLength)
            actualQuat.setSlerp(releasedQuat, snapToQuat, easeOut(snapBackFrameCount, 0, 1, animationLength));
        else {
            actualQuat.set(snapToQuat);
            swingingBack = false;
        }
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
    private float easeOut(float t, float b, float c, float d) {
        t /= d;
        t--;
        return c*(t*t*t + 1) + b;
    }

    public Quaternion getActualQuat() {
        return actualQuat;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }
}
