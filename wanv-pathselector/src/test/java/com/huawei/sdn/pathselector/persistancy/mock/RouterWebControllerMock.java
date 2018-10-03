package com.huawei.sdn.pathselector.persistancy.mock;

import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.selector.routing.message.RouteInfo;
import com.huawei.sdn.pathselector.web.RouterWebController;
import com.huawei.sdn.pathselector.web.RouterWebControllerImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 3/10/2015.
 */

public class RouterWebControllerMock implements RouterWebController {
    @Override
    public RouteInfo getRouteInfo() {
        return null;
    }

    @Override
    public RouteInfo getFullRouteInfo() {
        return null;
    }

    @Override
    public ResponseEntity getLocalIpAddresses() {
        return null;
    }

    @Override
    public ResponseEntity setAndSaveLocalIpAddress(@RequestParam String localIp, @RequestParam String localSubnet) {
        return null;
    }

    @Override
    public ResponseEntity deleteLocalIpAddress(@RequestParam String id) {
        return null;
    }

    @Override
    public ResponseEntity getStaticRoutes() {
        return null;
    }

    @Override
    public ResponseEntity setAndSaveStaticRoute(@RequestParam String destIp, @RequestParam String destSubnet, @RequestParam String nextHop) {
        return null;
    }

    @Override
    public ResponseEntity deleteRoute(@RequestParam String id) {
        return null;
    }

    @Override
    public ResponseEntity getTunnels() {
        return null;
    }

    @Override
    public ResponseEntity setAndSaveTunnel(@RequestParam String tunnelId, @RequestParam String localIp, @RequestParam String remoteIp) {
        return null;
    }

    @Override
    public ResponseEntity deleteTunnel(@RequestParam String id) {
        return null;
    }

    @Override
    public ResponseEntity deleteDefaultGateway(@RequestParam String id) {
        return null;
    }

    @Override
    public ResponseEntity getGateways() {
        return null;
    }

    @Override
    public ResponseEntity setAndSaveDefaultGateway(@RequestParam String ip) {
        return null;
    }

}
