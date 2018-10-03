package com.huawei.sdn.pathselector.persistancy;

import com.huawei.sdn.commons.db.RoutingDataController;
import com.huawei.sdn.pathselector.db.RoutingDataControllerImpl;
import com.huawei.sdn.pathselector.persistancy.mock.RouterWebControllerMock;
import com.huawei.sdn.pathselector.web.RouterWebController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by oWX212574 on 1/5/2015.
 */
@Configuration
@ImportResource("classpath:database-test.xml")
public class TestDbConfiguration {


    @Bean
    public RoutingDataController routingDataController(){
        return new RoutingDataControllerImpl();
    }


    @Bean
    public RouterWebController routerWebController(){
        return new RouterWebControllerMock();
    }

}
