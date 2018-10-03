package com.huawei.sdn.commons.selector.csp.choco;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import org.springframework.beans.factory.annotation.Autowired;
import solver.ResolutionPolicy;
import solver.Solver;
import solver.constraints.IntConstraintFactory;
import solver.search.solution.Solution;
import solver.search.strategy.selectors.values.IntDomainMin;
import solver.search.strategy.selectors.variables.InputOrder;
import solver.search.strategy.strategy.IntStrategy;
import solver.variables.IntVar;
import solver.variables.VariableFactory;

import com.huawei.sdn.commons.data.CurrentLinkMetrics;
import com.huawei.sdn.commons.data.OFMetricsPerPort;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.data.PSPacketIn;

/**
 * Created with IntelliJ IDEA. User: oWX212574 Date: 6/1/14 Time: 5:02 PM To
 * change this template use File | Settings | File Templates.
 */
public class ChocoCspPathSelectorImpl implements PathSelectorEngine{

    private final Random r = new Random();
    private final Map<String, CurrentLinkMetrics> metricsMap = new ConcurrentHashMap<>();

    @Autowired
    RouteSelector routeSelector;

    public ChocoCspPathSelectorImpl() {
    }

    public PSConnector getConnectorForClient(PSPacketIn psPacket) {
//        final PSConnector connector = routeSelector.getConnector(psPacket.getNwSrc().getHostAddress(),psPacket.getNwDst().getHostAddress(),true);
//
//        final Byte nwTOS = psPacket.getNwTOS();
//
//        final int tosIntValue = nwTOS.intValue();
//        if (tosIntValue == 0) {
//            // Best Effort
//            return solveBestEffort(connectorList);
//        } else if (tosIntValue < 15) {
//            // Priority data
//            return solvePriorityData(connectorList);
//        } else if (tosIntValue < 39) {
//            // immediate, control
//            return solveImmediate(connectorList);
//        } else if (tosIntValue < 57) {
//            // real time
//            return solveRealTime(connectorList);
//        }
//        return getRandomConnector(connectorList);

        return null;
    }

    @Override
    public void enablePathSelector(boolean enable) {

    }

    @Override
    public boolean isPathSelectorEnabled() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PSConnector getConnectorForFlow(PSFlow flow, List<PSConnector> psConnectorList) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addNewFlow(PSFlow psFlow) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean flowExists(PSFlow psFlow) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void flowRemoved(PSFlow psFlowToRemove) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

//    @Override
//    public void flowRemoved(long flowId) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//
//    @Override
//    public void flowRemoved(String connectorId, long flowId) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }

    @Override
    public void removeFlows(List<PSFlow> flowsToRemove) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void linkStatusChanged(String connectorId, boolean connected) {

    }

    private PSConnector solveBestEffort(List<PSConnector> connectorList) {
        final Solver solver = new Solver();
        final int[] frameDelayArray = new int[connectorList.size()];
        final IntVar[] portArray = VariableFactory.boundedArray("portArray", connectorList.size(), 0, 1, solver);
        final IntVar single = VariableFactory.bounded("single", 1, 1, solver);
        solver.post(IntConstraintFactory.sum(portArray, single));
        CurrentLinkMetrics linkMetrics;
        for (int i = 0; i < connectorList.size(); i++) {
            linkMetrics = metricsMap.get(connectorList.get(i).getId());
            if (linkMetrics == null) {
                frameDelayArray[i] = 0;
                solver.post(IntConstraintFactory.arithm(portArray[i], "=", 1));
            } else {
                frameDelayArray[i] = linkMetrics.getFrameDelay();
                solver.post(IntConstraintFactory.arithm(portArray[i], "=", linkMetrics.isConnected() ? 1 : 0));
            }
        }

        final IntVar delay = VariableFactory.bounded("delay", 0, 10000, solver);
        solver.post(IntConstraintFactory.scalar(portArray, frameDelayArray, delay));

        final IntVar flowPerPort = VariableFactory.bounded("flowPerPort", 0, 10000, solver);
        solver.post(IntConstraintFactory.scalar(portArray, getFlowsPerPort(connectorList), flowPerPort));

        final IntVar[] scope = { delay, flowPerPort };

        solver.set(new IntStrategy(scope, new InputOrder<IntVar>(), new IntDomainMin()));

        solver.findParetoFront(ResolutionPolicy.MINIMIZE, delay, flowPerPort);

        final Solution lastSolution = solver.getSolutionRecorder().getLastSolution();

        if (lastSolution == null) {
            return getRandomConnector(connectorList);
        }

        for (int i = 0; i < connectorList.size(); i++) {
            if (lastSolution.getIntVal(portArray[i]) == 1) {
                return connectorList.get(i);
            }
        }

        // we should not get here
        throw new RuntimeException("error getting solution from solver");

    }

    private PSConnector solveImmediate(List<PSConnector> connectorList) {

        return solveBestEffort(connectorList);
    }

    private PSConnector solveRealTime(List<PSConnector> connectorList) {

        return solveBestEffort(connectorList);

    }

    private int[] getFlowsPerPort(List<PSConnector> connectorList) {

        final int[] flowsPerPort = new int[connectorList.size()];

        for (int i = 0; i < connectorList.size(); i++) {

            // Collection flowsByEgressPortId =
            // pathSelectorEngine.getFlowsByEgressPortId(connectorList.get(i).getId());
            //
            // flowsPerPort[i] = flowsByEgressPortId == null ? 0 :
            // flowsByEgressPortId.size();

            flowsPerPort[i] = 0;

        }

        return flowsPerPort;
    }

    private PSConnector solvePriorityData(List<PSConnector> connectorList) {

        return getRandomConnector(connectorList);

    }

    private PSConnector getRandomConnector(List<PSConnector> connectorList) {

        final int index = r.nextInt(connectorList.size());

        return connectorList.get(index);

    }

    @Override
    public void metricsReceived(List<CurrentLinkMetrics> metricsList) {
        for (final CurrentLinkMetrics currentLinkMetrics : metricsList) {
            metricsMap.put(currentLinkMetrics.getLinkId(), currentLinkMetrics);
        }
    }

    @Override
    public void metricsOVSReceived(List<OFMetricsPerPort> metricsPerPorts) {
        // TODO Auto-generated method stub
        System.out.println(metricsPerPorts);
    }

    @Override
    public Set<PSFlow> getAllKnownFlows() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getScore() {
        return 0;
    }

}
