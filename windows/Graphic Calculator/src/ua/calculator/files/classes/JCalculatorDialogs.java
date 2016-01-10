package ua.calculator.files.classes;

import ua.calculator.files.libs.StringUtils;
import ua.calculator.files.libs.exceptions.CustomException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JCalculatorDialogs {

    public static final Font DEFAULT_FONT = new Font(Calculator.MAIN_FONT_NAME, Font.PLAIN, 20);
    public static final Dimension OK_CANCEL_BUTTON_SIZE = new Dimension(120, 50);
    public static final Font ERROR_MESSAGE_FONT = new Font(Calculator.MAIN_FONT_NAME, Font.PLAIN, 18);
    public static final Dimension DEFAULT_LIST_SIZE = new Dimension(260, 220);
    public static final Font LIST_FONT = new Font(Calculator.MAIN_FONT_NAME, Font.PLAIN, 17);
    public static final char[] NON_VARIABLE_CHARS = new char[] {'.', '+', '-', '/', '*', '^', '(', ')', '\"'};
    private static String selectedVariableForView = "";


    public static void disposeFrame(JFrame frame) {
        Calculator.frame.setEnabled(true);
        frame.setVisible(false);
        frame.dispose();
    }

    private static JPanel setOkAndCancelButtons (JPanel panel, JButton okButton, JButton cancelButton) {
        int width = panel.getSize().width;
        int height = panel.getSize().height;

        okButton.setSize(OK_CANCEL_BUTTON_SIZE);
        cancelButton.setSize(OK_CANCEL_BUTTON_SIZE);
        okButton.setLocation(width / 2 - 150, height - 120);
        cancelButton.setLocation(width / 2 + 20, height - 120);

        okButton.setBackground(Calculator.ORANGE_COLOR);
        cancelButton.setBackground(Calculator.ORANGE_COLOR);
        okButton.setForeground(Calculator.NORMAL_TEXT_COLOR);
        cancelButton.setForeground(Calculator.NORMAL_TEXT_COLOR);

        panel.add(okButton);
        panel.add(cancelButton);
        return panel;
    }

    private static JList setupList(Point location, Object[] listData, JPanel panel) {
        JList list = new JList(listData);
        list.setVisibleRowCount(10);
        list.setLayoutOrientation(JList.VERTICAL);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setLocation(location);
        scrollPane.setSize(DEFAULT_LIST_SIZE);
        list.setFont(LIST_FONT);
        list.setBackground(Calculator.VERY_LIGHT_GREEN);
        panel.add(scrollPane);
        return list;
    }

    public static void errorMessage(CustomException exception) {
        String fullMessage = exception.getMessage();
        String nameOfException = fullMessage.substring(0, fullMessage.indexOf("#"));
        String exceptionDesc = fullMessage.substring(fullMessage.indexOf("#") + 1, fullMessage.length());

        UIManager.put("OptionPane.messageFont", ERROR_MESSAGE_FONT);
        UIManager.put("OptionPane.buttonFont", ERROR_MESSAGE_FONT);

        JOptionPane.showMessageDialog(null,
                nameOfException,
                exceptionDesc,
                JOptionPane.ERROR_MESSAGE);
    }

    public static void addingDialog(Calculator calculator) {
        Calculator.frame.setEnabled(false);
        CalculatorVariables variableMath = calculator.variableMath;
        JFrame frame = new JFrame("Add A New Variable");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(500, 300);

        JLabel nameLabel = new JLabel("Name of new variable: ");
        JLabel valueLabel = new JLabel("Value of new variable: ");
        JTextField nameField = new JTextField(10);
        JTextField valueField = new JTextField(10);
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        nameField.setFont(DEFAULT_FONT);
        nameLabel.setFont(DEFAULT_FONT);
        valueField.setFont(DEFAULT_FONT);
        valueLabel.setFont(DEFAULT_FONT);
        okButton.setFont(DEFAULT_FONT);
        cancelButton.setFont(DEFAULT_FONT);

        panel.add(nameLabel);
        nameLabel.setLocation(40, 50);
        nameLabel.setSize(230, 20);

        panel.add(nameField);
        nameField.setLocation(270, 42);
        nameField.setSize(200, 41);
        nameField.setBackground(Calculator.VERY_LIGHT_GREEN);

        panel.add(valueLabel);
        valueLabel.setLocation(40, 110);
        valueLabel.setSize(230, 20);

        panel.add(valueField);
        valueField.setLocation(270, 102);
        valueField.setSize(200, 41);
        valueField.setBackground(Calculator.VERY_LIGHT_GREEN);

        setOkAndCancelButtons(panel, okButton, cancelButton);
        frame.setContentPane(panel);
        frame.setSize(panel.getWidth(), panel.getHeight());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disposeFrame(frame);
            }
        });
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        frame.getRootPane().getActionMap().put("Cancel", new AbstractAction(){
            public void actionPerformed(ActionEvent e) { disposeFrame(frame); }
        });

        okButton.addActionListener(e -> {
            if (!nameField.getText().isEmpty() && !valueField.getText().isEmpty()) {
                if (variableMath.knownVariables.containsKey(nameField.getText())) {
                    errorMessage(new CustomException("VariableNameExists Error",
                            "Name of a new variable exists,\nso, please, enter anything else."));
                    nameField.setText("");
                    nameField.requestFocus();
                    return;
                } else if (nameField.getText().contains(" ")) {
                    errorMessage(new CustomException("InvalidName Error",
                            "Please, don't use spaces in names.\nYou can change them with underscores (\"_\")."));
                    for (char symbol : nameField.getText().toCharArray()) {
                        if (symbol == ' ') nameField.setText(nameField.getText().replace(symbol, '_'));
                    }
                    nameField.requestFocus();
                    return;
                } else if (ExpressionParser.ary1_has_ary2(nameField.getText().toCharArray(), NON_VARIABLE_CHARS)) {
                    errorMessage(new CustomException("InvalidName Error",
                            "Please, don't use charters like parts of \nnumbers, actions, brackets and quotes in name of variable."));
                    nameField.setText("");
                    nameField.requestFocus();
                    return;
                }

                variableMath.knownVariables.put(nameField.getText(), valueField.getText());
            } else if (nameField.getText().isEmpty() && !valueField.getText().isEmpty()){
                errorMessage(new CustomException("NothingEntered Error", "Please, enter data in name field."));
                nameField.requestFocus();
                return;
            } else if (!nameField.getText().isEmpty() && valueField.getText().isEmpty()){
                errorMessage(new CustomException("NothingEntered Error", "Please, enter data in value field."));
                valueField.requestFocus();
                return;
            } else if (nameField.getText().isEmpty() && valueField.getText().isEmpty()) {
                errorMessage(new CustomException("NothingEntered Error", "Please, enter data in name and value field."));
                nameField.requestFocus();
                return;
            }

            disposeFrame(frame);
        });

        cancelButton.addActionListener(e -> disposeFrame(frame));
    }

    public static void deletingDialog(Calculator calculator) {
        Calculator.frame.setEnabled(false);
        CalculatorVariables variableMath = calculator.variableMath;
        JFrame frame = new JFrame("Delete A Variable");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(420, 500);

        JLabel firstMode = new JLabel("Choose a variable to delete:");
        firstMode.setLocation(78, 50);
        firstMode.setSize(290, 30);
        firstMode.setFont(DEFAULT_FONT);
        panel.add(firstMode);

        JList existingNames = setupList(new Point(73, 120), variableMath.knownVariables.keySet().toArray(), panel);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            if (existingNames.getSelectedValue() != null)
                variableMath.knownVariables.remove(existingNames.getSelectedValue().toString());
            else {
                errorMessage(new CustomException("VariableNotSelected Error",
                        "Please, select variable to delete."));
                return;
            }

            disposeFrame(frame);
        });
        okButton.setFont(DEFAULT_FONT);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> disposeFrame(frame));
        cancelButton.setFont(DEFAULT_FONT);

        setOkAndCancelButtons(panel, okButton, cancelButton);
        frame.setContentPane(panel);
        frame.setSize(panel.getWidth(), panel.getHeight());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { disposeFrame(frame); }
        });
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        frame.getRootPane().getActionMap().put("Cancel", new AbstractAction(){
            public void actionPerformed(ActionEvent e) { disposeFrame(frame); }
        });
    }

    public static void changingDialog(Calculator calculator) {
        Calculator.frame.setEnabled(false);
        CalculatorVariables variableMath = calculator.variableMath;
        JFrame frame = new JFrame("Change A Variable");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(800, 500);

        JLabel infoLabel1 = new JLabel("Choose a variable to change:");
        infoLabel1.setLocation(65, 50);
        infoLabel1.setSize(290, 30);
        infoLabel1.setFont(DEFAULT_FONT);
        panel.add(infoLabel1);

        JList existingNames = setupList(new Point(73, 120), variableMath.knownVariables.keySet().toArray(), panel);

        JLabel arrowLabel = new JLabel("\u2192");
        arrowLabel.setFont(new Font(Calculator.UNICODE_FONT.getName(), Font.PLAIN, 100));
        arrowLabel.setLocation(350, 170);
        arrowLabel.setSize(100, 100);
        panel.add(arrowLabel);

        JLabel infoLabel2 = new JLabel("And set its new value:");
        infoLabel2.setFont(DEFAULT_FONT);
        infoLabel2.setLocation(495, 160);
        infoLabel2.setSize(250, 30);
        panel.add(infoLabel2);

        JTextField newValueField = new JTextField(10);
        newValueField.setLocation(460, 203);
        newValueField.setSize(275, 41);
        newValueField.setFont(DEFAULT_FONT);
        newValueField.setBackground(Calculator.VERY_LIGHT_GREEN);
        panel.add(newValueField);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            if (existingNames.getSelectedValue() != null) {
                if (!newValueField.getText().isEmpty())
                    variableMath.knownVariables.replace(existingNames.getSelectedValue().toString(), newValueField.getText());
                else {
                    errorMessage(new CustomException("VariableNotSelected Error",
                            "Please, enter new value of variable."));
                    newValueField.requestFocus();
                    return;
                }
            } else {
                errorMessage(new CustomException("VariableNotSelected Error",
                        "Please, select variable to change."));
                return;
            }

            disposeFrame(frame);
        });
        okButton.setFont(DEFAULT_FONT);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> disposeFrame(frame));
        cancelButton.setFont(DEFAULT_FONT);


        setOkAndCancelButtons(panel, okButton, cancelButton);
        frame.setContentPane(panel);
        frame.setSize(panel.getWidth(), panel.getHeight());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { disposeFrame(frame); }
        });
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        frame.getRootPane().getActionMap().put("Cancel", new AbstractAction(){
            public void actionPerformed(ActionEvent e) { disposeFrame(frame); }
        });
    }

    public static void viewingDialog(Calculator calculator) {
        Calculator.frame.setEnabled(false);
        CalculatorVariables variableMath = calculator.variableMath;
        JFrame frame = new JFrame("View Existing Variables");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(800, 500);

        JList prototypes = setupList(new Point(470, 103), new Object[0], panel);

        JList existingVariables = setupList(new Point(70, 103), variableMath.knownVariables.keySet().toArray(), panel);
        existingVariables.addListSelectionListener(e -> {
            if (existingVariables.getSelectedValue().equals(selectedVariableForView)) return;

            selectedVariableForView = (String) existingVariables.getSelectedValue();
            String rawData = "Error";
            try {
                rawData = variableMath.processText(selectedVariableForView, true);
                rawData = StringUtils.removeChar(rawData, rawData.length() - 1);
            } catch (CustomException e1) {
                errorMessage(e1);
            }
            prototypes.setListData(new Object[] {"<html><i>name</i> = \"" + selectedVariableForView + "\"</html>",
                    "<html><i>value</i> = \"" + variableMath.knownVariables.get(selectedVariableForView) + "\"</html>",
                    "<html><i>nested</i> = { " + variableMath.searchNested(selectedVariableForView + " ") + " }</html>",
                    "<html><i>raw data</i> = \"" + rawData + "\"</html>"});
        });

        JLabel arrowLabel = new JLabel("\u2192");
        arrowLabel.setFont(new Font(Calculator.UNICODE_FONT.getName(), Font.PLAIN, 100));
        arrowLabel.setLocation(350, 150);
        arrowLabel.setSize(100, 100);
        panel.add(arrowLabel);

        JLabel infoLabel1 = new JLabel("Select an existing variable:");
        infoLabel1.setFont(DEFAULT_FONT);
        infoLabel1.setLocation(70, 50);
        infoLabel1.setSize(280, 30);
        panel.add(infoLabel1);

        JLabel infoLabel2 = new JLabel("And view its prototypes:");
        infoLabel2.setFont(DEFAULT_FONT);
        infoLabel2.setLocation(485, 50);
        infoLabel2.setSize(280, 30);
        panel.add(infoLabel2);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> disposeFrame(frame));
        okButton.setFont(DEFAULT_FONT);
        okButton.setSize(OK_CANCEL_BUTTON_SIZE);
        okButton.setLocation(panel.getSize().width / 2 - 60, panel.getSize().height - 120);
        okButton.setBackground(Calculator.ORANGE_COLOR);
        okButton.setForeground(Calculator.NORMAL_TEXT_COLOR);
        panel.add(okButton);

        frame.setContentPane(panel);
        frame.setSize(panel.getWidth(), panel.getHeight());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { disposeFrame(frame); }
        });
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        frame.getRootPane().getActionMap().put("Cancel", new AbstractAction(){
            public void actionPerformed(ActionEvent e) { disposeFrame(frame); }
        });
    }
}
