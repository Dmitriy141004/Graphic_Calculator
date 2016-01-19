package ua.project.calculator.files.libs;

public class StringUtils {

    public static String charAryToString(char[] chars) {
        String out = "";
        for (char charValue : chars) {
            out += charValue;
        }
        return out;
    }

    public static String removeChar(String string, int index) {
        char[] tech = string.toCharArray();
        char[] out = ArrayUtils.deleteItem(tech, index);
        return charAryToString(out);
    }
}
