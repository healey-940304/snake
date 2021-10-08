package com.healey.snake.gl;

import java.awt.*;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int id;
    private String vertex, fragment;

    public Shader(String vertex, String fragment, boolean init) {
        this.vertex = vertex;
        this.fragment = fragment;
        if (init) glInit();
    }

    public Shader(String vertex, String fragment) {
        this(vertex, fragment, true);
    }

    public void glInit() {
        if (id != 0) throw new IllegalStateException("Shader already initialized!");

        int v = glCreateShader(GL_VERTEX_SHADER);
        int f = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(v, vertex);
        glShaderSource(f, fragment);
        vertex = fragment = null;

        glCompileShader(v);
        if (glGetShaderi(v, GL_COMPILE_STATUS) != 1) throw new RuntimeException(glGetShaderInfoLog(v));

        glCompileShader(f);
        if (glGetShaderi(f, GL_COMPILE_STATUS) != 1) throw new RuntimeException(glGetShaderInfoLog(f));

        id = glCreateProgram();
        glAttachShader(id, v);
        glAttachShader(id, f);

        glLinkProgram(id);
        if (glGetProgrami(id, GL_LINK_STATUS) != 1) throw new RuntimeException(glGetProgramInfoLog(id));

        glDeleteShader(v);
        glDeleteShader(f);
    }

    public void use() {
        glUseProgram(id);
    }

    public void setVec2(String name, Vec2 vec2) {
        glUniform2f(glGetUniformLocation(id, name), vec2.x, vec2.y);
    }

    public void setVec3(String name, Color color) {
        glUniform3f(glGetUniformLocation(id, name), color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
    }
}
