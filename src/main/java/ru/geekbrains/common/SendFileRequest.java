package ru.geekbrains.common;

public class SendFileRequest extends CommonRequest {
    private String fileName;
    private byte[] data;

    public SendFileRequest(String username, String fileName, byte[] data) {
        this.username = username;
        this.fileName = fileName;
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }
}
