package abk.com.gap;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by user on 4/06/2016.
 */
public class StreamUtils {
    public static byte[] read(InputStream in) throws IOException {
        InputStream bin = new BufferedInputStream(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buff = new byte[4096];
        int n = 0;
        while ((n = in.read(buff)) >= 0) {
            out.write(buff, 0, n);
        }
        bin.close();

        return out.toByteArray();
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4096];
        int read = 0;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
    }

    public static String fromStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }
}