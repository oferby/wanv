package com.huawei.sdn.pathselector.web;

import com.huawei.sdn.commons.config.ConfigurationLoader;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolver;
import com.huawei.sdn.commons.selector.routing.RouteConfigurator;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.commons.selector.routing.message.RouteInfo;
import com.huawei.sdn.commons.tools.NetUtils;
import com.huawei.sdn.commons.db.RoutingDataController;
import com.huawei.sdn.commons.db.model.Gateway;
import com.huawei.sdn.commons.db.model.LocalIpAddress;
import com.huawei.sdn.commons.db.model.StaticRoute;
import com.huawei.sdn.commons.db.model.Tunnel;
import com.huawei.sdn.pathselector.odl.ServiceHelper;
import com.huawei.sdn.pathselector.odl.y1731.OamManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by oWX212574 on 12/23/2014.
 */
@Controller
@RequestMapping("/routing")
public class RouterWebControllerImpl implements RouterWebController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouterWebControllerImpl.class);

    @Autowired
    private RouteSelector routeSelector;

    @Autowired
    private PathSelectorSolver pathSelectorSolver;

    @Autowired
    private RouteConfigurator routeConfigurator;

    @Autowired
    private RoutingDataController routingDataController;

    @Autowired
    private OamManager oamManager;

    @Autowired
    private ServiceHelper serviceHelper;

    @Autowired
    private ConfigurationLoader configurationLoader;


    @Override
    @RequestMapping(value = "info", method = RequestMethod.GET)
    @ResponseBody
    public RouteInfo getRouteInfo() {

        RouteInfo routeInfo = routeSelector.getRouteInfo();

        return routeInfo;
    }

    @Override
    @RequestMapping(value = "info/full", method = RequestMethod.GET)
    @ResponseBody
    public RouteInfo getFullRouteInfo() {

        return routeSelector.getFullRouteInfo();

    }

    @Override
    @RequestMapping(value = "set/local/ip", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getLocalIpAddresses() {

        List<LocalIpAddress> localIpAddressList = routingDataController.getLocalIpAddressList();

        return new ResponseEntity(localIpAddressList, HttpStatus.OK);

    }

    @Override
    @RequestMapping(value = "set/local/ip", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity setAndSaveLocalIpAddress(@RequestParam String localIp, @RequestParam String localSubnet) {

        LOGGER.debug("Got new local IP address to set: " + localIp + "/" + localSubnet);

        configurationLoader.setLocalIpAddress(localIp,localSubnet);

        return new ResponseEntity(HttpStatus.OK);

    }

    @Override
    @RequestMapping(value = "set/local/ip/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity deleteLocalIpAddress(@PathVariable String id) {

        routingDataController.deleteLocalIpAddress(Integer.parseInt(id));

        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "set/route", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getStaticRoutes() {

        List<StaticRoute> staticRouteList = routingDataController.getStaticRouteList();

        return new ResponseEntity(staticRouteList, HttpStatus.OK);


    }

    @Override
    @RequestMapping(value = "set/route", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity setAndSaveStaticRoute(@RequestParam String destIp, @RequestParam String destSubnet, @RequestParam String nextHop) {

        LOGGER.debug("setting new static route: " + destIp + "/" + destSubnet + ", next hop: " + nextHop);

        configurationLoader.setStaticRoute(destIp, destSubnet, nextHop);

        return new ResponseEntity(HttpStatus.OK);

    }

    @Override
    @RequestMapping(value = "set/route/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity deleteRoute(@PathVariable String id) {

        routingDataController.deleteStaticRoute(Integer.parseInt(id));

        return new ResponseEntity(HttpStatus.OK);
    }


    @Override
    @RequestMapping(value = "set/tunnel", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getTunnels() {

        List<Tunnel> tunnelList = routingDataController.getTunnelList();

        return new ResponseEntity(tunnelList, HttpStatus.OK);


    }

    @Override
    @RequestMapping(value = "set/tunnel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity setAndSaveTunnel(@RequestParam String tunnelId, @RequestParam String localIp, @RequestParam String remoteIp) {

        configurationLoader.setTunnel(tunnelId, localIp, remoteIp);

        return new ResponseEntity(HttpStatus.OK);

    }

    @Override
    @RequestMapping(value = "set/tunnel/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity deleteTunnel(@PathVariable String id) {

        routingDataController.deleteTunnel(Integer.parseInt(id));

        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "set/gateway/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity deleteDefaultGateway(@PathVariable String id) {

        routingDataController.deleteDefaultGateway(Integer.parseInt(id));

        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "set/gateway", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getGateways() {

        List<Gateway> gatewayList = routingDataController.getGatewayList();

        return new ResponseEntity(gatewayList, HttpStatus.OK);

    }

    @Override
    @RequestMapping(value = "set/gateway", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity setAndSaveDefaultGateway(@RequestParam String ip) {

        configurationLoader.setDefaultGateway(ip);

        return new ResponseEntity(HttpStatus.OK);

    }

}
