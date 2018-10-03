package com.huawei.sdn.pathselector.spring;

import org.opendaylight.controller.sal.action.Action;
import org.opendaylight.controller.sal.action.Output;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.flowprogrammer.Flow;
import org.opendaylight.controller.sal.utils.NodeConnectorCreator;
import org.opendaylight.controller.sal.utils.NodeCreator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/2/14
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpringMain {

    static {
        System.setProperty("siteName", "site1");
        System.setProperty("topologyFile", "D:/dev/conf/topology.xml");
    }

    public static void main(String[] args){

//        SpringContext context = SpringContext.getInstance();
//
//        Object bean = context.getBean(PacketHandler.class);


        Flow flow = new Flow();


        Node node = NodeCreator.createOFNode(1000L);
        NodeConnector port1 = NodeConnectorCreator.createNodeConnector(
                (short) 24, node);

        NodeConnector port2 = NodeConnectorCreator.createNodeConnector(
                (short) 25, node);

        List<Action>actions=new ArrayList<>();
        actions.add(new Output(port1));

        flow.setActions(actions);

        List<Action> actions1 = flow.getActions();

        for (int i = 0; i < actions1.size(); i++) {

            if(actions1.get(i) instanceof Output){
                actions1.set(i,new Output(port2));
            }

        }

        flow.setActions(actions1);

        assert flow!=null;


    }


}
