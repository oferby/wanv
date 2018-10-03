package com.huawei.sdn.commons.selector.routing.selector;

import com.huawei.sdn.commons.config.system.OsNetworkHelper;
import com.huawei.sdn.commons.config.topology.TopologyServiceHelper;
import com.huawei.sdn.commons.db.EventDbController;
import com.huawei.sdn.commons.db.RoutingDataController;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolver;
import com.huawei.sdn.commons.selector.routing.RouteConfigurator;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.commons.selector.routing.RouteSelectorImpl;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.backportconcurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

/**
 * Created by oWX212574 on 12/22/2014.
 */
@Configuration
public class RouteSelectorConfig {

    private RouteSelectorImpl routeSelector = new RouteSelectorImpl();

    @Bean
    public RouteSelector routeSelector(){
        return routeSelector;
    }

    @Bean
    public RouteConfigurator routeConfigurator(){
        return routeSelector;
    }

    @Bean
    public TaskExecutor taskExecutor(){
        return new ConcurrentTaskExecutor();
    }

    @Bean
    public EventDbController eventDbController(){
        return new EventDbControllerMock();
    }


    @Bean
    public TaskScheduler taskScheduler(){

        return new ConcurrentTaskScheduler();

    }

    @Bean
    public PathSelectorSolver pathSelectorSolver(){
        return new PathSelectorSolverMock();
    }

    @Bean
    public TopologyServiceHelper topologyServiceHelper(){
        return new TopologyServiceHelperMock();
    }

    @Bean
    public OsNetworkHelper osNetworkHelper(){
        return new OsNetworkHelperMock();
    }

    @Bean
    public RoutingDataController routingDataController(){
        return new RoutingDataControllerMock();
    }

}
