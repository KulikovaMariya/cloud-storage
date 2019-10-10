package ru.geekbrains.common;

public class AuthorizeRequest extends CommonRequest {
    private String password;

    public AuthorizeRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
