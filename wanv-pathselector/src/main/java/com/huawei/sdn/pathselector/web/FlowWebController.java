package com.huawei.sdn.pathselector.web;

import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolverImpl;
import com.huawei.sdn.commons.selector.flow.FlowProgrammer;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.pathselector.odl.ServiceHelper;
import com.huawei.sdn.pathselector.odl.flow.FlowManager;
import com.huawei.sdn.pathselector.statistics.StatisticManager;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.flowprogrammer.Flow;
import org.opendaylight.controller.sal.reader.FlowOnNode;
import org.opendaylight.controller.sal.reader.IReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 8/13/14
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/flow")
public class FlowWebController {

    @Autowired
    private FlowProgrammer flowProgrammer;
    @Autowired
    private ServiceHelper serviceHelper;
    @Autowired
    private PathSelectorEngine pathSelectorEngine;
    @Autowired
    private PathSelectorSolverImpl pathSelectorSolver;
    @Autowired
    private RouteSelector routeSelector;

    @Autowired
    private FlowManager flowManager;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Flow> getFlows() {

        IReadService readService = serviceHelper.getReadService();
        Node node = serviceHelper.getNode();

        List<Flow> flowList = new ArrayList<>();

        List<FlowOnNode> flowOnNodes = readService.readAllFlows(node);

        Flow flow;
        for (FlowOnNode flowOnNode : flowOnNodes) {
            flowList.add(flowOnNode.getFlow());
        }

        return flowList;

    }

    @RequestMapping(value = "count/gateway",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Integer>getDefaultGatewayFlowCount(){

        return flowManager.getFlowPerGatewayConnector();

    }

    @RequestMapping(value = "count/tunnel",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Integer>getTunnelFlowCount(){
        return flowManager.getFlowPerTunnelConnector();
    }



    @RequestMapping(value = "solution", method = RequestMethod.GET)
    @ResponseBody
    public Set<PSFlow> getPSFlows() {

        Set<PSFlow> allKnownFlows = pathSelectorEngine.getAllKnownFlows();

        for (PSFlow flow : allKnownFlows) {
            flow.setPacket(null);
            flow.setCtData(null);
        }

        return allKnownFlows;
    }

    @RequestMapping(value = "solution/id", method = RequestMethod.GET)
    @ResponseBody
    public List<Long> getPSFlowIds() {

        Set<PSFlow> allKnownFlows = pathSelectorEngine.getAllKnownFlows();
        List<Long> idList = new ArrayList<>();

        for (PSFlow psFlow : allKnownFlows) {
            idList.add(psFlow.getId());
        }

        return idList;

    }

    @RequestMapping(value = "solution/size", method = RequestMethod.GET)
    @ResponseBody
    public int getNumberOfFlows() {
        return pathSelectorSolver.getAllKnownFlowsFromSolution().size();
    }

    @RequestMapping(value = "solution/balance", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Integer> getFlowPerConnector() {

        return flowManager.getFlowPerConnectorFromSolution();

    }

}
