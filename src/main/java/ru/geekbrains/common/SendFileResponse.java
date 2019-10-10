package ru.geekbrains.common;

import java.io.Serializable;

public class SendFileResponse implements Serializable {
    private String sendFileName;
    private boolean status;
    private String statusDescription;

    public SendFileResponse(String sendFileName, boolean status) {
        this(sendFileName, status, "SUCCESS");
    }

    public SendFileResponse(String sendFileName, boolean status, String statusDescription) {
        this.sendFileName = sendFileName;
        this.status = status;
        this.statusDescription = statusDescription;
    }

    public boolean isStatus() {
        return status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public String getSendFileName() {
        return sendFileName;
    }
}
