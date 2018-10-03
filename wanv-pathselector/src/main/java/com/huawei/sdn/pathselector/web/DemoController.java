package com.huawei.sdn.pathselector.web;

import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.pathselector.web.pojo.QdiscParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by aWX209432 on 8/28/14.
 * <p/>
 * Controller for creating jmeter flows, changing qdisc parameters
 */

@Controller
@RequestMapping("/demo")
public class DemoController {

    private final static String MOUSE_TYPE = "mouse";
    private final static String ELEPHANT_TYPE = "elephant";
    private final static String SCRIPT_DIR = System.getenv("WANVHOME") + "/bin/";
    private final static String UP_LINK = "simLinkUp.sh";
    private final static String DOWN_LINK = "simLinkDn.sh";
    private final static String NOISE_LINK = "simLinkNoise.sh";
    private final static String MOUSE_SCRIPT = "mouse.jmx";
    private final static String SPACE = " ";
    private static Logger logger = LoggerFactory.getLogger(DemoController.class);
    @Autowired
    private RouteSelector routeSelector;
    @Autowired
    private Demo2WebController demo2WebController;
    @Autowired
    private PathSelectorEngine pathSelectorEngine;
    private ConcurrentHashMap<String, Boolean> delayStateMap = new ConcurrentHashMap<String, Boolean>();

    @RequestMapping(value = "createHttpRequests/{type}", method = RequestMethod.POST)
    @ResponseBody
    public void createHttpRequests(@PathVariable String type) {
        if (MOUSE_TYPE.equals(type)) { // activate mouse

        } else if (ELEPHANT_TYPE.equals(type)) { // activate elephant

        } else {
            throw new IllegalArgumentException("Value can be mouse|elephant");
        }
    }

    @RequestMapping(value = "changeQdiscParams", method = RequestMethod.POST, consumes = {"application/json"}, produces = "application/json")
    @ResponseBody
    public void changeQdiscParams(@RequestBody QdiscParams params) {

        logger.debug("Got request to change link. " + params.toString());

        this.setNoiseLink(params);

    }

    private void setNoiseLink(QdiscParams params) {

        try {

            String ipAddress = getIpByLinkId(params.getLinkId());

            if (ipAddress == null)
                return;

            ProcessBuilder processBuilder = new ProcessBuilder(SCRIPT_DIR + NOISE_LINK
                    , ipAddress
                    , params.getDelay() + ""
                    , params.getJitter() + ""
                    , params.getPacketLossRate() + "");

            Process process = processBuilder.start();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String getIpByLinkId(String id) {


        PSConnector tunnelConnector = routeSelector.getConnector(id);

        PSConnector outConnector = routeSelector.getConnector(tunnelConnector.getLocalIPAddress(), "1.1.1.1", true);

        if (outConnector==null)
            return null;

        return outConnector.getNextHopIpAddress();

    }

}
