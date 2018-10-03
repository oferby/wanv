package com.huawei.sdn.commons.selector.csp.drools.rule;

import com.huawei.sdn.commons.selector.csp.rule.ConstraintBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 9/1/14
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class RuleTestContext {

    @Bean
    public ConstraintBuilder constraintBuilder(){
        return new ConstraintBuilder();
    }


}
