package com.huawei.sdn.pathselector.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/1/14
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {TestContext.class})
@ContextConfiguration(locations = "classpath:WEB-INF/pathselector2-servlet.xml")
public class SpringTest {

    static {
        System.setProperty("siteName", "site1");
        System.setProperty("topologyFile", "D:/dev/conf/topology.xml");
        System.setProperty("propertiesFile", "D:/dev/conf/metrics.properties");
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @Autowired
//    private PacketHandler l2PacketHandler;

//    @Autowired
//    CacheManager cacheManager;

//    @Test
    public void testSpring(){
//        Assert.notNull(l2PacketHandler);
    }

//    @Test
//    public void testCacheManager(){
//        Assert.notNull(cacheManager);
//    }


    @Test
    public void testDatabase(){
        Assert.notNull(jdbcTemplate);
    }

}
