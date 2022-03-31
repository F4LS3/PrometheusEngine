package dev.f4ls3.prometheus.engine.listeners;

import dev.f4ls3.prometheus.engine.Engine;
import dev.f4ls3.prometheus.engine.utils.Controller;
import org.lwjgl.glfw.GLFWGamepadState;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class ControllerListener {

    private List<Controller> controllers;

    public ControllerListener() {
        this.controllers = new ArrayList(){
            {
                for (int i = 0; i <= GLFW_JOYSTICK_LAST; i++) {
                    add(new Controller());
                }
            }
        };
    }

    public void registerConnectedJoysticks() {
        for (int i = 0; i <= GLFW_JOYSTICK_LAST; i++) {
            if(!glfwJoystickIsGamepad(i)) continue;

            Controller controller = this.controllers.get(i);
            controller.setControllerConnected(glfwJoystickPresent(i));
            controller.setControllerName(glfwGetJoystickName(i));
            this.controllers.set(i, controller);
            Engine.getLogger().info("Controller connected [" + controller.getControllerName() + "]");
        }
    }

    public void joystickConnectionCallback(final int joystickID, final int event) {
        if(joystickID > GLFW_JOYSTICK_LAST + 1) throw new IndexOutOfBoundsException(joystickID + " isn't a valid joystickID");

        Controller controller = this.controllers.get(joystickID);

        if(event == GLFW_CONNECTED) {
            if(!glfwJoystickIsGamepad(joystickID)) return;

            controller.setControllerConnected(glfwJoystickPresent(joystickID));
            controller.setControllerName(glfwGetJoystickName(joystickID));
            Engine.getLogger().info("Controller connected [" + controller.getControllerName() + "]");
        } else if(event == GLFW_DISCONNECTED) {
            Engine.getLogger().info("Controller disconnected [" + controller.getControllerName() + "]");
            controller.setControllerConnected(false);
            controller.setControllerName("");
        }

        this.controllers.set(joystickID, controller);
    }

    public void updateGamepadInputs() {
        for (Controller controller : this.controllers) {
            final GLFWGamepadState state = getGamepadState(this.controllers.indexOf(controller));

            // Get Gamepad Buttons
            for (int i = 0; i <= GLFW_GAMEPAD_BUTTON_LAST; i++) {
                int buttonState = state.buttons(i);
                if(buttonState == GLFW_PRESS) controller.updateControllerButtonState(i, true);
                else if(buttonState == GLFW_RELEASE) controller.updateControllerButtonState(i, false);
            }

            // Get Gamepad Axes
            for (int i = 0; i <= GLFW_GAMEPAD_AXIS_LAST; i++) controller.updateControllerAxisValue(i, state.axes(i));
        }
    }

    public boolean isButtonPressed(final int joystickID, final int button) {
        Controller controller = this.controllers.get(joystickID);
        if(controller == null) return false;
        return controller.getControllerButtons()[button];
    }

    public float getAxisPosition(final int joystickID, final int axis) {
        Controller controller = this.controllers.get(joystickID);
        if(controller == null) return 0.0f;
        return controller.getControllerAxes()[axis];
    }

    public GLFWGamepadState getGamepadState(final int joystickID) {
        if(joystickID > this.controllers.size()) throw new IndexOutOfBoundsException(joystickID + " isn't a valid joystickID");
        if(this.controllers.get(joystickID) == null) return null;
        GLFWGamepadState state = GLFWGamepadState.create();
        glfwGetGamepadState(joystickID, state);
        return state;
    }
}
