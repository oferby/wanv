package com.huawei.sdn.pathselector.odl.y1731.task;

import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.flow.FlowStatusHandler;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.commons.tools.NetUtils;
import com.huawei.sdn.pathselector.odl.ServiceHelper;
import com.huawei.sdn.pathselector.odl.y1731.pdu.dmm.LinkMeasurement;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.reader.NodeConnectorStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ofer Ben-Yacov on 11/5/2014.
 */
public class IngressTrafficCheckTask extends TimerTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngressTrafficCheckTask.class);

    private FlowStatusHandler flowStatusHandler;

    private RouteSelector routeSelector;

    private ServiceHelper serviceHelper;

    private Map<NodeConnector, TrafficRateStatistics> nodeConnectorDescriptiveStatisticsMap;

    private Set<NodeConnector> faultyLinkSet = new HashSet<>();

    private ScheduledThreadPoolExecutor scheduledExecutor;

    private String referenceUrl;

    private int connectionTimeout;

    public IngressTrafficCheckTask(FlowStatusHandler flowStatusHandler,
                                   RouteSelector routeSelector, ServiceHelper serviceHelper,
                                   Map<NodeConnector, TrafficRateStatistics> nodeConnectorDescriptiveStatisticsMap, String referenceUrl, int connectionTimeout, ScheduledThreadPoolExecutor scheduledExecutor) {

        this.routeSelector = routeSelector;
        this.referenceUrl = referenceUrl;
        this.connectionTimeout = connectionTimeout;
        this.flowStatusHandler = flowStatusHandler;
        this.serviceHelper = serviceHelper;
        this.nodeConnectorDescriptiveStatisticsMap = nodeConnectorDescriptiveStatisticsMap;

        this.scheduledExecutor = scheduledExecutor;

    }

    @Override
    public void run() {

        List<NodeConnectorStatistics> nodeConnectorStatistics = serviceHelper.getStatisticsManager().getNodeConnectorStatistics(serviceHelper.getNode());

        for (NodeConnectorStatistics nodeConnectorStatistic : nodeConnectorStatistics) {

            final NodeConnector nodeConnector = nodeConnectorStatistic.getNodeConnector();

            if (faultyLinkSet.contains(nodeConnector)) {
                continue;
            }

            PSConnector connector = routeSelector.getConnector(nodeConnector.getID().toString());
            if (!nodeConnectorDescriptiveStatisticsMap.containsKey(nodeConnector) ||
                    connector == null || connector.getType() != ConnectorType.REMOTE) {
                continue;
            }

            TrafficRateStatistics trafficRateStatistics = nodeConnectorDescriptiveStatisticsMap.get(nodeConnector);

            final long receiveByteCount = nodeConnectorStatistic.getReceiveByteCount();

            double sdScore = trafficRateStatistics.addSampleAndCheckSdScore(receiveByteCount);

            if (sdScore < 0) {

                LOGGER.warn("OAM: Ingress traffic drop detected on link: " + nodeConnector.getID().toString());

                scheduledExecutor.schedule(new Runnable() {
                    @Override
                    public void run() {

                        CloseableHttpClient httpclient = HttpClients.createDefault();

                        InetAddress localIpAddress = NetUtils.parseInetAddress(routeSelector.getConnector(nodeConnector.getID().toString()).getLocalIPAddress());

                            try {

                                RequestConfig requestConfig = RequestConfig.custom()
                                        .setLocalAddress(localIpAddress)
                                        .setConnectTimeout(connectionTimeout)
                                        .build();

                                HttpGet httpget = new HttpGet(referenceUrl);
                                httpget.setConfig(requestConfig);
                                CloseableHttpResponse response = httpclient.execute(httpget);

                                if (response != null && response.getStatusLine().getStatusCode() == 200) {

                                    if(faultyLinkSet.contains(nodeConnector)){
                                        LOGGER.info("OAM: Ingress traffic is back to normal in link: " + nodeConnector.getID());
                                        flowStatusHandler.linkStatusChanged(nodeConnector.getID().toString(), true);
                                        faultyLinkSet.remove(nodeConnector);

                                    } else {

                                        LOGGER.info("OAM: Ingress traffic normal in link: " + nodeConnector.getID());

                                    }

                                    return;
                                }

                                if(!faultyLinkSet.contains(nodeConnector)) {

                                    flowStatusHandler.linkStatusChanged(nodeConnector.getID().toString(), false);
                                    faultyLinkSet.add(nodeConnector);
                                }

                                scheduledExecutor.schedule(this, 2, TimeUnit.SECONDS);

                            } catch (Exception e) {

                                LOGGER.error("OAM: link ID: " + nodeConnector.getID() + ", "+ e.getMessage());

                                if(!faultyLinkSet.contains(nodeConnector)) {
                                    flowStatusHandler.linkStatusChanged(nodeConnector.getID().toString(), false);
                                    faultyLinkSet.add(nodeConnector);
                                }
                                scheduledExecutor.schedule(this, 2, TimeUnit.SECONDS);
                            } finally {
                                try {
                                    httpclient.close();
                                } catch (IOException e) {
                                    LOGGER.error("OAM: " + e.getMessage());
                                }
                            }

                    }
                }, 2, TimeUnit.SECONDS);


            }


        }

        scheduledExecutor.schedule(this,5500,TimeUnit.MILLISECONDS);


    }


}
