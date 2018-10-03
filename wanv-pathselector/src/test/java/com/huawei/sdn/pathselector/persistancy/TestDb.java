package com.huawei.sdn.pathselector.persistancy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by oWX212574 on 1/5/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {TestDbConfiguration.class})
@ContextConfiguration(locations = "classpath:database-test.xml")
public class TestDb {


    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    public void testDb(){

        assert jdbcTemplate!=null;

        try {
            jdbcTemplate.execute("create table local_ip_address ( ip_address varchar(15), subnet VARCHAR(2))");
            jdbcTemplate.execute("create table gateway_ip_address ( ip_address varchar(15))");
            jdbcTemplate.execute("create table tunnel_info ( tunnelId varchar(2), localIp VARCHAR(15), remoteIp VARCHAR(15))");
            jdbcTemplate.execute("create table static_route_info ( destIp varchar(15), destSubnet VARCHAR(2), nextHope VARCHAR(15))");

        } catch (DataAccessException e){
//            table exists
        }

        jdbcTemplate.execute("delete from local_ip_address");

        Object[] params = {"1.1.1.1" , "24"};

        jdbcTemplate.update("insert into local_ip_address values (?,?)",params);

        Object[] params2 = {"2.2.2.2" , "16"};

        jdbcTemplate.update("insert into local_ip_address values (?,?)",params2);

        int i = jdbcTemplate.queryForInt("select count(*) from local_ip_address");

        assert i==2;

        List<Map<String, Object>> ipAddressList = jdbcTemplate.queryForList("select * from local_ip_address");

        assert  ipAddressList!=null;

    }



}
