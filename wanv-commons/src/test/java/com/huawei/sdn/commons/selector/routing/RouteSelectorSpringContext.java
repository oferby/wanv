package com.huawei.sdn.commons.selector.routing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by root on 6/29/14.
 */

@Configuration
public class RouteSelectorSpringContext {

    @Bean
    public RouteSelector getRouteSelector() {
        return new RouteSelectorImpl();
    }
}
