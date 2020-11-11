package cn.wu.wRpc.util;

import cn.wu.wRpc.codec.Serialization;
import cn.wu.wRpc.rpc.DefaultResponse;
import cn.wu.wRpc.rpc.Request;
import cn.wu.wRpc.rpc.Response;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.*;

public class CodecUtil {


    public static byte[] encodeRequest(Request request, Serialization serialization){
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(outputStream);) {

            output.writeLong(request.getRequestId());
            output.writeUTF(request.getInterfaceName());
            output.writeUTF(request.getMethodName());
            output.writeUTF(request.getParamtersDesc());

            if (request.getArguments() != null && request.getArguments().length > 0) {
                for (Object argument : request.getArguments()) {
                    serialize(argument, serialization, output);
                }
            }
            output.flush();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("request encode error:");
        }
    }

    public static Request decodeRequest(byte[] data, Serialization serialization){
        try ( ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
              ObjectInputStream input = new ObjectInputStream(inputStream);) {
            long requestId = input.readLong();
            String interfaceName = input.readUTF();
            String methodName = input.readUTF();
            String paramsDesc = input.readUTF();

            Request request = new Request();
            request.setRequestId(requestId);
            request.setInterfaceName(interfaceName);
            request.setMethodName(methodName);
            request.setParamtersDesc(paramsDesc);

            if (paramsDesc.equals("")) {
                return null;
            }
            String[] paramClassNames = paramsDesc.split(",");
            Object[] paramObj = new Object[paramClassNames.length];
            for (int i = 0; i < paramClassNames.length; i++) {
                paramObj[i] = deserialize((byte[]) input.readObject(), Class.forName(paramClassNames[i]), serialization);

            }
            request.setArguments(paramObj);
            return request;
        } catch (Exception e) {
            throw new IllegalStateException("request encode error:");
        }
    }

    private static Object deserialize(byte[] value, Class<?> typeName, Serialization serialization) throws IOException {
        if (value == null) {
            return null;
        }
        return serialization.deserialize(value, typeName);
    }

    private static void serialize(Object msg, Serialization serialization, ObjectOutput output) throws IOException {
        if (msg == null) {
            output.writeObject(null);
        }
        output.writeObject(serialization.serialize(msg));
    }

    public static byte[] encodeResponse(Response response, Serialization serialization) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream output = new ObjectOutputStream(outputStream);) {
            output.writeLong(response.getRequestId());
            if (response.getException() != null) {
                output.writeByte(-1);
                output.writeUTF(response.getException().getClass().getName());
                serialize(response.getException(), serialization, output);
            } else if (response.getValue() == null) {
                output.writeByte(0);
            } else {
                output.writeByte(1);
                output.writeUTF(response.getValue().getClass().getName());
                serialize(response.getValue(), serialization, output);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("response encode error:");
        }
    }

    public static Response decodeResponse(byte[] data, Serialization serialization) {
        DefaultResponse response = new DefaultResponse();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             ObjectInputStream input = new ObjectInputStream(inputStream);) {
            long requestId = input.readLong();
            response.setRequestId(requestId);
            byte flag = input.readByte();
            if (flag == 0) {
                return response;
            }
            String exceptionName = input.readUTF();
            Class<?> clz = Class.forName(exceptionName);
            Object result = deserialize((byte[]) input.readObject(), clz, serialization);
            if (flag == -1) {
                response.setException((Exception) result);
            } else if (flag == 1) {
                response.setValue(result);
            } else {
                throw new IllegalArgumentException("decode error:response dataType not support");
            }
        } catch (Exception e) {
            throw new IllegalStateException("response encode error:");
        }
        return response;
    }
}
