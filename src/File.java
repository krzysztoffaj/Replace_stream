import com.sun.deploy.util.StringUtils;

import java.io.*;
//import org.apache.commons.

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
            byte[] bytes;
            String loadedFile = builder.toString();
            bytes = loadedFile.getBytes();

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

    public static int ifContains(String file, String stream) {
        int numberOfChanges = 0;

        //Check ignoring case
        if (file.toLowerCase().contains(stream.toLowerCase())) {
            numberOfChanges = org.apache.commons.lang3.StringUtils.countMatches(file, stream);
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
