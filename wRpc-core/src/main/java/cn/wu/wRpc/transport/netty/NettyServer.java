package cn.wu.wRpc.transport.netty;

import cn.wu.wRpc.rpc.URL;
import cn.wu.wRpc.transport.MessageHandler;
import cn.wu.wRpc.transport.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyServer implements Server {

    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;

    private URL url;
    private MessageHandler messageHandler;
    private boolean isAvailable = false;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private Channel serverChannel;
    private ExecutorService executor;


    public NettyServer(URL url, MessageHandler messageHandler) {
        this.url = url;
        this.messageHandler = messageHandler;
    }

    @Override
    public boolean open() {
        if (isAvailable) {
            return true;
        }
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup();

        executor = Executors.newCachedThreadPool();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        NettyChannelHandler handler = new NettyChannelHandler(executor, messageHandler, NettyServer.this);
                        pipeline.addLast("msgHandler", handler);
                    }
                });

        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(url.getPort()));
        serverChannel = channelFuture.channel();
        isAvailable = true;
        return isAvailable;
    }
}
