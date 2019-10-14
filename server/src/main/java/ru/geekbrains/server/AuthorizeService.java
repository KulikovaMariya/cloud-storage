package ru.geekbrains.server;

import java.util.HashMap;

public class AuthorizeService {
    private static HashMap<String, String> users = new HashMap<>();

    static {
        users.put("ivan", "123");
        users.put("kate", "456");
        users.put("petr", "789");
    }

    public static boolean authorize(String username, String password) {
        return password.equals(users.get(username));
    }
}
