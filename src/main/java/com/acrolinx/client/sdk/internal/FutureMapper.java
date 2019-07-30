package com.acrolinx.client.sdk.internal;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class FutureMapper<S, T> implements Future<T> {
    private Future<S> wrappedFuture;

    public FutureMapper(Future<S> wrappedFuture) {
        this.wrappedFuture = wrappedFuture;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return wrappedFuture.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return wrappedFuture.isCancelled();
    }

    @Override
    public boolean isDone() {
        return wrappedFuture.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return map(wrappedFuture.get());
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return map(wrappedFuture.get(timeout, unit));
    }

    protected abstract T map(S wrappedFutureResult);
}
