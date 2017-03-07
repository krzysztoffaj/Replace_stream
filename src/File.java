import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * Created by fajek on 2/20/17.
 */
public class File {
    private static FileInputStream input = null;

    public static String loadFile(String fileName) {
        try {
            input = new FileInputStream(fileName);
            StringBuilder builder = new StringBuilder();
            int readInt;
            while ((readInt = input.read()) != -1) {
                builder.append((char) readInt);
            }
            String loadedFile = builder.toString();

            return loadedFile;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    public static int ifContains(String file, String text) {
        int numberOfChanges = 0;

        //Check ignoring case
        if (file.contains(text)) {
            numberOfChanges = StringUtils.countMatches(file, text);
        }

        return numberOfChanges;
    }

    public static int ifContainsIgnoringCase(String file, String text) {
        int numberOfChanges = 0;

        //Check ignoring case
        if (file.toLowerCase().contains(text.toLowerCase())) {
            numberOfChanges = StringUtils.countMatches(file.toLowerCase(), text.toLowerCase());
        }

        return numberOfChanges;
    }

    public static void writeFile(String fileName, String changedString) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(changedString);

        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
