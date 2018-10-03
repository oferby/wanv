package com.huawei.sdn.commons.data;

import java.util.Arrays;

/**
 * Internal Physical Address used to cache it with a time stamp
 */
public class MacAddress {
    /**
     * The physical Address
     */
    private byte[] mac;

    public MacAddress(byte[] mac) {
        this.mac = mac == null ? null : mac.clone();
    }

    public byte[] getMac() {
        return mac;
    }

    public void setMac(byte[] mac) {
        this.mac = mac == null ? null : mac.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MacAddress)) return false;

        MacAddress that = (MacAddress) o;

        return Arrays.equals(mac, that.mac);

    }

    @Override
    public int hashCode() {
        return mac != null ? Arrays.hashCode(mac) : 0;
    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();
        result.append("MacAddress{");
        for (byte b : mac) {
            result.append(String.format("%02X ", b));
            result.append(":");
        }

        result.append('}');

        return result.toString();

    }
}
