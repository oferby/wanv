package com.huawei.sdn.commons.selector.csp.optaPlanner;

import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolver;
import com.huawei.sdn.commons.selector.csp.optaPlanner.config.PathSelectorTestConfig;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 1/25/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PathSelectorTestConfig.class})
public class TestGrePathSelection {


    @Autowired
    private PathSelectorSolver pathSelectorSolver;

    @Autowired
    private RouteSelector routeSelector;


    private Map<String,List<PSConnector>>siteToConnectorMap=new HashMap<>();

    @Test
    public void testGreSelection() throws InterruptedException, UnknownHostException {

        assert pathSelectorSolver!=null;

        this.addConnectors();

        Thread.sleep(5000);


        PSFlow flow;
        for (int i = 1; i < 10; i++) {

            flow = this.getPsFlow(null, "1.1.1." + i);

            pathSelectorSolver.addFlow(flow);

            Thread.sleep(1000);


        }

        Thread.sleep(1000000);

        Set<PSFlow> allKnownFlows = pathSelectorSolver.getAllKnownFlows();

        assert allKnownFlows.size()==1;




    }


    private void addConnectors(){

        List<PSConnector>psConnectorList = new ArrayList<>();

        PSConnector connector = new PSConnector("1", ConnectorType.GRE);
        connector.setGroupId("g1");
        psConnectorList.add(connector);
        pathSelectorSolver.addPSConnector(connector);

        connector = new PSConnector("2", ConnectorType.GRE);
        connector.setGroupId("g1");
        psConnectorList.add(connector);
        pathSelectorSolver.addPSConnector(connector);

        siteToConnectorMap.put("g1",psConnectorList);

        psConnectorList = new ArrayList<>();

        connector = new PSConnector("3", ConnectorType.GRE);
        connector.setGroupId("g2");
        psConnectorList.add(connector);
        pathSelectorSolver.addPSConnector(connector);

        connector = new PSConnector("4", ConnectorType.GRE);
        connector.setGroupId("g2");
        psConnectorList.add(connector);
        pathSelectorSolver.addPSConnector(connector);

        siteToConnectorMap.put("g2",psConnectorList);

        connector = new PSConnector("5", ConnectorType.REMOTE);
        pathSelectorSolver.addPSConnector(connector);

        connector = new PSConnector("6", ConnectorType.REMOTE);
        pathSelectorSolver.addPSConnector(connector);

        connector = new PSConnector("7", ConnectorType.LOCAL);
        pathSelectorSolver.addPSConnector(connector);

    }


    private PSFlow getPsFlow(PSConnector outConnector, String srcIp) throws UnknownHostException {

        PSFlow flow = new PSFlow();
        flow.setDstIp(InetAddress.getByName(srcIp));
        flow.setConnectorOut(outConnector);

        flow.setType(ConnectorType.GRE);
        flow.setGroupId("g1");

        return flow;

    }



}
