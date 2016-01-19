package ua.project.calculator.files.libs.parsers;

import java.util.HashMap;

public interface AbstractDataFileParser {
    HashMap<String, String> parse(String text);
}
