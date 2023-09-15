package org.labs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

import static java.lang.Math.abs;

public class Field extends JPanel {
    // Флаг приостановленности движения
    private boolean paused;
    // Динамический список скачущих мячей
    private final List<BouncingBall> balls = new ArrayList<>() {{add(null);}};
    private int ballIndex = 0;
    private final Wall wall;
    private final List<List<Integer>> blocks = List.of(
            new ArrayList<>() {{addAll(List.of(1, 1, 1, 1, 1, 1, 1, 1, 1, 1));}},
            new ArrayList<>() {{addAll(List.of(2, 2, 2, 2, 2, 2, 2, 2, 2, 2));}},
            new ArrayList<>() {{addAll(List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 0));}},
            new ArrayList<>() {{addAll(List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 0));}}
    );

    private int BLOCK_HEIGHT;
    private int BLOCK_WIDTH;

    private final Cloner cloner;
    private final Deleter deleter;
    private final Teleporter teleporter1;
    private final Teleporter teleporter2;

    // Конструктор класса BouncingBall
    public Field() {
// Установить цвет заднего фона белым
        setBackground(Color.WHITE);
// Запустить таймер
        // Класс таймер отвечает за регулярную генерацию событий ActionEvent
        // При создании его экземпляра используется анонимный класс,
        // реализующий интерфейс ActionListener
        // Задача обработчика события ActionEvent - перерисовка окна
        Timer repaintTimer = new Timer(10, ev -> {
// Задача обработчика события ActionEvent - перерисовка окна
            checkCollisionWithBlocks();
            checkCollisionWithWall();
            checkCollisionWithBuff();
            repaint();
        });

        wall = new Wall(this);
        cloner = new Cloner(this);
        deleter = new Deleter(this);
        teleporter1 = new Teleporter(this);
        teleporter2 = new Teleporter(this);
        teleporter2.link(teleporter1);
        teleporter1.link(teleporter2);

        repaintTimer.start();
    }

    // Унаследованный от JPanel метод перерисовки компонента
    public void paintComponent(Graphics g) {

        BLOCK_WIDTH = getWidth() / blocks.get(0).size();
        //we want to fill only 10% of the screens height
        BLOCK_HEIGHT = (int) (getHeight() * 0.1 / blocks.size());

// Вызвать версию метода, унаследованную от предка
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
// Последовательно запросить прорисовку от всех мячей из списка
        for (BouncingBall ball : balls) {
            if (ball != null) {
                ball.paint(canvas);
            }
        }

        for (int i = 0; i < blocks.size(); i++) {
            for (int j = 0; j < blocks.get(i).size(); j++) {
                if (blocks.get(i).get(j) != 0) {
                    canvas.setColor(Color.BLACK);
                    canvas.fillRect(
                            j * BLOCK_WIDTH,
                            i * BLOCK_HEIGHT,
                            BLOCK_WIDTH,
                            BLOCK_HEIGHT
                    );

                    canvas.setColor(Color.WHITE);
                    //draw outline
                    canvas.drawRect(
                            j * BLOCK_WIDTH,
                            i * BLOCK_HEIGHT,
                            BLOCK_WIDTH,
                            BLOCK_HEIGHT
                    );

                    canvas.drawString(
                            String.valueOf(blocks.get(i).get(j)),
                            j * BLOCK_WIDTH + BLOCK_WIDTH / 2,
                            i * BLOCK_HEIGHT + BLOCK_HEIGHT / 2
                    );
                }
            }
        }
        wall.paint(canvas);
        cloner.paint(canvas);
        deleter.paint(canvas);
        teleporter1.paint(canvas);
        teleporter2.paint(canvas);
    }

    // Метод добавления нового мяча в список
    public void addBall() {
//Заключается в добавлении в список нового экземпляра BouncingBall
// Всю инициализацию положения, скорости, размера, цвета
// BouncingBall выполняет сам в конструкторе
        if (ballIndex == balls.size()) {
            BouncingBall ball = new BouncingBall(this);
            balls.add(new BouncingBall(this));
        } else {
            balls.set(ballIndex, new BouncingBall(this));
        }
        ballIndex++;
        if (ballIndex == balls.size()) {
            ballIndex = 0;
        }
    }

    public void addBall(double x, double y) {
        addBall();
        balls.get(balls.size() - 1).setXY(x, y);
    }

    public void removeBall() {
        if (ballIndex == 0) {
            balls.remove(balls.size() - 1);
        } else {
            balls.remove(ballIndex - 1);
        }
    }

    // Метод синхронизированный, т.е. только один поток может
// одновременно быть внутри
    public synchronized void pause() {
// Включить режим паузы
        paused = true;
    }

    // Метод синхронизированный, т.е. только один поток может
// одновременно быть внутри
    public synchronized void resume() {
// Выключить режим паузы
        paused = false;
// Будим все ожидающие продолжения потоки
        notifyAll();
    }

    // Синхронизированный метод проверки, может ли мяч двигаться
// (не включен ли режим паузы?)
    public synchronized void canMove() throws
            InterruptedException {
        if (paused) {
// Если режим паузы включен, то поток, зашедший
// внутрь данного метода, засыпает
            wait();
        }
    }

    private void checkCollisionWithWall() {
        for (var ball : balls) {
            if (ball == null) {
                return;
            }
            double r = ball.getRadius();

            double wallTop = wall.getStartY();
            double wallLeft = wall.getStartX();
            double wallRight = wall.getEndX();
            double wallBottom = wall.getEndY();

            double ballTop = ball.getY() - r;
            double ballLeft = ball.getX() - r;
            double ballRight = ball.getX() + r;
            double ballBottom = ball.getY() + r;

            if (ballTop <= wallBottom &&
                    ball.getX() <= wallRight &&
                    ball.getX() >= wallLeft &&
                    abs(ballTop - wallBottom) < abs(wallTop - ballBottom)) {
                ball.setSpeed(ball.getSpeedX(), -ball.getSpeedY());
                return;
            }

            if (ballBottom >= wallTop &&
                    ball.getX() <= wallRight &&
                    ball.getX() >= wallLeft &&
                    abs(ballTop - wallBottom) > abs(wallTop - ballBottom)) {
                ball.setSpeed(ball.getSpeedX(), -ball.getSpeedY());
                return;
            }

            if (ballRight >= wallLeft &&
                    ball.getY() >= wallTop &&
                    ball.getY() <= wallBottom &&
                    abs(ballRight - wallLeft) < abs(ballLeft - wallRight)) {
                ball.setSpeed(-ball.getSpeedX(), ball.getSpeedY());
                return;
            }

            if (ballLeft <= wallRight &&
                    ball.getY() >= wallTop &&
                    ball.getY() <= wallBottom &&
                    abs(ballRight - wallLeft) > abs(ballLeft - wallRight)) {
                ball.setSpeed(-ball.getSpeedX(), ball.getSpeedY());
                return;
            }
        }
    }

    private void checkCollisionWithBlocks() {
        for (var ball : balls) {
            if (ball == null) {
                return;
            }
            for (int i = 0; i < blocks.size(); i++) {
                for (int j = 0; j < blocks.get(0).size(); j++) {
                    if (blocks.get(i).get(j) == 0) {
                        continue;
                    }
                    if (ball.getY() - ball.getRadius() <= (i + 1) * BLOCK_HEIGHT &&
                    ball.getX() <= (j + 1) * BLOCK_WIDTH &&
                    ball.getX() >= j * BLOCK_WIDTH
                    ) {
                        ball.setSpeed(ball.getSpeedX(), -ball.getSpeedY());
                        blocks.get(i).set(j, blocks.get(i).get(j) - 1);
                    }
                }
            }
        }
    }

    private void checkCollisionWithBuff() {
        boolean isAddBall = false;
        boolean isRemoveBall = false;
        BouncingBall ballToRemove = null;
        double whereToAddX = 0, whereToAddY = 0;
        for (var ball : balls) {
            if (ball == null) {
                return;
            }
            if (ball.getX() <= cloner.getX() + cloner.getWidth() &&
            ball.getX() >= cloner.getX() &&
            ball.getY() <= cloner.getY() + cloner.getHeight() &&
            ball.getY() >= cloner.getY()) {
                ballIndex = balls.size();
                isAddBall = true;
                whereToAddX = ball.getX();
                whereToAddY = ball.getY();
                cloner.moveToRandom();
            }

            if (ball.getX() <= deleter.getX() + deleter.getWidth() &&
                    ball.getX() >= deleter.getX() &&
                    ball.getY() <= deleter.getY() + deleter.getHeight() &&
                    ball.getY() >= deleter.getY()) {
                isRemoveBall = true;
                ballToRemove = ball;
                deleter.moveToRandom();
            }

            checkTeleportedAndTeleport(ball, teleporter1);

            checkTeleportedAndTeleport(ball, teleporter2);
        }
        if (isAddBall) {
            addBall(whereToAddX, whereToAddY);
        }
        if (isRemoveBall) {
            if (ballIndex == balls.size() - 1) {
                ballIndex = 0;
            }
            balls.remove(ballToRemove);
        }
    }

    private void checkTeleportedAndTeleport(BouncingBall ball, Teleporter teleporter) {
        if (ball.getX() <= teleporter.getX() + teleporter.getWidth() &&
                ball.getX() >= teleporter.getX() &&
                ball.getY() <= teleporter.getY() + teleporter.getHeight() &&
                ball.getY() >= teleporter.getY()) {

            ball.setXY(
                    teleporter.getOther().getX(),
                    teleporter.getOther().getY()
            );

            teleporter.moveToRandom();
            teleporter.getOther().moveToRandom();
        }
    }
}
