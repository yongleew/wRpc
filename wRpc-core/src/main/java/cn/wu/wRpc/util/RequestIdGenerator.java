package cn.wu.wRpc.util;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public class RequestIdGenerator {
    protected static final AtomicLong offset = new AtomicLong(0);

    public static long getRequestId() {
        return offset.getAndIncrement();
    }
}
