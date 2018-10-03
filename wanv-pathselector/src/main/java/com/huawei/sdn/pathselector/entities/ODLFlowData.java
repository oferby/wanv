package com.huawei.sdn.pathselector.entities;

import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.flowprogrammer.Flow;

public class ODLFlowData {
    /**
     * The OpenDayLight Flow Object
     */
    private Flow flow;
    /**
     * The OpenDayLight Node using this flow
     */
    private Node node;

    public ODLFlowData(Node node) {
        super();
        this.node = node;
    }

    public ODLFlowData(Flow flow, Node node) {
        super();
        this.flow = flow;
        this.node = node;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
