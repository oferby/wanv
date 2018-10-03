package com.huawei.sdn.commons.selector.csp.planning;

import com.huawei.sdn.commons.context.WanApplicationContextProvider;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolver;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Set;

/**
 * Created by Ofer Ben-Yacov on 11/23/2014.
 */
public class MovableConnectorFilter implements SelectionFilter<PSFlow> {

    private PathSelectorSolver pathSelectorSolver;

    @Override
    public boolean accept(ScoreDirector scoreDirector, PSFlow flow) {

        if(pathSelectorSolver==null){
                pathSelectorSolver = WanApplicationContextProvider.getBean(PathSelectorSolver.class);
        }


        PSConnector connectorOut = flow.getConnectorOut();

        if(connectorOut==null){
            return true;
        }

//        if(connectorOut.isMovable()){
//            return true;
//        }
//
//        if (connectorOut.getType() == ConnectorType.REMOTE && connectorOut.isActive()) {
//
//            PSFlow currentFlow = pathSelectorSolver.getCurrentFlow(flow.getId());
//
///*              check if it is the connector that was changed or the queue
//
//*/
//            return currentFlow == null || connectorOut.equals(currentFlow.getConnectorOut());
//
//        }

        return connectorOut.isMovable();

    }
}
