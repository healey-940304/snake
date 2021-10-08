package com.healey.snake.gl;

import java.util.Objects;

public class Vec2 {
    public static final Vec2 LEFT = new Vec2(-1.0f, 0.0f);
    public static final Vec2 RIGHT = new Vec2(1.0f, 0.0f);
    public static final Vec2 UP = new Vec2(0.0f, 1.0f);
    public static final Vec2 DOWN = new Vec2(0.0f, -1.0f);

    public final float x, y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(float f) {
        this(f, f);
    }

    public Vec2(float[] floats, int offset) {
        this(floats[offset], floats[offset + 1]);
    }

    public Vec2(float[] floats) {
        this(floats, 0);
    }

    public Vec2(double x, double y) {
        this((float) x, (float) y);
    }

    public Vec2() {
        this(0.0f);
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 add(float x, float y) {
        return new Vec2(this.x + x, this.y + y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec2 vec2 = (Vec2) o;
        return Float.compare(vec2.x, x) == 0 && Float.compare(vec2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vec2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
