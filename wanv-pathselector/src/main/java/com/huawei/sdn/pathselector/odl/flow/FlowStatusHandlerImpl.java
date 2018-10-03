package com.huawei.sdn.pathselector.odl.flow;

import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.selector.flow.FlowStatusHandler;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import com.huawei.sdn.pathselector.odl.L2.NodeConnectorHandler;
import org.opendaylight.controller.sal.flowprogrammer.Flow;
import org.opendaylight.controller.sal.match.Match;
import org.opendaylight.controller.sal.match.MatchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/20/14
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
@Component("flowStatusHandler")
public class FlowStatusHandlerImpl implements FlowStatusHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowStatusHandlerImpl.class);
    @Autowired
    NodeConnectorHandler nodeConnectorHandler;

    @Autowired
    private PathSelectorEngine pathSelectorEngine;

    @Autowired
    private RouteSelector routeSelector;

    @Override
    public void linkStatusChanged(String connectorId, boolean connected) {

        LOGGER.debug("Connector ID: " + connectorId + " state change to " + connected);

        routeSelector.linkStatusChanged(connectorId, connected);

        if (pathSelectorEngine.isPathSelectorEnabled())
            pathSelectorEngine.linkStatusChanged(connectorId, connected);

    }

    @Override
    public void flowRemoved(PSFlow psFlow) {

        if (!pathSelectorEngine.isPathSelectorEnabled())
            return;

        Flow flowToRemove = (Flow) psFlow.getCtData();

        Set<PSFlow> allKnownFlows = pathSelectorEngine.getAllKnownFlows();

        if(allKnownFlows.size()==0){
            LOGGER.debug("flow collection is empty");
        }

        Flow currentFlow;
        for (PSFlow flow : allKnownFlows) {

            currentFlow = (Flow) flow.getCtData();

            if(currentFlow==null){
                LOGGER.error("no CtData in flow for " + flow.toString());
                continue;
            }

            if (flowsEqual(currentFlow, flowToRemove)) {
                LOGGER.debug("Remove flow from path selector engine: " + flow.toString());
                pathSelectorEngine.flowRemoved(flow);
                return;
            }

        }

        LOGGER.debug("did not find flow to remove");

    }


    private boolean flowsEqual(Flow f1, Flow f2) {

        if (f1 == null || f2 == null) {
            return false;
        }

        Match match1 = f1.getMatch();
        Match match2 = f2.getMatch();

        boolean same = true;

        if (match1.getField(MatchType.NW_DST) != null || match1.getField(MatchType.NW_DST) != null) {

            if (match1.getField(MatchType.NW_DST) != null) {
                same = same && match1.getField(MatchType.NW_DST).equals(match2.getField(MatchType.NW_DST));
            } else {
                same = same && match2.getField(MatchType.NW_DST).equals(match1.getField(MatchType.NW_DST));
            }

        }

        if (match1.getField(MatchType.NW_SRC) != null || match1.getField(MatchType.NW_SRC) != null) {

            if (match1.getField(MatchType.NW_SRC) != null) {
                same = same && match1.getField(MatchType.NW_SRC).equals(match2.getField(MatchType.NW_SRC));
            } else {
                same = same && match2.getField(MatchType.NW_SRC).equals(match1.getField(MatchType.NW_SRC));
            }

        }

        if (match1.getField(MatchType.TP_SRC) != null || match1.getField(MatchType.TP_SRC) != null) {

            if (match1.getField(MatchType.TP_SRC) != null) {
                same = same && match1.getField(MatchType.TP_SRC).equals(match2.getField(MatchType.TP_SRC));
            } else {
                same = same && match2.getField(MatchType.TP_SRC).equals(match1.getField(MatchType.TP_SRC));
            }

        }

        if (match1.getField(MatchType.TP_DST) != null || match1.getField(MatchType.TP_DST) != null) {

            if (match1.getField(MatchType.TP_DST) != null) {
                same = same && match1.getField(MatchType.TP_DST).equals(match2.getField(MatchType.TP_DST));
            } else {
                same = same && match2.getField(MatchType.TP_DST).equals(match1.getField(MatchType.TP_DST));
            }

        }


        return same;


    }

}
