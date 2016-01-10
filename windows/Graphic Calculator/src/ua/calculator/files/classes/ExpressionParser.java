package ua.calculator.files.classes;

import ua.calculator.files.libs.ArrayUtils;
import ua.calculator.files.libs.StringUtils;
import ua.calculator.files.libs.exceptions.CustomException;

import java.util.ArrayList;
import java.util.Objects;

public class ExpressionParser {

    public double currentResult = 0;
    private double numberA = 0;
    private double numberB = 0;
    private int start = 0;
    private int end = 0;
    public static char[] actions = new char[] {'+', '-', '/', '*', '^'};

    public ExpressionParser() { newCalculation(); }

    public static boolean ary1_has_ary2(char[] ary1, char[] ary2) {
        boolean out = false;
        for (char item : ary1) {
            if (ArrayUtils.array_has(ary2, item)) out = true;
        }
        return out;
    }

    private int max(int[] array) {
        int out = 0;
        for (int item : array) {
            if (item > out) out = item;
        }
        return out;
    }

    public void calculate(String string) throws CustomException {
        newCalculation();
        string = formatInput(string);
        char[] workingText = StringUtils.toCharArray(string);

        for (int i = 0; i < workingText.length; i++) {
            char symbol = workingText[i];
            if (i > 0 && workingText[i - 1] != 'e') {
                if (ArrayUtils.array_has(actions, symbol)) {

                    if (workingText[i] == '-' && i - 1 >= 0
                            && workingText[i - 1] == ' ' && workingText[i + 1] != ' ') continue;

                    int indexOfDiv = StringUtils.charAryToString(workingText).indexOf("/");
                    int indexOfMlt = StringUtils.charAryToString(workingText).indexOf("*");
                    int indexOfPow = StringUtils.charAryToString(workingText).indexOf("^");

                    int prevIndexOfDiv = indexOfDiv;
                    int prevIndexOfMlt = indexOfMlt;
                    int prevIndexOfPow = indexOfPow;

                    if (prevIndexOfPow == -1) {
                        if (indexOfDiv == -1) indexOfDiv = max(new int[]{indexOfMlt, indexOfPow}) + 1;
                        if (indexOfMlt == -1) indexOfMlt = max(new int[]{indexOfDiv, indexOfPow}) + 1;
                        indexOfPow = max(new int[]{indexOfMlt, indexOfDiv}) + 1;

                        if (indexOfDiv < indexOfMlt && indexOfDiv < indexOfPow && prevIndexOfDiv != -1) i = indexOfDiv;
                        if (indexOfMlt < indexOfDiv && indexOfMlt < indexOfPow && prevIndexOfMlt != -1) i = indexOfMlt;
                    } else {
                        i = indexOfPow;
                    }

                    symbol = workingText[i];


                    try {

                        String sNumberA = findNumberA(i, workingText);
                        String sNumberB = findNumberB(i, workingText);
                        numberA = Double.parseDouble(sNumberA);
                        numberB = Double.parseDouble(sNumberB);

                    } catch (NumberFormatException someException) {
                        newCalculation();
                        throw new CustomException("Converting Error",
                                "Converting error appeared,\nwhen I converted values.");
                    } catch (ArrayIndexOutOfBoundsException someException) {
                        newCalculation();
                        throw new CustomException("Unknown Error", "Unknown error appeared.");
                    }

                    switch (symbol) {
                        case '+':
                            currentResult = numberA + numberB;
                            break;

                        case '-':
                            currentResult = numberA - numberB;
                            break;

                        case '/':
                            currentResult = numberA / numberB;
                            break;

                        case '*':
                            currentResult = numberA * numberB;
                            break;

                        case '^':
                            currentResult = Math.pow(numberA, numberB);
                            break;
                    }

                    String insertedResult = Double.toString(currentResult);
                    String beforeResult = "";
                    if (start > 0) {
                        beforeResult =
                                StringUtils.charAryToString(ArrayUtils.in_select(workingText, 0, start - 1));
                    }
                    String afterResult = StringUtils.charAryToString(ArrayUtils.in_select(workingText, end + 1, workingText.length - 1));
                    workingText = (beforeResult + insertedResult + afterResult).toCharArray();

                    String findingText = " " + StringUtils.charAryToString(workingText);

                    System.out.println("\t\t\t<PRINTED> " + findingText + " </PRINTED>");

                    int indexOfMns = findingText.indexOf("-");

                    if (ary1_has_ary2(findingText.toCharArray(), actions)) {
                        if (indexOfMns != -1) {
                            char[] actionsExceptMinus = ArrayUtils.deleteItem(actions, ArrayUtils.indexOf(actions, '-'));
                            if (!ary1_has_ary2(findingText.toCharArray(), actionsExceptMinus)) {
                                if (findingText.charAt(indexOfMns - 1) == ' ' &&
                                        findingText.charAt(indexOfMns + 1) != ' ') break;
                            }
                        }
                        calculate(StringUtils.charAryToString(workingText));
                    } else {
                        for (int j = 0; j < 3; j++) workingText = ArrayUtils.deleteItem(workingText, workingText.length - 1);
                        try {
                            currentResult = Double.parseDouble(StringUtils.charAryToString(workingText));
                        } catch (NumberFormatException e1) {
                            throw new CustomException("Converting Error",
                                    "Converting error appeared,\nwhen I converted values.");
                        }
                    }

                    break;

                }
            }
        }
    }

    public String process(String text) throws CustomException {
        char[] workingText = StringUtils.toCharArray(text);

        int opened = 0;
        int closed = 0;
        int prevOpened = opened;
        int start = 0;
        int end = text.length() - 4;

        for (int i = 0; i < workingText.length; i++) {
            if (workingText[i] == '(') opened++;
            if (workingText[i] == ')') closed++;
            if (opened != 0 && prevOpened == 0) start = i;
            if (opened != 0 && closed != 0 && opened == closed) {
                end = i;
                break;
            }
            prevOpened = opened;
        }

        System.out.println("\t\t\t<PRINTED> " + text + " </PRINTED>");

        if (text.contains("(") && text.contains(")")) {
            start += 1;
            end -= 1;

            char[] processingText = ArrayUtils.in_select(workingText, start, end);
            String processingTextS = StringUtils.charAryToString(processingText) + " = ";

            if (processingTextS.contains("(") && processingTextS.contains(")")) {
                processingText = StringUtils.toCharArray(process(processingTextS));
                for (int i = 0; i < 3; i++)
                    processingText = ArrayUtils.deleteItem(processingText, processingText.length - 1);
            }

            calculate(StringUtils.charAryToString(processingText) + " = ");

            String insertedResult = Double.toString(currentResult);
            String beforeResult = "";
            if (start > 0) {
                beforeResult =
                        StringUtils.charAryToString(ArrayUtils.in_select(workingText, 0, start - 2));
            }
            String afterResult = StringUtils.charAryToString(ArrayUtils.in_select(workingText, end + 2, workingText.length - 1));
            text = beforeResult + insertedResult + afterResult;
        }

        if (text.contains("(") && text.contains(")")) {

            process(text);

        } else {
            calculate(text);
            System.out.print("\t\t\t<PRINTED> " + text);
            System.out.println(currentResult + " </PRINTED>");
        }

        if (closed != opened) {
            throw new CustomException("Incorrect Expression Error",
                    "You typed incorrect math expression, it can't be processed.");
        }

        if (!text.contains("=")) {
            throw new CustomException("Unfinished Expression Error",
                    "You hadn't entered \"=\" sign,\nso I can't calculate values.");
        }
        return text;
    }

    public void newCalculation() {
        currentResult = 0;
        numberA = 0;
        numberB = 0;
    }

    private String findNumberA(int targetIndex, char[] workingText) {
        String outNumberA = "";

        int neededIndex = targetIndex - 2;

        while (neededIndex > 0 && (workingText[neededIndex - 1] != ' ')) {
            if (workingText[neededIndex - 1] != '(') neededIndex--;
            else break;
        }

        for (int i = neededIndex; i < targetIndex - 1; i++) outNumberA += workingText[i];

        start = neededIndex;

        return outNumberA;
    }

    private String findNumberB(int targetIndex, char[] workingText) {
        String outNumberB = "";

        int neededIndex = targetIndex + 2;

        while (neededIndex < workingText.length && workingText[neededIndex + 1] != ' ') {
            if (workingText[neededIndex + 1] != ')') neededIndex++;
            else break;
        }

        for (int i = targetIndex + 1; i < neededIndex + 1; i++) outNumberB += workingText[i];

        end = neededIndex;

        return outNumberB;
    }

    public String formatInput(String input) {
        if (input.charAt(0) == '-') input = " " + input;
        ArrayList<String> itemsOfExpression = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char symbol = input.charAt(i);
            if (ArrayUtils.array_has(actions, symbol) || symbol == '=') {
                itemsOfExpression.add(Character.toString(symbol));
            } else if (symbol != ' ') {
                int length = itemsOfExpression.size();
                String lastItem;
                if (length > 0) lastItem = itemsOfExpression.get(length - 1);
                else lastItem = "";
                if (itemsOfExpression.size() > 1) {
                    if (Objects.equals(lastItem, "-")) {
                        String beforeLastItem = itemsOfExpression.get(length - 2);
                        if (!(ArrayUtils.array_has(actions, lastChar(beforeLastItem)))) {
                            itemsOfExpression.add(Character.toString(symbol));
                            continue;
                        }
                    } else {
                        itemsOfExpression.add(Character.toString(symbol));
                        continue;
                    }
                }
                if (length > 0) lastItem = itemsOfExpression.get(length - 1);
                else lastItem = "";
                if (length > 0) {
                    itemsOfExpression.set(length - 1, lastItem + Character.toString(symbol));
                } else {
                    itemsOfExpression.add(Character.toString(symbol));
                }
            }
        }

        String out = "";
        for (String item : itemsOfExpression) {
            if (ArrayUtils.array_has(actions, item.charAt(item.length() - 1)) || Objects.equals(item, "=")) {
                out += " ";
                out += item;
                out += " ";
            } else out += item;
        }

        if (out.charAt(0) == '-') out = " " + out;

        return out;
    }

    public char lastChar(String string) { return string.charAt(string.length() - 1); }
}