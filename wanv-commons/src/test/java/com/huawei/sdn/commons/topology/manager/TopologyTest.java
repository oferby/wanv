package com.huawei.sdn.commons.topology.manager;

import com.huawei.sdn.commons.TestSpring;
import com.huawei.sdn.commons.config.ConfigurationLoader;
import com.huawei.sdn.commons.config.ConfigurationLoaderImpl;
import com.huawei.sdn.commons.config.topology.Queue;
import com.huawei.sdn.commons.config.topology.Topology;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 8/10/14
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TopologyConf.class})
public class TopologyTest extends TestSpring {

    @Autowired
    private ConfigurationLoader configurationLoader;

    @Autowired
    private RouteSelector routeSelector;

    //    @Test
    public void testConfigurationFile() {

        Assert.notNull(configurationLoader);
        Assert.notNull(routeSelector);

        PSConnector connector = routeSelector.getConnector("1.1.1.1", "10.1.50.1", true);

        Assert.notNull(connector);


    }


    @Test
    public void testMode() {

        ConfigurationLoader loader = new ConfigurationLoaderImpl("D:/dev/conf/topology.xml");

        Topology topology = loader.getTopology();

        for (Queue queue : topology.getQueues()) {

            assert queue.getMaxSec() > -1;
        }


        Queue defaultQueue = loader.getDefaultQueue();

        assert defaultQueue != null;

        assert loader.getTimeout() == 2000;

    }


}
