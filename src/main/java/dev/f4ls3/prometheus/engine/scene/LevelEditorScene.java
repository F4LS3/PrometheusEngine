package dev.f4ls3.prometheus.engine.scene;

import dev.f4ls3.prometheus.engine.Engine;

import static org.lwjgl.opengl.GL20.*;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {
        super(0);
    }

    public void init() {
        // Load Shaders from Files
        String vertexShader = "#version 330 core\n" +
                "layout (location=0) in vec3 aPos;\n" +
                "layout (location=1) in vec4 aColor;\n" +
                "\n" +
                "out vec4 fColor;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    fColor = aColor;\n" +
                "    gl_Position = vec4(aPos, 1.0);\n" +
                "}";
        String fragmentShader = "#version 330 core\n" +
                "\n" +
                "in vec4 fColor;\n" +
                "\n" +
                "out vec4 color;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    color = fColor;\n" +
                "}";

        // Load and Compile Vertex Shader
        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexShader);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            Engine.getLogger().severe("Vertex Shader compilation failed");
            Engine.getLogger().severe(glGetShaderInfoLog(vertexID, len));
        }
        Engine.getLogger().info("Vertex Shader compiled successfully");

        // Load and Compile Fragment Shader
        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentShader);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            Engine.getLogger().severe("Fragment Shader compilation failed");
            Engine.getLogger().severe(glGetShaderInfoLog(fragmentID, len));
        }
        Engine.getLogger().info("Fragment Shader compiled successfully");

        // Link Shaders and Check for Errors
        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            Engine.getLogger().severe("Shader linking failed");
            Engine.getLogger().severe(glGetProgramInfoLog(shaderProgram, len));
        }
        Engine.getLogger().info("Shaders linked successfully");
    }

    @Override
    public void update(final float delta) {
    }
}
