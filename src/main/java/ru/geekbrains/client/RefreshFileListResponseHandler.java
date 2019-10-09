package ru.geekbrains.client;

import ru.geekbrains.common.RefreshFileListResponse;

public class RefreshFileListResponseHandler {
    private final ResponseListener responseListener;

    public RefreshFileListResponseHandler(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void channelRead(RefreshFileListResponse msg) {
        responseListener.onRefreshFileList(msg.getServerFileList());
    }
}
