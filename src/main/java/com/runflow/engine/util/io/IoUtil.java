package com.runflow.engine.util.io;

import com.runflow.engine.ActivitiException;

import java.io.*;
import java.net.URL;

public class IoUtil {

    public static byte[] readInputStream(InputStream inputStream, String inputStreamName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[16 * 1024];
        try {
            int bytesRead = inputStream.read(buffer);
            while (bytesRead != -1) {
                outputStream.write(buffer, 0, bytesRead);
                bytesRead = inputStream.read(buffer);
            }
        } catch (Exception e) {
            throw new ActivitiException("couldn't read input stream " + inputStreamName, e);
        }
        return outputStream.toByteArray();
    }

    public static String readFileAsString(String filePath) {
        byte[] buffer = new byte[(int) getFile(filePath).length()];
        BufferedInputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(getFile(filePath)));
            inputStream.read(buffer);
        } catch (Exception e) {
            throw new ActivitiException("Couldn't read file " + filePath + ": " + e.getMessage());
        } finally {
            IoUtil.closeSilently(inputStream);
        }
        return new String(buffer);
    }

    public static File getFile(String filePath) {
        URL url = IoUtil.class.getClassLoader().getResource(filePath);
        try {
            return new File(url.toURI());
        } catch (Exception e) {
            throw new ActivitiException("Couldn't get file " + filePath + ": " + e.getMessage());
        }
    }

    public static void writeStringToFile(String content, String filePath) {
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(getFile(filePath)));
            outputStream.write(content.getBytes());
            outputStream.flush();
        } catch (Exception e) {
            throw new ActivitiException("Couldn't write file " + filePath, e);
        } finally {
            IoUtil.closeSilently(outputStream);
        }
    }

    /**
     * Closes the given stream. The same as calling {@link InputStream#close()}, but errors while closing are silently ignored.
     */
    public static void closeSilently(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ignore) {
            // Exception is silently ignored
        }
    }

    /**
     * Closes the given stream. The same as calling {@link OutputStream#close()} , but errors while closing are silently ignored.
     */
    public static void closeSilently(OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException ignore) {
            // Exception is silently ignored
        }
    }
}
