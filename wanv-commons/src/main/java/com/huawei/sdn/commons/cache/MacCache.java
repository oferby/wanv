package com.huawei.sdn.commons.cache;

import com.huawei.sdn.commons.data.MacAddress;
import com.huawei.sdn.commons.data.PSConnector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/3/14
 * Time: 8:41 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Qualifier("macCache")
public class MacCache extends ExpirableCacheImpl<MacAddress, PSConnector> implements ExpirableCache<MacAddress, PSConnector>{

}
