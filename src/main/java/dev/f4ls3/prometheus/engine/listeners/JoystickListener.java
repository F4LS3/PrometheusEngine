package dev.f4ls3.prometheus.engine.listeners;

import dev.f4ls3.prometheus.engine.utils.Settings;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class JoystickListener {

    private boolean[] joysticks;

    public JoystickListener() {
        this.joysticks = new boolean[16];
    }

    public void joystickConnectionCallback(int joystickID, int event) {
        if(event == GLFW_CONNECTED) {
            Settings.CONTROLLER_CONNECTED = true;
            Settings.CONTROLLER_NAME = glfwGetJoystickName(joystickID);
        } else if(event == GLFW_DISCONNECTED) {
            Settings.CONTROLLER_CONNECTED = false;
            Settings.CONTROLLER_NAME = "";
        }

        this.joysticks[joystickID] = glfwJoystickPresent(joystickID);
    }

    public void updateJoystickAxisStates() {
        for (int i = 0; i < this.joysticks.length; i++) {
            if(!this.joysticks[i]) continue;

            FloatBuffer axisState = glfwGetJoystickAxes(i);
            System.out.println(axisState.get());
        }
    }
}
