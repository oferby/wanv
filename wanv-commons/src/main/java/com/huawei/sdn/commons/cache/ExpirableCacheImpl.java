package com.huawei.sdn.commons.cache;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Amir on 6/29/14.
 *
 * This class holds entries in memory for a limited time.
 */
public class ExpirableCacheImpl<K, V> implements ExpirableCache<K, V> {

    private Map<K, Expirable<V>> entries = new ConcurrentHashMap<>();
    private long expirationTime;

    public ExpirableCacheImpl() {
        this.setExpirationTime(300000);
    }

    @Override
    public void addEntry(K key, V value) {
        entries.put(key, new Expirable<V>(value));
    }

    @Override
    public V getEntry(K key) {
        Expirable<V> value = entries.get(key);
        if(value == null) {
            return null;
        }
        if(System.currentTimeMillis() - value.getCreationTime() > expirationTime) {
            entries.remove(key);
            return null;
        }
        return value.getObject();
    }

    @Override
    public void remove(K key) {
        entries.remove(key);
    }

    @Override
    public Set<K> keys() {
        return entries.keySet();
    }

    @Override
    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Override
    public Map<K, Expirable<V>> getEntries() {
        return entries;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}
