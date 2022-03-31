package dev.f4ls3.prometheus.engine.utils;

import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_LAST;

public class Controller {

    private boolean controllerConnected;

    private boolean[] controllerButtons;
    private float[] controllerAxes;

    private String controllerName;

    public Controller() {
        this.controllerConnected = false;
        this.controllerName = "";
        this.controllerButtons = new boolean[GLFW_GAMEPAD_BUTTON_LAST + 1];
        this.controllerAxes = new float[GLFW_GAMEPAD_AXIS_LAST + 1];
    }

    public void updateControllerButtonState(final int button, final boolean state) {
        if(button > this.controllerButtons.length) throw new IndexOutOfBoundsException(button + " isn't a valid button");
        this.controllerButtons[button] = state;
    }

    public void updateControllerAxisValue(final int axis, final float value) {
        if(axis > this.controllerAxes.length) throw new IndexOutOfBoundsException(axis + " isn't a valid axis");
        this.controllerAxes[axis] = value;
    }

    public boolean isControllerConnected() {
        return controllerConnected;
    }

    public void setControllerConnected(boolean controllerConnected) {
        this.controllerConnected = controllerConnected;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public boolean[] getControllerButtons() {
        return controllerButtons;
    }

    public float[] getControllerAxes() {
        return controllerAxes;
    }
}
