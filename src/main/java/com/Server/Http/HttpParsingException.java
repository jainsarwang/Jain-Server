package com.Server.Http;

public class HttpParsingException extends Exception{


    private HttpStatusCode errorCode;

    HttpParsingException(HttpStatusCode errorCode)   {
        super(errorCode.MESSAGE);

        this.errorCode = errorCode;
    }

    public HttpStatusCode getErrorCode() {
        return errorCode;
    }
}
