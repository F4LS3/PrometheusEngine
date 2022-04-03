package dev.f4ls3.prometheus.engine;

import dev.f4ls3.prometheus.engine.listeners.ControllerListener;
import dev.f4ls3.prometheus.engine.listeners.KeyListener;
import dev.f4ls3.prometheus.engine.listeners.MouseListener;
import dev.f4ls3.prometheus.engine.scene.LevelEditorScene;
import dev.f4ls3.prometheus.engine.scene.SceneManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Engine {

    private static Logger logger = null;

    private static KeyListener keyListener = null;
    private static MouseListener mouseListener = null;
    private static ControllerListener controllerListener = null;

    public static void init() {
        InputStream stream = Engine.class.getClassLoader().getResourceAsStream("logging.properties");

        try {
            LogManager.getLogManager().readConfiguration(stream);
            logger = Logger.getLogger("Engine");
        } catch (IOException e) {
            e.printStackTrace();
        }

        SceneManager.init();
        SceneManager.registerScene(new LevelEditorScene());
        SceneManager.getCurrentScene().init();
    }

    public static KeyListener setKeyListener(KeyListener keyListener) {
        return Engine.keyListener = keyListener;
    }

    public static MouseListener setMouseListener(MouseListener mouseListener) {
        return Engine.mouseListener = mouseListener;
    }

    public static ControllerListener setControllerListener(ControllerListener controllerListener) {
        return Engine.controllerListener = controllerListener;
    }

    public static Logger getLogger() {
        if(logger == null) init();
        return logger;
    }

    public static KeyListener getKeyListener() {
        return keyListener;
    }

    public static MouseListener getMouseListener() {
        return mouseListener;
    }

    public static ControllerListener getControllerListener() {
        return controllerListener;
    }
}
