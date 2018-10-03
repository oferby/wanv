package com.huawei.sdn.pathselector.web;

import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.selector.routing.message.RouteInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 3/10/2015.
 */
public interface RouterWebController {
    @RequestMapping(value = "info", method = RequestMethod.GET)
    @ResponseBody
    RouteInfo getRouteInfo();

    @RequestMapping(value = "info/full", method = RequestMethod.GET)
    @ResponseBody
    RouteInfo getFullRouteInfo();

    @RequestMapping(value = "set/local/ip", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity getLocalIpAddresses();

    @RequestMapping(value = "set/local/ip", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity setAndSaveLocalIpAddress(@RequestParam String localIp, @RequestParam String localSubnet);

    @RequestMapping(value = "set/local/ip", method = RequestMethod.DELETE)
    @ResponseBody
    ResponseEntity deleteLocalIpAddress(@RequestParam String id);

    @RequestMapping(value = "set/route", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity getStaticRoutes();

    @RequestMapping(value = "set/route", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity setAndSaveStaticRoute(@RequestParam String destIp, @RequestParam String destSubnet, @RequestParam String nextHop);

    @RequestMapping(value = "set/route", method = RequestMethod.DELETE)
    @ResponseBody
    ResponseEntity deleteRoute(@RequestParam String id);

    @RequestMapping(value = "set/tunnel", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity getTunnels();

    @RequestMapping(value = "set/tunnel", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity setAndSaveTunnel(@RequestParam String tunnelId, @RequestParam String localIp, @RequestParam String remoteIp);

    @RequestMapping(value = "set/tunnel", method = RequestMethod.DELETE)
    @ResponseBody
    ResponseEntity deleteTunnel(@RequestParam String id);

    @RequestMapping(value = "set/gateway", method = RequestMethod.DELETE)
    @ResponseBody
    ResponseEntity deleteDefaultGateway(@RequestParam String id);

    @RequestMapping(value = "set/gateway", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity getGateways();

    @RequestMapping(value = "set/gateway", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity setAndSaveDefaultGateway(@RequestParam String ip);

}
