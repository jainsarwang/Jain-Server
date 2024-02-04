package com.Server.Http;

public enum HttpStatusCode {
    /*--- CLIENT ERRORS ---*/
    CLIENT_ERROR_200_OK(200, "OK"), //
    CLIENT_ERROR_400_BAD_REQUEST(400, "Bad Request"), //
    CLIENT_ERROR_401_METHOD_NOT_ALLOWED(401, "Method Not ALlowed"), // INVALID METHOD
    CLIENT_ERROR_403_FOR_BIDDEN(403, "Forbidden"), // INVALID METHOD
    CLIENT_ERROR_404_NOT_FOUND(404, "Not Found"), // INVALID METHOD
    CLIENT_ERROR_411_LENGTH_REQUIRED(411, "Length Required"),
    CLIENT_ERROR_414_URI_TOO_LONG(414, "Bad Request"),

    /*--- SERVER ERRORS ---*/
    SERVER_ERROR_500_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVER_ERROR_501_NOT_IMPLEMENTED(501, "Not Implemented"), // METHOD IS VALID BUT NOT IMPLEMENTED
    SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported")
    ;

    public final int STATUS_CODE;
    public final String MESSAGE;


    HttpStatusCode(int statusCode, String message) {
        STATUS_CODE = statusCode;
        MESSAGE = message;
    }
}
