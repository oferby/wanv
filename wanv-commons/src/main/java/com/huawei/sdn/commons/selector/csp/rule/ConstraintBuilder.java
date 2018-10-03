package com.huawei.sdn.commons.selector.csp.rule;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 9/1/14
 * Time: 8:51 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ConstraintBuilder {

    @Value("${CONF_DIR}")
    private String CONF_DIR;

    private List<RuleSetting> ruleList = new ArrayList<>();

    private StringBuilder getBuilderWithHeader() {

        StringBuilder builder = new StringBuilder();
        builder.append("dialect \"java\"\n")
                .append("import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScoreHolder;\n")
                .append("import com.huawei.sdn.commons.data.PSFlow;\n")
                .append("import com.huawei.sdn.commons.data.PSConnector;\n")
                .append("import com.huawei.sdn.commons.selector.csp.planning.LinkSelectionSolution;\n\n")
                .append("global HardMediumSoftScoreHolder scoreHolder;\n\n")
                .append("// ############################################################################\n")
                .append("// Medium constraints - Generated\n")
                .append("// ############################################################################\n\n");
//                .append("rule \"requiredType\"\n")
//                .append("\twhen\n")
//                .append("\t\t$link : PSConnector($type : type)\n")
//                .append("\t\t$flow : PSFlow(   type != $type, connectorOut == $link)\n")
//                .append("\tthen\n")
//                .append("\t\tscoreHolder.addMediumConstraintMatch(kcontext, -10);\n")
//                .append("end\n");

        return builder;

    }

    public void build(){

        StringBuilder builder = getBuilderWithHeader();

        for (RuleSetting rule : ruleList) {
            builder.append(rule.getStringValue());
        }

        try {

            File file = new File(CONF_DIR + "/MediumScoreRules.drl");
            FileUtils.writeStringToFile(file, builder.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<RuleSetting> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<RuleSetting> ruleList) {
        this.ruleList = ruleList;
    }


}
