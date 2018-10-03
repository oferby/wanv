var classModelsColumns = ["Class Model", "TOS", "DSCP", "PHB", "Queue Type"];
var policiesColumns = ["Class Model", "Constraints", "Penalty Score", "Actions"];
var constraintColumns = ["Open Bracket", "Metric Name", "Comparison Operator", "Threshold", "Closet Bracket", "Boolean Operator"];
var mapClassIdToClassName = {};
var mapClassNameToClassId = {};
var classModels = [];
classModels.push("Class Model");

function printErrorPolicyPage(error) {

    element = document.getElementById("errorMessage");

    if (element.childNodes.length == 0) {
        error = error.fontcolor("red");
        var h3 = document.createElement("h3");
        h3.setAttribute("id", "errorMessageH3");
        h3.innerHTML = error;
        element.appendChild(h3);
    } else {
        element.childNodes[0].nodeValue = error;
    }

    var refresher = setInterval(function () {
        $(document).ready(function () {
            element.removeChild(document.getElementById("errorMessageH3"));
        });
    }, 5000);

}

function init() {

    //$.ajax({
    //    type: "GET",
    //    dataType: "text",
    //    contentType: "application/json; charset=utf-8",
    //    url: "/pathselector/policies/classModels",
    //    data: JSON.stringify(myData),
    //    success: function (data) {
    //        var response = JSON.parse(data);
    //        var classModels = response.connectorList;
    //        for (var k = 0; k < classModels.length; k++) {
    //        }
    //    },
    //    error: function (xhr, ajaxOptions, thrownError) {
    //        printErrorPolicyPage("ERROR: " + xhr.status + " " + thrownError);

    //    }
    //});
}

function buildTables() {

    var container = document.getElementById('container');

    $fieldset = document.createElement('fieldset');
    $legend = document.createElement('legend');
    $legend.setAttribute("id", "legend");
    $legend.appendChild(document.createTextNode("Class Models"));
    $fieldset.appendChild($legend);


    var table = document.createElement("table");
    table.setAttribute("id", "classModels-table");
    $thead = document.createElement("thead");
    for (var key in classModelsColumns) {
        $th = document.createElement("th");
        $th.appendChild(document.createTextNode(classModelsColumns[key]));
        $thead.appendChild($th);
    }
    table.appendChild($thead);
    $fieldset.appendChild(table);

    $br = document.createElement('br');
    $fieldset.appendChild($br);

    container.appendChild($fieldset);

    $br = document.createElement('br');
    container.appendChild($br);

    var $fieldset = document.createElement('fieldset');
    $fieldset.setAttribute("id", "constraintFieldSet");

    $legend = document.createElement('legend');
    $legend.setAttribute("id", "legend");
    $legend.appendChild(document.createTextNode("Policies"));
    $fieldset.appendChild($legend);


    var table = document.createElement("table");
    table.setAttribute("id", "policies-table");
    $thead = document.createElement("thead");
    for (var key in policiesColumns) {
        $th = document.createElement("th");
        $th.appendChild(document.createTextNode(policiesColumns[key]));
        $thead.appendChild($th);
    }
    table.appendChild($thead);
    $fieldset.appendChild(table);

    var $br = document.createElement('br');
    $fieldset.appendChild($br);

    var $form = document.createElement("form");
    $form.setAttribute("id", "policies-table-form");

    $fieldset.appendChild($form);
    container.appendChild($fieldset);

}

function updateClassModelTable(row, rowKey, rowId) {
    var tableName = "classModels-table";
    var $tr = document.createElement("tr"), $td, key;
    for (var key in row) {
        if (row.hasOwnProperty(key)) {
            $td = document.createElement("td");
            $td.appendChild(document.createTextNode(row[key]));
            $td.setAttribute("id", tableName + rowKey + key);
            $tr.appendChild($td);
        }
    }
    table = document.getElementById(tableName);
    table.appendChild($tr);

    mapClassIdToClassName[rowId] = rowKey;
    mapClassNameToClassId[rowKey] = rowId;
    classModels.push(rowKey);
};


function updatePoliciesTable(policy) {

    var tableName = "policies-table";
    var table = document.getElementById(tableName);

    var lastTr = document.getElementById("entry-" + policy.classModel);
    if (lastTr != null) {
        var expressionTd = document.getElementById(tableName + policy.classModel + "constraints");
        expressionTd.innerHTML = policy.constraints;

        var scoreTd = document.getElementById(tableName + policy.classModel + "score");
        scoreTd.innerHTML = policy.score;

        return;
    }

    var $tr = document.createElement("tr"), $td, key;
    $tr.setAttribute("id", "entry-" + policy.classModel);
    for (var key in policy) {
        if (policy.hasOwnProperty(key)) {
            $td = document.createElement("td");
            $td.appendChild(document.createTextNode(policy[key]));
            $td.setAttribute("id", tableName + policy.classModel + key);
            $tr.appendChild($td);
        }
    }
    $td = document.createElement("td");
    $td.innerHTML = '<a data-op="edit" data-id="' + policy.classModel + '">Edit</a> | <a data-op="remove" data-id="' + policy.classModel + '">Remove</a>';



    $td.addEventListener("click", function (event) {
        var op = event.target.getAttribute("data-op");

        if (/edit|remove/.test(op)) {

            var dataId = event.target.getAttribute("data-id");

            if (op == "edit") {

                // remove the last form
                var constraintFieldSet = document.getElementById("constraintFieldSet");
                constraintFieldSet.removeChild(document.getElementById("policies-table-form"));
                var $form = document.createElement("form");
                $form.setAttribute("id", "policies-table-form");
                constraintFieldSet.appendChild($form);

                // build new one
                buildPoliciesForm();

                // set class model and score
                var tr = document.getElementById("entry-" + dataId);
                var classModel = tr.cells[0].firstChild.nodeValue;
                var expression = tr.cells[1].firstChild.nodeValue;
                var score = tr.cells[2].firstChild.nodeValue;

                document.getElementById("Class Model" + 0).value = classModel;
                document.getElementById("Penalty Score" + 0).value = score;

                // set expression
                var table = document.getElementById("constraint-table");
                var tokens = expression.split(" ");

                var lineNumber = 1;
                for (var i = 0; i < tokens.length; i++) {

                    do {
                        if (tokens[i] == "(") {
                            document.getElementById("Open Bracket" + lineNumber).value = tokens[i];
                            i++;
                        } else {
                            document.getElementById("Open Bracket" + lineNumber).value = "None";
                        }

                        if (tokens[i] == "(") {
                            lineNumber++;
                            var tr = createConstraintRow(lineNumber);
                            table.appendChild(tr);
                        }
                    } while (tokens[i] == "(");

                    if (tokens[i] == "Frame_Delay" || tokens[i] == "Jitter" || tokens[i] == "Packet_Loss_Rate" || tokens[i] == "Tx_Packtes" || tokens[i] == "Rx_Packtes" || tokens[i] == "Tx_Bytes" || tokens[i] == "Rx_Bytes" || tokens[i] == "Tx_Drops" || tokens[i] == "Rx_Drops" || tokens[i] == "Tx_Errors" || tokens[i] == "Rx_Errors" || tokens[i] == "Flows") {
                        document.getElementById("Metric Name" + lineNumber).value = tokens[i];
                        i++;
                    }

                    if (tokens[i] == ">" || tokens[i] == "<" || tokens[i] == "=" || tokens[i] == "!=" || tokens[i] == "<=" || tokens[i] == ">=") {
                        document.getElementById("Comparison Operator" + lineNumber).value = tokens[i];
                        i++;
                    }

                    if (isNumber(tokens[i])) {
                        document.getElementById("Threshold" + lineNumber).value = tokens[i];
                        i++;
                    }

                    do {
                        if (tokens[i] == ")") {
                            document.getElementById("Closet Bracket" + lineNumber).value = tokens[i];
                            i++;
                        } else {
                            document.getElementById("Closet Bracket" + lineNumber).value = "None";
                        }

                        if (tokens[i] == ")") {
                            lineNumber++;
                            var tr = createConstraintRow(lineNumber);
                            table.appendChild(tr);
                        }

                    } while (tokens[i] == ")");

                    do {
                        if (tokens[i] == "AND" || tokens[i] == "OR") {
                            document.getElementById("Boolean Operator" + lineNumber).value = tokens[i];
                            i++;
                        } else {
                            document.getElementById("Boolean Operator" + lineNumber).value = "None";
                        }

                        if (tokens[i] == "AND" || tokens[i] == "OR") {
                            lineNumber++;
                            var tr = createConstraintRow(lineNumber);
                            table.appendChild(tr);
                        }
                    } while (tokens[i] == "AND" || tokens[i] == "OR");

                    i--;

                    if (i + 1 < tokens.length) {
                        lineNumber++;
                        var tr = createConstraintRow(lineNumber);
                        table.appendChild(tr);
                    }
                }


            }
            else if (op == "remove") {
                if (confirm('Are you sure you want to remove "' + dataId + '?')) {
                    var table = document.getElementById("policies-table");
                    table.removeChild(document.getElementById("entry-" + dataId));
                    sendConstraintsToServer();
                }
            }

            tamingselect();

            event.preventDefault();
        }
    }, true);

    $tr.appendChild($td);
    table.appendChild($tr);
};

function buildPoliciesForm() {
    var form = document.getElementById("policies-table-form");
    var br = document.createElement('br');
    form.appendChild(br);
    form.appendChild(br);

    var table = document.createElement("table");
    table.setAttribute("id", "constraint-table");
    table.setAttribute("class", "constraintTable")
    var tr = document.createElement("tr");
    tr.setAttribute("class", "constraintTable")
    tr.setAttribute("id", "firstRow");

    addSelectToTable(classModels, tr, 0);
    var options = ["Penalty Score", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "DROP"];
    addSelectToTable(options, tr, 0);
    table.appendChild(tr);

    var tr = createConstraintRow(1);
    table.appendChild(tr);

    form.appendChild(table);

    $br = document.createElement('br');
    form.appendChild($br);

    $div3 = document.createElement("div");
    $div3.setAttribute("class", "button-wrapper");
    form.appendChild(br);

    $div4 = document.createElement("div");
    $div4.setAttribute("class", "item button button-default");
    $div5 = document.createElement("div");
    $div5.setAttribute("class", "field");
    var $input1 = document.createElement("input");
    $input1.setAttribute("type", "button");
    $input1.setAttribute("id", "save");
    $input1.setAttribute("value", "Save");
    $input1.setAttribute("onclick", "addConstraint()");

    $div5.appendChild($input1);


    var $input1 = document.createElement("input");
    $input1.setAttribute("type", "button");
    $input1.setAttribute("id", "save");
    $input1.setAttribute("value", "Restart CBR");
    $input1.setAttribute("onclick", "restartCBR()");

    $div5.appendChild($input1);

    $div4.appendChild($div5);
    $div3.appendChild($div4);


    form.appendChild($div3);

}


function createSelect(options) {
    var select = document.createElement("SELECT");
    select.setAttribute("class", "turnintodropdown");
    var index;
    for (index in options) {
        var z = document.createElement("option");
        z.setAttribute("value", options[index]);
        var t = document.createTextNode(options[index]);
        z.appendChild(t);
        select.appendChild(z);
    }
    return select;

    //var div = document.createElement("div");
    //div.setAttribute("class", "wrapper-dropdown-3");

    //var ul = document.createElement("ul");
    //ul.setAttribute("class", "dropdown");

    //var index;
    //for (index in options) {
    //    ul.appendChild('<li><a href="#"><i class="icon-envelope icon-large"></i>' + options[index] + '</a></li>');
    //}

    //div.appendChild(ul);

    //return div;
}

function addSelectToTable(options, tr, index) {
    var select = createSelect(options);
    select.setAttribute("id", options[0] + index);
    select.selectedIndex = 0;
    var p = document.createElement("p");
    p.appendChild(select);

    var td = document.createElement("td");
    td.appendChild(p);
    tr.appendChild(td);
}

function createConstraintRow(index) {
    var tr = document.createElement("tr");
    tr.setAttribute("class", "constraintTable")
    tr.setAttribute("id", "constraintTable" + index);

    var options = ["Open Bracket", "None", "("];
    addSelectToTable(options, tr, index);
                         
    var options = ["Metric Name", "Frame_Delay", "Jitter", "Packet_Loss_Rate", "Tx_Packtes", "Rx_Packets", "Tx_Bytes", "Rx_Bytes", "Tx_Drops", "Rx_Drops", "Tx_Errors", "Rx_Errors", "Flows"];
    addSelectToTable(options, tr, index);

    var options = ["Comparison Operator", ">", "<", "=", "!=", "<=", ">="];
    addSelectToTable(options, tr, index);

    var $input = document.createElement("input");
    $input.setAttribute("type", "text");
    $input.setAttribute("class", "textbox");
    $input.setAttribute("id", "Threshold" + index);
    $input.setAttribute("value", "Threshold");

    var td = document.createElement("td");
    td.appendChild($input);
    tr.appendChild(td);

    var options = ["Closet Bracket", "None", ")"];
    addSelectToTable(options, tr, index);

    var options = ["Boolean Operator", "None", "AND", "OR"];
    addSelectToTable(options, tr, index);

    return tr;
}

function onChangeSelection(id, newValue, index) {
    if (id.indexOf("Boolean Operator") >= 0) {
        if (newValue != "None") {
            var table = document.getElementById("constraint-table");
            if (id.indexOf(table.rows.length - 1) > 0) {
                var tr = createConstraintRow(table.rows.length);
                table.appendChild(tr);
                tamingselect();
            }
        }
    }

}

function addConstraint() {

    var table = document.getElementById("constraint-table");
    var numOfRows = table.rows.length;
    
    var tempClassModel = document.getElementById("Class Model0").value;
    var score = document.getElementById("Penalty Score0").value;
   
    var phrase = "";
    for (var i = 1; i < numOfRows; i++) {

        var element = document.getElementById("Open Bracket" + i).value;
        if (element != ""  && element == "(") {
            phrase = (phrase == "") ? element : (phrase + " " + element);
        }
        
        var element = document.getElementById("Metric Name" + i).value;
        if (element != "" && element != "Metric Name") {
            phrase = (phrase == "") ? element : (phrase + " " + element);
        }

        var element = document.getElementById("Comparison Operator" + i).value;
        if (element != "" && element != "Comparison Operator") {
            phrase = (phrase == "") ? element : (phrase + " " + element);
        }

        var element = document.getElementById("Threshold" + i).value;
        if (element != "" && element != "Threshold") {
            if (!isNumber(element)) {
                printErrorPolicyPage("ERROR: Threshold must be a number");
                return;
            }
            phrase = (phrase == "") ? element : (phrase + " " + element);
        }


        var element = document.getElementById("Closet Bracket" + i).value;
        if (element != "" && element == ")") {
            phrase = (phrase == "") ? element : (phrase + " " + element);
        }

        var element = document.getElementById("Boolean Operator" + i).value;
        if (element != "" && element != "None" && element != "Boolean Operator") {
            phrase = (phrase == "") ? element : (phrase + " " + element);
        }

    }

    if (phrase != "" && score != "Penalty Score" && tempClassModel != "ClassModel") {
        var counter = 0;
        for (var i = 0; i < phrase.length; i++) {
            if (phrase[i] == "(") {
                counter = counter + 1;
            } else if (phrase[i] == ")") {
                counter = counter - 1;
            }
        }

        if (counter != 0) {
            printErrorPolicyPage("ERROR: syntax error - brackets");
            return;
        }

        //if (!isValid(phrase)) {
        //    printErrorPolicyPage("ERROR: invalid expression");
        //    return;
        //}

        var policy =
           {
               classModel: tempClassModel,
               constraints: phrase,
               score: score,
           };

        updatePoliciesTable(policy);

        sendConstraintsToServer();

    } else {
        printErrorPolicyPage("ERROR: missing value");
    }

}

function isValid(expression) {
    var patt = new RegExp("[( ]*([(]? (Frame Delay|Jitter|Packet Loss Rate|Tx Packtes|Rx Packets|Tx Bytes|Rx Bytes|Tx Drops|Rx Drops|Tx Errors|Rx Errors|Flows) (>|<|=|!=|<=|>=) [0-9]+ ?[)]?( AND |OR )?)+[) ]*");
    return expression + " " + patt.test(expression);
}

function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}

// TODO: REMOVE IT WHEN THE REST WILL SUPPORT IT
function initClassModelTable() {
        var row1 =
          {
              className: "Real Time VOIP",
              tos: 184,
              dscp: 46,
              phb: "EF",
              queueType: "Priority queue"
          };

        updateClassModelTable(row1, row1.className, 1);

        var row2 =
       {
           className: "Real Time Video Streaming",
           tos: 160,
           dscp: 34,
           phb: "AF41",
           queueType: "Priority queue"
       };
        updateClassModelTable(row2, row2.className, 2);

        var row3 =
      {
          className: "Signaling/Control",
          tos: 64,
          dscp: 16,
          phb: "CS2",
          queueType: "Bandwidth queue"
      };
        updateClassModelTable(row3, row3.className, 3);


        var row4 =
    {
        className: "Critical Data",
        tos: 12,
        dscp: 10,
        phb: "AF1",
        queueType: "Bandwidth queue"
    };
        updateClassModelTable(row4, row4.className, 4);

        var row5 =
    {
        className: "Best Effort",
        tos: 0,
        dscp: 0,
        phb: "BE",
        queueType: "Default queue"
    };
        updateClassModelTable(row5, row5.className, 5);

}

// TODO: REMOVE IT WHEN THE REST WILL SUPPORT IT
function initPolicies() {
    var policy =
       {
           classModel: "Real Time VOIP",
           constraints: "Frame_Delay >= 150 OR Jitter >= 30 OR Packet_Loss_Rate >= 1",
           score: 5,
       };

    updatePoliciesTable(policy);

    var policy =
   {
       classModel: "Real Time Video Streaming",
       constraints: "Frame_Delay >= 4000 OR Packet_Loss_Rate >= 5",
       score: 10,
   };

    updatePoliciesTable(policy);

    var policy =
  {
      classModel: "Signaling/Control",
      constraints: "Packet_Loss_Rate >= 5",
      score: 10,
  };

    updatePoliciesTable(policy);


    var policy =
{
    classModel: "Critical Data",
    constraints: "Frame_Delay >= 5000 Packet_Loss_Rate >= 5",
    score: 1,
};

    updatePoliciesTable(policy);
}

function sendConstraintsToServer() {

    var rules = [];

    var table = document.getElementById("policies-table");

    for (var i = 0, row; row = table.rows[i]; i++) {

        var classModel = row.cells[0].firstChild.nodeValue;
        var expression = row.cells[1].firstChild.nodeValue;
        var score = -1 * (row.cells[2].firstChild.nodeValue == "DROP" ? 20 : row.cells[2].firstChild.nodeValue);
       
        var constraint = createRuleObject(classModel, score, classModel, expression);
        
        rules.push(constraint);
    }

    var json = JSON.stringify(rules).replace(/\\/g, ' ');
    alert(json);
    //printErrorPolicyPage(json);

    $.ajax({
        type: "POST",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        url: "/pathselector/constraint/data/rule",
        data: json,
        success: function (data) {
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (xhr.status != 200) {
                printErrorPolicyPage("ERROR: " + xhr.status + " " + thrownError);
            }

        }
    });
}

function restartCBR() {
    $.ajax({
        type: "GET",
        url: "/pathselector/constraint/data/restart",
        success: function (data) {
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (xhr.status != 200) {
                printErrorPolicyPage("ERROR: " + xhr.status + " " + thrownError);
            }

        }
    });
}
