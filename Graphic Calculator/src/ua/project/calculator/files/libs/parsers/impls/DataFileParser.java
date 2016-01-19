package ua.project.calculator.files.libs.parsers.impls;

import ua.project.calculator.files.libs.parsers.AbstractDataFileParser;

import java.util.HashMap;

/** <h1><b>======= DataFileParser =======</b></h1>
 *
 * <p>Данный класс используется для обработки файлов с информацией, представляющей из себя такую вещь:</p>
 * <p><tt>(имя1)=(значение1),(имя2)=(значение2),(имя3)=(значение3),(имя4)=(значение4)</tt></p>
 * <p>Т.е. в одной строке содержится несколько значений. Он ищет это всё, и потом - возвращает
 * <tt>HashMap (String -> String, имя -> значение)</tt> со всеми именами параметров и их значениями.</p>
 *
 * @author Дмитрий Мелешко
 * @since 1.8.3
 * @see DataFileParser#parse(String)
 */
public class DataFileParser implements AbstractDataFileParser {
    /** <b><h1>======= Метод для поиска параметров и значений =======</h1></b>
     * <p>О принципе работы - у него есть переменная <tt>readingName</tt>, которая отвечает за чтение в
     * <tt>currentName (true)</tt> или <tt>currentValue (false)</tt>. В начале она будет <tt>true</tt>, когда главный
     * цикл перебора символов доходит до символа "=" (равно) <tt>readingName</tt> станет <tt>false</tt>, а при символе
     * "," (запятуя) - станет <tt>false</tt>.</p>
     *
     * @param text текст для обработки.
     * @return <tt>HashMap (String -> String, имя -> значение)</tt> со всеми именами параметров и их значениями.
     */
    public HashMap<String, String> parse(String text) {
        HashMap<String, String> values = new HashMap<>();     // Возвращяемая колекция

        boolean readingName = true;                           // Читаем имя?
        String currentName = "";                              // Имя параметра
        String currentValue = "";                             // Значение параметра
        // ГЛАВНЫЙ ЦИКЛ ПО ПЕРЕБОРУ СИМВОЛОВ
        for (int i = 0; i < text.length(); i++) {

            if (text.charAt(i) == '=' && readingName) {       // Символ равно
                readingName = false;                                                        // Надо только переключить режим
                continue;
            }

            if (text.charAt(i) == ',' && !readingName) {      // Символ запятуи. Тут происходит обнуление
                readingName = true;
                values.put(currentName, currentValue);
                currentName = "";
                currentValue = "";
                continue;
            }

            if (text.length() <= i + 1) values.put(currentName, currentValue);     // На случай, если символ последний

            // Ну и конечно, надо прибавить символы
            if (readingName) currentName += text.charAt(i);
            else currentValue += text.charAt(i);
        }

        return values;
    }
}
