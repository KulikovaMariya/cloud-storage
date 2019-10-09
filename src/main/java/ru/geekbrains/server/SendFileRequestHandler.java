package ru.geekbrains.server;

import io.netty.channel.ChannelHandlerContext;
import ru.geekbrains.common.SendFileRequest;
import ru.geekbrains.common.SendFileResponse;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SendFileRequestHandler {
    private static String SERVER_DIR = "C:\\coding\\cloud-storage\\cloud-storage\\serverDir\\";

    public void channelRead(ChannelHandlerContext ctx, SendFileRequest sendFileRequest) throws Exception {
        System.out.println("file received");
        if (!Files.exists(Paths.get(SERVER_DIR + sendFileRequest.getName()))) {
            Files.write(Paths.get(SERVER_DIR + sendFileRequest.getName()),
                    sendFileRequest.getData(), StandardOpenOption.CREATE);
            boolean operationStatus = Files.exists(Paths.get(SERVER_DIR + sendFileRequest.getName()));
            ctx.writeAndFlush(new SendFileResponse(sendFileRequest.getName(), operationStatus));
        } else {
            ctx.writeAndFlush(new SendFileResponse(sendFileRequest.getName(), false, "Файл с таким именм уже сущестует"));
        }
        System.out.println("file created");
    }
}
