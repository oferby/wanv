package com.huawei.sdn.commons.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huawei.sdn.commons.config.ConfigurationLoader;
import com.huawei.sdn.commons.config.ConfigurationLoaderImpl;
import com.huawei.sdn.commons.context.WanApplicationContextProvider;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.csp.planning.FlowDifficultyComparator;
import com.huawei.sdn.commons.selector.csp.planning.MovableConnectorFilter;
import com.huawei.sdn.commons.config.topology.Queue;
import com.huawei.sdn.commons.tools.NetUtils;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.net.InetAddress;
import java.util.*;

@PlanningEntity(difficultyComparatorClass = FlowDifficultyComparator.class, movableEntitySelectionFilter = MovableConnectorFilter.class)
public class PSFlow<T, V> {

    private long id;

    private Queue queue;

    private long creationTime = Calendar.getInstance().getTimeInMillis();

//    private int elapseTime = 0;

    /**
     * The Path selector Packet In used to create this flow
     */
//    private PSPacketIn psPacket;
    /**
     * The Controller specific Flow Data member
     */
    private T ctData;


    private V packet;



    /**
     * The in/Out Switch Port of the Flow.
     */

    private PSConnector connectorIn, connectorOut;
    private ConnectorType type;
    private String groupId = "";
    private InetAddress srcIp, dstIp;
    private Short dlVlan, dlType, tpSrc, tpDst;
    private Byte dlVlanPriority, nwProto;
    private Byte nwTOS = new Byte("0");
    /**
     * The Number of Packet In Received for an existing flow in the cache
     */
    private short nbPackectIn;
    /**
     * The physical Destination and Source Address
     */
    private byte[] macSrc, macDest;

    public PSFlow() {
        super();
        this.nbPackectIn = 0;
    }

    public PSFlow(PSPacketIn psPacket, PSConnector connectorIn, PSConnector connectorOut) {
        super();
//        this.psPacket = psPacket;
        this.connectorIn = connectorIn;
        this.connectorOut = connectorOut;
        this.nbPackectIn = 0;
    }

    public static PSFlow getNewFlow() {
        return new PSFlow();

    }

    public long getId() {
        return id;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public int getElapseTime(){
        return (int) (Calendar.getInstance().getTimeInMillis() - creationTime) / 1000;
    }

//    public int getElapseTime() {
//        return elapseTime;
//    }
//
//    public void setElapseTime(){
//        elapseTime = (int) (Calendar.getInstance().getTimeInMillis() - creationTime) / 1000;
//    }

    public void setId(long id) {
        this.id = id;
    }

    public T getCtData() {
        return ctData;
    }

    public void setCtData(T ctData) {
        this.ctData = ctData;
    }

    public PSConnector getConnectorIn() {
        return connectorIn;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setConnectorIn(PSConnector connectorIn) {
        this.connectorIn = connectorIn;
    }

    public Short getDlType() {
        return dlType;
    }

    public void setDlType(Short dlType) {
        this.dlType = dlType;
    }

    @PlanningVariable(valueRangeProviderRefs = {"queueRange"})
    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    @ValueRangeProvider(id = "queueRange")
    public Set<Queue> getQueueSet() {

        ConfigurationLoader configurationLoader = WanApplicationContextProvider.getBean(ConfigurationLoader.class);

        return configurationLoader.getQueueSet();
    }



    @PlanningVariable(valueRangeProviderRefs = {"connectorRange"}, nullable = true)
    public PSConnector getConnectorOut() {
        return connectorOut;
    }

    public void setConnectorOut(PSConnector connectorOut) {
        this.connectorOut = connectorOut;
    }



    public V getPacket() {
        return packet;
    }

    public void setPacket(V packet) {
        this.packet = packet;
    }

    public short getNbPackectIn() {
        return nbPackectIn;
    }

    public void setNbPackectIn(short nbPackectIn) {
        this.nbPackectIn = nbPackectIn;
    }

    public void addPackectIn() {
        nbPackectIn++;
    }

    public byte[] getMacSrc() {
        return macSrc;
    }

    public void setMacSrc(byte[] macSrc) {
        this.macSrc = macSrc == null ? null : macSrc.clone();
    }

    public byte[] getMacDest() {
        return macDest;
    }

    public String getDestMacString(){

        return NetUtils.bytesToHexString(macDest);

    }


    public void setMacDest(byte[] macDest) {
        this.macDest = macDest == null ? null : macDest.clone();
    }

    public ConnectorType getType() {
        return type;
    }

    public void setType(ConnectorType type) {
        this.type = type;
    }

    public InetAddress getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(InetAddress srcIp) {
        this.srcIp = srcIp;
    }

    public InetAddress getDstIp() {
        return dstIp;
    }

    public void setDstIp(InetAddress dstIp) {
        this.dstIp = dstIp;
    }

    public Short getDlVlan() {
        return dlVlan;
    }

    public void setDlVlan(Short dlVlan) {
        this.dlVlan = dlVlan;
    }

    public Short getTpSrc() {
        return tpSrc;
    }

    public void setTpSrc(Short tpSrc) {
        this.tpSrc = tpSrc;
    }

    public Short getTpDst() {
        return tpDst;
    }

    public void setTpDst(Short tpDst) {
        this.tpDst = tpDst;
    }

    public Byte getDlVlanPriority() {
        return dlVlanPriority;
    }

    public void setDlVlanPriority(Byte dlVlanPriority) {
        this.dlVlanPriority = dlVlanPriority;
    }

    public Byte getNwProto() {
        return nwProto;
    }

    public void setNwProto(Byte nwProto) {
        this.nwProto = nwProto;
    }

    public Byte getNwTOS() {
        return nwTOS;
    }

    public void setNwTOS(Byte nwTOS) {
        this.nwTOS = nwTOS;
    }

    public boolean outerEquals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final PSFlow<T,V> other = (PSFlow<T,V>) obj;
        if (connectorIn == null) {
            if (other.connectorIn != null) {
                return false;
            }
        } else if (!connectorIn.equals(other.connectorIn)) {
            return false;
        }
        if (connectorOut == null) {
            if (other.connectorOut != null) {
                return false;
            }
        } else if (!connectorOut.equals(other.connectorOut)) {
            return false;
        }
        return true;
    }


    @JsonIgnore
    public PSFlow getCopy(){

        PSFlow psFlow = new PSFlow();
        psFlow.setId(id);
        psFlow.setConnectorOut(this.connectorOut);
        psFlow.setCtData(this.ctData);
        psFlow.setSrcIp(srcIp);
        psFlow.setDstIp(this.dstIp);
        psFlow.setTpSrc(tpSrc);
        psFlow.setTpDst(tpDst);
        psFlow.setGroupId(this.groupId);
        psFlow.setPacket(this.packet);
        psFlow.setConnectorIn(this.connectorIn);
        psFlow.setDlType(dlType);
        psFlow.setDlVlan(dlVlan);
        psFlow.setMacDest(macDest);
        psFlow.setMacSrc(macSrc);
        psFlow.setQueue(queue);
        psFlow.creationTime = creationTime;
        psFlow.setNwTOS(nwTOS);
        psFlow.setNwProto(nwProto);
        psFlow.nbPackectIn =nbPackectIn;
        psFlow.setType(type);


        return psFlow;

    }



    @Override
    public String toString() {
        return String.format("PSFlow [id=%s, groupId=%s, elapseTime=%s, qId=%s, qMinSec=%s, qMaxSec=%s, dlType=%s,dstIp=%s,srcIp=%s,nwProto=%s,tpSrc=%s,tpDst=%s,nwTOS=%s,connectorIn=%s,type=%s, connectorOut=%s, ctData=%s]",
                id, groupId, getElapseTime(), queue==null?-1:queue.getId(),queue==null?-1:queue.getMinSec(),queue==null?-1:queue.getMaxSec(), dlType, dstIp, srcIp,nwProto,tpSrc,tpDst,nwTOS,
                connectorIn, type, connectorOut, ctData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PSFlow psFlow = (PSFlow) o;

        return !(dlType != null ? !dlType.equals(psFlow.dlType) : psFlow.dlType != null) &&
                !(dlVlan != null ? !dlVlan.equals(psFlow.dlVlan) : psFlow.dlVlan != null) &&
                !(dlVlanPriority != null ? !dlVlanPriority.equals(psFlow.dlVlanPriority) : psFlow.dlVlanPriority != null) &&
                !(dstIp != null ? !dstIp.equals(psFlow.dstIp) : psFlow.dstIp != null) &&
                Arrays.equals(macDest, psFlow.macDest) &&
                Arrays.equals(macSrc, psFlow.macSrc) &&
                !(nwProto != null ? !nwProto.equals(psFlow.nwProto) : psFlow.nwProto != null) &&
                !(nwTOS != null ? !nwTOS.equals(psFlow.nwTOS) : psFlow.nwTOS != null) &&
                !(srcIp != null ? !srcIp.equals(psFlow.srcIp) : psFlow.srcIp != null) &&
                !(tpDst != null ? !tpDst.equals(psFlow.tpDst) : psFlow.tpDst != null) &&
                !(tpSrc != null ? !tpSrc.equals(psFlow.tpSrc) : psFlow.tpSrc != null) &&
                type == psFlow.type;

    }

    @Override
    public int hashCode() {
        int result = srcIp != null ? srcIp.hashCode() : 0;
        result = 31 * result + (dstIp != null ? dstIp.hashCode() : 0);
        result = 31 * result + (dlVlan != null ? dlVlan.hashCode() : 0);
        result = 31 * result + (dlType != null ? dlType.hashCode() : 0);
        result = 31 * result + (tpSrc != null ? tpSrc.hashCode() : 0);
        result = 31 * result + (tpDst != null ? tpDst.hashCode() : 0);
        result = 31 * result + (dlVlanPriority != null ? dlVlanPriority.hashCode() : 0);
        result = 31 * result + (nwProto != null ? nwProto.hashCode() : 0);
        result = 31 * result + (nwTOS != null ? nwTOS.hashCode() : 0);
        result = 31 * result + (macSrc != null ? Arrays.hashCode(macSrc) : 0);
        result = 31 * result + (macDest != null ? Arrays.hashCode(macDest) : 0);
        return result;
    }
}
