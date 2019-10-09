package ru.geekbrains.common;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadFileResponse implements Serializable {
    private String name;
    private static String SERVER_DIR = "C:\\coding\\cloud-storage\\cloud-storage\\serverDir\\";
    private byte[] data;

    public DownloadFileResponse(String name) {
        this.name = name;
        try {
            this.data = Files.readAllBytes(Paths.get(SERVER_DIR + name));
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
