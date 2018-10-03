package com.huawei.sdn.pathselector.odl.y1731;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.InetAddress;
import java.net.URI;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ofer Ben-Yacov on 11/5/2014.
 */
@RunWith(JUnit4.class)
public class TestHttpGet {


    @Test
    public void testQueue() throws InterruptedException {


        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

        executor.schedule(new Runnable() {
            @Override
            public void run() {

                System.out.print("done");

            }
        },2,TimeUnit.SECONDS);


        Thread.sleep(10000);

    }




    @Test
    public void testHttpGet(){


        CloseableHttpClient httpclient = HttpClients.createDefault();

        // Local interface1:
//        byte ip1[] = new byte[]{(byte) 10, (byte) 200, (byte) 201, (byte) 205};
        byte ip1[] = new byte[]{(byte) 172, (byte) 16, (byte) 200, (byte) 15};

        try {


            try {

                RequestConfig requestConfig = RequestConfig.custom()
                        .setLocalAddress(InetAddress.getByAddress(ip1))
                        .setConnectTimeout(2000)
                        .build();

//                HttpGet httpget = new HttpGet("http://localhost:8080");
                HttpGet httpget = new HttpGet("http://172.16.150.25/WANV/Monitoring.html");

                if(httpget.getURI().getHost().equals("172.16.150.25")){
                        System.out.println("yep");
                }

                URI uri = new URI("http://172.16.150.25/WANV/Monitoring.html");

                if (uri.getHost().equals("172.16.150.25"))
                    System.out.println("yep");

                httpget.setConfig(requestConfig);




                System.out.println("executing request" + httpget.getRequestLine());
                CloseableHttpResponse response = httpclient.execute(httpget);

                if (response != null && response.getStatusLine().getStatusCode() == 200) {

                    System.out.println("got response");

                }


            } finally {
                httpclient.close();
            }


        } catch (Exception e) {

            //                            we got no response from the server. reschedule check.

            System.out.println("got no response");

        }




    }


}
