package com.huawei.sdn.commons.selector.csp.rule;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 9/1/14
 * Time: 9:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class RuleVariable{
    private String name;
    private List<String> variableList = new ArrayList();

    public void setName(String name) {
        this.name = name;
    }

    public void addVariableName(String name){
        variableList.add(name);
    }

    public String getName() {
        return name;
    }

    public List<String> getVariableList() {
        return variableList;
    }
}
