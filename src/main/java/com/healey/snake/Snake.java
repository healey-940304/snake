package com.healey.snake;

import com.healey.snake.gl.Vec2;

import java.util.ArrayList;
import java.util.List;

public final class Snake extends SnakePiece {
    private final List<SnakePiece> pieces = new ArrayList<>();
    public Direction direction = Direction.RIGHT;
    public Direction directionLastMoved = Direction.RIGHT;

    Snake() {pieces.add(this);}

    public void move() {
        for (int i = pieces.size() - 1; i >= 0; i--) {
            if (i != 0) pieces.get(i).position = pieces.get(i - 1).position;
            else {
                switch (direction) {
                    case LEFT -> position = position.add(Vec2.LEFT);
                    case RIGHT -> position = position.add(Vec2.RIGHT);
                    case UP -> position = position.add(Vec2.UP);
                    case DOWN -> position = position.add(Vec2.DOWN);
                }
                directionLastMoved = direction;
            }
        }
    }

    public List<SnakePiece> getPieces() {
        return pieces;
    }
}
