package com.huawei.sdn.commons.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * Net utilities methods/constants
 */
public final class NetUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetUtils.class);

    /**
     * Constant holding the number of bytes in MAC Address
     */
    public static final int MAC_ADDR_LENGTH_IN_BYTES = 6;

    /**
     * utility class with only static methods
     */
    private NetUtils() {
    }

    /**
     * This method converts byte array into String format with ":" inserted.<br/>
     * useful for MAC addresses
     *
     * @param bytes The byte array to convert to string
     * @return The hexadecimal representation of the byte array. If bytes is
     * null, "null" string is returned
     */
    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return "null";
        }
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (i > 0) {
                buf.append(':');
            }
            final short u8byte = (short) (bytes[i] & 0xff);
            final String tmp = Integer.toHexString(u8byte);
            if (tmp.length() == 1) {
                buf.append("0");
            }
            buf.append(tmp);
        }
        return buf.toString();
    }

    /*
    * transform string based MAC address to byte array
    * */
    public static byte[] hexStringToByteArray(String macAddress) {

        String[] macAddressParts = macAddress.split(":");

        // convert hex string to byte values
        byte[] macAddressBytes = new byte[6];

        for (int i = 0; i < 6; i++) {
            Integer hex = Integer.parseInt(macAddressParts[i], 16);
            macAddressBytes[i] = hex.byteValue();

        }

        return macAddressBytes;

    }

    /**
     * Determines the IP address of a host, given the host's name.
     *
     * @param addressString the specified host, or null.
     * @return an IP address for the given host name.
     */
    public static InetAddress parseInetAddress(String addressString) {
        try {
            return InetAddress.getByName(addressString);
        } catch (final UnknownHostException e) {
            LOGGER.error("exception during address parsing", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts an IP address passed as integer value into the respective
     * InetAddress object
     *
     * @param address the IP address in integer form
     * @return the IP address in InetAddress form
     */
    public static InetAddress getInetAddress(int address) {
        try {
            return InetAddress.getByAddress(NetUtils.intToByteArray4(address));
        } catch (final UnknownHostException e) {
            LOGGER.error("exception during address parsing", e);
            return null;
        }
    }


    public static int getIpAddressStringAsInt(String ipAddress) {

        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        return getIpAddressAsInt(inetAddress);

    }


    public static int getIpAddressAsInt(InetAddress inetAddress) {

        return ByteBuffer.wrap(inetAddress.getAddress()).getInt();

    }


    /**
     * Converts an integer number into a 4 bytes array
     *
     * @param i the integer number
     * @return the byte array
     */
    public static byte[] intToByteArray4(int i) {
        return new byte[]{(byte) (i >> 24 & 0xff), (byte) (i >> 16 & 0xff), (byte) (i >> 8 & 0xff), (byte) (i & 0xff)};
    }

}
