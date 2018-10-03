
var operands = new Array();
var operators = new Array();

var dictionary = {};
dictionary["Frame_Delay"] = "connectorStatistics.frameDelay";
dictionary["Jitter"] = "connectorStatistics.frameDelayVariation";
dictionary["Packet_Loss_Rate"] = "connectorStatistics.packetLossRate";
dictionary["Tx_Packtes"] = "connectorStatistics.txPackets";
dictionary["Rx_Packets"] = "connectorStatistics.rxPackets";
dictionary["Tx_Bytes"] = "connectorStatistics.txBytes";
dictionary["Rx_Bytes"] = "connectorStatistics.rxBytes";
dictionary["Tx_Drops"] = "connectorStatistics.txDrops";
dictionary["Rx_Drops"] = "connectorStatistics.rxDrops";
dictionary["Tx_Errors"] = "connectorStatistics.txErrors";
dictionary["Rx_Errors"] = "connectorStatistics.rxErrors";
dictionary["Flows"] = "connectorStatistics.flows";


var dictionaryTos = {};
dictionaryTos["Real Time VOIP"] = "46";
dictionaryTos["Real Time Video Streaming"] = "34";
dictionaryTos["Signaling/Control"] = "16";
dictionaryTos["Critical Data"] = "10";
dictionaryTos["Best Effort"] = "0";


function createRuleObject(classModel, ruleScore, ruleName, expression) {
    if (expression == undefined) {
        return;
    }
    
    var connectorEntity = buildConnectorEntity(expression);

    var flowEntity = buildFlowEntity(classModel);

    var json = {
        name: ruleName,
        score: ruleScore,
        whenSettings: [connectorEntity, flowEntity]
    };

    return json;
}

function getOperatorName(operator) {
    //if (operator == "||") {
    //    return "OR";
    //}

    //if (operator == "&&") {
    //    return "AND";
    //}

    if (operator == "==" || operator == "=") {
        return "EQ";
    }

    if (operator == "!=") {
        return "NEQ";
    }

    if (operator == ">") {
        return "GT";
    }

    if (operator == "<=") {
        return "NGT";
    }

    if (operator == "<") {
        return "LT";
    }

    if (operator == ">=") {
        return "NLT";
    }
}

function buildConnectorEntity(expression) {
    
    var tokens = expression.split(" ");
   
    for (var i = 0; i < tokens.length; i++) {
        if (tokens[i] == "AND" || tokens[i] == "OR") {
            operators.push(tokens[i]);
        } else if (tokens[i] != "(" && tokens[i] != ")") {

            var constraint = {
                variable: dictionary[tokens[i]],
                operator: getOperatorName(tokens[i + 1]),
                value: tokens[i + 2]
            }

            i = i + 2;
            
            operands.push(constraint);


        } else if ((tokens[i] == ")")) {            
            var constraint = {
                leftSide: operands.pop(),
                operator: operators.pop(),
                rightSide: operands.pop()
            };
            operands.push(constraint);
        } 
    }

    while (operators.length > 1) {
        var constraint = {
            leftSide: operands.pop(),
            operator: operators.pop(),
            rightSide: operands.pop()
        };
        operands.push(constraint);
    };

    if (operators.length == 1) {
        var connectorConstraint = {
            leftSide: operands.pop(),
            operator: operators.pop(),
            rightSide: operands.pop()
        };

        
        var connectorEntity = {
            entityName: "PSConnector",
            constraint: connectorConstraint
        };

        return connectorEntity;

    } else {
       
        var connectorEntity = {
            entityName: "PSConnector",
            constraint: operands.pop()
        };

        return connectorEntity;
    }   
}

function buildFlowEntity(classModel) {
    var flowConstraintLeft = {
        variable: "connectorOut",
        operator: "EQ",
        value: "$psconnector"
    };

    var flowConstraintRight = {
        variable: "nwTOS",
        operator: "EQ",
        value: dictionaryTos[classModel]
    };

    var flowConstraint = {
        leftSide: flowConstraintLeft,
        operator: "AND",
        rightSide: flowConstraintRight
    };

    var flowEntity = {
        entityName: "PSFlow",
        constraint: flowConstraint
    };

    return flowEntity;
}
