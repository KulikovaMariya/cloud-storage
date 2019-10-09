package ru.geekbrains.client;

import java.util.List;

public interface ResponseListener {
    void onRefreshFileList(List<String> fileList);

    void onDeleteFile(String fileName, boolean status, String statusDescription);

    void onSendFile(String sendFileName, boolean status, String statusDescription);

    void onDownloadFile(String fileName, byte[] data);
}
