package ru.geekbrains.common;

import java.io.Serializable;

public class SendFileResponse implements Serializable {
    private String sendFileName;
    private boolean status;
    private String statusDiscription;

    public SendFileResponse(String sendFileName, boolean status) {
        this.sendFileName = sendFileName;
        this.status = status;
    }

    public SendFileResponse(String sendFileName, boolean status, String statusDiscription) {
        this.sendFileName = sendFileName;
        this.status = status;
        this.statusDiscription = statusDiscription;
    }

    public boolean isStatus() {
        return status;
    }

    public String getStatusDescription() {
        return statusDiscription;
    }

    public String getSendFileName() {
        return sendFileName;
    }
}
