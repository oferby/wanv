package com.huawei.sdn.commons.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 8/25/14
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class SiteInformation {

    private String siteName;
    private Integer ruleComplianceScore;

    private List<PSConnector>connectorList=new ArrayList<>();

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Integer getRuleComplianceScore() {
        return ruleComplianceScore;
    }

    public void setRuleComplianceScore(Integer ruleComplianceScore) {
        this.ruleComplianceScore = ruleComplianceScore;
    }

    public List<PSConnector> getConnectorList() {
        return connectorList;
    }

    public void setConnectorList(List<PSConnector> connectorList) {
        this.connectorList = connectorList;
    }
}
