package com.huawei.sdn.commons.cache;

import com.huawei.sdn.commons.data.MacAddress;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/3/14
 * Time: 8:30 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Qualifier("arpCache")
public class ArpCache extends ExpirableCacheImpl<String, MacAddress> implements ExpirableCache<String, MacAddress>{



}
