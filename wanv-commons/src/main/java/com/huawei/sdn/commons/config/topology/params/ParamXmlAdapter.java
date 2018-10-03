package com.huawei.sdn.commons.config.topology.params;

import com.huawei.sdn.commons.enums.TunnelParams;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Amir Kost on 7/9/14.
 */
public class ParamXmlAdapter extends XmlAdapter<ParamsMap, Map<TunnelParams, String>> {


    @Override
    public Map<TunnelParams, String> unmarshal(ParamsMap params) throws Exception {
        if(params == null) {
            return null;
        }
        Map<TunnelParams, String> map = new HashMap<>();
        for(Param param : params.getParams()) {
            map.put(param.getName(), param.getValue());
        }
        return map;
    }

    @Override
    public ParamsMap marshal(Map<TunnelParams, String> tunnelParamsStringMap) throws Exception {
        if(tunnelParamsStringMap == null) {
            return null;
        }
        ParamsMap params = new ParamsMap();
        List<Param> paramList = new ArrayList<>();
        for(Map.Entry<TunnelParams, String> entry : tunnelParamsStringMap.entrySet()) {
            Param param = new Param();
            param.setName(entry.getKey());
            param.setValue(entry.getValue());
            paramList.add(param);
        }
        params.setParams(paramList);
        return params;
    }
}
