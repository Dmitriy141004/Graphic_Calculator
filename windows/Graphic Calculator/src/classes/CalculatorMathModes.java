package classes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <h1><b>======= CalculatorMathModes =======</b></h1>
 *
 * Данный класс является Action Listener'ом для переключателя режимов в меню "Режим",
 * Но также, он общается с графической составляюшей проекта, или же с Calculator'ом,
 * для того, чтобы можно было выбрать уровень сложности.
 *
 * @author Дмитрий Мелешко
 * @since 1.4
 * @see CalculatorMathModes#actionPerformed(java.awt.event.ActionEvent)
 */
public class CalculatorMathModes implements ActionListener {

    //                Режимы
    public static final String SIMPLE_MATH = "Simple Math";     // Режим "Простая Математика"
    public static final String NORMAL_MATH = "Normal Math";     // Режим "Нормальная Математика"
    public static final String HIGH_MATH = "High Math";         // Режим "Высшая Математика"

    private Calculator caller;                            // Калькулятор, каторый вызвал обработчик (графика)

    /** <h1><b> Конструктор класса </b></h1>
     * <p> Сохранение вызывателя (к какому калькулятору привязан?, ссылка на него)
     * и предварительная настройка.
     * И так как калькулятор сейчас в нормальном режиме, надо
     * графику поставить в этот режим. </p>
     *
     * @param caller ссылка на графический класс (например, this).
     */
    CalculatorMathModes(Calculator caller) {
        this.caller = caller;
        this.caller.buttonBracket1.setEnabled(false);
        this.caller.buttonBracket2.setEnabled(false);
        this.caller.buttonPoint.setEnabled(false);
        this.caller.buttonNegativeNum.setEnabled(false);
        this.caller.buttonPowerX2.setEnabled(false);
        this.caller.buttonPowerX3.setEnabled(false);
        this.caller.buttonPowerXY.setEnabled(false);
    }

    /** <h1><b> Action Listener </b></h1>
     * <p> Срабатывает, если переключили
     * режим математики, и сразу же
     * перестраивает графику. </p>
     *
     * @param e event, который передал комп. Он в любом случае будет передан, по нему определяется, какой режим выбрали.
     */
    public void actionPerformed(ActionEvent e)  {
        AbstractButton selectedModeButton = (AbstractButton) e.getSource();    // Выбраная кнопка-режим
        String mode = selectedModeButton.getText();                            // Получение его имени

        // А дальше, идёт выбор включаемых и выключаемых кнопок

        switch (mode) {              // РЫЧАГ РЕЖИМОВ (-> "Лёгкий", "Нормальный", "Высшый")

            case SIMPLE_MATH:
                caller.buttonBracket1.setEnabled(false);
                caller.buttonBracket2.setEnabled(false);
                caller.buttonPoint.setEnabled(false);
                caller.buttonNegativeNum.setEnabled(false);
                caller.buttonPowerX2.setEnabled(false);
                caller.buttonPowerX3.setEnabled(false);
                caller.buttonPowerXY.setEnabled(false);
                caller.calcEngine.newCounting("");
                break;

            case NORMAL_MATH:
                caller.buttonBracket1.setEnabled(true);
                caller.buttonBracket2.setEnabled(true);
                caller.buttonPoint.setEnabled(true);
                caller.buttonNegativeNum.setEnabled(true);
                caller.buttonPowerX2.setEnabled(true);
                caller.buttonPowerX3.setEnabled(true);
                caller.buttonPowerXY.setEnabled(true);
                caller.calcEngine.newCounting("");
                break;

            case HIGH_MATH:
                caller.buttonBracket1.setEnabled(true);
                caller.buttonBracket2.setEnabled(true);
                caller.buttonPoint.setEnabled(true);
                caller.buttonNegativeNum.setEnabled(true);
                caller.buttonPowerX2.setEnabled(true);
                caller.buttonPowerX3.setEnabled(true);
                caller.buttonPowerXY.setEnabled(true);
                caller.calcEngine.newCounting("");
                break;
        }
    }
}
