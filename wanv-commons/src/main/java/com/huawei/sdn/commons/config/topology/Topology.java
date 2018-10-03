package com.huawei.sdn.commons.config.topology;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.huawei.sdn.commons.enums.OperationConfig;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Topology {

    @XmlAttribute(name = "switch-name")
    private String switchName;
    @XmlElementWrapper(name = "sites")
    @XmlElement(name = "site")
    private Site[] _sites;

    @XmlElementWrapper(name = "queues")
    @XmlElement(name = "queue")
    private Queue[] _queues;

    @XmlElementWrapper(name = "operations")
    @XmlElement(name = "operation")
    private Operation[] _operations;
    @XmlElement(name = "config")
    private Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public Operation[] getOperations() {
        return _operations;
    }

    public Operation getHeartBitOperation(){

        for (Operation operation : _operations) {
            if (operation.getOperationConfig() == OperationConfig.HEARTBEAT){
                return operation;
            }
        }

        return null;

    }

    public Operation getDelayOperation(){

        for (Operation operation : _operations) {
            if (operation.getOperationConfig() == OperationConfig.FRAME_DELAY){
                return operation;
            }
        }

        return null;

    }




    public void setOperations(Operation[] operations) {

        if(operations==null)
            return;

        _operations = new Operation[operations.length];

        System.arraycopy(operations,0,_operations,0,operations.length);
    }

    public Queue[] getQueues() {
        return _queues;
    }

    public void set_queues(Queue[] queues) {

        if(queues==null) {
            return;
        }

        queues = new Queue[queues.length];

        System.arraycopy(queues,0, _queues,0,queues.length);

    }

    public Site[] getSites() {
        return _sites;
    }

    public void setSites(Site[] sites) {

        if(sites==null) {
            return;
        }

        sites = new Site[sites.length];

        System.arraycopy(sites,0,_sites,0,sites.length);
    }

}
