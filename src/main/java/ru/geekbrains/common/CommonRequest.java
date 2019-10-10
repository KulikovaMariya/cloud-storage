package ru.geekbrains.common;

import java.io.Serializable;

public abstract class CommonRequest implements Serializable {
    protected String username;

    public String getUsername() {
        return username;
    }
}
