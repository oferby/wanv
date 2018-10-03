function customersController($scope){
    $scope.connectors=[{"id":"11",
                        "groupId":"",
        "localIPAddress":"3.1.2.1",
        "destinationAddress":"1.1.2.1",
        "nextHopIpAddress":null,
        "localMacAddress":"MTAwMDAwMDAwMDEx",
        "remoteMacAddress":"MTAwMDAwMDAwMDEy",
        "type":"GRE",
        "active":false,
        "connectorStatistics":{
            "timeStamp":{
                "fraction":3560527888,
                "date":1423749900829,
                "seconds":3632738700,
                "time":1423749900829
            },
            "linkId":null,
            "frameDelay":0.0,
            "frameDelayVariation":0.0,
            "packetLossRate":0.0,
            "txPackets":0,
            "rxPackets":0,
            "txBytes":0,
            "rxBytes":0,
            "txDrops":0,
            "rxDrops":0,"txErrors":0,"rxErrors":0,"flows":0
        }
    },
    {
        "id":"2",
        "groupId":"",
        "localIPAddress":"4.1.2.1",
        "destinationAddress":null,
        "nextHopIpAddress":"4.1.2.2",
        "localMacAddress":"MTAwMDAwMDAwMDEx",
        "remoteMacAddress":"MTAwMDAwMDAwMDEy",
        "type":"REMOTE",
        "active":true,
        "connectorStatistics":{
            "timeStamp":{
                "fraction":3165390897,"date":1423749900737,"seconds":3632738700,"time":1423749900737
            },
            "linkId":null,"frameDelay":0.0,"frameDelayVariation":0.0,"packetLossRate":0.0,"txPackets":0,"rxPackets":0,"txBytes":0,"rxBytes":0,"txDrops":0,"rxDrops":0,"txErrors":0,"rxErrors":0,"flows":0
        }
    },
    {
        "id":"10000","groupId":"","localIPAddress":"192.168.20.1","destinationAddress":null,"nextHopIpAddress":null,"localMacAddress":"MTAwMDAwMDAwMDEx","remoteMacAddress":"MTAwMDAwMDAwMDEy","type":"LOCAL","active":true,"connectorStatistics":{"timeStamp":{"fraction":2744484102,"date":1423749900639,"seconds":3632738700,"time":1423749900639},"linkId":null,"frameDelay":0.0,"frameDelayVariation":0.0,"packetLossRate":0.0,"txPackets":0,"rxPackets":0,"txBytes":0,"rxBytes":0,"txDrops":0,"rxDrops":0,"txErrors":0,"rxErrors":0,"flows":0}},{"id":"10001","groupId":"","localIPAddress":"3.1.2.1","destinationAddress":null,"nextHopIpAddress":null,"localMacAddress":"MTAwMDAwMDAwMDEx","remoteMacAddress":"MTAwMDAwMDAwMDEy","type":"LOCAL","active":true,"connectorStatistics":{"timeStamp":{"fraction":2748779069,"date":1423749900640,"seconds":3632738700,"time":1423749900640},"linkId":null,"frameDelay":0.0,"frameDelayVariation":0.0,"packetLossRate":0.0,"txPackets":0,"rxPackets":0,"txBytes":0,"rxBytes":0,"txDrops":0,"rxDrops":0,"txErrors":0,"rxErrors":0,"flows":0}},{"id":"1","groupId":"","localIPAddress":"3.1.2.1","destinationAddress":null,"nextHopIpAddress":"3.1.2.2","localMacAddress":"MTAwMDAwMDAwMDEx","remoteMacAddress":"MTAwMDAwMDAwMDEy","type":"REMOTE","active":true,"connectorStatistics":{"timeStamp":{"fraction":3165390897,"date":1423749900737,"seconds":3632738700,"time":1423749900737},"linkId":null,"frameDelay":0.0,"frameDelayVariation":0.0,"packetLossRate":0.0,"txPackets":0,"rxPackets":0,"txBytes":0,"rxBytes":0,"txDrops":0,"rxDrops":0,"txErrors":0,"rxErrors":0,"flows":0}},{"id":"10002","groupId":"","localIPAddress":"4.1.2.1","destinationAddress":null,"nextHopIpAddress":null,"localMacAddress":"MTAwMDAwMDAwMDEx","remoteMacAddress":"MTAwMDAwMDAwMDEy","type":"LOCAL","active":true,"connectorStatistics":{"timeStamp":{"fraction":2748779069,"date":1423749900640,"seconds":3632738700,"time":1423749900640},"linkId":null,"frameDelay":0.0,"frameDelayVariation":0.0,"packetLossRate":0.0,"txPackets":0,"rxPackets":0,"txBytes":0,"rxBytes":0,"txDrops":0,"rxDrops":0,"txErrors":0,"rxErrors":0,"flows":0}},{"id":"12","groupId":"","localIPAddress":"4.1.2.1","destinationAddress":"2.1.2.1","nextHopIpAddress":null,"localMacAddress":"MTAwMDAwMDAwMDEx","remoteMacAddress":"MTAwMDAwMDAwMDEy","type":"GRE","active":false,"connectorStatistics":{"timeStamp":{"fraction":3629247365,"date":1423749900845,"seconds":3632738700,"time":1423749900845},"linkId":null,"frameDelay":0.0,"frameDelayVariation":0.0,"packetLossRate":0.0,"txPackets":0,"rxPackets":0,"txBytes":0,"rxBytes":0,"txDrops":0,"rxDrops":0,"txErrors":0,"rxErrors":0,"flows":0}}];
}

function routeController($scope){
    $scope.route ={"siteId":"00:00:ce:03:c2:a0:09:4e","macAddress":"ce:03:c2:a0:09:4e","subnetSet":["192.168.10.1/24"],"remoteSiteSet":[{"siteId":"00:00:f2:72:0b:c7:d6:44","macAddress":null,"subnetSet":["192.168.20.1/24"],"remoteSiteSet":[]}]};
}

function flowController($scope){
    $scope.flows =
        [
            {"id":10573,"queue":{"id":0,"tosList":null,"priority":null,"minRate":null,"maxRate":null,"minSec":0,"maxSec":10,"defaultQueue":true},"creationTime":1424011732832,"ctData":null,"packet":null,"connectorIn":null,"connectorOut":{"id":"12","groupId":"00:00:f2:72:0b:c7:d6:44","localIPAddress":"2.1.2.1","destinationAddress":"4.1.2.1","nextHopIpAddress":null,"localMacAddress":"zgPCoAlO","remoteMacAddress":"8nILx9ZE","type":"GRE","active":true,"movable":true,"connectorStatistics":{"timeStamp":{"fraction":1318554959,"time":1424009324307,"date":1424009324307,"seconds":3632998124},"linkId":"12","frameDelay":0.0,"frameDelayVariation":0.0,"packetLossRate":0.0,"txPackets":0,"rxPackets":0,"txBytes":0,"rxBytes":0,"txDrops":0,"rxDrops":0,"txErrors":0,"rxErrors":0,"flows":0}},"type":"GRE","groupId":"00:00:f2:72:0b:c7:d6:44","srcIp":"192.168.10.2","dstIp":"192.168.20.2","dlVlan":null,"dlType":2048,"tpSrc":null,"tpDst":null,"dlVlanPriority":null,"nwProto":1,"nwTOS":0,"nbPackectIn":0,"macSrc":null,"macDest":null,"elapseTime":93,"queueSet":[{"id":3,"tosList":null,"priority":null,"minRate":null,"maxRate":null,"minSec":71,"maxSec":2147483647,"defaultQueue":false},{"id":2,"tosList":null,"priority":null,"minRate":null,"maxRate":null,"minSec":41,"maxSec":70,"defaultQueue":false},{"id":1,"tosList":null,"priority":null,"minRate":null,"maxRate":null,"minSec":11,"maxSec":40,"defaultQueue":false},{"id":0,"tosList":null,"priority":null,"minRate":null,"maxRate":null,"minSec":0,"maxSec":10,"defaultQueue":true}]},
            {"id":10571,"queue":{"id":0,"tosList":null,"priority":null,"minRate":null,"maxRate":null,"minSec":0,"maxSec":10,"defaultQueue":true},"creationTime":1424011732831,"ctData":null,"packet":null,"connectorIn":null,"connectorOut":{"id":"10000","groupId":"","localIPAddress":"192.168.10.1","destinationAddress":null,"nextHopIpAddress":null,"localMacAddress":"AQAAAAAB","remoteMacAddress":"AQAAAAAC","type":"LOCAL","active":true,"movable":false,"connectorStatistics":{"timeStamp":{"fraction":146028888,"time":1424009324034,"date":1424009324034,"seconds":3632998124},"linkId":null,"frameDelay":0.0,"frameDelayVariation":0.0,"packetLossRate":0.0,"txPackets":0,"rxPackets":0,"txBytes":0,"rxBytes":0,"txDrops":0,"rxDrops":0,"txErrors":0,"rxErrors":0,"flows":0}},"type":"LOCAL","groupId":"","srcIp":null,"dstIp":"192.168.10.2","dlVlan":null,"dlType":2048,"tpSrc":null,"tpDst":null,"dlVlanPriority":null,"nwProto":null,"nwTOS":0,"nbPackectIn":0,"macSrc":null,"macDest":null,"elapseTime":93,"queueSet":[{"id":3,"tosList":null,"priority":null,"minRate":null,"maxRate":null,"minSec":71,"maxSec":2147483647,"defaultQueue":false},{"id":2,"tosList":null,"priority":null,"minRate":null,"maxRate":null,"minSec":41,"maxSec":70,"defaultQueue":false},{"id":1,"tosList":null,"priority":null,"minRate":null,"maxRate":null,"minSec":11,"maxSec":40,"defaultQueue":false},{"id":0,"tosList":null,"priority":null,"minRate":null,"maxRate":null,"minSec":0,"maxSec":10,"defaultQueue":true}]}
        ];
}