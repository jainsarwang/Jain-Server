package com.Server.Http;

import com.Server.utils.MIMEType;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final int CR = 0x0D; // 13
    private static final int LF = 0x0A; // 10
    private static final String CRLF = "\r\n";
    private final int CHUNK_SIZE = 4096;
    private HttpVersion httpVersion;
    private HttpStatusCode statusCode;
    private HashMap<String, String> headers = new HashMap<>();
    private String responseBody;
    private FileInputStream file;

    public HttpResponse() {
        // server info
        addHeaders("X-Powered-by", "jainsarwang");

        // date
        DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.now();
        addHeaders("Date", dateTime.format(formatObj) + " GMT");
    }

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
    public String getHeader(String key) { return this.headers.get(key); }

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
            if(fileName.matches("(.*)(" + mime.getExtension() + ")$")) {
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
        this.file = new FileInputStream(file);
//        this.file = new FileReader(file);
    }

    public void sendResponse(OutputStream writer) throws IOException {
        StringBuilder processingData = new StringBuilder();
        // creating status line
        processingData.append(this.httpVersion.LITERAL + " " + this.statusCode.STATUS_CODE + " " + this.statusCode.MESSAGE + CRLF);

        // creating headers
        for(Map.Entry<?, ?> header : this.headers.entrySet()) {
            processingData.append(header.getKey() + ": " + header.getValue() + CRLF);
        }
        processingData.append(CRLF);

        // flushing previous data to client
        writer.write(processingData.toString().getBytes());
        processingData.delete(0, processingData.length());

        if(file != null) {

            if(getHeader("transfer-encoding") != null) {
                //content encoding is set

                byte[] fileData = new byte[CHUNK_SIZE];
                int _len, total = 0;

                while((_len = file.read(fileData)) > 0) {
                    // response chunk
                    /**
                     *  format {
                         * SizeOfChunkInHexaDecimal
                         * CRLF
                         * DATA
                         * CRLF
                     * }
                     */
                    writer.write(Integer.toString(_len, 16).toUpperCase().getBytes());
                    writer.write(CRLF.getBytes());
                    writer.write(fileData);
                    writer.write(CRLF.getBytes());
                    writer.flush();
                }

                processingData.append(0 + CRLF + CRLF);
            }else {
                // when no content encoding | all the data send at once
                writer.write(file.readAllBytes());

            }
        }else if(responseBody != null){
            processingData.append(this.responseBody);
        }

        // end of response
        processingData.append(CRLF);
        processingData.append(CRLF);
        writer.write(processingData.toString().getBytes());
    }
}
