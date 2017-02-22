import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Created by fajek on 2/22/17.
 */
public class Stream {
    private static BufferedInputStream inputStream;
//    private static String toReplace;
//    private static

    public static String readStreamToReplace() {
        System.out.println("Stream to replace: ");
        String toReplace = new String();
        try {
            inputStream = new BufferedInputStream(System.in);
            byte[] contents = new byte[1024];
            int bytesRead = 0;
            bytesRead = inputStream.read(contents);
            toReplace = new String(contents, 0, bytesRead);
            toReplace = toReplace.substring(0, toReplace.length() - 1);

        } catch (Exception e) {
            System.out.println(e);
        }

        return toReplace;
    }

    public static String readStreamReplacing() {
        System.out.println("Stream replacing: ");
        String replacing = new String();
        try {
            inputStream = new BufferedInputStream(System.in);
            byte[] contents = new byte[1024];
            int bytesRead = 0;
            bytesRead = inputStream.read(contents);
            replacing = new String(contents, 0, bytesRead);
            replacing = replacing.substring(0, replacing.length() - 1);

        } catch (Exception e) {
            System.out.println(e);
        }

        return replacing;
    }

}
