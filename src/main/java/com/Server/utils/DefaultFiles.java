package com.Server.utils;

public enum DefaultFiles {
    ERROR_403_FILE("src/main/resources/error/403.html"),
    ERROR_404_FILE("src/main/resources/error/404.html");

    private String filePath;
    DefaultFiles(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return this.filePath;
    }
}
