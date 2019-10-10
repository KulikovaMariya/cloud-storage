package ru.geekbrains.server;

import io.netty.channel.ChannelHandlerContext;
import ru.geekbrains.common.SendFileRequest;
import ru.geekbrains.common.SendFileResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SendFileRequestHandler {
    public void channelRead(ChannelHandlerContext ctx, SendFileRequest sendFileRequest) throws Exception {
        Path path = Paths.get(Server.SERVER_DIR + sendFileRequest.getUsername() + "\\" + sendFileRequest.getFileName());
        if (!Files.exists(path)) {
            Files.write(path, sendFileRequest.getData(), StandardOpenOption.CREATE);
            boolean operationStatus = Files.exists(path);
            ctx.writeAndFlush(new SendFileResponse(sendFileRequest.getFileName(), operationStatus));
        } else {
            ctx.writeAndFlush(new SendFileResponse(sendFileRequest.getFileName(), false, "Файл с таким именм уже сущестует"));
        }
    }
}
