package com.Server.Http;

import com.Server.utils.URLParser;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class HttpParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; // 32
    private static final int CR = 0x0D; // 13
    private static final int LF = 0x0A; // 10
    private static final int CL = 0x3A; // 58 --> colon

    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest request = new HttpRequest();

        try {
            parseRequestLine(reader, request);
            parseHeader(reader, request);
//            parseBody(reader, request);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return request;
    }


    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
        StringBuilder processingDataBuffer = new StringBuilder();

        int _byte;
        boolean parsedMethod = false, parsedRequestTarget = false, parsedVersion = false;

        while((_byte = reader.read()) >= 0 ) {
            if(_byte == CR) {
                _byte = reader.read();
                if(_byte == LF) {

                    if(!parsedMethod || !parsedRequestTarget) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    try {
                        request.setHttpVersion(processingDataBuffer.toString());
                    } catch (BadHttpVersionException e) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }

                    return;
                }

                throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
            }

            if(_byte == SP) {
                // PROCESS PREVIOUS DATA
                if(!parsedMethod) {
                    request.setMethod(processingDataBuffer.toString());
                    parsedMethod = true;
                }
                else if(!parsedRequestTarget) {
                    request.setRequestTarget((processingDataBuffer.toString()));
                    parsedRequestTarget = true;
                }else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                processingDataBuffer.delete(0, processingDataBuffer.length());

            }else {
                // handling long string
                if(!parsedMethod) {
                    // for method
                    if(processingDataBuffer.length() > HttpMethod.MAX_LENGTH) {
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    };
                }

                processingDataBuffer.append((char) _byte);
            }

        }
    }

    private void parseHeader(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
        StringBuilder processString = new StringBuilder();
        Set<HttpHeader> headers = new HashSet<>();
        HttpHeader header = null;

        int _byte;

        while((_byte = reader.read()) >= 0) {
            if(_byte == CR) {
                if((_byte = reader.read()) == LF) {
                    // another header ended, store it
                    if (header == null) {
                        // header part completed

                        break;
                    }

                    header.setHeaderValue(processString.toString());
                    headers.add(header);
                    header = null;

                    processString.delete(0, processString.length());
                }else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }

            }else if(_byte == SP && header == null) {
                // header have space or before header there is space

                throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
            }else if(_byte == CL && header == null) {
                // colon detected

                header = new HttpHeader(processString.toString());
                processString.delete(0, processString.length());

            }else {
                processString.append((char) _byte);
            }
        }

        request.setHttpHeaders(headers);
    }

    private void parseBody (InputStreamReader reader , HttpRequest request) throws IOException {
        int _byte;
        StringBuilder processingData = new StringBuilder();

        while((_byte = reader.read()) >= 0) {
            System.out.println(_byte);
            if(_byte == CR) {
                System.out.println("CR DETECTED");
                if(_byte == LF) {
                System.out.println("LF DETECTED");
                    return;
                }
            }else {
                processingData.append((char) _byte);
                System.out.println(_byte);
            }
        }
    }




}
