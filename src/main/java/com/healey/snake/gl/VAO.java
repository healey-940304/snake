package com.healey.snake.gl;

import com.healey.snake.util.Utils;

import static org.lwjgl.opengl.GL30.*;

public class VAO {
    private int vao;
    private int vbo;
    private int ebo;
    private int indiceLength;

    public VAO(Vec2[] vertices, int[] indices) {
        indiceLength = indices.length;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Utils.toArray(vertices), GL_STATIC_DRAW);
        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, Float.BYTES * 2, 0);
        glEnableVertexAttribArray(0);
    }

    public void bind() {
        glBindVertexArray(vao);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void draw() {
        glDrawElements(GL_TRIANGLES, indiceLength, GL_UNSIGNED_INT, 0);
    }
}
