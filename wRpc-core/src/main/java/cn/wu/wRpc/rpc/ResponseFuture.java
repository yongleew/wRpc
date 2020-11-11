package cn.wu.wRpc.rpc;

import java.time.temporal.ValueRange;
import java.util.concurrent.*;

public interface ResponseFuture extends Response {

    void onSuccess(Response response);

    void onFailure(Response response);

    boolean isDone();

}

