package dev.f4ls3.prometheus.engine;

import dev.f4ls3.prometheus.engine.listeners.ControllerListener;
import dev.f4ls3.prometheus.engine.listeners.KeyListener;
import dev.f4ls3.prometheus.engine.listeners.MouseListener;
import dev.f4ls3.prometheus.engine.utils.Controller;
import dev.f4ls3.prometheus.engine.utils.Time;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width, height;
    private String title;

    private long windowHandle;

    private MouseListener mouseListener;
    private KeyListener keyListener;
    private ControllerListener controllerListener;

    private Controller controller = new Controller();

    public Window(final int width, final int height, final String title) {
        this.width = width;
        this.height = height;
        this.title = title;

        this.mouseListener = new MouseListener();
        this.keyListener = new KeyListener();
        this.controllerListener = new ControllerListener();

        GLFWErrorCallback.createPrint(System.err).set();

        if(!GLFW.glfwInit()) throw new IllegalStateException("GLFW couldn't be initialized");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

        this.windowHandle = GLFW.glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(this.windowHandle == NULL) throw new RuntimeException("Couldn't create GLFW window");

        // Mouse Listener Callbacks
        glfwSetCursorPosCallback(this.windowHandle, this.mouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(this.windowHandle, this.mouseListener::mouseButtonCallback);
        glfwSetScrollCallback(this.windowHandle, this.mouseListener::mouseScrollCallback);

        // Keyboard Listener Callback
        glfwSetKeyCallback(this.windowHandle, this.keyListener::keyCallback);

        // Joystick Listener Callbacks
        glfwSetJoystickCallback((jid, event) -> this.controllerListener.joystickConnectionCallback(jid, event));
        this.controllerListener.registerConnectedJoysticks();

        glfwMakeContextCurrent(this.windowHandle);
        glfwSwapInterval(1);
        glfwShowWindow(this.windowHandle);

        createCapabilities();
    }

    public void startGameLoop() {
        float beginTime = Time.getTime();
        float endTime;

        while(!GLFW.glfwWindowShouldClose(this.windowHandle)) {
            GLFW.glfwPollEvents();

            // Updating Controller Inputs
            this.controllerListener.updateGamepadInputs();

            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(this.windowHandle);

            endTime = Time.getTime();
            float dt = endTime - beginTime;
            beginTime = endTime;
        }

        glfwFreeCallbacks(this.windowHandle);
        glfwDestroyWindow(this.windowHandle);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public ControllerListener getControllerListener() {
        return controllerListener;
    }
}