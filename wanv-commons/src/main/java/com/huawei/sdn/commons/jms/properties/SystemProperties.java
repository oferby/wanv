package com.huawei.sdn.commons.jms.properties;

import com.huawei.sdn.commons.enums.HeaderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by root on 5/18/14.
 */
public class SystemProperties {

    private final static int DEFAULT_NUM_OF_THREADS_PER_QUEUE = 2;
    private final static int DEFAULT_STATS_REPORT_PERIOD = 10;

    private static Logger logger = LoggerFactory.getLogger(SystemProperties.class);
    private final List<HeaderType> DEFAULT_PACKET_HEADERS = Arrays.asList(new HeaderType[] { HeaderType.VXLAN,
            HeaderType.Y1731 });

    private Properties properties;
    private String jmsHost;
    private Integer jmsPort;
    private String matchFields;
    private Integer numOfThreadsPerQueue;
    private List<HeaderType> headerTypes;

    public SystemProperties(Properties properties) {
        this.properties = properties;
    }

    public SystemProperties(String propertiesFile) {
        properties = new Properties();
        try {
            final FileReader fileReader = new FileReader(propertiesFile);
            // since Java 7 there is no need to close the file reader,
            // it will be closed automatically
            properties.load(fileReader);
        } catch (final Exception e) {
            logger.error("Failed loading system properties from file " + propertiesFile, e);
        }
    }

    public String getJMSHost() {
        if (jmsHost == null) {
            jmsHost = properties.getProperty(PropertyKeys.JMS_HOST.alias());
        }
        return jmsHost;
    }

    public int getJMSPort() {
        if (jmsPort == null) {
            jmsPort = Integer.decode(properties.getProperty(PropertyKeys.JMS_PORT.alias()));
        }
        return jmsPort;
    }

    public String getMatchFields() {
        if (matchFields == null) {
            matchFields = properties.getProperty(PropertyKeys.MATCH_FIELDS.alias());
        }
        return matchFields;
    }

    public Integer getNumOfThreadsPerQueue() {
        if (numOfThreadsPerQueue == null) {
            numOfThreadsPerQueue = Integer.decode(properties.getProperty(PropertyKeys.NUM_OF_THREADS_PER_QUEUE.alias(),
                    Integer.toString(DEFAULT_NUM_OF_THREADS_PER_QUEUE)));
        }
        return numOfThreadsPerQueue;
    }

    public List<HeaderType> getHeaderTypes() {
        if (headerTypes == null) {
            final String headerTypesStr = properties.getProperty(PropertyKeys.HEADERS_TYPES.alias());
            if (headerTypesStr == null) {
                headerTypes = DEFAULT_PACKET_HEADERS;
                return headerTypes;
            }
            final String[] headers = headerTypesStr.split(",");
            headerTypes = new ArrayList<HeaderType>(headers.length);
            for (final String header : headers) {
                final HeaderType headerType = HeaderType.getByName(header);
                if (headerType == null) {
                    throw new RuntimeException("Header " + header + " does not exist.");
                }
                headerTypes.add(headerType);
            }
        }
        return headerTypes;
    }

    public int getStatsReportPeriod() {
        Integer reportPeriod = Integer.decode(properties.getProperty(PropertyKeys.STATS_REPORT_PERIOD.alias()));
        if (reportPeriod == null) {
            reportPeriod = DEFAULT_STATS_REPORT_PERIOD;
        }
        return reportPeriod;
    }
}
