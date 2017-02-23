import java.io.*;

/**
 * Created by fajek on 2/20/17.
 */
public class File {
    private static FileInputStream input = null;
//    private static FileOutputStream output = null;

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
//            System.out.println(bytes);
//            System.out.println(builder);

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

    public static boolean ifContains(String file, String stream) {
        boolean ifIs = false;

        //Check ignoring case
        if (file.toLowerCase().contains(stream.toLowerCase())) {
//            System.out.println("File contains certain stream.");
            ifIs = true;
        } else {
//            System.out.println("Input stream was not found in the file.");
            ifIs = false;
        }

        return ifIs;
    }

    public static void writeFile(String fileName, String changedString) {
        BufferedWriter writer = null;
        try {
//            output = new FileOutputStream("input1.txt");
//            int stream;
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
