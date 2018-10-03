package com.huawei.sdn.pathselector.web;

import com.huawei.sdn.commons.selector.csp.PathSelectorSolver;
import com.huawei.sdn.commons.selector.csp.rule.Constraint;
import com.huawei.sdn.commons.selector.csp.rule.ConstraintBuilder;
import com.huawei.sdn.commons.selector.csp.rule.RuleSetting;
import com.huawei.sdn.commons.selector.csp.rule.WhenSetting;
import com.huawei.sdn.pathselector.odl.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 9/3/14
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/constraint/data")
public class ConstraintWebController {

    private Logger logger = LoggerFactory.getLogger(ConstraintWebController.class);

    @Autowired
    private ConstraintBuilder constraintBuilder;

    @Autowired
    private PathSelectorSolver pathSelectorSolver;

    @Autowired
    private ServiceHelper serviceHelper;

    @RequestMapping(value = "/rule", method = RequestMethod.GET)
    @ResponseBody
    public List<RuleSetting> getAllRuleSettings() {
        return constraintBuilder.getRuleList();
    }

    @RequestMapping(value = "/rule", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveRuleSettings(@RequestBody RuleSetting[] ruleSettings) {

        constraintBuilder.setRuleList(Arrays.asList(ruleSettings));
        constraintBuilder.build();

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/restart", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseEntity restartSolver() {

        logger.debug("Got request to restart solver");

        pathSelectorSolver.restart();

        serviceHelper.getProgrammer().removeAllFlows(serviceHelper.getNode());

        return new ResponseEntity(HttpStatus.OK);

    }


    @RequestMapping(value = "/rule/sample", method = RequestMethod.GET)
    @ResponseBody
    public List<RuleSetting> getSampleRuleSettings() {

        List<RuleSetting> ruleSettings = new ArrayList<>();


        RuleSetting ruleSetting = new RuleSetting();
        ruleSetting.setName("rule1");
        ruleSetting.setScore(-1);

        WhenSetting[] whenSettings = new WhenSetting[2];


        WhenSetting whenSetting = new WhenSetting();
        whenSetting.setEntityName("PSConnector");

        Constraint constraint = new Constraint();
        constraint.setVariable("connectorStatistics.packetLossRate");
        constraint.setOperator("GT");
        constraint.setValue("1");

        whenSetting.setConstraint(constraint);
        whenSettings[0] = whenSetting;


        whenSetting = new WhenSetting();
        whenSetting.setEntityName("PSFlow");

        constraint = new Constraint();
        constraint.setVariable("connectorOut");
        constraint.setOperator("EQ");
        constraint.setValue("$psconnector");

        whenSetting.setConstraint(constraint);
        whenSettings[1] = whenSetting;

        ruleSetting.setWhenSettings(whenSettings);
        ruleSettings.add(ruleSetting);


        ruleSetting = new RuleSetting();
        ruleSetting.setName("rule2");
        ruleSetting.setScore(-10);

        whenSettings = new WhenSetting[2];

        whenSetting = new WhenSetting();
        whenSetting.setEntityName("PSConnector");

        constraint = new Constraint();
        constraint.setVariable("connectorStatistics.frameDelayVariation");
        constraint.setOperator("GT");
        constraint.setValue("30");

        whenSetting.setConstraint(constraint);
        whenSettings[0] = whenSetting;


        whenSetting = new WhenSetting();
        whenSetting.setEntityName("PSFlow");

        constraint = new Constraint();

        Constraint leftSide = new Constraint();
        leftSide.setVariable("connectorOut");
        leftSide.setOperator("EQ");
        leftSide.setValue("$psconnector");

        constraint.setLeftSide(leftSide);
        constraint.setOperator("AND");

        Constraint upperRightSide = new Constraint();
        constraint.setRightSide(upperRightSide);

        leftSide = new Constraint();
        leftSide.setVariable("nwTOS");
        leftSide.setOperator("EQ");
        leftSide.setValue("40");

        Constraint rightSide = new Constraint();
        rightSide.setVariable("nwTOS");
        rightSide.setOperator("EQ");
        rightSide.setValue("48");

        upperRightSide.setLeftSide(leftSide);
        upperRightSide.setOperator("OR");
        upperRightSide.setRightSide(rightSide);


        whenSetting.setConstraint(constraint);
        whenSettings[1] = whenSetting;


        ruleSetting.setWhenSettings(whenSettings);
        ruleSettings.add(ruleSetting);


        return ruleSettings;
    }


}
