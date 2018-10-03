package com.huawei.sdn.pathselector.persistancy;

import com.huawei.sdn.commons.db.EventDbController;
import com.huawei.sdn.commons.db.EventDbControllerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 4/13/2015.
 */
@Configuration
@ImportResource("classpath:WEB-INF/db-beans.xml")
public class EventLogTestConfigurator {


    @Bean
    public EventDbController getEventDbController(){
        return new EventDbControllerImpl();
    }


}
