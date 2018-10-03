package com.huawei.sdn.commons.topology.manager;

import java.io.File;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.SimpleType;

import com.huawei.sdn.commons.data.OFMetricsPerQueue;

public class TestJson {

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        List<OFMetricsPerQueue> stats = mapper.readValue(new File("c:/temp/queueStats.json"),
                CollectionType.construct(List.class, SimpleType.construct(OFMetricsPerQueue.class)));
        System.out.println(stats);
    }
}
