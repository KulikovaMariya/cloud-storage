package ru.geekbrains.server;

import java.util.HashMap;

public class AuthorizeService {
    private HashMap<String, String> users = new HashMap<>();
    private static final AuthorizeService instance = new AuthorizeService();

    public AuthorizeService() {
        users.put("ivan", "123");
        users.put("kate", "456");
        users.put("petr", "789");
    }

    public static AuthorizeService getInstance() {
        return instance;
    }

    public boolean authorize(String username, String password) {
        return password.equals(users.get(username));
    }
}
