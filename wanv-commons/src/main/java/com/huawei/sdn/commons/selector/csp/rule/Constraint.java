package com.huawei.sdn.commons.selector.csp.rule;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 9/1/14
 * Time: 8:51 AM
 * To change this template use File | Settings | File Templates.
 */
@JsonInclude(Include.NON_NULL)
public class Constraint {

    private String variable;
    private Constraint leftSide;
    private Operator operator;
    private Constraint rightSide;
    private String value;

    public Constraint() {
    }


    public Constraint(String variable, Operator operator, String value) {
        this.variable = variable;
        this.operator = operator;
        this.value = value;
    }

    public Constraint(Constraint leftSide, Operator operator, Constraint rightSide) {
        this.leftSide = leftSide;
        this.operator = operator;
        this.rightSide = rightSide;
    }

    public String getVariable() {
        return variable;
    }

    public Constraint getLeftSide() {
        return leftSide;
    }

    public Operator getOperator() {
        return operator;
    }

    public Constraint getRightSide() {
        return rightSide;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public void setOperator(String operator) {
        this.operator = Operator.valueOf(operator);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setLeftSide(Constraint leftSide) {
        this.leftSide = leftSide;
    }

    public void setRightSide(Constraint rightSide) {
        this.rightSide = rightSide;
    }

    @JsonIgnore
    public String getStringValue() {
        StringBuilder builder = new StringBuilder();
        if (variable != null) {

            builder.append("( ")
                    .append(variable)
                    .append(operator.getStringSign())
                    .append(value)
                    .append(" )");

        } else {

            builder.append("( ")
                    .append(leftSide.getStringValue())
                    .append(operator.getStringSign())
                    .append(rightSide.getStringValue())
                    .append(" )");
        }

        return builder.toString();

    }
}
