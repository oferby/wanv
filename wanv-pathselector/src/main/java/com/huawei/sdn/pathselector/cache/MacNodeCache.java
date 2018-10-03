package com.huawei.sdn.pathselector.cache;

import com.huawei.sdn.commons.cache.ExpirableCache;
import com.huawei.sdn.commons.cache.ExpirableCacheImpl;
import com.huawei.sdn.commons.data.MacAddress;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/3/14
 * Time: 8:44 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Qualifier("macNodeCache")
public class MacNodeCache extends ExpirableCacheImpl<MacAddress, NodeConnector> implements ExpirableCache<MacAddress, NodeConnector> {
}
