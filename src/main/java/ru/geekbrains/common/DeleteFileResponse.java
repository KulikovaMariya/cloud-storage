package ru.geekbrains.common;

import java.io.Serializable;

public class DeleteFileResponse implements Serializable {
    private String fileName;
    private boolean status;
    private String statusDescription;

    public DeleteFileResponse(String fileName, boolean status) {
        this.fileName = fileName;
        this.status = status;
    }

    public DeleteFileResponse(String fileName, boolean status, String statusDescription) {
        this.fileName = fileName;
        this.status = status;
        this.statusDescription = statusDescription;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isStatus() {
        return status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }
}
