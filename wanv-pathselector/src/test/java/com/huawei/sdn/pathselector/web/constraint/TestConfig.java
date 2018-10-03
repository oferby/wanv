package com.huawei.sdn.pathselector.web.constraint;

import com.huawei.sdn.commons.selector.csp.PathSelectorSolver;
import com.huawei.sdn.commons.selector.csp.rule.ConstraintBuilder;
import com.huawei.sdn.pathselector.odl.ServiceHelper;
import com.huawei.sdn.pathselector.web.ConstraintWebController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 6/17/2015.
 */
@Configuration
public class TestConfig {


    @Bean
    public ConstraintBuilder constraintBuilder(){
        return new ConstraintBuilder();
    }

    @Bean
    public ConstraintWebController constraintWebController(){
        return new ConstraintWebController();
    }

    @Bean
    public ServiceHelper serviceHelper(){
        return new ServiceHelperMock();
    }

    @Bean
    public PathSelectorSolver pathSelectorSolver(){
        return new PathSelectorSolverMock();
    }

}
