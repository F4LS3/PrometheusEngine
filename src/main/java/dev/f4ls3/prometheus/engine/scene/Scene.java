package dev.f4ls3.prometheus.engine.scene;

public abstract class Scene {

    private int sceneID;

    protected Scene(final int sceneID) {
        this.sceneID = sceneID;

    }

    public void init(){}

    public abstract void update(final float delta);

    public int getSceneID() {
        return sceneID;
    }
}
