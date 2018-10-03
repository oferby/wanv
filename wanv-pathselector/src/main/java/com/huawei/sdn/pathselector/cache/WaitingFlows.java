package com.huawei.sdn.pathselector.cache;

import com.huawei.sdn.commons.cache.ExpirableCache;
import com.huawei.sdn.commons.cache.ExpirableCacheImpl;
import com.huawei.sdn.commons.data.MacAddress;
import com.huawei.sdn.commons.data.PSFlow;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * User: oWX212574
 * Date: 7/3/14
 * Time: 8:47 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Qualifier("waitingFlows")
public class WaitingFlows extends ExpirableCacheImpl<InetAddress, PSFlow> implements ExpirableCache<InetAddress, PSFlow> {


}
