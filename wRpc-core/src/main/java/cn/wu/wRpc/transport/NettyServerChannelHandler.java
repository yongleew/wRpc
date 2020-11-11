package cn.wu.wRpc.transport;

import cn.wu.wRpc.codec.FastJsonSerialization;
import cn.wu.wRpc.codec.Serialization;
import cn.wu.wRpc.rpc.Request;
import cn.wu.wRpc.rpc.Response;
import cn.wu.wRpc.util.CodecUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyServerChannelHandler extends SimpleChannelInboundHandler {

    private MessageHandler messageHandler;
    private Serialization serialization = FastJsonSerialization.SINGLETON;

    public NettyServerChannelHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteMsg = (ByteBuf) msg;
        byte[] data = new byte[byteMsg.readableBytes()];
        byteMsg.readBytes(data);
        Request request = CodecUtil.decodeRequest(data, serialization);
        Object result = messageHandler.handle(request);
        byte[] respData = CodecUtil.encodeResponse((Response) result, serialization);
        ctx.writeAndFlush(Unpooled.copiedBuffer(respData));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }

}
