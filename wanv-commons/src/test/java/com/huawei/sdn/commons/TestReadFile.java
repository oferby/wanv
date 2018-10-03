package com.huawei.sdn.commons;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Ofer Ben-Yacov on 6/29/2014.
 */
public class TestReadFile {


    public static void main(String[] args){

        URL resource = TestReadFile.class.getResource("/SolverConfig.xml");

        try {

            File f = new File(resource.toURI());

            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));

            System.out.println(bufferedReader.readLine());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

}
