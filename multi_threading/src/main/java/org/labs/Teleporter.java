package org.labs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Teleporter {

    private final double width = 30;
    private final double height = 30;
    private double x;
    private double y;
    private Teleporter other;

    public Teleporter(Field field) {
        x = (Math.random() * 1000);
        y = (Math.random() * 1000);
    }

    public void moveToRandom() {
        x = (Math.random() * 1000);
        y = (Math.random() * 1000);
    }

    public void paint(Graphics2D canvas) {
        canvas.setColor(Color.YELLOW);
        canvas.setPaint(Color.YELLOW);
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

    public void link(Teleporter teleporter) {
        other = teleporter;
    }

    public Teleporter getOther() {
        return other;
    }
}
