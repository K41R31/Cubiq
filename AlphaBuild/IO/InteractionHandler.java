package AlphaBuild.IO;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class InteractionHandler implements MouseListener {

    private int lastMouseLocationX, lastMouseLocationY;
    private float angleXaxis = 0, angleYaxis = 0;


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
            lastMouseLocationX = mouseEvent.getX();
            lastMouseLocationY = mouseEvent.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        int currentX = mouseEvent.getX();
        int currentY = mouseEvent.getY();

        if (mouseEvent.getButton() == 1) {

            if (Math.abs(lastMouseLocationX - currentX) > 10) {
                System.out.println("MOVED 10");
            }
/*
            double deltaX = currentX - lastMouseLocationX;
            double deltaY = currentY - lastMouseLocationY;
            lastMouseLocationX = currentX;
            lastMouseLocationY = currentY;
            angleXaxis += 0.1 * deltaY;
            angleYaxis += 0.1 * deltaX;

 */
        }
    }

    @Override
    public void mouseWheelMoved(MouseEvent mouseEvent) {

    }

    public float getAngleXaxis() {
        return angleXaxis;
    }

    public void setAngleXaxis(float angleXaxis) {
        this.angleXaxis = angleXaxis;
    }

    public float getAngleYaxis() {
        return angleYaxis;
    }

    public void setAngleYaxis(float angleYaxis) {
        this.angleYaxis = angleYaxis;
    }
}
