package com.healey.snake.util;

import com.healey.snake.gl.VAO;
import com.healey.snake.gl.Vec2;

import java.io.IOException;

public class Utils {
    public static float[] toArray(Vec2[] vec2s) {
        float[] floats = new float[vec2s.length * 2];
        for (int i = 0; i < vec2s.length; i++) {
            var vec2 = vec2s[i];
            floats[i * 2] = vec2.x;
            floats[i * 2 + 1] = vec2.y;
        }
        return floats;
    }

    public static String readResource(String path) {
        var is = Utils.class.getClassLoader().getResourceAsStream(path);
        if (is == null) return null;

        StringBuilder builder = new StringBuilder();
        try {
            while (is.available() > 0) builder.append((char) is.read());
        } catch (IOException e) {throw new RuntimeException(e);}

        return builder.toString();
    }

    public static VAO createBoxVAO() {
        return new VAO(new Vec2[] {
                new Vec2(-0.5f, -0.5f),
                new Vec2(0.5f, -0.5f),
                new Vec2(-0.5f, 0.5f),
                new Vec2(0.5f, 0.5f)
        }, new int[] {
                0, 1, 2, 1, 2, 3
        });
    }
}
