package ru.geekbrains.server;

import io.netty.channel.ChannelHandlerContext;
import ru.geekbrains.common.DownloadFileRequest;
import ru.geekbrains.common.DownloadFileResponse;

public class DownloadFileRequestHandler {

    public void channelRead(ChannelHandlerContext ctx, DownloadFileRequest downloadFileRequest) {
        ctx.writeAndFlush(new DownloadFileResponse(downloadFileRequest.getFileName()));
        System.out.println("requested file send");
    }
}
