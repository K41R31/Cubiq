package cubiq.alphaBuilds.cubeExplorer.io;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class InteractionHandler implements MouseListener {

    private int lastX, lastY;
    private float angleXaxis = 0, angleYaxis = 0, angleZaxis = 0;
    private float tempAngleXaxis, tempAngleYaxis, tempAngleZaxis;
    private int MOVE_DISTANCE_THRESHOLD = 5;
    private int halfWindowWidth = 350, halfWindowHeight = 350;
    private int direction = 0; // 0 -> no direction; 1 -> x; 2 -> y left; 3 -> y right


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
        if (mouseEvent.getButton() == 1) {
            lastX = mouseEvent.getX();
            lastY = mouseEvent.getY();
            tempAngleXaxis = angleXaxis;
            tempAngleYaxis = angleYaxis;
            tempAngleZaxis = angleZaxis;
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 1) {
            direction = 0;
            float mod90Y = angleYaxis % 90;

            if (tempAngleYaxis < angleYaxis) {
                if (mod90Y < -45 || mod90Y > 0 && mod90Y <= 45)
                    angleYaxis = tempAngleYaxis - 90;
                else if (mod90Y >= -45 && mod90Y <= 0 || mod90Y > 45)
                    angleYaxis = tempAngleYaxis;
            }
            else {
                if (mod90Y >= 0 && mod90Y < 45 || mod90Y > -45)
                    angleYaxis = tempAngleYaxis;
                else if (mod90Y > 45 || mod90Y < -45)
                    angleYaxis = tempAngleYaxis + 90;
            }
            System.out.println("actualY: " + angleYaxis + ", tempY: " + tempAngleYaxis + ", mod90Y: " + mod90Y);
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 1) {

            int currentX = mouseEvent.getX();
            int currentY = mouseEvent.getY();
            int movedX = Math.abs(lastX - currentX);
            int movedY = Math.abs(lastY - currentY);

            switch (direction) {
                case 0:
                    if (movedX > MOVE_DISTANCE_THRESHOLD || movedY > MOVE_DISTANCE_THRESHOLD) {
                        if (movedX > movedY)
                            direction = 1;
                        else {
                            if (lastX < halfWindowWidth)
                                direction = 2;
                            else
                                direction = 3;
                        }
                    }
                    break;
                case 1:
                    angleYaxis = (float)(tempAngleYaxis + (lastX - currentX) * -0.15);
                    break;
                case 2:
                    angleZaxis = (float)(tempAngleZaxis + (lastY - currentY) * -0.15);
                    break;
                case 3:
                    angleXaxis = (float)(tempAngleXaxis + (lastY - currentY) * -0.15);
                    break;
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseEvent mouseEvent) {

    }

    public float getAngleXaxis() {
        return angleXaxis;
    }

    public float getAngleYaxis() {
        return angleYaxis;
    }

    public float getAngleZaxis() {
        return angleZaxis;
    }
}
