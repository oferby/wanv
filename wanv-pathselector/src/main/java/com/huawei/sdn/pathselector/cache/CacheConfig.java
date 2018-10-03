package com.huawei.sdn.pathselector.cache;

import net.sf.ehcache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/28/14
 * Time: 9:55 AM
 * To change this template use File | Settings | File Templates.
 */
//@Configuration
public class CacheConfig {


    @Bean
    public CacheManager getCacheManager() {

        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        cacheManager.setCacheManager(getEhCacheManagerFactoryBean().getObject());

        return cacheManager;
    }

    @Bean
    public EhCacheManagerFactoryBean getEhCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("WEB-INF/ehcache.xml"));

        return factoryBean;
    }

}
