package cn.wu.wRpc.transport;

import cn.wu.wRpc.config.AbstractConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.FutureListener;

public class NettyServer {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;
    private MessageHandler messageHandler;
    private AbstractConfig<?> config;
    private boolean isOpen;

    public NettyServer(AbstractConfig<?> config, MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        this.config = config;
    }

    public boolean open() {
        if (isOpen) {
            return true;
        }
        if (bossGroup == null) {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
        }

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        NettyServerChannelHandler handler = new NettyServerChannelHandler(messageHandler);
                        pipeline.addLast("handler", handler);
                    }
                })
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture f = serverBootstrap.bind(config.getPort());
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("wRpc is listening port:" + config.getPort());
                } else {
                    System.out.println("wRpc bind failed port:" + config.getPort());
                }
            }
        });
        f.syncUninterruptibly();
        serverChannel = f.channel();
        isOpen = true;
        return isOpen;
    }
}
