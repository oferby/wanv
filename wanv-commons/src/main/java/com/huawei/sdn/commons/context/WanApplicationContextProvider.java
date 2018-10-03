package com.huawei.sdn.commons.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 8/6/14
 * Time: 5:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class WanApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext ctx;

    public static <T> T getBean(Class clazz) {

        return (T) ctx.getBean(clazz);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    public static boolean isReady(){
        return ctx!=null;
    }


}
