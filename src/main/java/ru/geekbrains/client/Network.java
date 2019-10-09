package ru.geekbrains.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

public class Network {
    private static final int PORT = 8080;
    private final ResponseListener responseListener;

    private Channel currentChannel;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public Network(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.remoteAddress(new InetSocketAddress("localhost", PORT));
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                            new ObjectEncoder(),
                            new ClientMainHandler(responseListener));

                }
            });
            ChannelFuture channelFuture = bootstrap.connect().sync();

            currentChannel = channelFuture.channel();
            countDownLatch.countDown();

            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ChannelFuture sendRequestAsync(Object object) {
        try {
            countDownLatch.await();
            return currentChannel.writeAndFlush(object);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ChannelFuture sendRequestSync(Object object) {
        try {
            return sendRequestAsync(object).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}

