import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by fajek on 2/16/17.
 */
public class Main {
    private static String fileText;
    private static String toReplace;
    private static String replacing;
    private static boolean contains;
    private static File file;
    private static Stream input;

    public static void main(String[] args) throws IOException {
        System.out.println("A program to replace streams.");

        fileText = file.loadFile("input1.txt");
        toReplace = input.readStreamToReplace();
        String replacedString = null;
        replacing = input.readStreamReplacing();
        contains = file.ifContains(fileText, toReplace);

        while (contains != false) {
            replacedString = fileText.replace(toReplace, replacing);
            contains = file.ifContains(replacedString, toReplace);
        }

        if (replacedString != null) {
            file.writeFile("input1.txt", replacedString);
        }
//        System.out.println(replacedString);
    }
}
