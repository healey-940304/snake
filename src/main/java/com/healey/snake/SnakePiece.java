package com.healey.snake;

import com.healey.snake.gl.VAO;
import com.healey.snake.gl.Vec2;

import java.awt.*;

public sealed class SnakePiece permits Snake {
    public Vec2 position = new Vec2();
    public VAO vao;
    public Color color = Color.GREEN;
}
