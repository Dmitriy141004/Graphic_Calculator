package ua.project.calculator.files.classes;

import ua.project.calculator.files.classes.objects.ExpressionInHistory;
import ua.project.calculator.files.libs.ArrayUtils;
import ua.project.calculator.files.libs.parsers.ExpressionParser;
import ua.project.calculator.files.libs.parsers.VariableParser;
import ua.project.calculator.files.libs.StringUtils;
import ua.project.calculator.files.libs.CustomException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class JCalculatorDialogs {

    public static final Font DEFAULT_FONT = new Font(Calculator.MAIN_FONT_NAME, Font.PLAIN, 20);
    public static final Dimension OK_CANCEL_BUTTON_SIZE = new Dimension(120, 50);
    public static final Font DIALOG_MESSAGE_FONT = new Font(Calculator.MAIN_FONT_NAME, Font.PLAIN, 18);
    public static final Dimension DEFAULT_LIST_SIZE = new Dimension(260, 220);
    public static final Font LIST_FONT = new Font(Calculator.MAIN_FONT_NAME, Font.PLAIN, 17);
    public static final char[] NON_VARIABLE_CHARS = new char[] {'.', '+', '-', '/', '*', '^', '(', ')', '\"', ',', '=', '\\'};
    public static final char[] NUMBER_CHARS = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    private static String selectedVariableForView = "";

    public static void disposeFrame(JFrame frame, Calculator calculator) {
        calculator.frame.setEnabled(true);
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

    private static JLabel setupJLabel(Point location, Dimension size, String text, JPanel panel) {
        return setupJLabel(location, size, text, panel, DEFAULT_FONT);
    }

    private static JLabel setupJLabel(Point location, Dimension size, String text, JPanel panel, Font font) {
        JLabel label = new JLabel(text);
        label.setLocation(location);
        label.setSize(size);
        label.setFont(font);
        panel.add(label);
        return label;
    }

    public static void errorMessage(CustomException exception) {
        String fullMessage = exception.getMessage();
        String nameOfException = fullMessage.substring(0, fullMessage.indexOf("#"));
        String exceptionDesc = fullMessage.substring(fullMessage.indexOf("#") + 1, fullMessage.length());

        UIManager.put("OptionPane.messageFont", DIALOG_MESSAGE_FONT);
        UIManager.put("OptionPane.buttonFont", DIALOG_MESSAGE_FONT);

        JOptionPane.showMessageDialog(null,
                nameOfException,
                exceptionDesc,
                JOptionPane.ERROR_MESSAGE);
    }

    public static void addingDialog(Calculator calculator) {
        calculator.frame.setEnabled(false);
        VariableParser variableMath = calculator.variableMath;
        JFrame frame = new JFrame("Add A New Variable");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(500, 300);

        setupJLabel(new Point(40, 50), new Dimension(230, 20), "Name of new variable: ", panel);
        setupJLabel(new Point(40, 110), new Dimension(230, 20), "Value of new variable: ", panel);
        JTextField nameField = new JTextField(10);
        JTextField valueField = new JTextField(10);
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        nameField.setFont(DEFAULT_FONT);
        valueField.setFont(DEFAULT_FONT);
        okButton.setFont(DEFAULT_FONT);
        cancelButton.setFont(DEFAULT_FONT);

        panel.add(nameField);
        nameField.setLocation(270, 42);
        nameField.setSize(200, 41);
        nameField.setBackground(Calculator.VERY_LIGHT_GREEN);

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
                disposeFrame(frame, calculator);
            }
        });
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        frame.getRootPane().getActionMap().put("Cancel", new AbstractAction(){
            public void actionPerformed(ActionEvent e) { disposeFrame(frame, calculator); }
        });
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "OK");
        frame.getRootPane().getActionMap().put("OK", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                if (!nameField.getText().isEmpty() && valueField.getText().isEmpty()) {
                    valueField.requestFocus();
                } else if (nameField.getText().isEmpty() && !valueField.getText().isEmpty()) {
                    nameField.requestFocus();
                } else if (!nameField.getText().isEmpty() && !valueField.getText().isEmpty()) {
                    okButton.getActionListeners()[0].actionPerformed(e);
                }
            }
        });

        okButton.addActionListener(e -> {
            nameField.setText(nameField.getText().trim());
            valueField.setText(valueField.getText().trim());

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
                } else if (nameField.getText().equals(valueField.getText())) {
                    errorMessage(new CustomException("SameNameAndValue Error",
                            "Name of new variable and its value\ncan\'t equal - this can cause recursion."));
                    valueField.setText("");
                    valueField.requestFocus();
                    return;
                }

                boolean containsOtherChars = false;

                for (char c : nameField.getText().toCharArray()) {
                    if (!ArrayUtils.array_has(NUMBER_CHARS, c)) {
                        containsOtherChars = true;
                        break;
                    }
                }

                if (!containsOtherChars) {
                    errorMessage(new CustomException("InvalidName Error",
                            "Please, don't use charters like numbers in name of variable."));
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

            disposeFrame(frame, calculator);
        });

        cancelButton.addActionListener(e -> disposeFrame(frame, calculator));
    }

    public static void deletingDialog(Calculator calculator) {
        calculator.frame.setEnabled(false);
        VariableParser variableMath = calculator.variableMath;
        JFrame frame = new JFrame("Delete A Variable");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(420, 500);

        setupJLabel(new Point(78, 50), new Dimension(290, 30), "Choose a variable to delete:", panel);

        JList existingNames = setupList(new Point(73, 120), variableMath.knownVariables.keySet().toArray(), panel);

        existingNames.requestFocus();

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            if (existingNames.getSelectedValue() != null)
                variableMath.knownVariables.remove(existingNames.getSelectedValue().toString());
            else {
                errorMessage(new CustomException("VariableNotSelected Error",
                        "Please, select variable to delete."));
                return;
            }

            disposeFrame(frame, calculator);
        });
        okButton.setFont(DEFAULT_FONT);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> disposeFrame(frame, calculator));
        cancelButton.setFont(DEFAULT_FONT);

        setOkAndCancelButtons(panel, okButton, cancelButton);
        frame.setContentPane(panel);
        frame.setSize(panel.getWidth(), panel.getHeight());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { disposeFrame(frame, calculator); }
        });
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        frame.getRootPane().getActionMap().put("Cancel", new AbstractAction(){
            public void actionPerformed(ActionEvent e) { disposeFrame(frame, calculator); }
        });
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "OK");
        frame.getRootPane().getActionMap().put("OK", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                okButton.getActionListeners()[0].actionPerformed(e);
            }
        });
    }

    public static void changingDialog(Calculator calculator) {
        calculator.frame.setEnabled(false);
        VariableParser variableMath = calculator.variableMath;
        JFrame frame = new JFrame("Change A Variable");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(800, 500);

        setupJLabel(new Point(65, 50), new Dimension(290, 30), "Choose a variable to change:", panel);

        JList existingNames = setupList(new Point(73, 120), variableMath.knownVariables.keySet().toArray(), panel);
        existingNames.requestFocus();

        setupJLabel(new Point(350, 170), new Dimension(100, 100), "\u2192", panel,
                new Font(Calculator.UNICODE_FONT.getName(), Font.PLAIN, 100));

        setupJLabel(new Point(495, 160), new Dimension(250, 30), "And set its new value:", panel);

        JTextField newValueField = new JTextField(10);
        newValueField.setLocation(460, 203);
        newValueField.setSize(275, 41);
        newValueField.setFont(DEFAULT_FONT);
        newValueField.setBackground(Calculator.VERY_LIGHT_GREEN);
        panel.add(newValueField);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            newValueField.setText(newValueField.getText().trim());

            if (existingNames.getSelectedValue() != null) {
                if (!newValueField.getText().isEmpty())
                    variableMath.knownVariables.replace(existingNames.getSelectedValue().toString(), newValueField.getText());
                else if (existingNames.getSelectedValue().equals(newValueField.getText())) {
                    errorMessage(new CustomException("SameNameAndValue Error",
                            "Name of variable and its new value\ncan\'t equal - this can cause recursion."));
                    newValueField.setText("");
                    newValueField.requestFocus();
                    return;
                } else {
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

            disposeFrame(frame, calculator);
        });
        okButton.setFont(DEFAULT_FONT);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> disposeFrame(frame, calculator));
        cancelButton.setFont(DEFAULT_FONT);


        setOkAndCancelButtons(panel, okButton, cancelButton);
        frame.setContentPane(panel);
        frame.setSize(panel.getWidth(), panel.getHeight());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { disposeFrame(frame, calculator); }
        });

        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        frame.getRootPane().getActionMap().put("Cancel", new AbstractAction(){
            public void actionPerformed(ActionEvent e) { disposeFrame(frame, calculator); }
        });
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "OK");
        frame.getRootPane().getActionMap().put("OK", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                if (newValueField.getText().isEmpty() && !existingNames.isSelectionEmpty()) {
                    newValueField.requestFocus();
                } else if (!newValueField.getText().isEmpty() && !existingNames.isSelectionEmpty()) {
                    okButton.getActionListeners()[0].actionPerformed(e);
                }
            }
        });
    }

    public static void viewingDialog(Calculator calculator) {
        calculator.frame.setEnabled(false);
        VariableParser variableMath = calculator.variableMath;
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
        prototypes.setFocusable(false);
        prototypes.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                super.setSelectionInterval(-1, -1);
            }
        });

        setupJLabel(new Point(350, 150), new Dimension(100, 100), "\u2192", panel,
                new Font(Calculator.UNICODE_FONT.getName(), Font.PLAIN, 100));

        setupJLabel(new Point(70, 50), new Dimension(280, 30), "Select an existing variable:", panel);

        setupJLabel(new Point(485, 50), new Dimension(280, 30), "And view its prototypes:", panel);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> disposeFrame(frame, calculator));
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
            public void windowClosing(WindowEvent e) { disposeFrame(frame, calculator); }
        });

        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        frame.getRootPane().getActionMap().put("Cancel", new AbstractAction(){
            public void actionPerformed(ActionEvent e) { disposeFrame(frame, calculator); }
        });
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "OK");
        frame.getRootPane().getActionMap().put("OK", new AbstractAction(){
            public void actionPerformed(ActionEvent e) { disposeFrame(frame, calculator); }
        });
    }

    public static void historyViewingDialog(Calculator calculator) {
        ArrayList<ExpressionInHistory> history = calculator.calcEngine.history;
        calculator.frame.setEnabled(false);
        JFrame frame = new JFrame("View History");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(900, 500);

        ToggleableSelection disableSelection1 = new ToggleableSelection();
        ToggleableSelection disableSelection2 = new ToggleableSelection();

        int firstPos = 60;
        setupJLabel(new Point(135, 50), new Dimension(200, 50), "Expression:", panel);
        setupJLabel(new Point(415, 50), new Dimension(200, 50), "Result:", panel);
        setupJLabel(new Point(640, 50), new Dimension(200, 50), "When Used:", panel);

        ArrayList<String> expressionFields =
                history.stream().map(item -> item.expression).collect(Collectors.toCollection(ArrayList<String>::new));
        JList expressionColumn = setupList(new Point(firstPos, 100), expressionFields.toArray(), panel);
        expressionColumn.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ArrayList<String> resultFields =
                history.stream().map(item -> item.result).collect(Collectors.toCollection(ArrayList<String>::new));
        JList resultColumn = setupList(new Point(firstPos + 260, 100), resultFields.toArray(), panel);
        resultColumn.setSelectionModel(disableSelection1);
        resultColumn.setFocusable(false);

        ArrayList<String> dateFields =
                history.stream().map(item -> item.date).collect(Collectors.toCollection(ArrayList<String>::new));
        JList dateColumn = setupList(new Point(firstPos + 520, 100), dateFields.toArray(), panel);
        dateColumn.setSelectionModel(disableSelection2);
        dateColumn.setFocusable(false);

        expressionColumn.addListSelectionListener(e -> {
            int index = ArrayUtils.indexOf(expressionFields.toArray(), expressionColumn.getSelectedValue());

            disableSelection1.cmdSelect = true;
            disableSelection2.cmdSelect = true;
            resultColumn.setSelectedIndex(index);
            dateColumn.setSelectedIndex(index);
            disableSelection1.cmdSelect = false;
            disableSelection2.cmdSelect = false;
        });

        JButton clearHistoryButton = new JButton("Clear History");
        clearHistoryButton.setFont(DEFAULT_FONT);
        clearHistoryButton.setBackground(Calculator.RED_COLOR);
        clearHistoryButton.setForeground(Calculator.NORMAL_TEXT_COLOR);
        clearHistoryButton.setSize(230, 50);
        clearHistoryButton.setLocation(70, panel.getSize().height - 120);
        panel.add(clearHistoryButton);
        clearHistoryButton.addActionListener(e -> {
            history.clear();
            expressionColumn.setListData(new Object[0]);
            resultColumn.setListData(new Object[0]);
            dateColumn.setListData(new Object[0]);
        });

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> disposeFrame(frame, calculator));
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
            public void windowClosing(WindowEvent e) { disposeFrame(frame, calculator); }
        });

        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        frame.getRootPane().getActionMap().put("Cancel", new AbstractAction(){
            public void actionPerformed(ActionEvent e) { disposeFrame(frame, calculator); }
        });
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "OK");
        frame.getRootPane().getActionMap().put("OK", new AbstractAction(){
            public void actionPerformed(ActionEvent e) { disposeFrame(frame, calculator); }
        });
    }

    private static class ToggleableSelection extends DefaultListSelectionModel {

        private boolean cmdSelect = false;

        @Override
        public void setSelectionInterval(int index0, int index1) {
            if (!cmdSelect) super.setSelectionInterval(-1, -1);
            else super.setSelectionInterval(index0, index1);
        }
    }
}
