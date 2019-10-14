package ru.geekbrains.common;

public class DeleteFileRequest extends CommonRequest {
    private String fileName;

    public DeleteFileRequest(String username, String fileName) {
        this.username = username;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
