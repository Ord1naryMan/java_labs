package org.labs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Wall {

    private final double width = 100;
    private final double height = 20;
    private double x = 0;
    private double y = 0;
    private final Field field;

    public Wall(Field field) {
        this.field = field;
        field.addMouseMotionListener(new WallMouseMotionListener());
    }


    public void paint(Graphics2D canvas) {
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

    private class WallMouseMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            x = mouseEvent.getX();
            y = mouseEvent.getY();
        }
    }
}
