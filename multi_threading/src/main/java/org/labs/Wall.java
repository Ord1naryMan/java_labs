package org.labs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class Wall implements Runnable{

    private final double width = 100;
    private final double height = 20;
    private double x = -1;
    private double y = -1;
    private double MOVEMENT_SPEED;

    private WallKeyboardListener wallKeyboardListener;
    private boolean hasAI = false;
    private boolean isSecond = false;
    private final Field field;

    public Wall(Field field, boolean hasAi) {
        this.field = field;
        this.hasAI = hasAi;
        MOVEMENT_SPEED = 20;
    }

    public Wall(Field field, Difficulty difficulty) {
        this.field = field;

        setDifficulty(difficulty);
    }


    public void paint(Graphics2D canvas) {
        if (x == -1 && y == -1) {
            moveToStart();
        }
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        Rectangle2D.Double rectangle = new Rectangle2D.Double(
                x - width / 2,
                y - height / 2,
                width,
                height
        );

        canvas.draw(rectangle);
        canvas.fill(rectangle);
    }

    private void moveToStart() {
        if (hasAI || isSecond) {
            y = height * 3;
        } else {
            y = field.getToolkit().getScreenSize().getHeight() - height * 8;
        }
        x = field.getToolkit().getScreenSize().getWidth() / 2;
    }

    public double getStartX() {
        return x - width / 2.0;
    }

    public double getStartY() {
        return y - height / 2.0;
    }

    public double getEndX() {
        return x + width / 2.0;
    }

    public double getEndY() {
        return y + height / 2.0;
    }

    public void setDifficulty(Difficulty diff) {
        MOVEMENT_SPEED = switch (diff) {
            case EASY -> 20;
            case NORMAL -> 15;
            case HARD -> 10;
        };
    }

    public WallKeyboardListener getKeyListener() {
        if (wallKeyboardListener == null) {
            wallKeyboardListener = new WallKeyboardListener();
        }
        return wallKeyboardListener;
    }

    @Override
    public void run() {
        while (true) {
            if (!hasAI) {
                throw new UnsupportedOperationException("This wall doesn't have an AI");
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            List<BouncingBall> balls = field.getBalls();
            if (balls.isEmpty()) {
                continue;
            }
            BouncingBall nearestBall = balls.get(0);
            for (var ball : balls) {
                if (ball == null) {
                    break;
                }
                if (ball.getY() < nearestBall.getY()) {
                    nearestBall = ball;
                }
            }
            if (nearestBall == null) {
                continue;
            }
            if (x < nearestBall.getX()) {
                x += MOVEMENT_SPEED;
            }
            if (x > nearestBall.getX()) {
                x -= MOVEMENT_SPEED;
            }
        }
    }

    public void makeSecond() {
        isSecond = true;
    }

    public class WallKeyboardListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent keyEvent) {
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if ((keyEvent.getKeyCode() == 39 && !isSecond) ||
                    (isSecond && keyEvent.getKeyCode() == 68)) {
                x += MOVEMENT_SPEED;
                if (x > field.getWidth()) {
                    x = field.getWidth();
                }
            }

            if ((keyEvent.getKeyCode() == 37 && !isSecond) ||
                    (isSecond && keyEvent.getKeyCode() == 65)) {
                x -= MOVEMENT_SPEED;
                if (x < 0) {
                    x = 0;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
        }
    }
}
