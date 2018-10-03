package com.huawei.sdn.commons.selector.csp.drools.rule;

import com.huawei.sdn.commons.TestSpring;
import com.huawei.sdn.commons.data.ConnectorStatistics;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.selector.csp.rule.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 9/1/14
 * Time: 10:59 AM
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(classes = {RuleTestContext.class,TestSpring.class})
public class TestRuleBuilder extends TestSpring{

    @Autowired
    private ConstraintBuilder constraintBuilder;


    @Test
    public void testRuleString(){


        Assert.notNull(constraintBuilder);


    }



}
