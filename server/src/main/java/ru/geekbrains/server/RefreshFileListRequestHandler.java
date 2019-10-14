package ru.geekbrains.server;

import io.netty.channel.ChannelHandlerContext;
import ru.geekbrains.common.RefreshFileListRequest;
import ru.geekbrains.common.RefreshFileListResponse;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class RefreshFileListRequestHandler {

    public void readChannel(ChannelHandlerContext ctx, RefreshFileListRequest msg) throws IOException {
        final RefreshFileListResponse refreshFileListResponse = new RefreshFileListResponse();
        Path path = Paths.get(Server.SERVER_DIR + msg.getUsername());
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Files.walkFileTree(path, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                refreshFileListResponse.addToFileList(file.getFileName().toString());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        ctx.writeAndFlush(refreshFileListResponse);
    }
}
