package org.labs;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MemoryCell implements Displayable {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    private final String name;
    private double value;
    private final int x, y;
    private final MemoryGrid associatedGrid;
    private boolean isChecked = false;
    private final JButton button;

    public MemoryCell(String name, int x, int y, MemoryGrid memGrid) {
        this.name = name;
        this.x = x;
        this.y = y;
        associatedGrid = memGrid;
        button = new JButton(value + "");
    }

    @Override
    public void display(MainFrame frame) {

        JLabel textAbove = new JLabel();
        textAbove.setSize(100, 10);
        textAbove.setLocation(x + WIDTH/2 - 18, y - 15);
        textAbove.setText(name);

        button.setSize(WIDTH, HEIGHT);
        button.setLocation(x, y);


        button.addActionListener(listener -> {
            associatedGrid.uncheckAll();
            check();
        });
        //default color
        button.setBorder(new LineBorder(Color.RED, 5));
        frame.add(textAbove);
        frame.add(button);
    }

    public void check() {
        isChecked = true;
        button.setBorder(new LineBorder(Color.GREEN, 5));
    }

    public void uncheck() {
        isChecked = false;
        button.setBorder(new LineBorder(Color.RED, 5));
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void add(double number) {
        value += number;
        updateText();
    }

    public void clear() {
        value = 0;
        updateText();
    }

    private void updateText() {
        button.setText(value + "");
    }
}
