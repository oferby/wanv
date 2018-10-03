package com.huawei.sdn.commons.selector.csp.rule;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 9/3/14
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class WhenSetting {
    private String entityName;
    private Constraint constraint;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public void setConstraint(Constraint constraint) {
        this.constraint = constraint;
    }

//    @JsonIgnore
    public String getStringValue() {

        StringBuilder builder = new StringBuilder();

        builder.append("\t\t$")
                .append(entityName.toLowerCase())
                .append(" : ")
                .append(entityName);
        if (constraint == null) {
            builder.append("( )");
        } else {
            builder.append(constraint.getStringValue());
        }

        builder.append("\n");

        return builder.toString();


    }


}
