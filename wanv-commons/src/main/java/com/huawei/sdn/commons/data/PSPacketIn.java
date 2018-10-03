package com.huawei.sdn.commons.data;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Set;

/**
 * The Packet Path Selector internal Object taking in account the Match Fields
 * Parameters configuration for equals and hash methods implementation.
 */
public class PSPacketIn {
    /**
     * The Possible Match Fields Parameters Enumeration
     */
    public static enum OfbMatchFields {
        IN_PORT, DL_SRC, DL_DST, DL_VLAN, DL_VLAN_PR, DL_TYPE, NW_TOS, NW_PROTO, NW_SRC, NW_DST, TP_SRC, TP_DST
    }

    private final Set<OfbMatchFields> ofbMatchFields;
    // All the Packet data that can be fix for the Match
    private String inPort;
    private byte[] dlSrc, dlDst;
    private Short dlVlan, dlType, tpSrc, tpDst;
    private Byte dlVlanPriority, nwProto;
    private Byte nwTOS = new Byte("0");
    private InetAddress nwSrc, nwDst;

    public PSPacketIn(Set<OfbMatchFields> ofbMatchFields) {
        super();
        this.ofbMatchFields = ofbMatchFields;
    }

    public PSPacketIn(Set<OfbMatchFields> ofbMatchFields, InetAddress nwSrc, InetAddress nwDst) {
        super();
        this.ofbMatchFields = ofbMatchFields;
        this.nwSrc = nwSrc;
        this.nwDst = nwDst;
    }

    public Set<OfbMatchFields> getOfbMatchFields() {
        return ofbMatchFields;
    }

    /**
     * retrieve a field of the Packet Object with the OfbMatchFields enumeration
     * 
     * @param field
     *            the field to retrieve
     * @return the field the Packet Object
     */
    public Object getField(OfbMatchFields field) {
        switch (field) {
        case IN_PORT:
            return inPort;
        case DL_SRC:
            return dlSrc;
        case DL_DST:
            return dlDst;
        case DL_VLAN:
            return dlVlan;
        case DL_VLAN_PR:
            return dlVlanPriority;
        case DL_TYPE:
            return dlType;
        case NW_TOS:
            return nwTOS;
        case NW_PROTO:
            return nwProto;
        case NW_SRC:
            return nwSrc;
        case NW_DST:
            return nwDst;
        case TP_SRC:
            return tpSrc;
        case TP_DST:
            return tpDst;
        }
        return null;
    }

    public String getInPort() {
        return inPort;
    }

    public void setInPort(String inPort) {
        this.inPort = inPort;
    }

    public byte[] getDlSrc() {
        return dlSrc;
    }

    public void setDlSrc(byte[] dlSrc) {
        this.dlSrc = dlSrc == null ? null : dlSrc.clone();
    }

    public byte[] getDlDst() {
        return dlDst;
    }

    public void setDlDst(byte[] dlDst) {
        this.dlDst = dlDst == null ? null : dlDst.clone();
    }

    public Short getDlVlan() {
        return dlVlan;
    }

    public void setDlVlan(Short dlVlan) {
        this.dlVlan = dlVlan;
    }

    public Short getDlType() {
        return dlType;
    }

    public void setDlType(Short dlType) {
        this.dlType = dlType;
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

    public Byte getNwTOS() {
        return nwTOS;
    }

    public void setNwTOS(Byte nwTOS) {
        this.nwTOS = nwTOS;
    }

    public Byte getNwProto() {
        return nwProto;
    }

    public void setNwProto(Byte nwProto) {
        this.nwProto = nwProto;
    }

    public InetAddress getNwSrc() {
        return nwSrc;
    }

    public void setNwSrc(InetAddress nwSrc) {
        this.nwSrc = nwSrc;
    }

    public InetAddress getNwDst() {
        return nwDst;
    }

    public void setNwDst(InetAddress nwDst) {
        this.nwDst = nwDst;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if (ofbMatchFields != null) {
            if (ofbMatchFields.contains(OfbMatchFields.DL_DST)) {
                result = prime * result + Arrays.hashCode(dlDst);
            }
            if (ofbMatchFields.contains(OfbMatchFields.DL_SRC)) {
                result = prime * result + Arrays.hashCode(dlSrc);
            }
            if (ofbMatchFields.contains(OfbMatchFields.DL_TYPE)) {
                result = prime * result + (dlType == null ? 0 : dlType.hashCode());
            }
            if (ofbMatchFields.contains(OfbMatchFields.DL_VLAN)) {
                result = prime * result + (dlVlan == null ? 0 : dlVlan.hashCode());
            }
            if (ofbMatchFields.contains(OfbMatchFields.DL_VLAN_PR)) {
                result = prime * result + (dlVlanPriority == null ? 0 : dlVlanPriority.hashCode());
            }
            if (ofbMatchFields.contains(OfbMatchFields.IN_PORT)) {
                result = prime * result + (inPort == null ? 0 : inPort.hashCode());
            }
            if (ofbMatchFields.contains(OfbMatchFields.NW_DST)) {
                result = prime * result + (nwDst == null ? 0 : nwDst.hashCode());
            }
            if (ofbMatchFields.contains(OfbMatchFields.NW_PROTO)) {
                result = prime * result + (nwProto == null ? 0 : nwProto.hashCode());
            }
            if (ofbMatchFields.contains(OfbMatchFields.NW_SRC)) {
                result = prime * result + (nwSrc == null ? 0 : nwSrc.hashCode());
            }
            if (ofbMatchFields.contains(OfbMatchFields.NW_TOS)) {
                result = prime * result + (nwTOS == null ? 0 : nwTOS.hashCode());
            }
            if (ofbMatchFields.contains(OfbMatchFields.TP_DST)) {
                result = prime * result + (tpDst == null ? 0 : tpDst.hashCode());
            }
            if (ofbMatchFields.contains(OfbMatchFields.TP_SRC)) {
                result = prime * result + (tpSrc == null ? 0 : tpSrc.hashCode());
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PSPacketIn other = (PSPacketIn) obj;
        if (ofbMatchFields != null) {
            if (ofbMatchFields.contains(OfbMatchFields.DL_DST) && !Arrays.equals(dlDst, other.dlDst)) {
                return false;
            }
            if (ofbMatchFields.contains(OfbMatchFields.DL_SRC) && !Arrays.equals(dlSrc, other.dlSrc)) {
                return false;
            }
            if (ofbMatchFields.contains(OfbMatchFields.DL_TYPE)) {
                if (dlType == null) {
                    if (other.dlType != null) {
                        return false;
                    }
                } else if (!dlType.equals(other.dlType)) {
                    return false;
                }
            }
            if (ofbMatchFields.contains(OfbMatchFields.DL_VLAN)) {
                if (dlVlan == null) {
                    if (other.dlVlan != null) {
                        return false;
                    }
                } else if (!dlVlan.equals(other.dlVlan)) {
                    return false;
                }
            }
            if (ofbMatchFields.contains(OfbMatchFields.DL_VLAN_PR)) {
                if (dlVlanPriority == null) {
                    if (other.dlVlanPriority != null) {
                        return false;
                    }
                } else if (!dlVlanPriority.equals(other.dlVlanPriority)) {
                    return false;
                }
            }
            if (ofbMatchFields.contains(OfbMatchFields.IN_PORT)) {
                if (inPort == null) {
                    if (other.inPort != null) {
                        return false;
                    }
                } else if (!inPort.equals(other.inPort)) {
                    return false;
                }
            }
            if (ofbMatchFields.contains(OfbMatchFields.NW_DST)) {
                if (nwDst == null) {
                    if (other.nwDst != null) {
                        return false;
                    }
                } else if (!nwDst.equals(other.nwDst)) {
                    return false;
                }
            }
            if (ofbMatchFields.contains(OfbMatchFields.NW_PROTO)) {
                if (nwProto == null) {
                    if (other.nwProto != null) {
                        return false;
                    }
                } else if (!nwProto.equals(other.nwProto)) {
                    return false;
                }
            }
            if (ofbMatchFields.contains(OfbMatchFields.NW_SRC)) {
                if (nwSrc == null) {
                    if (other.nwSrc != null) {
                        return false;
                    }
                } else if (!nwSrc.equals(other.nwSrc)) {
                    return false;
                }
            }
            if (ofbMatchFields.contains(OfbMatchFields.NW_TOS)) {
                if (nwTOS == null) {
                    if (other.nwTOS != null) {
                        return false;
                    }
                } else if (!nwTOS.equals(other.nwTOS)) {
                    return false;
                }
            }
            if (ofbMatchFields.contains(OfbMatchFields.TP_DST)) {
                if (tpDst == null) {
                    if (other.tpDst != null) {
                        return false;
                    }
                } else if (!tpDst.equals(other.tpDst)) {
                    return false;
                }
            }
            if (ofbMatchFields.contains(OfbMatchFields.TP_SRC)) {
                if (tpSrc == null) {
                    if (other.tpSrc != null) {
                        return false;
                    }
                } else if (!tpSrc.equals(other.tpSrc)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PSPacket [ofbMatchFields=");
        builder.append(ofbMatchFields);
        builder.append(", inPort=");
        builder.append(inPort);
        builder.append(", dlSrc=");
        builder.append(Arrays.toString(dlSrc));
        builder.append(", dlDst=");
        builder.append(Arrays.toString(dlDst));
        builder.append(", dlVlan=");
        builder.append(dlVlan);
        builder.append(", dlType=");
        builder.append(dlType);
        builder.append(", tpSrc=");
        builder.append(tpSrc);
        builder.append(", tpDst=");
        builder.append(tpDst);
        builder.append(", dlVlanPriority=");
        builder.append(dlVlanPriority);
        builder.append(", nwTOS=");
        builder.append(nwTOS);
        builder.append(", nwProto=");
        builder.append(nwProto);
        builder.append(", nwSrc=");
        builder.append(nwSrc);
        builder.append(", nwDst=");
        builder.append(nwDst);
        builder.append("]");
        return builder.toString();
    }

}
