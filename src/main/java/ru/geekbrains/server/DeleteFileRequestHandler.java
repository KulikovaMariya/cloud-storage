package ru.geekbrains.server;

import io.netty.channel.ChannelHandlerContext;
import ru.geekbrains.common.DeleteFileRequest;
import ru.geekbrains.common.DeleteFileResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeleteFileRequestHandler {
    public void channelRead(ChannelHandlerContext ctx, DeleteFileRequest deleteFileRequest) throws IOException {
        Path path = Paths.get(Server.SERVER_DIR + deleteFileRequest.getUsername() + "\\" + deleteFileRequest.getFileName());
        if (Files.exists(path)) {
            try {
                Files.delete(path);
                ctx.writeAndFlush(new DeleteFileResponse(deleteFileRequest.getFileName(), true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ctx.writeAndFlush(new DeleteFileResponse(deleteFileRequest.getFileName(), false, "Файл был удалён или изменён."));
        }
    }
}
