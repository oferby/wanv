package com.huawei.sdn.commons.jms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/28/14
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {SimpleServerConfig.class})
public class TestSimpleServer {

    @Autowired
    private SimpleServer simpleServer;


//    @Test
    public void testServer(){

        Assert.notNull(simpleServer);


        Socket clientSocket = null;
        try {
            clientSocket = new Socket("localhost", 9090);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes("11=1" + '\n');


            outToServer.writeBytes("12=0" + '\n');

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Assert.notNull(simpleServer);


    }

}
