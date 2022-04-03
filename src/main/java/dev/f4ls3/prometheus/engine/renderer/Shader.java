package dev.f4ls3.prometheus.engine.renderer;

import dev.f4ls3.prometheus.engine.Engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private final String shaderName;

    private int shaderProgramID;

    private String vertexShader;
    private String fragmentShader;

    public Shader(final String shaderPath, final String shaderName) {
        this.shaderName = shaderName;

        try {
            String sourceShader = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(shaderPath).toURI())));
            String[] splitShader = sourceShader.split("(#type)( )+([a-zA-Z]+)");

            int index = sourceShader.indexOf("#type") + 6;
            int eol = sourceShader.indexOf("\r\n", index);
            String firstPattern = sourceShader.substring(index, eol).trim();

            assert Objects.equals(firstPattern, "vertex") || Objects.equals(firstPattern, "fragment") : "Invalid type specification: " + firstPattern;

            index = sourceShader.indexOf("#type", eol) + 6;
            eol = sourceShader.indexOf("\r\n", index);
            String secondPattern = sourceShader.substring(index, eol).trim();

            assert Objects.equals(secondPattern, "vertex") || Objects.equals(secondPattern, "fragment") : "Invalid type specification: " + secondPattern;

            if(firstPattern.equals("vertex")) this.vertexShader = splitShader[1].trim();
            else this.fragmentShader = splitShader[1].trim();

            if(secondPattern.equals("vertex")) this.vertexShader = splitShader[2].trim();
            else this.fragmentShader = splitShader[2].trim();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void compile() {
        // Load and Compile Vertex Shader
        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, this.vertexShader);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            Engine.getLogger().severe("Vertex Shader [shader=" + this.shaderName + "] compilation failed");
            Engine.getLogger().severe(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }
        Engine.getLogger().info("Vertex Shader [shader=" + this.shaderName + "] compiled successfully");

        // Load and Compile Fragment Shader
        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, this.fragmentShader);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            Engine.getLogger().severe("Fragment Shader [shader=" + this.shaderName + "] compilation failed");
            Engine.getLogger().severe(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }
        Engine.getLogger().info("Fragment Shader [shader=" + this.shaderName + "] compiled successfully");

        // Link Shaders and Check for Errors
        this.shaderProgramID = glCreateProgram();
        glAttachShader(this.shaderProgramID, vertexID);
        glAttachShader(this.shaderProgramID, fragmentID);
        glLinkProgram(this.shaderProgramID);

        success = glGetProgrami(this.shaderProgramID, GL_LINK_STATUS);
        if(success == GL_FALSE) {
            int len = glGetProgrami(this.shaderProgramID, GL_INFO_LOG_LENGTH);
            Engine.getLogger().severe("Shader linking [shader=" + this.shaderName + "] failed");
            Engine.getLogger().severe(glGetProgramInfoLog(this.shaderProgramID, len));
            assert false : "";
        }
        Engine.getLogger().info("Shaders linked [shader=" + this.shaderName + "] successfully");
    }

    public void use() {
        glUseProgram(this.shaderProgramID);
    }

    public void detach() {
        glUseProgram(0);
    }
}
