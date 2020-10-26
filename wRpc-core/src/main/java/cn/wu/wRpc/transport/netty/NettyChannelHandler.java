package cn.wu.wRpc.transport.netty;

import cn.wu.wRpc.transport.Channel;
import cn.wu.wRpc.transport.MessageHandler;
import cn.wu.wRpc.transport.Server;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;

public class NettyChannelHandler extends ChannelDuplexHandler {

    private ExecutorService executorService;
    private MessageHandler messageHandler;
    private Server channel;

    public NettyChannelHandler(ExecutorService executorService, MessageHandler messageHandler, Server channel) {
        this.executorService = executorService;
        this.messageHandler = messageHandler;
        this.channel = channel;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (executorService != null) {
            executorService.execute(() -> processMessage(ctx, msg));
        } else {
            processMessage(ctx, msg);
        }
    }

    private void processMessage(ChannelHandlerContext ctx, Object msg) {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
