package ua.calculator.files.classes;

import ua.calculator.files.libs.ArrayUtils;
import ua.calculator.files.libs.exceptions.CustomException;

import java.util.*;

/**
 * <h1><b>======= CalculatorVariables =======</b></h1>
 *
 * Данный класс является обработчиком переменных в выражении, т.е. он проходит по выражению,
 * и если находит переменную - заменяет её на значение. Также, не маловажной деталью является
 * то, что он проходит по выражению несколько раз, т.е.:
 * <p>a = "c + d";</p>
 * <p>b = "c + d";</p>
 * <p>c = "2";</p>
 * <p>d = "3";</p>
 * <p></p>
 * <p>1. "a + b = ";</p>
 * <p>2. "c + d + c + d = ";</p>
 * <p>3. "2 + 3 + 2 + 3 = ";</p>
 *
 * Также, есть метод для поиска вложеных (nested) переменных, к примеру:
 * <p>a = "b + c"</p>
 * <p>b = "d * 2"</p>
 * <p>c = "5"</p>
 * <p>d = "2 + 7"</p>
 * <p>У переменной "a" будут такие вложеные (nested) переменные:</p>
 * <p>nested = {b = "d * 2", c = "5"}</p>
 * <p></p>
 * Также, в классе есть HashMap с всеми известными переменными. А для общения с этим HashMap'ом,
 * Есть класс с диалоговыми окнами.
 *
 * @author Дмитрий Мелешко
 * @since 1.7
 * @see JCalculatorDialogs
 * @see CalculatorVariables#processText(String, boolean)
 * @see CalculatorVariables#searchNested(String)
 */
public class CalculatorVariables {

    Map<String, String> knownVariables = new HashMap<>();             // Колекция/HashMap с известными переменными (название -> значение)
    public static final char[] VARIABLE_STOPPER_CHARS = new char[] {'(', ' ', ')'};       // Символы, которые никак не относятся к переменным,
    //                                                                                       О них - попозже

    /** <h1><b> Метод для проверки наличия символа в любой пременной </b></h1>
     * <p> Используется для того, чтоб понять, стоит ли начинать
     * обработку. Не так эффективен, но я не придумал другого способа. </p>
     *
     * @param c символ, который надо отыскать.
     * @return есть ли в какой-то переменной данный символ (да -> <tt>true</tt>, нет -> <tt>false</tt>).
     */
    private boolean anyVariableHasChar(char c) {
        boolean out = false;                          // Выводимое значение, по-умолчанию false, т.е. его не надо выставлять,
        //                                               если надо вернуть false
        for (String key : knownVariables.keySet()) {     // Пробегаем по всем переменным
            for (char charNow : key.toCharArray()) {     // Пробегаем по всем символам у переменной
                if (charNow == c) {                      // И если данный символ равен символу для поиска,
                    out = true;                          // 1. Надо вернуть true
                    break;                               // 2. и выйти из цикла, чтоб не создавать лишнюю нагрузку (для слабых процессоров)
                }
            }
        }
        return out;
    }

    /** <h1><b> Метод для замены переменных в выражении на их значения </b></h1>
     * <p> Напоминаю, запускается несколько раз. Вызывает ошибку если:
     * <ul>
     *     <li>Текст пустой (равен "")</li>
     *     <li>Если найденая переменная не объявлена</li>
     * </ul></p>
     *
     * @param text текст для обработки.
     * @param normalLaunch нужно ли повторять запуск? этот аргумент важен только для поиска вложеных (nested) переменных, там он <tt>false</tt>, в остальных случаях - <tt>true</tt>.
     * @return обработаный текст.
     * @throws CustomException если <ul>
     *     <li>Текст пустой (равен "")</li>
     *     <li>Если найденая переменная не объявлена</li>
     * </ul>
     * @see CalculatorVariables#anyVariableHasChar(char)
     */
    public String processText(String text, boolean normalLaunch) throws CustomException {
        //                   Проверка на пустой текст
        if (text.isEmpty()) throw new CustomException("EmptyData Error", "Please, type something in text field.");
        text = new ExpressionParser().formatInput(text) + " ";        // Форматирование текста

        //            ГЛАВНЫЙ ЦИКЛ FOR
        for (int index = 0; index < text.length(); index++) {
            char symbol = text.charAt(index);         // Текущий символ
            int endIndex;                             // Индекс конца имени переменной

            // Если символ - цифра, для безопасности выходим
            if (ArrayUtils.array_has(new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'}, symbol)) continue;
            if (anyVariableHasChar(symbol)) {         // Проверка на наличие символа в любой переменной
                //                                       Скорее всего, сейчас программа нашла переменную

                // Цикл для поиска конца имени. Он крутится и прибовляет к конечному индексу еденицу, пока
                // Не найдёт либо останавливающий символ ("(", " ", ")"),  либо не дойдёт до конца строки
                for (endIndex = index; endIndex < text.length(); endIndex++) {
                    if (ArrayUtils.array_has(VARIABLE_STOPPER_CHARS, text.charAt(endIndex)) ||
                            endIndex >= text.length()) break;
                }

                String beforeInserted = text.substring(0, index);       // Часть до вставляемого значения
                String inserted;                                             // Вставляемое значение

                // Установка вставляемого значение. Я использовал if-конструкцию, чтоб если переменная не объявлена
                // (метод get возвращает null), надо сообщить об ошибке
                inserted = knownVariables.get(text.substring(index, endIndex));
                if (inserted == null) throw new CustomException("VariableNotExists Error", "Variable \"" + text.substring(index, endIndex) +
                        "\" not exists.\nPlease, declare this variable.");

                String afterInserted = text.substring(endIndex, text.length());    // Часть после вставляемого значения

                text = beforeInserted + inserted + afterInserted;    // Объеденяем всё в одно выражение (до + значение + после)

                index = -1;     // Мы нашли конец, значит можно обнулить главный цикл. Сделано для избежания багов

                if (!normalLaunch) {
                    break;                            // При ненормальном запуске выходим из цикла
                }
            }
        }

        boolean needToRestart = false;                // Нужно ли перезапустить? (по-умолчанию - false)
        for (char charNow : text.toCharArray()) {     // Пробегаем по всем символам в выражении
            // Если символ - цифра, для безопасности выходим
            if (ArrayUtils.array_has(new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'}, charNow)) continue;
            if (anyVariableHasChar(charNow)) {
                needToRestart = true;     // Если любоя из переменных имеет данный символ -
                //                           Значит надо перезапустить
                break;                    // и выйти из цикла
            }
        }
        // И если нормальный запуск и надо перезапустить - перезапускаем
        if (normalLaunch && needToRestart) processText(text, true);

        return text;
    }

    /** <h1><b> Метод для поиска вложеных (nested) переменных </b></h1>
     * <p> Хочу отметить, что если образовуется "матрёшка" из
     * переменных, т.е.:</p>
     * <tt>
     *     <p>a = "b + c";</p>
     *     <p>b = "d - e";</p>
     *     <p>d = "8";</p>
     *     <p>e = "7";</p>
     *     <p>c = "f * g";</p>
     *     <p>f = "6";</p>
     *     <p>g = "5";</p>
     * </tt>
     * <p>То вложенными будут считаться только переменные "b" и "c". Т.е.:</p>
     * <tt><p>nested = {b = "d - e", c = "f * g"}</p></tt>
     *
     * @param text текст, который надо обработать. В основном строится так: <tt>"(имя_переменной) "</tt> (в конце - пробел).
     * @return либо вложенные переменные в формате <tt>"var1 = 5, var2 = 3."</tt>, либо если нет вложенных - <tt>"None"</tt>, либо, если происходит ошибка - <tt>"Error"</tt>.
     * @see CalculatorVariables#processText(String, boolean)
     */
    public String searchNested(String text) {
        // Пробуем опустить переменную на уровень ниже (т.е.: a = "b + c", вернёт "b + c"). Тут то и нужен ненормальный запуск!
        try {
            text = processText(text, false);
        } catch (CustomException e) {
            JCalculatorDialogs.errorMessage(e);
        }
        List<String> variables = new ArrayList<>();      // Колекция с переменными и х значениями, строится так: {"a = "1 + 2"", "b = "3 + 4""}

        //                                       ГЛАВНЫЙ ЦИКЛ FOR
        // Работает по похожему принципу как в методе processText, но всместо того чтобы заменить текст, он в колекцию
        // Добавляет переменную и её значение
        for (int i = 0; i < text.length(); i++) {

            // Если символ - цифра, для безопасности выходим
            if (ArrayUtils.array_has(new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'}, text.charAt(i))) continue;
            if (anyVariableHasChar(text.charAt(i))) {
                int endIndex;
                // Поиск конца переменной
                for (endIndex = i; endIndex < text.length(); endIndex++) {
                    if (ArrayUtils.array_has(VARIABLE_STOPPER_CHARS, text.charAt(endIndex))) break;
                }

                // И загрузка в колекцию
                if (knownVariables.get(text.substring(i, endIndex)) == null) return "Error";
                variables.add("<i>" + text.substring(i, endIndex) + "</i> = \"" + knownVariables.get(text.substring(i, endIndex)) + "\"");
                text = text.substring(endIndex, text.length());
                i = -1;
            }

        }

        // А после обработки, сшивает всё вместе через запятуи, а в конце ставит точку
        String out = "";
        for (String item : variables) {
            if (variables.indexOf(item) < variables.size() - 1) out += (item + ", ");
            else out += (item + ".");
        }
        if (out.isEmpty()) out = "None";

        return out;
    }

}
