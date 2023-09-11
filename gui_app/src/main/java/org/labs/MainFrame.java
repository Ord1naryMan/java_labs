package org.labs;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

public class MainFrame extends JFrame {

    public final static int WIDTH = 800;
    public final static int HEIGHT = 600;

    public MainFrame() {
        super("GUI APP");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void draw(List<Displayable> displayables) {
        addDisplayable(displayables);
        setLayout(new BorderLayout());
        pack();
        setResizable(false);
        setVisible(true);
    }

    private void addDisplayable(List<Displayable> displayables) {
        displayables.forEach(displayable -> displayable.display(this));
    }
}
