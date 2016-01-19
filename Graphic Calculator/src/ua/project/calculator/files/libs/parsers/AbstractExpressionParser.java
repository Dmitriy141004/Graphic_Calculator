package ua.project.calculator.files.libs.parsers;

import ua.project.calculator.files.libs.CustomException;

public interface AbstractExpressionParser {
    String process(String expression) throws CustomException;

    String formatInput(String input);
}
