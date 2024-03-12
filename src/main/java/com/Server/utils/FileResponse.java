package com.Server.utils;

import com.Server.Http.HttpStatusCode;

import java.io.File;

public class FileResponse {
    public File file;
    public HttpStatusCode statusCode;

    public FileResponse(HttpStatusCode statusCode, File file) {
        this.statusCode = statusCode;
        this.file = file;
    }
}
