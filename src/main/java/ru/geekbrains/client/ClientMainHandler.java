package ru.geekbrains.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.BlockingQueue;

public class ClientMainHandler extends ChannelInboundHandlerAdapter {
    private final BlockingQueue eventQueue;

    public ClientMainHandler(BlockingQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        eventQueue.put(msg);
    }
}
