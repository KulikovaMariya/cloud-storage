package ru.geekbrains.common;

import java.io.Serializable;

public class AuthorizeResponse implements Serializable {
    private boolean status;
    private String statusDescription;
    private String username;

    public AuthorizeResponse(boolean status, String statusDescription, String username) {
        this.status = status;
        this.statusDescription = statusDescription;
        this.username = username;
    }

    public AuthorizeResponse(boolean status, String username) {
        this.status = status;
        this.username = username;
    }

    public boolean isStatus() {
        return status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public String getUsername() {
        return username;
    }
}
