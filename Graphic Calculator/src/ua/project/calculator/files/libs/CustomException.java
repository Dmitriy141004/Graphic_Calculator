package ua.project.calculator.files.libs;

/**
 * <h1><b>======= CustomException =======</b></h1>
 *
 * <p>Данный класс является просто исключением, которое вызывает <tt>ExpressionParser</tt> (Обработчик мат. выражений),
 * Если происходит ошибка. Передаёт текст в формате:</p>
 *
 * <p><tt>(описание ошибки)#(имя ошибки)</tt></p>
 *
 * <p>Причём, имя и описание вгружаются в JOptionPane (диалоговое/информационное окно), имя служит заголовком
 * окна, а описание - текстом внутри.</p>
 *
 * @author Дмитрий Мелешко
 * @since 1.5.3
 *
 */
public class CustomException extends Exception {               // Забыл сказать - расширяет/наследует класс исключения
    public CustomException(String nameOfError, String text) { super(text + "#" + nameOfError); }
}