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
    private int playerScore = 0;
    private int aiScore = 0;
    // Динамический список скачущих мячей
    private final List<BouncingBall> balls = new ArrayList<>() {{
        add(null);
    }};
    private int ballIndex = 0;
    private final Wall wall;
    private final Deleter deleter;
    private final Cloner cloner;
    private final Teleporter teleporter1;
    private final Teleporter teleporter2;
    private final Wall wallWithAI;
    private String scoreText = "0:0";

    // Конструктор класса BouncingBall
    public Field() {
// Установить цвет заднего фона белым
        setBackground(Color.WHITE);
// Запустить таймер
        // Класс таймер отвечает за регулярную генерацию событий ActionEvent
        // При создании его экземпляра используется анонимный класс,
        // реализующий интерфейс ActionListener
        // Задача обработчика события ActionEvent - перерисовка окна
        Timer repaintTimer = new Timer(1, ev -> {
// Задача обработчика события ActionEvent - перерисовка окна
            checkCollisionWithWalls();
            checkCollisionWithBuff();
            repaint();
        });

        wall = new Wall(this, Difficulty.EASY);
        wallWithAI = new Wall(this, true);
        deleter = new Deleter(this);
        cloner = new Cloner(this);
        teleporter1 = new Teleporter(this);
        teleporter2 = new Teleporter(this);

        teleporter1.link(teleporter2);
        teleporter2.link(teleporter1);

        var wallWithAIThread = new Thread(wallWithAI);
        wallWithAIThread.setDaemon(true);
        wallWithAIThread.start();

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


        if (!(balls.get(0) == null)) {
            canvas.setColor(Color.BLACK);
            updateScore();
        }

        canvas.drawString(
                scoreText,
                getWidth() / 2,
                getHeight() / 2
        );

        wall.paint(canvas);
        deleter.paint(canvas);
        cloner.paint(canvas);
        teleporter1.paint(canvas);
        teleporter2.paint(canvas);
        wallWithAI.paint(canvas);
    }

    public void setDifficulty(Difficulty diff) {
        wall.setDifficulty(diff);
    }

    // Метод добавления нового мяча в список
    public void addBall() {
//Заключается в добавлении в список нового экземпляра BouncingBall
// Всю инициализацию положения, скорости, размера, цвета
// BouncingBall выполняет сам в конструкторе
        if (ballIndex == balls.size()) {
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

    public Wall getWall() {
        return wall;
    }

    public List<BouncingBall> getBalls() {
        return balls;
    }

    public void addScoreToPlayer() {
        playerScore++;
    }
    public void addScoreToAI() {
        aiScore++;
    }

    private void checkCollisionWithWalls() {
        checkCollisionWithWall(wall);
        checkCollisionWithWall(wallWithAI);
    }

    private void checkCollisionWithWall(Wall wall) {
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



    private void updateScore() {
        boolean isWinnerExist = false;
        if (playerScore >= 10) {
            scoreText = "You win!!";
            isWinnerExist = true;
        }else if (aiScore >= 10) {
            scoreText = "AI win!!";
            isWinnerExist = true;
        } else {
            scoreText = playerScore + " : " + aiScore;
        }

        if (isWinnerExist) {
            onWin();
        }
    }

    private void onWin() {
        balls.forEach(BouncingBall::stop);
        balls.clear();
        balls.add(null);
        ballIndex = 0;
        aiScore = 0;
        playerScore = 0;
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
