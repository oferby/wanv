var localIpUrl = "/pathselector/routing/set/local/ip";
var gatewayUrl = "/pathselector/routing/set/gateway";
var tunnelUrl = "/pathselector/routing/set/tunnel";
var routelUrl = "/pathselector/routing/set/route";

var content;

function init() {
    $.ajax({
        type: "GET",/*"GET|POST|DELETE|PUT"*/
        url: localIpUrl,
        dataType: "text", /*"text|html|json|jsonp|script|xml"*/
        success: function (resp) {
            handleResponseLocalIp(resp);
        }
    });


    $.ajax({
        type: "GET",/*"GET|POST|DELETE|PUT"*/
        url: gatewayUrl,
        dataType: "text", /*"text|html|json|jsonp|script|xml"*/
        success: function (resp) {
            handleResponseGateway(resp);
        }
    });

    $.ajax({
        type: "GET",/*"GET|POST|DELETE|PUT"*/
        url: tunnelUrl,
        dataType: "text", /*"text|html|json|jsonp|script|xml"*/
        success: function (resp) {
            handleResponseTunnel(resp);
        }
    });

    //$.ajax({
    //    type: "GET",/*"GET|POST|DELETE|PUT"*/
    //    url: routelUrl,
    //    dataType: "text", /*"text|html|json|jsonp|script|xml"*/
    //    success: function (resp) {
    //        handleResponseRoute(resp);
    //    }
    //});
}

function handleResponseLocalIp(resp) {
    var response = JSON.parse(resp);

    for (var k = 0; k < response.length; k++) {
        var entry = {
            id: response[k].id,
            localIpAddress: response[k].ip_address,
            localSubnet: response[k].subnet
        };

        addEntryToTable("Local IP Addresses", entry);
    }
}

function handleResponseGateway(resp) {
    var response = JSON.parse(resp);
    for (var k = 0; k < response.length; k++) {
        var entry = {
            id: response[k].id,
            gwIpAddress: response[k].ip_address
        };
        addEntryToTable("Gateways", entry);
    }
}

function handleResponseTunnel(resp) {
    var response = JSON.parse(resp);
    for (var k = 0; k < response.length; k++) {
        var entry = {
            id: response[k].id,
            localIp: response[k].localIp,
            remoteIp: response[k].remoteIp
        };
        addEntryToTable("Tunnels", entry);
    }
}

function handleResponseRoute(resp) {
    var response = JSON.parse(resp);
    for (var k = 0; k < response.length; k++) {
        var entry = {
            id: response[k].id,
            destIp: response[k].destIp,
            destSubnet: response[k].destSubnet,
            nextHop: response[k].nextHop
        };
        addEntryToTable("Static Routes", entry);
    }
}

function printTable(table) {
  
    $tables = document.getElementById("tables");

    tableName = table.name;
   
    document.write(' <br>');

    $fieldset = document.createElement('fieldset');
    $legend = document.createElement('legend');
    $legend.appendChild(document.createTextNode(table.name));
    $fieldset.appendChild($legend);

    $div = document.createElement("div");
    $div.setAttribute("class", "fieldSetContent");

    $innerTable = table.innerTable;
    if ($innerTable != null) {
        $outerTable = document.createElement("table");
        $tr1 = document.createElement("tr");
        $tr1.setAttribute("style", "background-color: #ffffff;");
        $td1 = document.createElement("td");
    }

    $table = createTable(tableName, table.fields);
    if ($innerTable != null) {
        $td1.appendChild($table);
    } else {
        $div.appendChild($table);
    }

    $form = createForm(tableName, table.fields, table.ids);

    if ($innerTable != null) {
        $td1.appendChild($form);
        $tr1.appendChild($td1);
        $outerTable.appendChild($tr1);

        $tr2 = document.createElement("tr");
        $tr2.setAttribute("style", "background-color: #ffffff;");
        $td2 = document.createElement("td");
        $table2 = createTable(table.innerTable.name, table.innerTable.fields);
        $form2 = createForm(table.innerTable.name, table.innerTable.fields, table.innerTable.ids);
        $td2.appendChild($table2);
        $td2.appendChild($form2);
        $tr2.appendChild($td2);

        $outerTable.appendChild($tr2);

        $div.appendChild($outerTable);
    } else {
        $div.appendChild($form);
    }

    $fieldset.appendChild($div);

    $tables.appendChild($fieldset);
    
 
}


// Function: create table
function createTable(tableName, fields) {
    $table = document.createElement("table");
    $table.setAttribute("id", tableName + "-table");

    $thead = document.createElement("thead");

    for (i = 0; i < fields.length; i++) {
        $th = document.createElement("th");
        $th.appendChild(document.createTextNode(fields[i]));
        $thead.appendChild($th);
    }
    $th = document.createElement("th");
    $th.appendChild(document.createTextNode("Actions"));
    $thead.appendChild($th);

    $table.appendChild($thead);
 
    return $table;
}

// Function: create form
function createForm(tableName, fields, ids) {
    $form = document.createElement("form");
    $form.setAttribute("id", tableName + "-form");

    for (i = 0; i < fields.length; i++) {
        if (fields[i].indexOf("IsEnabled") > -1) {
            $div0 = document.createElement("div");
            $div0.setAttribute("class", "radio");
            $label = document.createElement("label");
            $label.appendChild(document.createTextNode(fields[i] + ":"));
            $div0.appendChild($label);

            $input0 = document.createElement("input");
            $input0.setAttribute("type", "checkbox");
            $input0.setAttribute("name", (ids[i].indexOf("ID") > -1 ? "id" : ids[i]));
            $div0.appendChild($input0);

            $form.appendChild($div0);

        } else {
            $div1 = document.createElement("div");
            $div1.setAttribute("class", "item text");
            $label = document.createElement("label");
            $label.appendChild(document.createTextNode(fields[i] + ":"));
            $div1.appendChild($label);

            $div2 = document.createElement("div");
            $div2.setAttribute("class", "field");

            $input = document.createElement("input");
            $input.setAttribute("type", "text");
            $input.setAttribute("name", (ids[i].indexOf("ID") > -1 ? "id" : ids[i]));
            $input.setAttribute("id", tableName + "-form-" + ids[i]);
            $div2.appendChild($input);
            $div1.appendChild($div2);

            if (fields[i].indexOf("ID") > -1) {
                $input.setAttribute("type", "hidden");
                $label.style.display = 'none';
            }


            $form.appendChild($div1);
        }
    }

    $input = document.createElement("input");
    $input.setAttribute("type", "hidden");
    $input.setAttribute("name", "siteName");
    $input.setAttribute("value", "uknown");
    $form.appendChild($input);

    $div3 = document.createElement("div");
    $div3.setAttribute("class", "button-wrapper");

    $div4 = document.createElement("div");
    $div4.setAttribute("class", "item button");
    $div5 = document.createElement("div");
    $div5.setAttribute("class", "field");
    $input1 = document.createElement("input");
    $input1.setAttribute("type", "button");
    $input1.setAttribute("id", tableName + "-op-discard");
    $input1.setAttribute("value", "Discard");
    $input1.setAttribute("onclick", "discardClicked(this)");
    $div5.appendChild($input1);
    $div4.appendChild($div5);
    $div3.appendChild($div4);


   

    $div4 = document.createElement("div");
    $div4.setAttribute("class", "item button button-default");
    $div5 = document.createElement("div");
    $div5.setAttribute("class", "field");
    $input1 = document.createElement("input");
    $input1.setAttribute("type", "submit");
    $input1.setAttribute("id", tableName + "-op-save");
    $input1.setAttribute("value", "Save");
    $input1.setAttribute("onclick", "saveClicked(this)");

    $div5.appendChild($input1);
    $div4.appendChild($div5);
    $div3.appendChild($div4);

    $form.appendChild($div3);

    return $form;
}



function addEntryToTable(tableName, entry) {
    var uniqueKey = "";
    for (key in entry) {
        uniqueKey += entry[key] + " ";       
        if (key != "id" && entry[key].toString() == "") {
            return;
        }        
    }
   
    if (uniqueKey.trim() == "") {
        return;
    }    

    var $tableRef = document.getElementById(tableName + "-table");
    var $tr = document.getElementById(tableName + "entry-" + uniqueKey);
   
    if ($tr == undefined) {
        var $tr = document.createElement("tr"), $td, key;
        for (key in entry) {
            if (key != "site") {
                if (entry.hasOwnProperty(key)) {
                    $td = document.createElement("td");
                    $td.setAttribute("id", tableName + "-" + key);
                    $td.appendChild(document.createTextNode(entry[key]));
                    $tr.appendChild($td);              
                }
            }
        }


        var $td = document.createElement("td");
        $td.innerHTML = '<a data-op="edit" data-id="' + tableName + "-" + uniqueKey + '">Edit</a> | <a data-op="remove" data-id="' + tableName + "-" + uniqueKey + '">Remove</a>';
        $tr.appendChild($td);
        $tr.setAttribute("id", tableName + "entry-" + uniqueKey);
      

        $tr.addEventListener("click", function (event) {
            var op = event.target.getAttribute("data-op");
             var mainSplit = event.target.getAttribute("data-id").split("-");
             var tableName1 = mainSplit[0];
             var res = mainSplit[1].split(" ");
             
             if (/edit|remove/.test(op)) {
                 if (op == "edit") {
                     if (tableName1 == "Local IP Addresses") {
                         (document.getElementById(tableName1 + "-form-" + "id")).value = res[0];
                         (document.getElementById(tableName1 + "-form-" + "localIpAddress")).value = res[1];
                         (document.getElementById(tableName1 + "-form-" + "localSubnet")).value = res[2];
                     } else if (tableName1 == "Gateways") {
                         (document.getElementById(tableName1 + "-form-" + "id")).value = res[0];
                         (document.getElementById(tableName1 + "-form-" + "gwIpAddress")).value = res[1];
                     } else if (tableName1 == "Tunnels") {
                         (document.getElementById(tableName1 + "-form-" + "id")).value = res[0];
                         (document.getElementById(tableName1 + "-form-" + "localIp")).value = res[1];
                         (document.getElementById(tableName1 + "-form-" + "remoteIp")).value = res[2];
                     } else if (tableName1 == "Static Routes") {
                         (document.getElementById(tableName1 + "-form-" + "id")).value = res[0];
                         (document.getElementById(tableName1 + "-form-" + "destinationIpAddress")).value = res[1];
                         (document.getElementById(tableName1 + "-form-" + "destinationSubnet")).value = res[2];
                         (document.getElementById(tableName1 + "-form-" + "nextHop")).value = res[3];
                     }
                     sendDelete(tableName1, res);
                     this.parentNode.removeChild(this);

                 } else if (op == "remove") {
                     if (confirm('Are you sure you want to remove this raw?')) {
                         sendDelete(tableName1, res);
                         this.parentNode.removeChild(this);
                     }
                 }
             }

             event.preventDefault();
            

        }, true);
        $tableRef.appendChild($tr);

        document.getElementById(tableName + "-form").reset();
    } else {
        alert("Raw already exist");
    }
}

function sendDelete(tableName1, res) {

    var text = res[0];

    if (tableName1 == "Local IP Addresses") {
        sendDeleteToServer(localIpUrl, text);
    } else if (tableName1 == "Gateways") {
        sendDeleteToServer(gatewayUrl, text);
    } else if (tableName1 == "Tunnels") {
        sendDeleteToServer(tunnelUrl, text);
    } else if (tableName1 == "Static Routes") {
        sendDeleteToServer(routelUrl, text);
    }
}

function discardClicked(elmnt) {
    var res = elmnt.id.split("-");
    var tableName = res[0];
    var $form = document.getElementById(tableName + "-form");
    $form.reset();
}

function saveClicked(elmnt) {
    alert("a");
    var res = elmnt.id.split("-");
    var tableName = res[0];
   
    if (tableName == "Local IP Addresses") {

        var entry = {
            id: document.getElementById(tableName + "-form-" + "id").value,
            localIpAddress: document.getElementById(tableName + "-form-" + "localIpAddress").value,
            localSubnet: document.getElementById(tableName + "-form-" + "localSubnet").value,
        };
        //addEntryToTable(tableName, entry);
       
        var text = "localIp=" + entry.localIpAddress + "&localSubnet=" + entry.localSubnet;
        sendPostToServer(localIpUrl, text);

    } else if (tableName == "Gateways") {

        var entry = {
            id: document.getElementById(tableName + "-form-" + "id").value,
            gwIpAddress: document.getElementById(tableName + "-form-" + "gwIpAddress").value
        };
        //addEntryToTable(tableName, entry);

        var text = "ip=" + entry.gwIpAddress;
        sendPostToServer(gatewayUrl, text);


    } else if (tableName == "Tunnels") {

        var entry = {
            id: document.getElementById(tableName + "-form-" + "id").value,
            localIp: document.getElementById(tableName + "-form-" + "localIp").value,
            remoteIp: document.getElementById(tableName + "-form-" + "remoteIp").value

        };
        //addEntryToTable(tableName, entry);

        var text = "tunnelId=" + entry.id + "&localIp=" + entry.localIp + "&remoteIp=" + entry.remoteIp;
        sendPostToServer(tunnelUrl, text);
    } else if (tableName == "Static Routes") {

        var entry = {
            id: document.getElementById(tableName + "-form-" + "id").value,
            destinationIpAddress: document.getElementById(tableName + "-form-" + "destinationIpAddress").value,
            destinationSubnet: document.getElementById(tableName + "-form-" + "destinationSubnet").value,
            nextHop: document.getElementById(tableName + "-form-" + "nextHop").value
        };
        //addEntryToTable(tableName, entry);

        var text = "localIp=" + entry.localIp + "&destinationIpAddress=" + entry.destinationIpAddress + "&destinationSubnet=" + entry.destinationSubnet + "&nextHop=" + entry.nextHop;
        sendPostToServer(routelUrl, text);
    }
  
}



function sendPostToServer(url, text) {
    $.ajax({
        type: "POST",
        dataType: "text",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        url: url,
        data: text,
        success: function (data) {
            location.reload();
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (xhr.status != 200) {
                printErrorPolicyPage("ERROR: " + xhr.status + " " + thrownError);
            }

        }
    });
}

function sendDeleteToServer(url, text) {

    $.ajax({
        type: "POST",
        dataType: "text",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        url: url + "/delete/" + text,
        success: function (data) {
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (xhr.status != 200) {
                printErrorPolicyPage("ERROR: " + xhr.status + " " + thrownError);
            }

        }
    });
}




