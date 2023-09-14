package org.labs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Optional;
import javax.swing.JPanel;
import javax.swing.Timer;

import static java.lang.Math.abs;

public class Field extends JPanel {
    // Флаг приостановленности движения
    private boolean paused;
    // Динамический список скачущих мячей
    private final BouncingBall[] balls = new BouncingBall[8];
    private int ballIndex = 0;

    private double clickX;
    private double clickY;
    private Optional<BouncingBall> clickedBall = Optional.empty();
    private final Wall wall;

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
            checkCollisionWithBalls();
            checkCollisionWithBlock();
            repaint();
        });

        wall = new Wall(this);

        repaintTimer.start();
    }

    // Унаследованный от JPanel метод перерисовки компонента
    public void paintComponent(Graphics g) {
// Вызвать версию метода, унаследованную от предка
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
// Последовательно запросить прорисовку от всех мячей из списка
        for (BouncingBall ball : balls) {
            if (ball != null) {
                ball.paint(canvas);
            }
        }
        wall.paint(canvas);
    }

    // Метод добавления нового мяча в список
    public void addBall() {
//Заключается в добавлении в список нового экземпляра BouncingBall
// Всю инициализацию положения, скорости, размера, цвета
// BouncingBall выполняет сам в конструкторе
        balls[ballIndex] = new BouncingBall(this);
        ballIndex++;
        if (ballIndex == balls.length) {
            ballIndex = 0;
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

    private void checkCollisionWithBalls() {
        for (int i = 0; i < balls.length - 1; i++) {
            for (int j = i + 1; j < balls.length; j++) {
                if (balls[i] == null || balls[j] == null) {
                    continue;
                }
                double distance = Math.pow(
                        Math.pow(balls[i].getX() - balls[j].getX(), 2) +
                                Math.pow(balls[i].getY() - balls[j].getY(), 2),
                        0.5);
                if (distance <= balls[i].getRadius() + balls[j].getRadius()) {

                    double cx = balls[j].getX() - balls[i].getX();
                    double cy = balls[j].getY() - balls[i].getY();

                    // Вектор C (вектор, соединяющий центры шаров).

                    double cSqr = cx * cx + cy * cy;

                    // Скалярное произведение векторов.
                    double scalar1 = balls[i].getSpeedX() * cx +
                            balls[i].getSpeedY() * cy;

                    double scalar2 = balls[j].getSpeedX() * cx +
                            balls[j].getSpeedY() * cy;

                    // Разложение скорости шара № 1 на нормальную и тагенсальную.

                    double ball1Nvx = (cx * scalar1) / cSqr;
                    double ball1Nvy = (cy * scalar2) / cSqr;
                    double ball1Tvx = balls[i].getSpeedX() - ball1Nvx;
                    double ball1Tvy = balls[i].getSpeedY() - ball1Nvy;

                    // Разложение скорости шара № 2 на нормальную и тагенсальную.

                    double ball2Nvx = (cx * scalar2) / cSqr;
                    double ball2Nvy = (cy * scalar2) / cSqr;
                    double ball2Tvx = balls[j].getSpeedX() - ball2Nvx;
                    double ball2Tvy = balls[j].getSpeedY() - ball2Nvy;

                    // Реализация обмена нормальными скоростями
                    // (тагенсальные остаются неизменными).

                    //производим пересчет тангенсальных скоростей
                    //учитывая массу
                    //так как масса в нашем случае зависит от объема, а объем
                    //зависит от куба радиуса, я обозначу
                    //куб объемя массой

                    double tempX = ball1Nvx;
                    double tempY = ball1Nvy;

                    double m1 = Math.pow(balls[i].getRadius(), 3);
                    double m2 = Math.pow(balls[j].getRadius(), 3);

                    ball1Nvx = (m1 - m2) * ball1Nvx / (m1 + m2) +
                            2 * m2 * ball2Nvx  / (m1 + m2);

                    ball1Nvy = (m1 - m2) * ball1Nvy / (m1 + m2) +
                            2 * m2 * ball2Nvy  / (m1 + m2);

                    ball2Nvx = 2 * m1 * tempX / (m1 + m2)  +
                            (m2 - m1) * ball2Nvx / (m1 + m2);

                    ball2Nvy = 2 * m1 * tempY / (m1 + m2)  +
                            (m2 - m1) * ball2Nvy / (m1 + m2);

                    balls[i].setSpeed(
                            ball1Nvx + ball1Tvx,
                            ball1Nvy + ball1Tvy
                    );

                    balls[j].setSpeed(
                            ball2Nvx + ball2Tvx,
                            ball2Nvy + ball2Tvy
                    );
                }

            }
        }
    }

    private void checkCollisionWithBlock() {
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

    private Optional<BouncingBall> getBallClicked(MouseEvent me) {
        for (BouncingBall ball : balls) {
            if (ball == null) {
                break;
            }
            double distance =
                    Math.pow((me.getX() - ball.getX()), 2) +
                            Math.pow((me.getY() - ball.getY()), 2);
            if (distance <= ball.getRadius() * ball.getRadius()) {
                return Optional.of(ball);
            }
        }
        return Optional.empty();
    }
}
