package com.huawei.sdn.commons;

import com.huawei.sdn.commons.selector.routing.RouteSelector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/2/14
 * Time: 9:09 AM
 * To change this template use File | Settings | File Templates.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
public abstract class TestSpring {

    private static String CONF_DIR = "D:/dev/conf";

    static {
        System.setProperty("topologyFile", "D:/dev/conf/topology.xml");
        System.setProperty("propertiesFile", "D:/dev/conf/metrics.properties");
        System.setProperty("CONF_DIR", CONF_DIR);
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceHolderConfigurer(){

        return new PropertySourcesPlaceholderConfigurer();
    }


}
