package com.huawei.sdn.commons.selector.csp.drools;

import com.huawei.sdn.commons.context.WanApplicationContextProvider;
import com.huawei.sdn.commons.data.ConnectorStatistics;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolver;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolverImpl;
import com.huawei.sdn.commons.selector.csp.drools.config.DroolsAppConfig;
import com.huawei.sdn.commons.selector.flow.FlowProgrammer;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 8/6/14
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DroolsAppConfig.class})
public class OptaPlannerTest {

    public static final AtomicLong flowIdGenerator = new AtomicLong(10000);

    private static String CONF_DIR = "D:/dev/conf";

    static {
        System.setProperty("siteName", "site1");
        System.setProperty("topologyFile", "D:/dev/conf/topology.xml");
        System.setProperty("CONF_DIR", CONF_DIR);
    }

    private static Logger logger = LoggerFactory.getLogger(OptaPlannerTest.class);
    Random r = new Random();
    List<PSFlow> psFlowList = new ArrayList<>();
    //    PSPacketIn packetIn = new PSPacketIn(null);
    private Map<String, Integer> integerMap = new HashMap<>();
    @Autowired
    private PathSelectorSolver pathSelectorSolver;
    @Autowired
    private RouteSelector routeSelector;
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FlowProgrammer flowProgrammerMock;

    //    @Test
    public void testInit() {

        Assert.notNull(applicationContext);

    }

    @Test
    public void testQueueRules() {

        assert pathSelectorSolver != null;


    }


    @Test
    public void testDropConnector() {

        WanApplicationContextProvider contextProvider = new WanApplicationContextProvider();
        contextProvider.setApplicationContext(applicationContext);

        Assert.notNull(pathSelectorSolver);


        PSFlow flow = PSFlow.getNewFlow();
        flow.setNwTOS(Byte.decode("46"));
        flow.setId(flowIdGenerator.getAndIncrement());
        flow.setType(ConnectorType.REMOTE);

        PSConnector connector = routeSelector.getConnector("", "REMOTE", true);
        flow.setConnectorOut(connector);

        pathSelectorSolver.addFlow(flow);


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ConnectorStatistics connectorStatistics = new ConnectorStatistics();
        connectorStatistics.setFrameDelay(400);

        Map<String, ConnectorStatistics> statisticsMap = new HashMap<>();

//        for (int i = 0; i < connectorList.size() - 1; i++) {
//            statisticsMap.put(connectorList.get(i).getId(), connectorStatistics);
//        }

//        for (PSConnector psConnector : connectorList) {
//            statisticsMap.put(psConnector.getId(),connectorStatistics);
//        }

        System.out.println("*******  1 link good *********** ");

        pathSelectorSolver.connectorMetrics(statisticsMap);


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        for (PSConnector psConnector : connectorList) {
//            statisticsMap.put(psConnector.getId(), connectorStatistics);
//        }

        System.out.println("*******  all links bad *********** ");

        pathSelectorSolver.connectorMetrics(statisticsMap);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testDefaultGatewayFlow() throws UnknownHostException {

        WanApplicationContextProvider contextProvider = new WanApplicationContextProvider();
        contextProvider.setApplicationContext(applicationContext);

        Set<PSConnector> allConnectors = routeSelector.getAllConnectors();
        for (PSConnector connector : allConnectors) {

            if(connector.getType()==ConnectorType.REMOTE || connector.getType()==ConnectorType.GRE) {
                pathSelectorSolver.addPSConnector(connector);
            }

        }

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PSFlow flow = PSFlow.getNewFlow();
        flow.setId(flowIdGenerator.getAndIncrement());
        flow.setSrcIp(InetAddress.getByName("1.1.1.1"));
        flow.setType(ConnectorType.REMOTE);


        pathSelectorSolver.addFlow(flow);

        psFlowList.add(flow);

        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        flow = PSFlow.getNewFlow();
        flow.setId(flowIdGenerator.getAndIncrement());
        flow.setSrcIp(InetAddress.getByName("2.2.2.2"));
        flow.setType(ConnectorType.REMOTE);

        pathSelectorSolver.addFlow(flow);

        psFlowList.add(flow);

        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        assert !psFlowList.isEmpty();


    }



    @Test
    public void testSolution() {

        WanApplicationContextProvider contextProvider = new WanApplicationContextProvider();
        contextProvider.setApplicationContext(applicationContext);

        Set<PSConnector> allConnectors = routeSelector.getAllConnectors();
        for (PSConnector connector : allConnectors) {

            if(connector.getType()==ConnectorType.REMOTE || connector.getType()==ConnectorType.GRE) {
                pathSelectorSolver.addPSConnector(connector);
            }

        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PSFlow tempFlow;


//        packetIn.setNwTOS(new Byte("1"));

        PSFlow flow = PSFlow.getNewFlow();
        flow.setId(flowIdGenerator.getAndIncrement());
//        flow.setPSPacket(packetIn);
        flow.setType(ConnectorType.REMOTE);

        pathSelectorSolver.addFlow(flow);
        psFlowList.add(flow);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        flow = PSFlow.getNewFlow();
        flow.setId(flowIdGenerator.getAndIncrement());
//        flow.setPSPacket(packetIn);
        flow.setType(ConnectorType.REMOTE);

        pathSelectorSolver.addFlow(flow);
        psFlowList.add(flow);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        checkOutConnector();


        integerMap.put("0", 0);
        integerMap.put("1", 0);
        integerMap.put("2", 0);


        addFlows();

        int score = pathSelectorSolver.getScore();


        checkOutConnector();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        checkOutConnector();


//        for (PSFlow psFlow : psFlowList) {
//            pathSelectorSolver.removeFlow(psFlow);
//        }


//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        logger.debug("********** wait after remove ***********");
//        addFlows();

        addPacketLoss();


        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


//            checkOutConnector();

        }
    }

    private void addFlows() {

        logger.debug("adding flows");

        psFlowList.clear();

        for (int i = 0; i < 10; i++) {

            int type = r.nextInt(3);

            PSFlow flow = PSFlow.getNewFlow();
            flow.setId(flowIdGenerator.getAndIncrement());
            ConnectorType connectorType = ConnectorType.values()[type];
            flow.setType(connectorType);
//            flow.setPSPacket(packetIn);

            flow.setTpDst((short) (i + 100));

            PSConnector connector = routeSelector.getConnector("", connectorType.value(), true);
            flow.setConnectorOut(connector);

            pathSelectorSolver.addFlow(flow);
            psFlowList.add(flow);
            integerMap.put(type + "", integerMap.get(type + "") + 1);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    private void checkOutConnector() {

        int notNullConnectors = 0;


        Set<PSConnector> allConnectors = routeSelector.getAllConnectors();
        Map<String, Integer> connectorUsageMap = new HashMap<>();
        Map<String, PSConnector> connectorMap = new HashMap<>();

        for (PSConnector connector : allConnectors) {
            connectorUsageMap.put(connector.getId(), 0);
            connectorMap.put(connector.getId(), connector);
        }


        for (Iterator<PSFlow> iterator = ((PathSelectorSolverImpl) pathSelectorSolver).getPsFlowBestSolutionMap().values().iterator(); iterator.hasNext(); ) {

            PSFlow psFlow = iterator.next();

            if (psFlow.getConnectorOut() != null) {
                Assert.isTrue(psFlow.getType().equals(psFlow.getConnectorOut().getType()));
                notNullConnectors++;
                connectorUsageMap.put(psFlow.getConnectorOut().getId(), connectorUsageMap.get(psFlow.getConnectorOut().getId()) + 1);
            }

        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String key : connectorUsageMap.keySet()) {

            int type = connectorMap.get(key).getType().ordinal();

            stringBuilder.append("\nConnector:").append(key)
                    .append(", type: ").append(connectorMap.get(key).getType().toString())
                    .append(", flows: ").append(connectorUsageMap.get(key))
                    .append(", of: ").append(integerMap.get(type + ""));
        }

        logger.debug("****  number of not null connectors: " + notNullConnectors + " ******");
        logger.debug(stringBuilder.toString());

    }

    private void addPacketLoss() {

//        int i = r.nextInt(10);
//        if(i==0){
//
//            logger.debug("Clearing all connector statistics");
//
//            Set<PSConnector> allConnectors = routeSelector.getAllConnectors();
        ConnectorStatistics statistics;
//            for (PSConnector connector : allConnectors) {
//
        statistics = new ConnectorStatistics();
//                statistics.setLinkId(connector.getId());
//
//                pathSelectorSolver.connectorMetrics(statistics);
//
//            }
//
//
//            int index = r.nextInt(allConnectors.size());

//            String id = ((PSConnector) allConnectors.toArray()[index]).getId();

        String id = "3";
        logger.debug("****  Setting connector id: " + id + " to 3% packet loss ****");
        statistics = new ConnectorStatistics();
        statistics.setLinkId(id);
        statistics.setPacketLossRate(3.0);

//        pathSelectorSolver.connectorMetrics(statistics);

//        }

    }


}
