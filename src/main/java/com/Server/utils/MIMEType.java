package com.Server.utils;

public enum MIMEType {
    TEXT_HTML("text/html", "html|htm|ico"),
    TEXT_PHP("text/html", "php"),
    TEXT_CSS("text/css", "css"),
    TEXT_JS("text/javascript", "js"),
    TEXT_TXT("text/plain", "txt"),
    IMAGE_PNG("image/png", "png"),
    IMAGE_JPEG("image/jpeg", "jpeg|jpg"),
    IMAGE_GIF("image/gif", "gif"),
    IMAGE_WEBP("image/webp", "webp");

    private final String contentType;
    private final String extension;

    MIMEType(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }

    public String getContentType() {
        return contentType;
    }

    public String getExtension() {
        return extension;
    }
}
