package com.huawei.sdn.commons.selector.csp;

import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.selector.PathSelectorEngine;
import com.huawei.sdn.commons.data.PSConnector;

/**
 * Created with IntelliJ IDEA. User: oWX212574 Date: 6/1/14 Time: 5:03 PM To
 * change this template use File | Settings | File Templates.
 */
public interface CspPathSelector extends PathSelectorEngine {

    PSConnector getConnectorForFlow(PSFlow flow);

}
