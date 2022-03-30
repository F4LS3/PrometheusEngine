package dev.f4ls3.prometheus.engine.listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean mouseButtonPressed[];
    private boolean isDragging;

    public MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;

        this.mouseButtonPressed = new boolean[5];
    }

    public void mousePosCallback(long _window, double xPos, double yPos) {
        this.lastX = this.xPos;
        this.lastY = this.yPos;

        this.xPos = xPos;
        this.yPos = yPos;

        this.isDragging = this.mouseButtonPressed[0]
                || this.mouseButtonPressed[1]
                || this.mouseButtonPressed[2]
                || this.mouseButtonPressed[3]
                || this.mouseButtonPressed[4];
    }

    public void mouseButtonCallback(long _window, int button, int action, int mods) {
        if(button > this.mouseButtonPressed.length) return;

        if(action == GLFW_RELEASE) {
            this.mouseButtonPressed[button] = false;
            this.isDragging = false;
            return;
        }

        if(action == GLFW_PRESS) this.mouseButtonPressed[button] = true;
    }

    public void mouseScrollCallback(long _window, double xOffset, double yOffset) {
        this.scrollX = xOffset;
        this.scrollY = yOffset;
    }

    public double getScrollX() {
        return scrollX;
    }

    public double getScrollY() {
        return scrollY;
    }

    public double getX() {
        return xPos;
    }

    public double getY() {
        return yPos;
    }

    public double getLastX() {
        return lastX;
    }

    public double getLastY() {
        return lastY;
    }

    public float getDx() {
        return (float)(this.lastX - this.xPos);
    }

    public float getDy() {
        return (float)(this.lastY - this.yPos);
    }

    public boolean isDragging() {
        return isDragging;
    }

    public boolean mouseButtonDown(int button) {
        if(button > this.mouseButtonPressed.length) return false;

        return this.mouseButtonPressed[button];
    }
}
