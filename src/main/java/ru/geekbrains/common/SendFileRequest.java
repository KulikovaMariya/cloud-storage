package ru.geekbrains.common;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

import java.nio.file.Paths;

public class SendFileRequest implements Serializable {
    private String name;
    private static String DIR = "C:\\coding\\cloud-storage\\cloud-storage\\clientDir\\";
    private byte[] data;

    public SendFileRequest(String name) {
        this.name = name;
        String temp = DIR + name;
        try {
            this.data = Files.readAllBytes(Paths.get(temp));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }
}
