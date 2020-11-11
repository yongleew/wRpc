package cn.wu.wRpc.transport;

import cn.wu.wRpc.codec.FastJsonSerialization;
import cn.wu.wRpc.codec.Serialization;
import cn.wu.wRpc.config.AbstractConfig;
import cn.wu.wRpc.rpc.DefaultResponseFuture;
import cn.wu.wRpc.rpc.Request;
import cn.wu.wRpc.rpc.Response;
import cn.wu.wRpc.rpc.ResponseFuture;
import cn.wu.wRpc.util.CodecUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class NettyClient {
    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
    private Bootstrap bootstrap;
    private AbstractConfig<?> config;
    private Channel channel;
    Serialization serialization = FastJsonSerialization.SINGLETON;

    private boolean isAvailable = false;

    private ConcurrentHashMap<Long, ResponseFuture> callbackMap = new ConcurrentHashMap<>();

    public NettyClient(AbstractConfig<?> config) {
        this.config = config;
    }

    public boolean open() {
        if (isAvailable) {
            return true;
        }
        bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

        bootstrap.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        NettyClientChannelHandler channelHandler = new NettyClientChannelHandler(message -> {
                            Response response = (Response) message;
                            ResponseFuture responseFuture = callbackMap.get(response.getRequestId());
                            if (responseFuture == null) {
                                return null;
                            }
                            if (response.getException() != null) {
                                responseFuture.onFailure(response);
                            } else {
                                responseFuture.onSuccess(response);
                            }
                            return null;
                        });
                        pipeline.addLast("handler", channelHandler);
                    }
                });

        ChannelFuture connect = bootstrap.connect(new InetSocketAddress(config.getHost(), config.getPort()));
        connect.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("connect success to " + config.getHost() + ":" + config.getPort());
                }
            }
        });
        connect.syncUninterruptibly();
        channel = connect.channel();
        isAvailable = true;
        return isAvailable;
    }

    public Response request(Request request) {
        DefaultResponseFuture responseFuture = new DefaultResponseFuture(request);
        callbackMap.put(request.getRequestId(), responseFuture);
        byte[] msg = CodecUtil.encodeRequest(request, serialization);
        ChannelFuture writeFuture = channel.writeAndFlush(Unpooled.copiedBuffer(msg));
        writeFuture.awaitUninterruptibly();
        return responseFuture;
    }
}
