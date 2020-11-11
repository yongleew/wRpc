package cn.wu.wRpc.rpc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultResponseFuture implements ResponseFuture {

    private ReentrantLock lock = new ReentrantLock();
    private Condition doneWait = lock.newCondition();
    private Object value;
    private Exception exception;
    private Request request;

    private volatile boolean state = false;

    public DefaultResponseFuture(Request request) {
        this.request = request;
    }

    @Override
    public void onSuccess(Response response) {
        this.value = response.getValue();
        done();
    }

    private void done() {
        lock.lock();
        try {
            state = true;
            doneWait.signalAll();
        } finally {
            lock.unlock();
        }
    }


    @Override
    public void onFailure(Response response) {
        this.exception = response.getException();
        done();
    }

    @Override
    public boolean isDone() {
        return state;
    }


    @Override
    public Object getValue() {
        lock.lock();
        try {
            while (!isDone()) {
                try {
                    doneWait.await(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {

                }
            }
            return this.value;
        } finally {
            lock.unlock();
        }

    }

    @Override
    public Exception getException() {
        return this.exception;
    }

    @Override
    public long getRequestId() {
        return this.request.getRequestId();
    }
}
