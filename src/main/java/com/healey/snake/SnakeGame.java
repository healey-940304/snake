package com.healey.snake;

import com.healey.snake.gl.Shader;
import com.healey.snake.gl.VAO;
import com.healey.snake.gl.Vec2;
import com.healey.snake.util.Utils;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;

public class SnakeGame {
    private final Thread renderThread = new Thread(this::renderLoop, "RenderThread");
    private final Thread moveThread = new Thread(this::moveLoop, "MoveThread");

    private final Queue<Runnable> renderTasks = new LinkedList<>();

    private final Snake snake;
    private final SnakePiece cherry;
    private int score;
    private volatile boolean inGame;

    public SnakeGame() {

        cherry = new SnakePiece();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        cherry.position = new Vec2(random.nextInt(21) - 10, random.nextInt(21) - 10);
        cherry.color = Color.RED;
        renderTasks.add(() -> cherry.vao = Utils.createBoxVAO());
        renderThread.start();
        moveThread.start();
        snake = new Snake();
    }

    public static void main(String[] args) {
        new SnakeGame();
    }

    private void renderLoop() {
        if (Thread.currentThread() != renderThread) throw new IllegalCallerException();

        glfwInit();
        glfwDefaultWindowHints();

        long window = glfwCreateWindow(640, 640, "Snake", 0, 0);
        glfwMakeContextCurrent(window);
        createCapabilities();

        glfwSetWindowSizeCallback(window, (window1, width, height) -> {
            if (width != height) {
                glfwSetWindowSize(window, height, height);
                glViewport(0, 0, height, height);
            } else glViewport(0, 0, width, height);
        });

        var shader = new Shader(Utils.readResource("vertex.shader"), Utils.readResource("fragment.shader"));
        snake.vao = Utils.createBoxVAO();

        glfwSetKeyCallback(window, (window1, key, scancode, action, mods) -> {
//            if (snake.directionLastMoved != Direction.DOWN && key == GLFW_KEY_W) snake.direction = Direction.UP;
//            else if (snake.directionLastMoved != Direction.UP && key == GLFW_KEY_S) snake.direction = Direction.DOWN;
//            else if (snake.directionLastMoved != Direction.RIGHT && key == GLFW_KEY_A) snake.direction = Direction.LEFT;
//            else if (snake.directionLastMoved != Direction.LEFT && key == GLFW_KEY_D) snake.direction = Direction.RIGHT;
        });

        inGame = true;
        long lastFrame = System.currentTimeMillis();
        while (!glfwWindowShouldClose(window)) {
            long thisFrame = System.currentTimeMillis();
            long delta = thisFrame - lastFrame;

            if (snake.directionLastMoved != Direction.DOWN && glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) snake.direction = Direction.UP;
            else if (snake.directionLastMoved != Direction.UP && glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) snake.direction = Direction.DOWN;
            else if (snake.directionLastMoved != Direction.RIGHT && glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) snake.direction = Direction.LEFT;
            else if (snake.directionLastMoved != Direction.LEFT && glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) snake.direction = Direction.RIGHT;

            synchronized (renderTasks) {
                if (!renderTasks.isEmpty())
                    for (int i = 0; i < 50; i++) {
                        if (renderTasks.isEmpty()) break;
                        renderTasks.poll().run();
                    }
            }

            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

            shader.use();
            synchronized (snake.getPieces()) {
                snake.getPieces().forEach(snakePiece -> {
                    if (snakePiece.vao == null) return;
                    snakePiece.vao.bind();
                    shader.setVec2("objectOffset", snakePiece.position);
                    shader.setVec3("objectColor", snakePiece.color);
                    snakePiece.vao.draw();
                    snakePiece.vao.unbind();
                });
            }
            cherry.vao.bind();
            shader.setVec2("objectOffset", cherry.position);
            shader.setVec3("objectColor", cherry.color);
            cherry.vao.draw();
            cherry.vao.unbind();

            glfwSwapBuffers(window);
            glfwPollEvents();

            lastFrame = thisFrame;
        }

        glfwTerminate();
        System.exit(1);
    }

    private void moveLoop() {
        if (Thread.currentThread() != moveThread) throw new IllegalCallerException();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        long lastMove = System.currentTimeMillis();
        while (renderThread.isAlive()) {
            long thisMove = System.currentTimeMillis();
            long delta = thisMove - lastMove;

            while (!inGame) Thread.onSpinWait();
            snake.move();
            if (snake.position.x > 10) snake.position = new Vec2(-10.0f, snake.position.y);
            if (snake.position.x < -10) snake.position = new Vec2(10.0f, snake.position.y);
            if (snake.position.y > 10) snake.position = new Vec2(snake.position.x, -10.0f);
            if (snake.position.y < -10) snake.position = new Vec2(snake.position.x, 10.0f);
            if (snake.getPieces().stream().filter(snakePiece -> snakePiece.position.equals(snake.position)).count() > 1) {
                System.exit(1);
                // TODO: 10/8/2021 game over
            }
            if (snake.position.equals(cherry.position)) {

                score++;

                var snakePiece = new SnakePiece();
                snakePiece.position = snake.getPieces().get(snake.getPieces().size() - 1).position;
                snakePiece.color = new Color(Math.max(Math.min((score - 20) % 50 * 5, 255), 0), Math.max(255 - (score % 50 * 10), 0), Math.min(score % 50 * 10, 255));
                synchronized (renderTasks) {
                    renderTasks.add(() -> snakePiece.vao = Utils.createBoxVAO());
                }
                synchronized (snake.getPieces()) {
                    snake.getPieces().add(snakePiece);
                }
                cherry.position = new Vec2(random.nextInt(21) - 10, random.nextInt(21) - 10);
            }

            while (System.currentTimeMillis() - thisMove < 100) Thread.onSpinWait();
            lastMove = thisMove;
        }
    }
}
