package ru.geekbrains.common;

import java.io.Serializable;

public class DownloadFileResponse implements Serializable {
    private String fileName;
    private byte[] data;

    public DownloadFileResponse(String name, byte[] data) {
        this.fileName = name;
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }
}
