package io.github.johannesschaefer.simplenetworkmonitor;

import org.junit.Test;

import static org.junit.Assert.*;

public class SNMUtilsTest {

    @Test
    public void testGetMacAddr() {
        assertEquals("11:22:33:aa:bb:cc", SNMUtils.getMacAddr("xxx (192.168.178.1) at 11:22:33:aa:bb:cc [ether] on eth0"));
        assertEquals("11:22:33:aa:bb:cc", SNMUtils.getMacAddr("xxx (192.168.178.1) at 11:22:33:aa:bb:cc [ether] on eth0 aa:bb:cc:11:22:33"));
        assertEquals("", SNMUtils.getMacAddr("xxx (192.168.178.1) at  [ether] on eth0"));
        assertEquals("", SNMUtils.getMacAddr(""));
        assertEquals("", SNMUtils.getMacAddr(null));
    }
/*
    @Test
    public void testGetMacAddrHost() {
        assertEquals("", SNMUtils.getMacAddrHost("johannes-mbp"));
    }*/
}