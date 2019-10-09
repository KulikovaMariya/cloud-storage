package ru.geekbrains.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RefreshFileListResponse implements Serializable {

    private List<String> serverFileList = new ArrayList<>();

    public List<String> getServerFileList() {
        return serverFileList;
    }

    public void addToFileList(String fileName) {
        this.serverFileList.add(fileName);
    }
}
