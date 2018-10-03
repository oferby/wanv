package com.huawei.sdn.commons.cache;

import java.util.Map;
import java.util.Set;

/**
 * Created by root on 6/29/14.
 */
public interface ExpirableCache<K, V> {

    void addEntry(K key, V value);
    V getEntry(K key);
    void remove(K key);
    Set<K> keys();
    void setExpirationTime(long expirationTime);
    Map<K, Expirable<V>>getEntries();
}
