package org.labs;

import lombok.Getter;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import static java.lang.Math.*;

public class Equation implements Displayable {


    private final JTextArea resultField;
    private final JRadioButton radioButton1;
    private final JRadioButton radioButton2;

    private final JTextField textFieldX;
    private final JTextField textFieldZ;
    private final JTextField textFieldY;

    private ButtonGroup buttonGroup = new ButtonGroup();
    public double result = 0;
    private int formulaType = 1;

    public Equation() {
        resultField = new JTextArea();
        radioButton1 = addRadiobutton("Type 1", 1);
        radioButton2 = addRadiobutton("Type 2", 2);

        radioButton1.setLocation(20, 150);
        radioButton2.setLocation(210, 150);

        textFieldX = new JTextField();
        textFieldY = new JTextField();
        textFieldZ = new JTextField();
    }

    @Override
    public void display(MainFrame frame) {
        resultField.setText(result + "");
        resultField.setLocation(20, 25);
        resultField.setSize(380, 100);

        buttonGroup.getElements().nextElement().setSelected(true);

        textFieldX.setSize(150, 20);
        textFieldX.setLocation(40, 530);
        JLabel textAboveX = new JLabel("X");
        textAboveX.setSize(20, 20);
        textAboveX.setLocation(115, 510);

        textFieldY.setSize(150, 20);
        textFieldY.setLocation(230, 530);
        JLabel textAboveY = new JLabel("Y");
        textAboveY.setSize(20, 20);
        textAboveY.setLocation(305, 510);

        textFieldZ.setSize(150, 20);
        textFieldZ.setLocation(420, 530);
        JLabel textAboveZ = new JLabel("Z");
        textAboveZ.setSize(20, 20);
        textAboveZ.setLocation(495, 510);

        JButton button = new JButton("Calculate");

        button.setSize(150, 20);
        button.setLocation(610, 530);
        button.addActionListener(listener -> calculateAndUpdate());

        frame.add(textAboveX);
        frame.add(textAboveY);
        frame.add(textAboveZ);
        frame.add(button);
        frame.add(textFieldX);
        frame.add(textFieldY);
        frame.add(textFieldZ);
        frame.add(radioButton1);
        frame.add(radioButton2);
        frame.add(resultField);
    }

    public void calculateAndUpdate() {
        double x = Double.parseDouble(textFieldX.getText());
        double y = Double.parseDouble(textFieldY.getText());
        double z = Double.parseDouble(textFieldZ.getText());

        if (formulaType == 1) {
            setResult(String.valueOf(calcType1(x, y, x)));
        } else {
            setResult(String.valueOf(calcType2(x, y, x)));
        }
    }

    public void setResult(String res) {
        resultField.setText(res);
    }

    private JRadioButton addRadiobutton(String name, int type) {
        JRadioButton button = new JRadioButton(name);
        button.addActionListener(listener -> {
            formulaType = type;
            Context.photo.setImage(type);
        });
        buttonGroup.add(button);
        button.setSize(190, 100);
        return button;
    }

    private double calcType1(double x, double y, double z) {
        return result =
                sin(log(y) + sin(PI * y * y)) *
                pow(x*x + sin(z) + pow(E, cos(z)), 0.25);
    }

    private double calcType2(double x, double y, double z) {
        return result =
                pow(cos(pow(E, x) + pow(log(1 + y), 2)) +
                    pow(
                        pow(E, cos(x)) + pow(sin(PI * z), 2)
                        ,0.5
                    ) + pow(1/x, 0.5) + cos(y*y),
                sin(z)
        );
    }
}
