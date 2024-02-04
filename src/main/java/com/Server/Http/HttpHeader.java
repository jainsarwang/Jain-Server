package com.Server.Http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpHeader {
    private String headerName;
    private String headerValue;
    private static int messageLength;

    public HttpHeader(String headerName) {
        this.headerName = headerName.toLowerCase();
    }

    public HttpHeader(String headerName, String headerValue) {
        this.headerName = headerName.toLowerCase();
        this.headerValue = headerValue.toLowerCase();
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName.toLowerCase();
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        // TODO: NEED TO FIX VALID HEADER VALUE PARSER => https://datatracker.ietf.org/doc/html/rfc7230#section-3.2.6

        this.headerValue = headerValue.toLowerCase();
    }
}
