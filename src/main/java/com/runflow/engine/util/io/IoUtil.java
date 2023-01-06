package com.runflow.engine.util.io;

import com.runflow.engine.RunFlowException;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

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
            throw new RunFlowException("couldn't read input stream " + inputStreamName, e);
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
            throw new RunFlowException("Couldn't read file " + filePath + ": " + e.getMessage());
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
            throw new RunFlowException("Couldn't get file " + filePath + ": " + e.getMessage());
        }
    }

    public static void writeStringToFile(String content, String filePath) {
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(getFile(filePath)));
            outputStream.write(content.getBytes());
            outputStream.flush();
        } catch (Exception e) {
            throw new RunFlowException("Couldn't write file " + filePath, e);
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


    /**
     * 流转换成文件
     *
     * @param inputStream
     */
    public static File inputStreamToFile(InputStream inputStream) {


        try {



            //新建文件
            File tempFile = File.createTempFile("bmpn", ".tmp");
            OutputStream os = new FileOutputStream(tempFile);
            int read = 0;
            byte[] bytes = new byte[1024 * 1024];
            //先读后写
            while ((read = inputStream.read(bytes)) > 0) {
                byte[] wBytes = new byte[read];
                System.arraycopy(bytes, 0, wBytes, 0, read);
                os.write(wBytes);
            }
            os.flush();
            os.close();
            inputStream.close();

            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
