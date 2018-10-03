package com.huawei.sdn.commons.jms.properties;

/**
 * Created by root on 5/18/14.
 */
public enum PropertyKeys {

    JMS_HOST("jmsHost"),
    JMS_PORT("jmsPort"),
    NUM_OF_THREADS_PER_QUEUE("numOfTheadsPerQueue"),
    HEADERS_TYPES("headerTypes"),
    MATCH_FIELDS("matchFileds"),
    STATS_REPORT_PERIOD("statsReportPeriod");

    private String alias;

    private PropertyKeys(String alias) {
        this.alias = alias;
    }

    public String alias() {
        return alias;
    }

}
