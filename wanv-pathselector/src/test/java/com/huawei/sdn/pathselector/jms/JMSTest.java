package com.huawei.sdn.pathselector.jms;

import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.selector.flow.FlowStatusHandler;
import com.huawei.sdn.pathselector.ccm.CCMConsumer;
import com.huawei.sdn.pathselector.mock.jms.FlowStatusHandlerMock;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

/**
 * Created by Amir Kost on 7/3/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-jms3.xml"})
@ComponentScan("com.huawei.sdn.pathselector.mock.jms")
public class JMSTest {

    @Autowired
    private CCMConsumer ccmConsumer;
    @Autowired
    private TestCCMConsumer testCCMConsumer;
    @Autowired
    private TestConsumer testConsumer;
    @Autowired
    private TestPublisher testPublisher;
    @Autowired
    private CCMPublisher ccmPublisher;
    @Autowired
    private PathSelectorEngine pathSelectorEngine;
    @Autowired
    private FlowStatusHandler flowStatusHandler;

    @Test
    public void testContext() {
        assertNotNull(ccmConsumer);
        assertNotNull(ccmPublisher);
    }

    @Test
    public void sendMessage() throws InterruptedException {
        ccmPublisher.changeStatus("link1", true);
        Thread.sleep(1000);
        PathSelectorEngineMock mock = (PathSelectorEngineMock) pathSelectorEngine;
//        assertEquals(1, flowStatusHandlerMock.getNumOfMessagesReceived());
//        assertTrue("link1".equals(mock.getCurrentLink()));
    }


   // Should be tested manually with standalone ActiveMQ
//    @Test
    public void stressTest() throws InterruptedException {
        for(int i = 0; i < 1000; i++) {
            ccmPublisher.changeStatus("link1", i % 2 == 0);
            testPublisher.sendMessage();
            System.out.println("Publishing message no. " + i);
        }
        Thread.sleep(1000);
        FlowStatusHandlerMock flowStatusHandlerMock = (FlowStatusHandlerMock) flowStatusHandler;
        assertEquals(1000, flowStatusHandlerMock.getNumOfMessagesReceived());
        assertEquals(1000, testConsumer.getReceivedMsgCount());
        Set<Integer> receivedMessages = testCCMConsumer.getReceivedMessages();
        for(int i = 0; i < 1000; i++) {
            assertTrue(receivedMessages.contains(i));
        }
    }

}
