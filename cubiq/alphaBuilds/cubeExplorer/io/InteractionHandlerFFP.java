package cubiq.alphaBuilds.cubeExplorer.io;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class InteractionHandlerFFP implements MouseListener {

    private final int MOVE_DISTANCE_THRESHOLD = 5;
    private final int FAST_ROTATION_CLICK_SPEED = 7;
    private final float CUBE_ROTATION_SPEED = 0.15f;
    private final float CUBE_SNAP_BACK_SPEED = 2.5f;
    private int pressedPosX, pressedPosY;
    private final float[] actualAngles, pressedAngles, releasedAngles, diffsToSteps, nextSteps;
    private float[] xOrientation, yOrientation, zOrientation;
    private int windowWidth, windowHeight;
    private int direction = -1; // -1 -> no direction; 0 -> x; 1 -> y; 2 -> z
    private boolean mousePressed = false;
    private boolean swingingBack = false;
    private int snapBackFrameCount = 0;
    private int actualFrame, pressedFrame;

    // TODO ACHSEN VERÄNDERN SICH BEIM DREHEN


    public InteractionHandlerFFP() {
        actualAngles = new float[] {0, 0, 0};
        pressedAngles = new float[] {0, 0, 0};
        releasedAngles = new float[] {0, 0, 0};
        diffsToSteps = new float[] {0, 0, 0};
        nextSteps = new float[] {0, 0, 0};
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
        if (mouseEvent.getButton() == 1 && !swingingBack) {
            mousePressed = true;
            pressedFrame = actualFrame;
            pressedPosX = mouseEvent.getX();
            pressedPosY = mouseEvent.getY();

            // Store the angles of the cube when the mouse was pressed
            System.arraycopy(actualAngles, 0, pressedAngles, 0, 3);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mousePressed) {
            mousePressed = false;
            // Reset the animation frame counter
            snapBackFrameCount = 0;
            float[] diffAngles = new float[3];

            for (int i = 0; i < 3; i++) {
                // The angle that the cube moved since the mouse was pressed
                diffAngles[i] = actualAngles[i] - pressedAngles[i];
                // Next angle in 90° steps
                nextSteps[i] = pressedAngles[i] + Math.round(diffAngles[i] / 90) * 90;
            }

            // If the mouse was released shortly after it was pressed
            if (actualFrame < pressedFrame + FAST_ROTATION_CLICK_SPEED) {
                // If the mouse travelled enough to trigger the direction
                if (direction > -1) {
                    // Set the angle the cube should rotate to, one 90° step further
                    if (diffAngles[direction] > 0)
                        nextSteps[direction] += 90;
                    else
                        nextSteps[direction] -= 90;
                }
            }

            for (int i = 0; i < 3; i++) {
                // Calculate the angles the cube must rotate to reach the next 90° steps
                diffsToSteps[i] = nextSteps[i] - actualAngles[i];
                // Save the angles of the cube when the mouse was released
                releasedAngles[i] = actualAngles[i];
            }

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

            int currentPosX = mouseEvent.getX();
            int currentPosY = mouseEvent.getY();
            int mouseMovedX = Math.abs(pressedPosX - currentPosX);
            int mouseMovedY = Math.abs(pressedPosY - currentPosY);
            int mouseAbsoluteMoved;

            if (direction == -1) {
                // Process in which direction the mouse moved
                if (mouseMovedX > MOVE_DISTANCE_THRESHOLD || mouseMovedY > MOVE_DISTANCE_THRESHOLD) {
                    if (mouseMovedX > mouseMovedY)
                        direction = 1;
                    else {
                        if (pressedPosX < windowWidth / 2)
                            direction = 2;
                        else
                            direction = 0;
                    }
                }
            }
            else {
                // Differentiation whether the X or Y axis is used for the calculation
                if (direction == 1) mouseAbsoluteMoved = currentPosX - pressedPosX;
                else mouseAbsoluteMoved = currentPosY - pressedPosY;

                // Dragged movement of the cube
                actualAngles[direction] = pressedAngles[direction] + mouseAbsoluteMoved * CUBE_ROTATION_SPEED;
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseEvent mouseEvent) {
    }

    public void nextFrame() {
        actualFrame++;
        swingBack();
    }

    private void swingBack() {
        if (!mousePressed) {
            for (int i = 0; i < 3; i++) {
                if (actualAngles[i] != nextSteps[i]) {
                    swingingBack = true;

                    int animationLength = Math.round(Math.abs(diffsToSteps[i] / CUBE_SNAP_BACK_SPEED));
                    actualAngles[i] = Math.round(easeOut(snapBackFrameCount, releasedAngles[i], diffsToSteps[i], animationLength) * 100f) / 100f;

                    snapBackFrameCount++;
                    if (snapBackFrameCount >= animationLength) {
                        actualAngles[i] = nextSteps[i];
                        swingingBack = false;
                    }
                }
            }
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

    public float getActualAngleX() {
        return actualAngles[0];
    }

    public float getActualAngleY() {
        return actualAngles[1];
    }

    public float getActualAngleZ() {
        return actualAngles[2];
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }
}
