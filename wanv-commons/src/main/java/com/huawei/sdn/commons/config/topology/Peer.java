package com.huawei.sdn.commons.config.topology;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Peer {

    @XmlElement
    private Address address;
    @XmlElement
    private Site site;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Site getSite() {
        return site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Peer)) {
            return false;
        }

        final Peer peer = (Peer) o;

        return address.equals(peer.address) && site.equals(peer.site);

    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + site.hashCode();
        return result;
    }

    public void setSite(Site site) {
        this.site = site;
    }

}
