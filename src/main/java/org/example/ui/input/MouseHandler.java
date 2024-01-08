package org.example.ui.input;

import org.example.ui.main.GamePanel;


import java.awt.*;
import java.awt.event.*;

public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {
    GamePanel gp;
    public Point cameraRelativeClick;
    public Point guiRelativeClick;
    public Point cameraRelativeMousePosition;
    public Point guiRelativeMousePosition;
    public Point dragStartPosition;
    public Point dragEndPosition;
    public int wheelRotation;
    public int dragOffsetX;
    public int dragOffsetY;
    public boolean mousePressed = false;


    public MouseHandler(GamePanel gp) {
        this.gp = gp;
        cameraRelativeClick = new Point();
        guiRelativeClick = new Point();
        cameraRelativeMousePosition = new Point();
        guiRelativeMousePosition = new Point();
        dragStartPosition = new Point();
        dragEndPosition = new Point();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int adjustedX = e.getX() + gp.camera.x;
        int adjustedY = e.getY() + gp.camera.y;
        cameraRelativeClick.setLocation(adjustedX, adjustedY);
        guiRelativeClick.setLocation(e.getPoint());
    }

    @Override
    public void  mousePressed(MouseEvent e) {
        dragStartPosition.setLocation(e.getPoint());
        dragEndPosition.setLocation(-1, -1);
        mousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        cameraRelativeClick.setLocation(-1, -1);
        guiRelativeClick.setLocation(-1, -1);
        dragStartPosition.setLocation(-1, -1);
        dragEndPosition.setLocation(e.getPoint());
        dragOffsetX = 0;
        dragOffsetY = 0;
        mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dragOffsetX = e.getX() - dragStartPosition.x;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int adjustedX = e.getX() + gp.camera.x;
        int adjustedY = e.getY() + gp.camera.y;
        cameraRelativeMousePosition.setLocation(adjustedX, adjustedY);
        guiRelativeMousePosition.setLocation(e.getPoint());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        wheelRotation = e.getWheelRotation();
    }

    public void resetClick() {
        guiRelativeClick.setLocation(-1, -1);
        cameraRelativeClick.setLocation(-1, -1);
    }
}
