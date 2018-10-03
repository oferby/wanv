package com.huawei.sdn.pathselector.tools;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Bean factory that initialize all the beans.<br>
 * The convention is to have the Class Name as <InterfaceName>Impl.<br>
 * All bean must have a default constructor. No automatic injection is
 * implemented.<br>
 */
public final class BeanFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanFactory.class);

    /**
     * The bean factory singleton
     */
    private static BeanFactory instance;

    /**
     * The existing beans
     */
    private final Map<String, Object> objects;

    /**
     * Only create it using getInstance
     */
    private BeanFactory() {
        objects = new HashMap<String, Object>();
    }

    /**
     * get the Beans factory instance
     *
     * @return the Bean factory instance
     */
    public static BeanFactory getInstance() {
        if (instance == null) {
            synchronized (BeanFactory.class) {
                if (instance == null) {
                    instance = new BeanFactory();
                }
            }
        }
        return instance;
    }

    /**
     * Get a bean of a class type.<br>
     * return always the same instance.<br>
     * Create it if not exists.<br>
     *
     * @param clazz
     *            The interface class
     * @return the bean instance
     */
    public Object getBean(Class<?> clazz) {
        Object res = objects.get(clazz.getName());
        if (res == null) {
            synchronized (objects) {
                res = objects.get(clazz.getName());
                if (res == null) {
                    try {
                        res = Class.forName(clazz.getName() + "Impl").newInstance();
                        objects.put(clazz.getName(), res);
                    } catch (final Exception e) {
                        LOGGER.warn("error during " + clazz.getName() + " instantation.", e);
                    }
                }
            }
        }
        return res;
    }

    /**
     * Add a bean to the factory
     *
     * @param clazz
     *            The Bean class type
     * @param obj
     *            The bean Object
     * @return The bean Object
     */
    public Object addBean(Class<?> clazz, Object obj) {
        synchronized (objects) {
            objects.put(clazz.getName(), obj);
        }
        return obj;
    }

}
