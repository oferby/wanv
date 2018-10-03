package com.huawei.sdn.commons.config.topology.params;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by root on 7/9/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ParamsMap {

    @XmlElementRef(name = "param", type = Param.class)
    private List<Param> params;

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }
}
