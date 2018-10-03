package com.huawei.sdn.commons.selector.csp.planning;

import com.huawei.sdn.commons.config.ConfigurationLoader;
import com.huawei.sdn.commons.context.WanApplicationContextProvider;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.data.PSFlowTimer;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import java.util.*;

/**
 * Created by oWX212574 on 6/23/2014.
 */
@PlanningSolution
public class LinkSelectionSolution implements Solution<HardMediumSoftScore> {

    //    Planning Entity
    private Set<PSFlow> psFlowList = new HashSet<>();

    private List<PSFlowTimer> psFlowTimerList = new ArrayList<>();

    private Set<PSConnector> psConnectorSet = new HashSet<>();

    private HardMediumSoftScore hardSoftScore;

    @PlanningEntityCollectionProperty
    public Set<PSFlow> getPsFlowList() {
        return psFlowList;
    }

    public void setPsFlowList(Set<PSFlow> psFlowList) {
        this.psFlowList = psFlowList;
    }

    public void addFlow(PSFlow flow) {
        this.psFlowList.add(flow);
    }

    public void addPSConnector(PSConnector psConnector){
        psConnectorSet.add(psConnector);
    }

    public void removePSConnector(PSConnector psConnector){
        psConnectorSet.remove(psConnector);
    }

    @ValueRangeProvider(id = "connectorRange")
    public Set<PSConnector> getPsConnectorSet() {
        return psConnectorSet;
    }

    @Override
    public HardMediumSoftScore getScore() {
        return hardSoftScore;
    }

    @Override
    public void setScore(HardMediumSoftScore score) {
        this.hardSoftScore = score;
    }

    public void addPSFlowTimer(PSFlowTimer flowTimer) {
        this.psFlowTimerList.add(flowTimer);
    }

    public List<PSFlowTimer> getPsFlowTimerList() {
        return psFlowTimerList;
    }

    public void setPsFlowTimerList(List<PSFlowTimer> psFlowTimerList) {
        this.psFlowTimerList = psFlowTimerList;
    }

    @Override
    public Collection<?> getProblemFacts() {
        List facts = new ArrayList<>();

        facts.addAll(psFlowList);

        facts.addAll(psConnectorSet);

        PSConnector dropConnector = new PSConnector("-1", ConnectorType.DROP);
        dropConnector.setMovable(true);
        facts.add(dropConnector);

        facts.addAll(psFlowTimerList);

        ConfigurationLoader configurationLoader = WanApplicationContextProvider.getBean(ConfigurationLoader.class);

        facts.addAll(configurationLoader.getQueueSet());

        return facts;
    }
}
