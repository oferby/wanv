package com.huawei.sdn.pathselector.db;

import com.huawei.sdn.commons.db.RoutingDataController;
import com.huawei.sdn.commons.db.model.Gateway;
import com.huawei.sdn.commons.db.model.LocalIpAddress;
import com.huawei.sdn.commons.db.model.StaticRoute;
import com.huawei.sdn.commons.db.model.Tunnel;
import com.huawei.sdn.pathselector.web.RouterWebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by oWX212574 on 1/5/2015.
 */
@Component
public class RoutingDataControllerImpl implements RoutingDataController {

    private Logger logger = LoggerFactory.getLogger(RoutingDataControllerImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RouterWebController routerWebController;


    public void setup() {

        logger.debug("building network tables");

        try {

//            jdbcTemplate.execute("create table local_ip_address ( id IDENTITY, ip_address varchar(15), subnet VARCHAR(2))");
            jdbcTemplate.execute("create table gateway_ip_address ( id IDENTITY, ip_address varchar(15))");
            jdbcTemplate.execute("create table tunnel_info ( id IDENTITY, localIp VARCHAR(15), remoteIp VARCHAR(15))");
            jdbcTemplate.execute("create table static_route_info ( id IDENTITY, destIp varchar(15), destSubnet VARCHAR(2), nextHop VARCHAR(15))");


        } catch (DataAccessException e) {
//            table exists
            logger.debug("tables exists");

        }

    }

    @Override
    public List<LocalIpAddress> getLocalIpAddressList() {

        List<LocalIpAddress> localIpAddresses = jdbcTemplate.query("select * from local_ip_address", new BeanPropertyRowMapper(LocalIpAddress.class));

        return localIpAddresses;
    }

    @Override
    public void saveLocalIpAddress(String ipAddress, String subnet) {

        logger.debug("adding local ip address to the db");
        Object[] params = {ipAddress, subnet};
        jdbcTemplate.update("insert into local_ip_address values(NULL,?,?)", params);

    }

    @Override
    public void deleteLocalIpAddress(int id) {
        jdbcTemplate.update("delete from local_ip_address where id=?", id);
    }

    @Override
    public List<Gateway> getGatewayList() {

        List<Gateway> gateways = jdbcTemplate.query("select * from gateway_ip_address", new BeanPropertyRowMapper(Gateway.class));

        return gateways;
    }

    @Override
    public void saveDefaultGateway(String ipAddress) {
        logger.debug("adding default gateway to the db");
        Object[] params = {ipAddress};
        jdbcTemplate.update("insert into gateway_ip_address values(NULL,?)", params);

    }

    @Override
    public void deleteDefaultGateway(int id) {
        jdbcTemplate.update("delete from gateway_ip_address where id=?",id);
    }

    @Override
    public List<Tunnel> getTunnelList() {

        List<Tunnel> tunnels = jdbcTemplate.query("select * from tunnel_info", new BeanPropertyRowMapper(Tunnel.class));

        return tunnels;
    }


    @Override
    public void saveTunnel(String tunnelId, String localIp, String remoteIp) {
        logger.debug("adding tunel info to the db");
        Object[] params = {tunnelId, localIp, remoteIp};
        jdbcTemplate.update("insert into tunnel_info values(?,?,?)", params);

    }

    @Override
    public void deleteTunnel(int id) {
        jdbcTemplate.update("delete from tunnel_info where id=?",id);
    }

    @Override
    public List<StaticRoute> getStaticRouteList() {

        List<StaticRoute> staticRoutes = jdbcTemplate.query("select* from static_route_info", new BeanPropertyRowMapper(StaticRoute.class));

        return staticRoutes;
    }

    @Override
    public void saveStaticRoute(String destIp, String destSubnet, String nextHop) {

        logger.debug("adding static route info to the db");
        Object[] params = {destIp, destSubnet, nextHop};
        jdbcTemplate.update("insert into static_route_info values(NULL,?,?,?)", params);

    }

    @Override
    public void deleteStaticRoute(int id) {
        jdbcTemplate.update("delete from static_route_info where id=?",id);
    }
}
