package ru.geekbrains.client;

import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.geekbrains.common.SendFileResponse;

public class SendFileResponseHandler extends ChannelInboundHandlerAdapter {
    private final ResponseListener responseListener;

    public SendFileResponseHandler(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void channelRead(SendFileResponse msg) throws Exception {
            responseListener.onSendFile(msg.getSendFileName(), msg.isStatus(), msg.getStatusDescription());
    }
}
