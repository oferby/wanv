package com.huawei.sdn.commons.selector.csp.rule;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 9/1/14
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */
public enum  Operator {

    OR(" || "),AND(" && "),EQ(" == "),NEQ(" != "),GT(" > "),NGT(" <= "),LT(" < "),NLT(" >= ");
    private String sign;

    private Operator(String sign) {
        this.sign = sign;
    }

    public String getStringSign() {
        return sign;
    }

}
