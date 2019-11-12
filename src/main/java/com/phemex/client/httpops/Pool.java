package com.phemex.client.httpops;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Pool<T> {

    final LinkedBlockingQueue<T> objects;

    public Pool(int capacity) {
        objects = new LinkedBlockingQueue<>(capacity);
    }

    public Pool() {
        this(Integer.MAX_VALUE);
    }

    private T borrow() {
        T t = objects.poll();
        if (t == null) {
            t = newObject();
        }
        prepareObject(t);
        return t;
    }

    private void release(T t) {
        objects.offer(t);
    }

    protected abstract T newObject();
    protected void prepareObject(T t) {

    }

    public <R> R borrowAndApply(Function<T, R> f) {
        T t = borrow();
        try {
            return f.apply(t);
        } finally {
            release(t);
        }
    }

    public void borrowAndAccept(Consumer<T> c) {
        T t = borrow();
        try {
            c.accept(t);
        } finally {
            release(t);
        }
    }


}