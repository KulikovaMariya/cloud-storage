package ru.geekbrains.common;

import java.io.Serializable;

public class DeleteFileRequest implements Serializable {

    private String fileName;

    public DeleteFileRequest(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
