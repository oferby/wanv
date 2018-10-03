package com.huawei.sdn.pathselector.web;

import com.huawei.sdn.commons.cache.Expirable;
import com.huawei.sdn.commons.cache.ExpirableCache;
import com.huawei.sdn.commons.data.MacAddress;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.commons.data.PSConnector;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/6/14
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/setup")
public class SetupWebController {

    @Autowired
    RouteSelector routeSelector;

    @Autowired
    @Qualifier("arpCache")
    public ExpirableCache<String, MacAddress> arpCache;

    @Autowired
    @Qualifier("macNodeCache")
    public ExpirableCache<MacAddress, NodeConnector> macNodeCache;

    @Autowired
    private PathSelectorEngine pathSelectorEngine;

    @RequestMapping("connector")
    @ResponseBody
    public Set<PSConnector> getDefaultGateways() {

        Set<PSConnector> allConnectors = routeSelector.getAllConnectors();

        for (PSConnector connector : allConnectors) {

            if(connector.getType()== ConnectorType.LOCAL){
                connector.setLocalIPAddress(routeSelector.getCidrForLocalAddress(connector.getLocalIPAddress()));
            }

        }

        return allConnectors;

    }

    @RequestMapping("ip2mac")
    @ResponseBody
    public Map<String, Expirable<MacAddress>> getArpCache(){
        return arpCache.getEntries();
    }

    @RequestMapping("mac2port")
    @ResponseBody
    public Map<MacAddress, Expirable<NodeConnector>> getAddressNodeConnectorMap(){
        return macNodeCache.getEntries();
    }

    @RequestMapping(value = "path-selector/enable/{enable}", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void enablePathSelector(@PathVariable boolean enable){
        pathSelectorEngine.enablePathSelector(enable);
    }


}
