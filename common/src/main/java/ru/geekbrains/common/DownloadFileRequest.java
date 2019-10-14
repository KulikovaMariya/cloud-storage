package ru.geekbrains.common;

public class DownloadFileRequest extends CommonRequest {
    private String fileName;

    public DownloadFileRequest(String username, String fileName) {
        this.username = username;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
