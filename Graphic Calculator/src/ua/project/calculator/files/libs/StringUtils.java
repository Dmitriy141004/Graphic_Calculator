package ua.project.calculator.files.libs;

public class StringUtils {
    public static char[] toCharArray(String string) {
        return string.toCharArray();
    }

    public static char toChar(String string) {
        char out = '\n';
        if (string.length() == 1) {
            out = string.charAt(0);
        }
        return out;
    }

    public static void println(Object obj) {
        System.out.println(obj);
    }
    public static void println() {
        System.out.println();
    }

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

    public static String charToString(char c) {
        return Character.toString(c);
    }
}
