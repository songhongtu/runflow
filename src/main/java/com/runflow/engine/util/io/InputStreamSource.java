package com.runflow.engine.util.io;

import com.runflow.engine.RunFlowException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamSource implements StreamSource {
    // This class is used for bpmn parsing.
    // The bpmn parsers needs to go over the stream at least twice:
    // Once for the schema validation and once for the parsing itself.
    // So we keep the content of the inputstream in memory so we can
    // re-read it.

    protected BufferedInputStream inputStream;
    protected byte[] bytes;

    public InputStreamSource(InputStream inputStream) {
        this.inputStream = new BufferedInputStream(inputStream);
    }

    public InputStream getInputStream() {
        if (bytes == null) {
            try {
                bytes = getBytesFromInputStream(inputStream);
            } catch (IOException e) {
                throw new RunFlowException("Could not read from inputstream", e);
            }
        }
        return new BufferedInputStream(new ByteArrayInputStream(bytes));
    }

    public String toString() {
        return "InputStream";
    }

    public byte[] getBytesFromInputStream(InputStream inStream) throws IOException {
        long length = inStream.available();
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = inStream.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new RunFlowException("Could not completely read inputstream ");
        }

        // Close the input stream and return bytes
        inStream.close();
        return bytes;
    }

}
