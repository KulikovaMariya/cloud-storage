package ru.geekbrains.client;

import ru.geekbrains.common.DeleteFileResponse;

public class DeleteFileResponseHandler {
    private final ResponseListener responseListener;

    public DeleteFileResponseHandler(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void readChannel(DeleteFileResponse msg) {
        responseListener.onDeleteFile(msg.getFileName(), msg.isStatus(), msg.getStatusDescription());
        System.out.println(msg.getFileName() + " " + msg.isStatus());
    }
}
