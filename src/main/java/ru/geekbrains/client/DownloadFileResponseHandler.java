package ru.geekbrains.client;

import ru.geekbrains.common.DownloadFileResponse;

public class DownloadFileResponseHandler {

    private final ResponseListener responseListener;

    public DownloadFileResponseHandler(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void channelRead(DownloadFileResponse msg) throws Exception {
        responseListener.onDownloadFile(msg.getName(), msg.getData());
    }
}

