package com.huawei.sdn.pathselector.web;

import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.data.SiteInformation;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.pathselector.odl.ServiceHelper;
import com.huawei.sdn.pathselector.statistics.FlowStatisticsController;
import com.huawei.sdn.pathselector.statistics.StatisticManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 8/25/14
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/stats")
public class StatisticsWebController {

    @Autowired
    private RouteSelector routeSelector;
    @Autowired
    private ServiceHelper serviceHelper;
    @Autowired
    private PathSelectorEngine pathSelectorEngine;
    @Autowired
    private StatisticManager statisticManager;

    @Autowired
    private FlowStatisticsController flowStatisticsController;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public SiteInformation getSiteInformation() {

        try {
            SiteInformation siteInformation = new SiteInformation();
            siteInformation.setSiteName(serviceHelper.getNode().getNodeIDString());
            siteInformation.setRuleComplianceScore(pathSelectorEngine.getScore());

            List<PSConnector> connectorStatistics = statisticManager.getConnectorStatistics();
            siteInformation.getConnectorList().addAll(connectorStatistics);

            return siteInformation;

        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = "base", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void setBase() {
        Set<PSConnector> allConnectors = routeSelector.getAllConnectors();
        for (PSConnector connector : allConnectors) {
            connector.getConnectorStatistics().setBase();
        }

    }

    @RequestMapping(value = "site", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getRemoteSiteList() {

        return routeSelector.getRemoteSiteIdList();

    }


    @RequestMapping(value = "site/{siteId}/tunnels", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, String>> getSiteTunnelStatistics(@PathVariable String siteId) {

        return statisticManager.getSiteTunnelStatistics(siteId);


    }

    @RequestMapping(value = "site/defaultGateways", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, String>> getSiteDefaultGatewaysStatistics() {

        return statisticManager.getSiteTunnelStatistics("defaultGateways");


    }

    @RequestMapping(value = "analytics/ipPerPort", method = RequestMethod.GET)
    @ResponseBody
    public Map getIpPerPort() {

//        add the title to the graph
        Map<String, Object> rootMap = new HashMap<>();

        rootMap.put("name","Requests Per Port and IP");

        List<Map>childrenPortList = new ArrayList<>();
        rootMap.put("children",childrenPortList);


        Map<String, Map<String, Integer>> flowPerPortAndIp = flowStatisticsController.getNumberOfDestinationPortAndIpRequests();

        Map<String, Object> childrenMap;
        for (String port : flowPerPortAndIp.keySet()) {

            childrenMap = new HashMap<>();
            childrenPortList.add(childrenMap);

            childrenMap.put("name",port);

            List<Map>childrenIpList = new ArrayList<>();
            childrenMap.put("children",childrenIpList);


            Map<String, Integer> ipPerPort = flowPerPortAndIp.get(port);

            Map<String,Object > ipWithSize;
            for (String ip : ipPerPort.keySet()) {

                ipWithSize = new HashMap<>();
                childrenIpList.add(ipWithSize);

                ipWithSize.put("name",ip);
                ipWithSize.put("size", ipPerPort.get(ip));

            }

        }

        return rootMap;

    }


    @RequestMapping(value = "analytics/ipToIpPort", method = RequestMethod.GET)
    @ResponseBody
    public Map getIpToIpAndPort() {

        //        add the title to the graph
        Map<String, Object> rootMap = new HashMap<>();

        rootMap.put("name","Requests from Source IP");
        List<Map>childrenSourceIPList = new ArrayList<>();
        rootMap.put("children",childrenSourceIPList);

        Map<String, Map<String, Set<String>>> ipToIpAndPort = flowStatisticsController.getIpToIpAndPort();

        Map<String,Object>childrenSourceIpMap;
        for (String sourceIp : ipToIpAndPort.keySet()) {
            childrenSourceIpMap = new HashMap<>();
            childrenSourceIPList.add(childrenSourceIpMap);

            childrenSourceIpMap.put("name",sourceIp);

            List<Map>childrenDestIPList = new ArrayList<>();

            childrenSourceIpMap.put("children",childrenDestIPList);

            Map<String,Object>childrenDestIpMap;
            Map<String, Set<String>> destIpAndPortMap = ipToIpAndPort.get(sourceIp);

            for (String destIp : destIpAndPortMap.keySet()) {
                childrenDestIpMap = new HashMap<>();
                childrenDestIPList.add(childrenDestIpMap);
                childrenDestIpMap.put("name",destIp);

                List<Map>childrenDestPortList = new ArrayList<>();
                childrenDestIpMap.put("children",childrenDestPortList);

                Set<String> portList = destIpAndPortMap.get(destIp);
                Map<String,String>portMap;
                for (String port : portList) {
                    portMap = new HashMap<>();
                    childrenDestPortList.add(portMap);
                    portMap.put("name",port);

                }


            }

        }

        return rootMap;
    }



}
