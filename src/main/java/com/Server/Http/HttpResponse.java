package com.Server.Http;

import com.Server.utils.MIMEType;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final int CR = 0x0D; // 13
    private static final int LF = 0x0A; // 10
    private static final String CRLF = "\r\n";
    private final int CHUNK_SIZE = 100;
    private HttpVersion httpVersion;
    private HttpStatusCode statusCode;
    private HashMap<String, String> headers = new HashMap<>();
    private String responseBody;
    private FileReader file;

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(HttpVersion httpVersion) {
        this.httpVersion = httpVersion;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void addHeaders(String key, String value) {
        this.headers.put(key, value);
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;

        if(headers.get("content-type") == null){
            addHeaders("content-type", MIMEType.TEXT_HTML.getContentType());
        }

        addHeaders("content-length", Integer.toString(responseBody.getBytes().length));
    }

    public void setResponseBody(File file ) throws FileNotFoundException {
        String fileName = file.getName();

        for(MIMEType mime : MIMEType.values()) {
            if(fileName.matches("(" + mime.getExtension() + "$)")) {
                addHeaders("content-type", mime.getContentType());
                break;
            }
        }

        if(headers.get("content-type") == null){
            addHeaders("content-type", MIMEType.TEXT_HTML.getContentType());
        }

        addHeaders("content-length", Long.toString(file.length()));

        if(Integer.parseInt(headers.get("content-length")) > CHUNK_SIZE) {
            addHeaders("transfer-encoding", "chunked");
        }

        this.file = new FileReader(file);
    }

    public void sendResponse(OutputStream writer) throws IOException {
        StringBuilder processingData = new StringBuilder();
        processingData.append(this.httpVersion.LITERAL + " " + this.statusCode.STATUS_CODE + " " + this.statusCode.MESSAGE + CRLF);

        for(Map.Entry<?, ?> header : this.headers.entrySet()) {
            processingData.append(header.getKey() + ": " + header.getValue() + CRLF);
        }

        processingData.append(CRLF);

        writer.write(processingData.toString().getBytes());
        processingData.delete(0, processingData.length());

        if(file != null) {

            int _len;
            char[] character = new char[CHUNK_SIZE];
            while((_len = file.read(character)) > 0) {
                processingData.append(character, 0, _len);

                String rawData = Integer.toString(_len, 16).toUpperCase() + CRLF + processingData.toString() + CRLF;
                processingData.delete(0, processingData.length());
                writer.write(rawData.getBytes());
                writer.flush();

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            processingData.append(0 + CRLF + CRLF);

            processingData.append(CRLF);
            processingData.append(CRLF);

        }else if(responseBody != null){
            processingData.append(this.responseBody);
            processingData.append(CRLF);
            processingData.append(CRLF);
        }

        writer.write(processingData.toString().getBytes());
    }
}
