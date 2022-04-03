package dev.f4ls3.prometheus.engine.scene;

import dev.f4ls3.prometheus.engine.renderer.Shader;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private float[] vertexArray = {
        0.5f, -0.5f, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f,
        -0.5f, 0.5f, 0.0f,   0.0f, 1.0f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f,    0.0f, 0.0f, 1.0f, 1.0f,
        -0.5f, -0.5f, 0.0f,  1.0f, 1.0f, 0.0f, 1.0f,
    };

    private int[] elementArray = {
        2, 1, 0,
        0, 1, 3
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader = new Shader("shaders/default.glsl", "default");

    public LevelEditorScene() {
        super(0);
    }

    public void init() {
        this.defaultShader.compile();

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create FloatBuffer of veticies
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create indices
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeByte = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeByte;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeByte);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(final float delta) {
        this.defaultShader.use();

        // Bind VAO
        glBindVertexArray(vaoID);

        // Enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        this.defaultShader.detach();
    }
}
