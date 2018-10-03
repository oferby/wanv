package com.huawei.sdn.commons.config.topology;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.huawei.sdn.commons.enums.ConnectorType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Link {

    @XmlAttribute
    private String id;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private ConnectorType type;
    @XmlElementWrapper(name = "peers")
    @XmlElement(name = "peer")
    private Peer[] _peers;
    @XmlElementWrapper(name = "operations")
    @XmlElement(name = "operation")
    private Operation[] _operations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConnectorType getType() {
        return type;
    }

    public void setType(ConnectorType type) {
        this.type = type;
    }

    public Peer[] getPeers() {
        return _peers;
    }

    public void setPeers(Peer[] peers) {

        if(peers==null)
            return;

        _peers = new Peer[peers.length];

        System.arraycopy(peers, 0, _peers, 0, peers.length);

    }

    public Operation[] getOperations() {
        return _operations;
    }

    public void setOperations(Operation[] operations) {

        if(operations==null)
            return;

        _operations = new Operation[operations.length];

        System.arraycopy(operations,0,_operations,0,operations.length);
    }
}
