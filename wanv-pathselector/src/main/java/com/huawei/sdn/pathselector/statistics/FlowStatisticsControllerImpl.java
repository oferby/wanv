package com.huawei.sdn.pathselector.statistics;

import com.huawei.sdn.commons.data.PSFlow;
import com.huawei.sdn.commons.db.model.FlowEntity;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Ofer Ben-Yacov (oWX212574) on 3/31/2015.
 */
@Component
public class FlowStatisticsControllerImpl implements FlowStatisticsController {

    private Logger logger = LoggerFactory.getLogger(FlowStatisticsControllerImpl.class);

    private CircularFifoBuffer circularFifoBuffer = new CircularFifoBuffer(50000);

    @Override
    public synchronized void save(final FlowEntity flowEntity) {

        circularFifoBuffer.add(flowEntity);

    }

    @Override
    public void save(PSFlow psFlow) {

        if(psFlow.getSrcIp()==null || psFlow.getDstIp()==null || psFlow.getTpSrc() ==null || psFlow.getTpDst()==null){
            return;
        }

        this.save(new FlowEntity(new Date(),
                psFlow.getSrcIp().getHostAddress(),
                psFlow.getDstIp().getHostAddress(),
                psFlow.getTpSrc().toString(),
                psFlow.getTpDst().toString()
        ));

    }

    @Override
    public Map<String, Map<String, Integer>> getNumberOfDestinationPortAndIpRequests() {

        Map<String, Map<String, Integer>> portAndIpRequestMap = new HashMap<>();

        Iterator iterator = circularFifoBuffer.iterator();

        while (iterator.hasNext()) {

            FlowEntity flowEntity = (FlowEntity) iterator.next();

            Map<String, Integer> ipPerPort = portAndIpRequestMap.get(flowEntity.getDestinationPort());
            if (ipPerPort == null) {
                ipPerPort = new HashMap<>();
                portAndIpRequestMap.put(flowEntity.getDestinationPort(), ipPerPort);
            }

            Integer requestsPerIp = ipPerPort.get(flowEntity.getDestinationIp()+ "." + flowEntity.getDestinationPort());
            if (requestsPerIp == null) {
                ipPerPort.put(flowEntity.getDestinationIp() + "." + flowEntity.getDestinationPort(), 1);
            } else {
                ipPerPort.put(flowEntity.getDestinationIp() + "." + flowEntity.getDestinationPort(), requestsPerIp + 1);
            }

        }


        return portAndIpRequestMap;

    }

    @Override
    public Map<String, Map<String, Set<String>>> getIpToIpAndPort() {

        Map<String, Map<String, Set<String>>> ipToIpAndPort = new HashMap<>();

        Iterator iterator = circularFifoBuffer.iterator();

        while (iterator.hasNext()) {

            FlowEntity flowEntity = (FlowEntity) iterator.next();

            Map<String, Set<String>> sourceIpMap = ipToIpAndPort.get(flowEntity.getSourceIp());
            if (sourceIpMap == null) {
                sourceIpMap = new HashMap<>();
                ipToIpAndPort.put(flowEntity.getSourceIp(), sourceIpMap);
            }

            Set<String> portList = sourceIpMap.get(flowEntity.getDestinationIp());
            if (portList == null) {
                portList = new HashSet<>();
                sourceIpMap.put(flowEntity.getDestinationIp(), portList);
            }

            portList.add(flowEntity.getDestinationPort());
        }

        return ipToIpAndPort;
    }


}
