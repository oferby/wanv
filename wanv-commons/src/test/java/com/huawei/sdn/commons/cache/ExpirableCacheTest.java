package com.huawei.sdn.commons.cache;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by root on 6/29/14.
 */
public class ExpirableCacheTest {

    @Test
    public void testCache() throws InterruptedException {
        ExpirableCacheImpl<String, String> cache = new ExpirableCacheImpl<>();
        cache.setExpirationTime(1000);
        cache.addEntry("k1", "v1");
        String val = cache.getEntry("k1");
        assertNotNull(val);
        assertEquals("v1", val);
        cache.addEntry("k2", "v2");
        Thread.sleep(2000);
        String val2 = cache.getEntry("k2");
        assertNull(val2);
    }
}
