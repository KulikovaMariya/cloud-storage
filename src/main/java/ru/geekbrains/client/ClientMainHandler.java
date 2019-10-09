package ru.geekbrains.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.geekbrains.common.DeleteFileResponse;
import ru.geekbrains.common.DownloadFileResponse;
import ru.geekbrains.common.RefreshFileListResponse;
import ru.geekbrains.common.SendFileResponse;

public class ClientMainHandler extends ChannelInboundHandlerAdapter {
    private final DownloadFileResponseHandler downloadFileResponseHandler;
    private final SendFileResponseHandler sendFileResponseHandler;
    private final DeleteFileResponseHandler deleteFileResponseHandler;
    private final RefreshFileListResponseHandler refreshFileListResponseHandler;

    public ClientMainHandler(ResponseListener responseListener) {
        downloadFileResponseHandler = new DownloadFileResponseHandler(responseListener);
        sendFileResponseHandler = new SendFileResponseHandler(responseListener);
        deleteFileResponseHandler = new DeleteFileResponseHandler(responseListener);
        refreshFileListResponseHandler = new RefreshFileListResponseHandler(responseListener);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof DownloadFileResponse) {
            downloadFileResponseHandler.channelRead((DownloadFileResponse) msg);
        } else if (msg instanceof SendFileResponse) {
            sendFileResponseHandler.channelRead((SendFileResponse) msg);
        } else if (msg instanceof DeleteFileResponse) {
            deleteFileResponseHandler.readChannel((DeleteFileResponse) msg);
        } else if (msg instanceof RefreshFileListResponse) {
            refreshFileListResponseHandler.channelRead((RefreshFileListResponse) msg);
        }
    }
}
