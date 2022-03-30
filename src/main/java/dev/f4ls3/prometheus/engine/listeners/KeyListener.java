package dev.f4ls3.prometheus.engine.listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

    private final boolean keyPressed[];

    public KeyListener() {
        this.keyPressed = new boolean[350];
    }

    public void keyCallback(long _window, int key, int _scancode, int action, int mods) {
        if(key > this.keyPressed.length) return;

        if(action == GLFW_PRESS) this.keyPressed[key] = true;
        else if(action == GLFW_RELEASE) this.keyPressed[key] = false;
    }

    public boolean isKeyPressed(int key) {
        if(key > this.keyPressed.length) throw new IndexOutOfBoundsException(key + " is not a valid keycode");
        return this.keyPressed[key];
    }
}
