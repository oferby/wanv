package com.huawei.sdn.commons.selector.csp.rule;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 9/3/14
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class RuleSetting {

    private String name;
    private WhenSetting[] _whenSettings;
    private Integer score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WhenSetting[] getWhenSettings() {
        return _whenSettings;
    }

    public void setWhenSettings(WhenSetting[] whenSettings) {

        if (whenSettings == null) {
            return;
        }

        this._whenSettings = new WhenSetting[whenSettings.length];

        System.arraycopy(whenSettings, 0, _whenSettings, 0, whenSettings.length);
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

//    @JsonIgnore
    public String getStringValue() {

        StringBuilder builder = new StringBuilder();
        builder.append("rule \"")
                .append(name)
                .append("\"\n")
                .append("\twhen\n");

        for (WhenSetting whenSetting : _whenSettings) {
            builder.append(whenSetting.getStringValue());
        }

        builder.append("\tthen\n\t\tscoreHolder.addMediumConstraintMatch( kcontext, ")
                .append(score)
                .append(" );\nend\n");

        return builder.toString();

    }


}
