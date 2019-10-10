package ru.geekbrains.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.geekbrains.common.*;

public class ServerMainHandler extends ChannelInboundHandlerAdapter {
    private DownloadFileRequestHandler downloadFileRequestHandler = new DownloadFileRequestHandler();
    private SendFileRequestHandler sendFileRequestHandler = new SendFileRequestHandler();
    private DeleteFileRequestHandler deleteFileRequestHandler = new DeleteFileRequestHandler();
    private RefreshFileListRequestHandler refreshFileListRequestHandler = new RefreshFileListRequestHandler();
    private AuthorizeRequestHandler authorizeRequestHandler = new AuthorizeRequestHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof DownloadFileRequest) {
            downloadFileRequestHandler.channelRead(ctx, (DownloadFileRequest) msg);
        } else if (msg instanceof SendFileRequest) {
            sendFileRequestHandler.channelRead(ctx, (SendFileRequest) msg);
        } else if (msg instanceof DeleteFileRequest) {
            deleteFileRequestHandler.channelRead(ctx, (DeleteFileRequest) msg);
        } else if(msg instanceof RefreshFileListRequest) {
            refreshFileListRequestHandler.readChannel(ctx, (RefreshFileListRequest) msg);
        } else if (msg instanceof AuthorizeRequest) {
            authorizeRequestHandler.readChannel(ctx, (AuthorizeRequest) msg);
        }
    }
}
