package ua.project.calculator.files.libs.parsers;

import ua.project.calculator.files.libs.CustomException;

public interface AbstractVariableParser {
    String processText(String text, boolean normalLaunch) throws CustomException;

    String searchNested(String text);
}
