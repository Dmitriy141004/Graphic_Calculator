package ua.project.calculator.files.classes;

import ua.project.calculator.files.classes.objects.ExpressionInHistory;
import ua.project.calculator.files.libs.parsers.impls.ExpressionParser;
import ua.project.calculator.files.libs.CustomException;
import ua.project.calculator.files.libs.ArrayUtils;
import ua.project.calculator.files.libs.StringUtils;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

/**
 * <h1><b>======= CalculatorEngine =======</b></h1>
 *
 * Данный класс обрабатывает события, а точнее нажатия на кнопки,
 * А принимает он события от Calculator'а.
 * После принятия, при вычислении (нажата кнопка "="), класс отсылает текст
 * математического выражения другому классу - ExpressionParser'у,
 * который делает все вычисления.
 *
 * @author Дмитрий Мелешко
 * @since 1.0
 * @see CalculatorEngine#actionPerformed(java.awt.event.ActionEvent)
 * @see ExpressionParser
 */
public class CalculatorEngine implements ActionListener, FocusListener {    // "Движок" Калькулятора, ActionListener,
    // Ну, или просто - обработчик событий

    Calculator parent;                  // Ссылка на графический класс
    String displayText = "";            // Текст на десплее
    String actionButtons[] = new String[]{"+", "-", "/", "*", "="};    // Текст кнопок с действиями, потом увидите зачем это надо
    String memory = "";                          // Текст в памяти
    ExpressionParser math = new ExpressionParser();  // ExpressionParser - это класс, который считает математическое выражение из строки
    boolean resultCounted = false;               // Посчитан ли результат? (если правда, то надо при нажатии кнопки обнулить Text Field
    boolean needOnlyNumber = true;               // Нужно ли только число? (чтобы ползователь не ввёл такое: "5 +  +  +  +  5 = "
    ArrayList<ExpressionInHistory> history = new ArrayList<>();
    //                                              Колекция/ArrayList с историей вычислений. Хранит выражения, их ответы и дату вычисления
    boolean askAboutCleanUp = true;              // Спрашивать ли про подчистку выражения? Используется в методе подсчёта
    boolean cleanUp = false;                     // Подчисщать ли выражения?

    /**
     * <h1><b> Конструктор, привязка к калькулятору </b></h1>
     *
     * @param parent ссылка на графический класс, который вызвал и использует движок. Нужно чтобы можно было взаимодействовать графикой.
     */
    public CalculatorEngine(Calculator parent) {
        this.parent = parent;
        newCounting("");                         // А также, перезапуск системы
    }

    /**
     * <h1><b> Метод обнуления </b></h1>
     *
     * @param newText текст, котрый будет выведен в Text Field (обычно бывает "", но иногда - displayText или "Error").
     */
    public void newCounting(String newText) {
        parent.displayField.setText(newText);
        displayText = "";
    }

    /**
     * <h1><b> Метод подсчёта (обращения к обработчику и вывод на экран) выражения </b></h1>
     *
     * @see ExpressionParser#process(String)
     * @param text текст, содержащий выражение.
     */
    public void count(String text) {
        resultCounted = true;
        needOnlyNumber = true;

        // Этот код отвечает за подчистку текста перед подсчётом

        // Эта часть срабатывает только если не была выбран флажок "Не спрашивать меня снова", открывает диалог
        if (askAboutCleanUp) {
            // Это панелька с содержимым диалога
            JPanel msgPanel = new JPanel(new GridLayout(4, 1));

            JLabel msg1 = new JLabel("Your expression may contain clean-up-able");
            msg1.setFont(JCalculatorDialogs.DIALOG_MESSAGE_FONT);
            msgPanel.add(msg1);

            JLabel msg2 = new JLabel("items. Would you like to clean up expression?");
            msg2.setFont(JCalculatorDialogs.DIALOG_MESSAGE_FONT);
            msgPanel.add(msg2);

            // И я сделал пустую надпись для отступа между текстом и флажком
            msgPanel.add(new JLabel(""));

            JCheckBox jcb = new JCheckBox("Don\'t ask me again");
            jcb.setFont(JCalculatorDialogs.DIALOG_MESSAGE_FONT);
            msgPanel.add(jcb);

            UIManager.put("OptionPane.buttonFont", JCalculatorDialogs.DIALOG_MESSAGE_FONT);

            // Включаем диалог
            cleanUp = JOptionPane.showConfirmDialog(null, msgPanel, "Clean Up Expression First?", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;

            if (jcb.isSelected()) askAboutCleanUp = false;
        }

        // Если надо подчистить - делаем
        if (cleanUp) {
            try {
                parent.displayField.setText(parent.variableMath.processText(text, true) + ((displayText.contains("=")) ? "" : " = "));
                text = parent.displayField.getText();
                needOnlyNumber = false;
            } catch (CustomException e1) {
                handleCustomException(e1);
                resultCounted = true;
                needOnlyNumber = true;
                parent.fileMenu.requestFocus();
                return;
            }
        }

        System.out.println("<OUT>");
        System.out.println("\t<COUNT>");
        System.out.println("\t\t<PROCESS " + text + ">");

        try {
            math.process(text);                // Обработка выражения классом ExpressionParser
        } catch (CustomException ce1) {      // Если возникла ошибка (исключение)...
            handleCustomException(ce1);      // Обрабатываем её и сообщаем о ней

            System.out.println("\t\t\t<PRINTED> ERROR </PRINTED>");
            System.out.println("\t\t</PROCESS>");
            System.out.println("\t\t<RESULT>");
            System.out.println("\t\t\t<PRINTED> ERROR </PRINTED>");
            System.out.println("\t\t</RESULT>");
            System.out.println("\t</COUNT>");
            System.out.println("</OUT>\n");
            return;                            // Выход из метода, чтоб не выполнять остального
        }

        //               Если всё пошло по плану...

        System.out.println("\t\t</PROCESS>");
        System.out.println("\t\t<RESULT>");
        System.out.println("\t\t\t<PRINTED> " + math.currentResult + " </PRINTED>");
        System.out.println("\t\t</RESULT>");
        System.out.println("\t</COUNT>");
        System.out.println("</OUT>");
        System.out.print("\n");

        double currentResult = math.currentResult;          // Цифра с результатом
        // Этот код отвечает за то, что если число целое, не выводить в конце ".0"
        String simplifiedResult;
        if (currentResult == (double) (long) currentResult) simplifiedResult = "" + (long) currentResult;   // Если целое
        else simplifiedResult = "" + currentResult;                                                         // Если не целое

        history.add(new ExpressionInHistory(simplifiedResult, displayText, new SimpleDateFormat("dd-MM-yyyy").format(new Date())));

        displayText = simplifiedResult;

        newCounting(displayText);            // Загрузка результата в Text Field
    }

    /**
     * <h1><b> Главный метод для обработки событий </b></h1>
     *
     * @see CalculatorEngine#CalculatorEngine(Calculator)
     * @param e event, который передал комп. Он в любом случае будет передан, по нему определяется, какую кнопку нажали.
     */
    public void actionPerformed(ActionEvent e) {

        JButton clickedButton = (JButton) e.getSource();                // Получение нажатой кнопки
        String clickedButtonLabel = clickedButton.getText();            // Получение имени нажатой кнопки

        // Небольшая часть кода, отвечающая за то, что если выведен результат, и нажали новую кнопку, надо сначал обнулить Text Field
        if (resultCounted && !Objects.equals(clickedButtonLabel, "M+") && !Objects.equals(clickedButtonLabel, "M-"))
            resultCounted = false;
        else displayText = parent.displayField.getText();

        if (displayText.equals("Error"))
            newCounting("");       // Если на экране есть "Error", то надо сначала очистить экранчик

        // ========================== ПРИБОВЛЕНИЕ ЗНАКОВ К ТЕКСТУ =============================
        boolean needToFormat = true;        // Нужно ли форматировать текст?
        if (ArrayUtils.array_has(actionButtons, clickedButtonLabel)) {  // Если это просто знак,
            if (!needOnlyNumber) {                                      // И можно его ввести,
                displayText += (" " + clickedButtonLabel + " ");        // То надо его прибавить его, и сделать отступы сзади и спереди
                needOnlyNumber = true;
                needToFormat = false;
            } else if (displayText.isEmpty()) needToFormat = false;
        } else {                        // Иначе, это цифра или спец. знак/кнопка

            switch (clickedButtonLabel) {      // РЫЧАГ НА ЦИФРЫ И СПЕЦ. КНОПКИ/ЗНАКИ

                case "\u2190":                 // Если "Backspace"
                    char[] text = displayText.toCharArray();
                    if (text.length > 1 && ArrayUtils.array_has(actionButtons, Character.toString(text[text.length - 2]))) {
                        for (int i = 0; i < 3; i++)
                            displayText = StringUtils.removeChar(displayText, displayText.length() - 1);
                    } else if (text.length > 0) {
                        displayText = StringUtils.removeChar(displayText, displayText.length() - 1);
                    }
                    needToFormat = false;
                    break;

                case "M+":                 // Если "M+" (Заптсь в память результата)
                    if (resultCounted) memory = parent.displayField.getText();
                    newCounting("");
                    needToFormat = false;
                    System.out.println(memory);
                    break;

                case "M-":                 // Если "M-" (Запись в память по формуле "0 - результат", или " '-' + 'результат' ")
                    if (resultCounted) {
                        if (parent.displayField.getText().toCharArray()[0] != '-')
                            memory = "-" + parent.displayField.getText();
                        else memory = StringUtils.removeChar(parent.displayField.getText(), 0);
                    }
                    newCounting("");
                    needToFormat = false;
                    break;

                case "MC":                 // Если "MC" (Очистка памяти)
                    memory = "";
                    needToFormat = false;
                    break;

                case "MR":                 // Если "MR" (Очистка экранчика, или вызов из памяти)
                    if (memory.equals("")) displayText = "";
                    else displayText += memory;
                    needToFormat = false;
                    break;

                case "-x":             // Если отрицательное число
                    displayText += "-";
                    needToFormat = false;
                    break;

                case "x\u00B2":              // Если "x ^ 2" (x в квадрате)
                    if (!needOnlyNumber) displayText += " ^ 2";
                    else if (displayText.isEmpty()) needToFormat = false;
                    break;

                case "x\u00B3":              // Если "x ^ 3" (y в кубе)
                    if (!needOnlyNumber) displayText += " ^ 3";
                    else if (displayText.isEmpty()) needToFormat = false;
                    break;

                case "x\u207F":              // Если "x ^ n" (x в степени n)
                    if (!needOnlyNumber) {
                        displayText += " ^ ";
                        needOnlyNumber = true;
                    } else if (displayText.isEmpty()) needToFormat = false;
                    break;

                default:                   // Иначе... (это цифра)
                    displayText += clickedButtonLabel;
                    if (needOnlyNumber) needOnlyNumber = false;
            }
        }

        if (needToFormat) displayText = math.formatInput(displayText);            // Авто-форматирование
        parent.displayField.setText(displayText);               // Загрузка текста в Text Field

        if (clickedButtonLabel.equals("=")) {
            count(displayText);      // Если нажали на равно, считаем (заметьте, равно надо добавить)
        }
    }

    /**
     * <h1><b> Метод обработки + сообщения об ошибке (исключении) <tt>CountingError</tt> </b></h1>
     *
     * @param exception ошибка (исключение), возникшее во время run-date'а. Обязательный параметр, по нему определяется текст ошибки (исключения)
     * @see CustomException
     */
    public void handleCustomException(CustomException exception) {
        String fullMessage = exception.getMessage();                                                        // Получение текста исключения
        String nameOfException = fullMessage.substring(0, fullMessage.indexOf("#"));                        // Имя ошибки
        String exceptionDesc = fullMessage.substring(fullMessage.indexOf("#") + 1, fullMessage.length());   // Описание ошибки

        //             Настройка шрифтов информационного окна
        UIManager.put("OptionPane.messageFont", JCalculatorDialogs.DIALOG_MESSAGE_FONT);
        UIManager.put("OptionPane.buttonFont", JCalculatorDialogs.DIALOG_MESSAGE_FONT);

        newCounting("Error");                                    // Новый подсчёт, загрузка в Text Field текста "Error"

        //             Вызов информационного окна
        JOptionPane.showMessageDialog(null,
                nameOfException,
                exceptionDesc,
                JOptionPane.ERROR_MESSAGE);
    }

    // Методы для улавливания фокуса на поле
    public void focusGained(FocusEvent e) {
        if (resultCounted) newCounting("");
        if (displayText.equals("Error")) displayText = "";
    }

    public void focusLost(FocusEvent e) { }
}