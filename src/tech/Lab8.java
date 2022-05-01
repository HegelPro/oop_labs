package tech;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class DivByZeroError extends Exception {}
class NegativeValueInSqrtError extends Exception {}

class Calculator {
    private double value = 0;
    private String curOperation = null;
    private String errorMessage = null;

    public void setCurOperation(String curOperation) {
        this.curOperation = curOperation;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void reset() {
        this.value = 0;
        curOperation = null;
        errorMessage = null;
    }

    void plus(double value) {
        this.value += value;
    }

    void minus(double value) {
        this.value -= value;
    }

    void multi(double value) {
        this.value *= value;
    }

    void div(double value) throws DivByZeroError {
        if (value == 0) throw new DivByZeroError();

        this.value /= value;
    }

    void sqrt() throws NegativeValueInSqrtError {
        if (this.value < 0) throw new NegativeValueInSqrtError();

        this.value = Math.sqrt(this.value);
    }

    void pow(double value) {
        this.value = Math.pow(this.value, value);
    }

    boolean hasCurOperation() {
        return this.curOperation != null;
    }

    void doOperation(String value) {
        try {
            double parsedValue = Double.parseDouble(value);
            if (curOperation == "+") {
                plus(parsedValue);
            } else if (curOperation == "-") {
                minus(parsedValue);
            } else if (curOperation == "*") {
                multi(parsedValue);
            } else if (curOperation == "/") {
                div(parsedValue);
            } else if (curOperation == "^") {
                pow(parsedValue);
            } else if (curOperation == "√") {
                this.value = parsedValue;
                sqrt();
            }
            this.curOperation = null;
        } catch (NumberFormatException e) {
            this.errorMessage = "Value isn't a double, please press R button";
        } catch (DivByZeroError e) {
            this.errorMessage = "You can't divine by zero, please press R button";
        } catch (NegativeValueInSqrtError e) {
            this.errorMessage = "Sqrt negative number, please press R button";
        }

    }

    String getDisplayValue() {
        if (this.errorMessage != null) {
            return this.errorMessage;
        }
        if (this.value == 0) {
            return "";
        }
        return String.valueOf(this.value);
    }
}

public class Lab8 {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        JFrame f = new JFrame("Calculator");


        final JTextField tf = new JTextField();
        tf.setText(calculator.getDisplayValue());
        tf.setBounds(50,50, 250,50);
        tf.setEnabled(false);
        f.add(tf);

        JButton b0 = new JButton("0");
        b0.setBounds(50,250,50,50);
        b0.addActionListener(e -> tf.setText(tf.getText() + "0"));
        f.add(b0);

        JButton b1 = new JButton("1");
        b1.setBounds(50,200,50,50);
        b1.addActionListener(e -> tf.setText(tf.getText() + "1"));
        f.add(b1);

        JButton b2 = new JButton("2");
        b2.setBounds(100,200,50,50);
        b2.addActionListener(e -> tf.setText(tf.getText() + "2"));
        f.add(b2);

        JButton b3 = new JButton("3");
        b3.setBounds(150,200,50,50);
        b3.addActionListener(e -> tf.setText(tf.getText() + "3"));
        f.add(b3);

        JButton b4 = new JButton("4");
        b4.setBounds(50,150,50,50);
        b4.addActionListener(e -> tf.setText(tf.getText() + "4"));
        f.add(b4);

        JButton b5 = new JButton("5");
        b5.setBounds(100,150,50,50);
        b5.addActionListener(e -> tf.setText(tf.getText() + "5"));
        f.add(b5);

        JButton b6 = new JButton("6");
        b6.setBounds(150,150,50,50);
        b6.addActionListener(e -> tf.setText(tf.getText() + "6"));
        f.add(b6);

        JButton b7 = new JButton("7");
        b7.setBounds(50,100,50,50);
        b7.addActionListener(e -> tf.setText(tf.getText() + "7"));
        f.add(b7);

        JButton b8 = new JButton("8");
        b8.setBounds(100,100,50,50);
        b8.addActionListener(e -> tf.setText(tf.getText() + "8"));
        f.add(b8);

        JButton b9 = new JButton("9");
        b9.setBounds(150,100,50,50);
        b9.addActionListener(e -> tf.setText(tf.getText() + "9"));
        f.add(b9);

        JButton bPoint = new JButton(".");
        bPoint.setBounds(100,250,50,50);
        bPoint.addActionListener(e -> {
            if (!tf.getText().contains(".")) {
                tf.setText(tf.getText() + ".");
            }
        });
        f.add(bPoint);

        JButton bPlus = new JButton("+");
        bPlus.setBounds(200,250,50,50);
        bPlus.addActionListener(e -> {
            if (calculator.hasCurOperation()) {
               // todo
            } else {
                calculator.setCurOperation("+");
                // todo parse in the method
                calculator.setValue(Double.valueOf(tf.getText()));
                tf.setText("");
            }
        });
        f.add(bPlus);

        JButton bMinus = new JButton("-");
        bMinus.setBounds(200,200,50,50);
        bMinus.addActionListener(e -> {
            if (calculator.hasCurOperation()) {
                // todo
            } else {
                calculator.setCurOperation("-");
                // todo parse in the method
                calculator.setValue(Double.valueOf(tf.getText()));
                tf.setText("");
            }
        });
        f.add(bMinus);

        JButton bMulti = new JButton("*");
        bMulti.setBounds(200,150,50,50);
        bMulti.addActionListener(e -> {
            if (calculator.hasCurOperation()) {
                // todo
            } else {
                calculator.setCurOperation("*");
                // todo parse in the method
                calculator.setValue(Double.valueOf(tf.getText()));
                tf.setText("");
            }
        });
        f.add(bMulti);

        JButton bDiv = new JButton("/");
        bDiv.setBounds(200,100,50,50);
        bDiv.addActionListener(e -> {
            if (calculator.hasCurOperation()) {
                // todo
            } else {
                calculator.setCurOperation("/");
                // todo parse in the method
                calculator.setValue(Double.valueOf(tf.getText()));
                tf.setText("");
            }
        });
        f.add(bDiv);

        JButton bResult = new JButton("=");
        bResult.setBounds(150,250,50,50);
        bResult.addActionListener(e -> {
            calculator.doOperation(tf.getText());
            tf.setText(calculator.getDisplayValue());
        });
        f.add(bResult);

        JButton bSqrt = new JButton("√");
        bSqrt.setBounds(250,100,50,50);
        bSqrt.addActionListener(e -> {
            calculator.setCurOperation("√");
            calculator.doOperation(tf.getText());
            tf.setText(calculator.getDisplayValue());
        });
        f.add(bSqrt);

        JButton bPow = new JButton("^");
        bPow.setBounds(250,150,50,50);
        bPow.addActionListener(e -> {
            if (calculator.hasCurOperation()) {
                // todo
            } else {
                calculator.setCurOperation("^");
                // todo parse in the method
                calculator.setValue(Double.valueOf(tf.getText()));
                tf.setText("");
            }
        });
        f.add(bPow);

        JButton bReset = new JButton("R");
        bReset.setBounds(250,200,50,100);
        bReset.addActionListener(e -> {
            calculator.reset();
            tf.setText(calculator.getDisplayValue());
        });
        f.add(bReset);



        // TODO input value from keyboard
//        5. выводить сообщение об ошибке в случае деления на 0, извлечения корня
//        из отрицательного числа, ввода не цифр;
//        6. при вводе чисел типа 2,,,,,56 автоматически преобразовывать
//        их и выводить на экран в формате 2,56 либо запретить ввод нескольких запятых подряд;
//        8. предусмотреть возможность ввода цифр и операций с клавиатуры (через обработку события нажатие клавиши).

        f.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                char c = keyEvent.getKeyChar();
                if (Character.isDigit(c)) {
                    tf.setText(tf.getText() + c);
                }
                if (c == '.') {
                    if (!tf.getText().contains(".")) {
                        tf.setText(tf.getText() + ".");
                    }
                }

                if (c == 'p') {
                    calculator.setCurOperation("+");
                    calculator.setValue(Double.valueOf(tf.getText()));
                    tf.setText("");
                }
                if (c == '-') {
                    calculator.setCurOperation("-");
                    calculator.setValue(Double.valueOf(tf.getText()));
                    tf.setText("");
                }
                if (c == '*') {
                    calculator.setCurOperation("*");
                    calculator.setValue(Double.valueOf(tf.getText()));
                    tf.setText("");
                }
                if (c == '/') {
                    calculator.setCurOperation("/");
                    calculator.setValue(Double.valueOf(tf.getText()));
                    tf.setText("");
                }

                if (c == '=') {
                    calculator.doOperation(tf.getText());
                    tf.setText(calculator.getDisplayValue());
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
        f.setFocusable(true);
        f.setSize(350,400);
        f.setLayout(null);
        f.setVisible(true);
    }
}
