package com.huawei.sdn.commons.selector.csp.planning;

import com.huawei.sdn.commons.data.PSFlow;
import org.apache.commons.lang.builder.CompareToBuilder;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Ofer Ben-Yacov on 6/23/2014.
 */
public class FlowDifficultyComparator implements Comparator<PSFlow>, Serializable{

    @Override
    public int compare(PSFlow f1, PSFlow f2) {
        return new CompareToBuilder()
                .append(f1.getNwTOS(), f2.getNwTOS())
                .toComparison();
    }
}
