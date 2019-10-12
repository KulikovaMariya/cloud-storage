package ru.geekbrains.server;

import io.netty.channel.ChannelHandlerContext;
import ru.geekbrains.common.AuthorizeRequest;
import ru.geekbrains.common.AuthorizeResponse;

public class AuthorizeRequestHandler {
    public void readChannel(ChannelHandlerContext ctx, AuthorizeRequest msg) {
        String username = msg.getUsername();
        if (AuthorizeService.authorize(username, msg.getPassword())) {
            ctx.writeAndFlush(new AuthorizeResponse(true, username));
        } else {
            ctx.writeAndFlush(new AuthorizeResponse(false, username, "Неверный пароль"));
        }
    }
}
