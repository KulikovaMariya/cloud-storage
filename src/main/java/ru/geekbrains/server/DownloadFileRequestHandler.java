package ru.geekbrains.server;

import io.netty.channel.ChannelHandlerContext;
import ru.geekbrains.common.DownloadFileRequest;
import ru.geekbrains.common.DownloadFileResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadFileRequestHandler {

    public void channelRead(ChannelHandlerContext ctx, DownloadFileRequest downloadFileRequest) {
        try {
            byte[] data = Files.readAllBytes(Paths.get(Server.SERVER_DIR + downloadFileRequest.getUsername() + "\\" + downloadFileRequest.getFileName()));
            ctx.writeAndFlush(new DownloadFileResponse(downloadFileRequest.getFileName(), data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
