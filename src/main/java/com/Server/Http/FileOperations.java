package com.Server.Http;

import com.Server.utils.DefaultFiles;
import com.Server.utils.FileResponse;

import java.io.File;

public class FileOperations {
    String docRoot;
    String filePath;
    public FileOperations(String docRoot, String filePath) {
        this.docRoot = docRoot;
        this.filePath = filePath;
    }

    public String getFileLocation() {
        return docRoot + filePath;
    }

    public FileResponse getMostSuitableFile()  {
        FileResponse res = null;
        File file = new File(getFileLocation());

        if (file.exists()) {
            if (file.isDirectory()) {
                file = new File(DefaultFiles.ERROR_403_FILE.getFilePath());
                res = new FileResponse(HttpStatusCode.CLIENT_ERROR_403_FOR_BIDDEN, file);
            } else {
                res = new FileResponse(HttpStatusCode.CLIENT_ERROR_200_OK, file);
            }
        } else {
            // file not found 404 error
            file = new File(DefaultFiles.ERROR_404_FILE.getFilePath());

            res = new FileResponse(HttpStatusCode.CLIENT_ERROR_404_NOT_FOUND, file);
        }

        return res;
    }
}
