package com.huawei.sdn.commons.selector.csp.drools.config;

import com.huawei.sdn.commons.config.ConfigurationLoader;
import com.huawei.sdn.commons.config.ConfigurationLoaderImpl;
import com.huawei.sdn.commons.data.PSConnector;
import com.huawei.sdn.commons.enums.ConnectorType;
import com.huawei.sdn.commons.selector.csp.drools.FlowProgrammerMock;
import com.huawei.sdn.commons.selector.csp.drools.RouteSelectorMock;
import com.huawei.sdn.commons.selector.flow.FlowProgrammer;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolver;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolverImpl;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 8/6/14
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class DroolsAppConfig {

    private static String CONF_DIR = "D:/dev/conf";


//    @Bean
//    public PathSelectorEngine pathSelectorEngine(){
//        return new CspPathSelectorImpl();
//    }
//
    @Bean
    public PathSelectorSolver pathSelectorSolver(){
        addPath();
        return new PathSelectorSolverImpl();
    }
//
//
    @Bean
    public ExecutorService executorService(){

        return new ThreadPoolExecutor(100, 200, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000));

    }

    @Bean
    public ConfigurationLoader configurationLoader(){
        return new ConfigurationLoaderImpl("D:/dev/conf/topology.xml");
    }

    @Bean
    public RouteSelector routeSelector(){
        return new RouteSelectorMock();
    }

//    @Bean
//    public TopologyManager topologyManager(){
//        return new TopologyManagerImpl();
//    }

    @Bean
    public FlowProgrammer flowProgrammer(){
        return new FlowProgrammerMock();

    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceHolderConfigurer(){

        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void addPath() {

        try {

            File f = new File(CONF_DIR);
            URL u = f.toURL();
            URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Class urlClass = URLClassLoader.class;
            Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(urlClassLoader, new Object[]{u});

        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

}
