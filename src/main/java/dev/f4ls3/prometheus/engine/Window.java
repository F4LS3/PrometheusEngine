package dev.f4ls3.prometheus.engine;

import dev.f4ls3.prometheus.engine.listeners.ControllerListener;
import dev.f4ls3.prometheus.engine.listeners.KeyListener;
import dev.f4ls3.prometheus.engine.listeners.MouseListener;
import dev.f4ls3.prometheus.engine.renderer.Shader;
import dev.f4ls3.prometheus.engine.scene.SceneManager;
import dev.f4ls3.prometheus.engine.utils.Time;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static float r;
    private static float g;
    private static float b;

    private final long windowHandle;

    private final ControllerListener controllerListener;

    public Window(final int width, final int height, final String title) {
        r = 1.0f;
        g = 1.0f;
        b = 1.0f;

        MouseListener mouseListener = new MouseListener();
        KeyListener keyListener = new KeyListener();
        this.controllerListener = new ControllerListener();

        // Set Global Engine Variables
        Engine.setKeyListener(keyListener);
        Engine.setMouseListener(mouseListener);
        Engine.setControllerListener(this.controllerListener);

        GLFWErrorCallback.createPrint(System.err).set();

        if(!GLFW.glfwInit()) throw new IllegalStateException("GLFW couldn't be initialized");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

        this.windowHandle = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
        if(this.windowHandle == NULL) throw new RuntimeException("Couldn't create GLFW window");

        // Mouse Listener Callbacks
        glfwSetCursorPosCallback(this.windowHandle, mouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(this.windowHandle, mouseListener::mouseButtonCallback);
        glfwSetScrollCallback(this.windowHandle, mouseListener::mouseScrollCallback);

        // Keyboard Listener Callback
        glfwSetKeyCallback(this.windowHandle, keyListener::keyCallback);

        // Joystick Listener Callbacks
        glfwSetJoystickCallback(this.controllerListener::joystickConnectionCallback);
        this.controllerListener.registerConnectedJoysticks();

        glfwMakeContextCurrent(this.windowHandle);
        glfwSwapInterval(1);
        glfwShowWindow(this.windowHandle);

        createCapabilities();

        Engine.init();
    }

    public void startGameLoop() {
        float beginTime = Time.getTime();
        float endTime;
        float delta = -1.0f;

        while(!GLFW.glfwWindowShouldClose(this.windowHandle)) {
            GLFW.glfwPollEvents();

            // Updating Controller Inputs
            this.controllerListener.updateGamepadInputs();

            glClearColor(r, g, b, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if(delta >= 0) SceneManager.getCurrentScene().update(delta);

            glfwSwapBuffers(this.windowHandle);

            endTime = Time.getTime();
            delta = endTime - beginTime;
            beginTime = endTime;
        }

        glfwFreeCallbacks(this.windowHandle);
        glfwDestroyWindow(this.windowHandle);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public static void setRGB(final float red, final float green, final float blue) {
        r = red;
        g = green;
        b = blue;
    }

    public static void setRGB(final float[] rgb) {
        setRGB(rgb[0], rgb[1], rgb[2]);
    }

    public static float[] getRGB() {
        return new float[]{r, g, b};
    }
}