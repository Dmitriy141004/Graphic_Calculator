package libs;

import java.util.Objects;

public class ArrayUtils {
    public static int[] deleteItem(int[] array, int index) {
        int[] out = new int[array.length - 1];
        int foundIndex = 0;
        for (int i = 0; i < index; i++) {
            out[i] = array[i];
            foundIndex++;
        }
        for (int i = foundIndex + 1; i < array.length; i++) {
            out[i - 1] = array[i];
        }
        return out;
    }
    public static float[] deleteItem(float[] array, int index) {
        float[] out = new float[array.length - 1];
        int foundIndex = 0;
        for (int i = 0; i < index; i++) {
            out[i] = array[i];
            foundIndex++;
        }
        for (int i = foundIndex + 1; i < array.length; i++) {
            out[i - 1] = array[i];
        }
        return out;
    }
    public static boolean[] deleteItem(boolean[] array, int index) {
        boolean[] out = new boolean[array.length - 1];
        int foundIndex = 0;
        for (int i = 0; i < index; i++) {
            out[i] = array[i];
            foundIndex++;
        }
        for (int i = foundIndex + 1; i < array.length; i++) {
            out[i - 1] = array[i];
        }
        return out;
    }
    public static String[] deleteItem(String[] array, int index) {
        String[] out = new String[array.length - 1];
        int foundIndex = 0;
        for (int i = 0; i < index; i++) {
            out[i] = array[i];
            foundIndex++;
        }
        for (int i = foundIndex + 1; i < array.length; i++) {
            out[i - 1] = array[i];
        }
        return out;
    }
    public static double[] deleteItem(double[] array, int index) {
        double[] out = new double[array.length - 1];
        int foundIndex = 0;
        for (int i = 0; i < index; i++) {
            out[i] = array[i];
            foundIndex++;
        }
        for (int i = foundIndex + 1; i < array.length; i++) {
            out[i - 1] = array[i];
        }
        return out;
    }


    public static void printAllInArray(int[] array, String split) {
        for (int object : array) {
            System.out.print(object);
            if (!Objects.equals(indexOf(array, object), array.length - 1)) System.out.print(split);
        }
        System.out.println("");
    }
    public static void printAllInArray(int[] array) {
        for (int object : array) {
            System.out.println(object);
        }
    }
    public static void printAllInArray(char[] array, String split) {
        for (char object : array) {
            System.out.print(object);
            if (!Objects.equals(indexOf(array, object), array.length - 1)) System.out.print(split);
        }
        System.out.println("");
    }
    public static void printAllInArray(char[] array) {
        for (int object : array) {
            System.out.println(object);
        }
    }
    public static void printAllInArray(float[] array, String split) {
        for (float object : array) {
            System.out.print(object);
            if (!Objects.equals(indexOf(array, object), array.length - 1)) System.out.print(split);
        }
        System.out.println("");
    }
    public static void printAllInArray(float[] array) {
        for (float object : array) {
            System.out.println(object);
        }
    }
    public static void printAllInArray(double[] array, String split) {
        for (double object : array) {
            System.out.print(object);
            if (!Objects.equals(indexOf(array, object), array.length - 1)) System.out.print(split);
        }
        System.out.println("");
    }
    public static void printAllInArray(double[] array) {
        for (double object : array) {
            System.out.println(object);
        }
    }
    public static void printAllInArray(String[] array, String split) {
        for (String object : array) {
            System.out.print(object);
            if (!Objects.equals(indexOf(array, object), array.length - 1)) System.out.print(split);
        }
        System.out.println("");
    }
    public static void printAllInArray(String[] array) {
        for (String object : array) {
            System.out.println(object);
        }
    }



    public static int[] range(int from, int to, int step) {
        int[] out = new int[to];
        for (int i = from; i < to + 1; i += step) {
            out[i - from] = i;
        }
        return out;
    }
    public static int[] range(int from, int to) {
        int[] out = new int[to];
        for (int i = from; i < to + 1; i += 1) {
            out[i - from] = i;
        }
        return out;
    }

    public static char[] deleteItem(char[] array, int index) {
        char[] out = new char[array.length - 1];
        int foundIndex = 0;
        for (int i = 0; i < index; i++) {
            out[i] = array[i];
            foundIndex++;
        }
        for (int i = foundIndex + 1; i < array.length; i++) {
            out[i - 1] = array[i];
        }
        return out;
    }

    public static boolean array_has(int[] array, int item) {
        boolean out = false;
        for (int thing : array) {
            if (Objects.equals(thing, item)) {
                out = true;
                break;
            }
        }
        return out;
    }
    public static boolean array_has(float[] array, float item) {
        boolean out = false;
        for (float thing : array) {
            if (Objects.equals(thing, item)) {
                out = true;
                break;
            }
        }
        return out;
    }
    public static boolean array_has(boolean[] array, boolean item) {
        boolean out = false;
        for (boolean thing : array) {
            if (Objects.equals(thing, item)) {
                out = true;
                break;
            }
        }
        return out;
    }
    public static boolean array_has(String[] array, String item) {
        boolean out = false;
        for (String thing : array) {
            if (Objects.equals(thing, item)) {
                out = true;
                break;
            }
        }
        return out;
    }
    public static boolean array_has(double[] array, double item) {
        boolean out = false;
        for (double thing : array) {
            if (Objects.equals(thing, item)) {
                out = true;
                break;
            }
        }
        return out;
    }
    public static boolean array_has(char[] array, char item) {
        boolean out = false;
        for (char thing : array) {
            if (Objects.equals(thing, item)) {
                out = true;
                break;
            }
        }
        return out;
    }

    public static int[] in_select(int[] array, int i1, int i22) {
        int i2 = i22 + 2;
        int[] out = new int[i2 - i1 - 1];
        for (int i = i1; i < i2 - 1; i++) {
            out[i - i1] = array[i];
        }
        return out;
    }
    public static float[] in_select(float[] array, int i1, int i22) {
        int i2 = i22 + 2;
        float[] out = new float[i2 - i1 - 1];
        for (int i = i1; i < i2 - 1; i++) {
            out[i - i1] = array[i];
        }
        return out;
    }
    public static boolean[] in_select(boolean[] array, int i1, int i22) {
        int i2 = i22 + 2;
        boolean[] out = new boolean[i2 - i1 - 1];
        for (int i = i1; i < i2 - 1; i++) {
            out[i - i1] = array[i];
        }
        return out;
    }
    public static String[] in_select(String[] array, int i1, int i22) {
        int i2 = i22 + 2;
        String[] out = new String[i2 - i1 - 1];
        for (int i = i1; i < i2 - 1; i++) {
            out[i - i1] = array[i];
        }
        return out;
    }
    public static double[] in_select(double[] array, int i1, int i22) {
        int i2 = i22 + 2;
        double[] out = new double[i2 - i1 - 1];
        for (int i = i1; i < i2 - 1; i++) {
            out[i - i1] = array[i];
        }
        return out;
    }
    public static char[] in_select(char[] array, int i1, int i22) {
        int i2 = i22 + 2;
        char[] out = new char[i2 - i1 - 1];
        for (int i = i1; i < i2 - 1; i++) {
            out[i - i1] = array[i];
        }
        return out;
    }

    public static int indexOf(int[] array, int item) {
        int out = 0;
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(item, array[i])) {
                out = i;
                break;
            }
        }
        return out;
    }
    public static int indexOf(float[] array, float item) {
        int out = 0;
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(item, array[i])) {
                out = i;
                break;
            }
        }
        return out;
    }
    public static int indexOf(boolean[] array, boolean item) {
        int out = 0;
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(item, array[i])) {
                out = i;
                break;
            }
        }
        return out;
    }
    public static int indexOf(String[] array, String item) {
        int out = 0;
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(item, array[i])) {
                out = i;
                break;
            }
        }
        return out;
    }
    public static int indexOf(double[] array, double item) {
        int out = 0;
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(item, array[i])) {
                out = i;
                break;
            }
        }
        return out;
    }
    public static int indexOf(char[] array, char item) {
        int out = 0;
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(item, array[i])) {
                out = i;
                break;
            }
        }
        return out;
    }


}
