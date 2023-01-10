package com.runflow.engine.util.io;

import com.runflow.engine.RunFlowException;

import java.io.*;
import java.net.URL;

public class IoUtil {

    private IoUtil(){}

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
            throw new RunFlowException("couldn't read input stream " + inputStreamName, e);
        }
        return outputStream.toByteArray();
    }


    public static File getFile(String filePath) {
        URL url = IoUtil.class.getClassLoader().getResource(filePath);
        try {
            return new File(url.toURI());
        } catch (Exception e) {
            throw new RunFlowException("Couldn't get file " + filePath + ": " + e.getMessage());
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
