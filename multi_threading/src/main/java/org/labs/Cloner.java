package org.labs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Cloner {

    private final double width = 30;
    private final double height = 30;
    private double x;
    private double y;

    public Cloner(Field field) {
        x = (Math.random() * 1000);
        y = (Math.random() * 1000);
    }

    public void moveToRandom() {
        x = (Math.random() * 1000);
        y = (Math.random() * 1000);
    }

    public void paint(Graphics2D canvas) {
        canvas.setColor(Color.BLUE);
        canvas.setPaint(Color.BLUE);
        Rectangle2D.Double rectangle = new Rectangle2D.Double(
                x,
                y,
                width,
                height
        );

        canvas.draw(rectangle);
        canvas.fill(rectangle);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
