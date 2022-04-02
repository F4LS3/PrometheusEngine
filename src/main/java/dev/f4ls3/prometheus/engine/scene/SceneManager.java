package dev.f4ls3.prometheus.engine.scene;

import dev.f4ls3.prometheus.engine.Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SceneManager {

    private static SceneManager instance;

    private static List<Scene> scenes;
    private static Scene currentScene;

    public static void init() {
        instance = new SceneManager();
        scenes = new ArrayList<>();
    }

    public static Scene changeScene(final int sceneID) {
        Scene scene = scenes.stream().filter(sc -> sc.getSceneID() == sceneID).findFirst().orElse(null);
        if(scene == null) {
            Engine.getLogger().warning("Couldn't change scene because no scene with ID " + sceneID + " has been found");
            return currentScene;
        }

        currentScene = scene;
        Engine.getLogger().info("Changed scene to " + scene.getSceneID());
        return scene;
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static void registerScene(Scene scene) {
        if(scene == null) {
            Engine.getLogger().severe("Tried to register scene which is null");
            return;
        }

        if(scenes.size() == 0) currentScene = scene;

        scenes.add(scene);
        Engine.getLogger().info("Scene has been registered [id=" + scene.getSceneID() + "]");
    }
}
