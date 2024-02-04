package com.Server.Http;

public enum HttpMethod {
    GET, HEAD, POST;

    public static final int MAX_LENGTH;
    static {
        int tempMaxLength = -1;
        for(HttpMethod method : HttpMethod.values()) {
            if (method.name().length() > tempMaxLength) {
                tempMaxLength = method.name().length();
            }
        }

        MAX_LENGTH = tempMaxLength;
    }
}
