package cn.wu.wRpc.codec;

import java.io.IOException;

public interface Codec {

    byte[] encode(Object message) throws IOException;


    Object decode(byte[] buffer) throws IOException;
}
