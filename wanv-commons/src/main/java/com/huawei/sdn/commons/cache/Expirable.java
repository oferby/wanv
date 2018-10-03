package com.huawei.sdn.commons.cache;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 8/5/14
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Expirable<T> {

    private T object;
    private long creationTime;

    public Expirable(T object) {
        this.object = object;
        creationTime = System.currentTimeMillis();
    }

    public T getObject() {
        return object;
    }

    public long getCreationTime() {
        return creationTime;
    }

}
