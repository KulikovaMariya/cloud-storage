package ru.geekbrains.common;

import java.io.Serializable;

public class DownloadFileRequest implements Serializable {

    private String fileName;

    public DownloadFileRequest(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
