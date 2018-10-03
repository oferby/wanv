package com.huawei.sdn.commons.topology.manager;

import com.huawei.sdn.commons.config.ConfigurationLoader;
import com.huawei.sdn.commons.config.ConfigurationLoaderImpl;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.commons.selector.routing.RouteSelectorImpl;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 8/10/14
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class TopologyConf {

    @Bean
    public ConfigurationLoader configurationLoader() {
        return new ConfigurationLoaderImpl("D:/dev/conf/topology.xml");
    }

    @Bean
    public RouteSelector routeSelector() {
        return new RouteSelectorImpl();
    }

    @Bean
    PropertyPlaceholderConfigurer propConfig() {
        return new PropertyPlaceholderConfigurer();
    }


}
