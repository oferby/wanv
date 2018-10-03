package com.huawei.sdn.commons.selector.csp.optaPlanner.config;

import com.huawei.sdn.commons.config.ConfigurationLoader;
import com.huawei.sdn.commons.context.WanApplicationContextProvider;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.selector.csp.CspPathSelectorImpl;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolver;
import com.huawei.sdn.commons.selector.csp.PathSelectorSolverImpl;
import com.huawei.sdn.commons.selector.csp.optaPlanner.mock.ConfigurationLoaderMock;
import com.huawei.sdn.commons.selector.csp.optaPlanner.mock.FlowProgrammerMock;
import com.huawei.sdn.commons.selector.csp.optaPlanner.mock.RouteSelectorMock;
import com.huawei.sdn.commons.selector.flow.FlowProgrammer;
import com.huawei.sdn.commons.selector.routing.RouteSelector;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 1/25/2015.
 */
@Configuration
public class PathSelectorTestConfig implements ApplicationContextAware {

    private static String CONF_DIR = "D:/dev/conf";

    private ApplicationContext ctx;

    @Bean
    public PathSelectorSolver pathSelectorSolver(){
        addPath();
        return new PathSelectorSolverImpl();
    }

    @Bean
    public FlowProgrammer flowProgrammer(){
        return new FlowProgrammerMock();
    }

    @Bean
    public RouteSelector routeSelector(){
        return new RouteSelectorMock();
    }

    @Bean
    public WanApplicationContextProvider wanApplicationContextProvider(){
        WanApplicationContextProvider wanApplicationContextProvider = new WanApplicationContextProvider();

        wanApplicationContextProvider.setApplicationContext(ctx);

        return wanApplicationContextProvider;
    }


    @Bean
    public ConfigurationLoader configurationLoader(){
        return new ConfigurationLoaderMock();
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;

    }
}
