package com.huawei.sdn.commons.jms;

import com.huawei.sdn.commons.selector.flow.FlowStatusHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/28/14
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
//@Component
public class SimpleServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServer.class);
    String clientSentence;
    ServerSocket welcomeSocket;
    @Autowired
    private FlowStatusHandler flowStatusHandler;

    public SimpleServer() {

        try {
            welcomeSocket = new ServerSocket(9090);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @PostConstruct
    public void init() throws IOException {

        new Thread(new ServerThread()).start();
    }

    private class ServerThread implements Runnable {


        @Override
        public void run() {


            while (true) {

                try {
                    Socket connectionSocket = welcomeSocket.accept();

                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                    while (true) {
                        clientSentence = inFromClient.readLine();

                        LOGGER.debug("Got message: " + clientSentence);

                        String[] split = clientSentence.split("=");

                        if (split.length != 2) {
                            LOGGER.error("bad message: " + clientSentence);
                            continue;
                        }

                        flowStatusHandler.linkStatusChanged(split[0], split[1].equals("1"));

                    }

                } catch (Exception e) {
                    LOGGER.error("Got exception in server thread",e);
                }

            }


        }
    }


}
