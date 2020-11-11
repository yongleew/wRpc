package cn.wu.wRpc.transport;

import cn.wu.wRpc.codec.FastJsonSerialization;
import cn.wu.wRpc.codec.Serialization;
import cn.wu.wRpc.rpc.Response;
import cn.wu.wRpc.util.CodecUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientChannelHandler extends SimpleChannelInboundHandler {

    public MessageHandler messageHandler;
    private Serialization serialization = FastJsonSerialization.SINGLETON;


    public NettyClientChannelHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) {
        ByteBuf byteMsg = (ByteBuf) msg;
        byte[] data = new byte[byteMsg.readableBytes()];
        byteMsg.readBytes(data);
        Response response = CodecUtil.decodeResponse(data, serialization);
        messageHandler.handle(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
