package com.huawei.sdn.pathselector.web;

import com.google.common.collect.Lists;
import com.huawei.sdn.commons.selector.csp.rule.RuleSetting;
import com.huawei.sdn.pathselector.web.constraint.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Ofer Ben-Yacov on 9/22/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TestConstraintWebController {

    @Autowired
    private ConstraintWebController constraintWebController;


    @Test
    public void testController() {

        List<RuleSetting> sampleRuleSettings = constraintWebController.getSampleRuleSettings();


        for (RuleSetting sampleRuleSetting : sampleRuleSettings) {
            System.out.println(sampleRuleSetting.getStringValue());

        }


        RuleSetting[] ruleSettings = new RuleSetting[sampleRuleSettings.size()];

        int i = 0;
        for (RuleSetting sampleRuleSetting : sampleRuleSettings) {
            ruleSettings[i++] = sampleRuleSetting;
        }


        constraintWebController.saveRuleSettings(ruleSettings);


    }


}
