package com.huawei.sdn.pathselector.web;

import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.selector.flow.FlowStatusHandler;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.pathselector.odl.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 9/3/14
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/demo2")
public class Demo2WebController {

    private final static String SCRIPT_DIR = System.getenv("WANVHOME") + "/bin/";
    private final static String UP_LINK = "simLinkUp.sh";
    private final static String DOWN_LINK = "simLinkDn.sh";
    private final static String UP_DOWN_LINK = "simLinkUpDown.sh";
    private static Logger logger = LoggerFactory.getLogger(Demo2WebController.class);
    @Autowired
    private FlowStatusHandler flowStatusHandler;
    @Autowired
    private PathSelectorEngine pathSelectorEngine;
    @Autowired
    private RouteSelector routeSelector;
    private Map<String, String> siteIpAddressMap = new HashMap<>();

    @Autowired
    private ServiceHelper serviceHelper;

    public Demo2WebController() {

        siteIpAddressMap.put("site1", "172.16.150.21");
        siteIpAddressMap.put("site2", "172.16.150.15");

    }

    @RequestMapping(value = "port/{id}/status/{status}/fromOtherSide/{fromOtherSide}", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void setPortStatus(@PathVariable String id, @PathVariable boolean status, @PathVariable boolean fromOtherSide) {

        logger.debug("Got port status changed. Port id: " + id + ", status: " + status);
        flowStatusHandler.linkStatusChanged(id, status);

    }

    @RequestMapping(value = "port/{id}/status/{status}", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void setPortStatus(@PathVariable String id, @PathVariable boolean status) {

        doStatusChange(id, status);

        String siteName = serviceHelper.getNode().getNodeIDString();

        siteName = siteName.equals("site1") ? "site2" : "site1";

        String ipAddress = siteIpAddressMap.get(siteName);

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForEntity(new URI("http://" + ipAddress + ":8080/pathselector/demo2/port/" + id + "/status/" + status + "/fromOtherSide/true"), null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private void doStatusChange(String id, boolean status) {

        logger.debug("Got port status changed. Port id: " + id + ", status: " + status);
        flowStatusHandler.linkStatusChanged(id, status);

        String ipAddress = getIpByLinkId(id);

        if(ipAddress==null)
            return;

        try {
            logger.debug("Sending " + (status ? "up" : "down") + " to port " + ipAddress);
            ProcessBuilder processBuilder = new ProcessBuilder(SCRIPT_DIR + UP_DOWN_LINK, ipAddress, (status ? "up" : "down"));
            Process process = processBuilder.start();

        } catch (IOException e) {
            logger.error("Caught Exception trying to start link " + id, e);
        }


    }

    public String getIpByLinkId(String id) {


        PSConnector tunnelConnector = routeSelector.getConnector(id);

        PSConnector outConnector = routeSelector.getConnector(tunnelConnector.getLocalIPAddress(), "1.1.1.1",true);

        if (outConnector == null)
            return null;

        return outConnector.getNextHopIpAddress();

    }


}
