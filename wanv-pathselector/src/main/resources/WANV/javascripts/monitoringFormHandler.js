var statsUrl = "/pathselector/stats";
var tunnelUrl = "/pathselector/flow/count/tunnel"
var gatewayUrl = "/pathselector/flow/count/gateway";
//var statsUrl = "http://172.16.150.21:8080/pathselector/stats";

var isFirstLoad = true;

var jsonPortFields = ["linkId", "type", "", "txPackets", "rxPackets", "txBytes", "rxBytes", "txDrops", "rxDrops", "txErrors", "rxErrors", "flows", "frameDelay", "frameDelayVariation", "packetLossRate"];
var portTableColumns = ["Link ID", "Link Type", "Link Status", "Tx Packets", "Rx Packets", "Tx Bytes", "Rx Bytes", "Tx Drops", "Rx Drops", "Tx Errors", "Rx Errors", "Flow Count", "Frame Delay", "Jitter", "Packet Loss Rate"];
var portMetricToDisplayAsHistogram = ["Tx Packets", "Rx Packets", "Tx Bytes", "Rx Bytes", "Tx Drops", "Rx Drops", "Tx Errors", "Rx Errors", "Flow Count"];

var refreshRate = 1000;
var canvasWidth = 1100;
var canvasHeight = 200;

function printError(error) {

    element = document.getElementById("errorMessage");

    if (element.childNodes.length == 0) {
        element.appendChild(document.createTextNode(error));
    } else {
        element.childNodes[0].nodeValue = error;
    }

    var refresher = setInterval(function () {
        $(document).ready(function () {
            element.childNodes[0].nodeValue = "";
        });
    }, 2000);

};

function buildTables() {

    var container = document.getElementById('container');

    $fieldset = document.createElement('fieldset');
    $legend = document.createElement('legend');
    $legend.setAttribute("id", "legend");
    $fieldset.appendChild($legend);

    $portTable = document.createElement("table");
    $portTable.setAttribute("id", "port-table");
    $thead = document.createElement("thead");
    for (var key in portTableColumns) {
        $th = document.createElement("th");
        $th.appendChild(document.createTextNode(portTableColumns[key]));
        $thead.appendChild($th);
    }
    $portTable.appendChild($thead);
    $fieldset.appendChild($portTable);

    $br = document.createElement('br');
    $fieldset.appendChild($br);

    $portCanvas = document.createElement("canvas");
    $portCanvas.setAttribute("id", "port-histogram");
    $portCanvas.setAttribute("width", canvasWidth);
    $portCanvas.setAttribute("height", canvasHeight);

    $fieldset.appendChild($portCanvas);

    $br = document.createElement('br');
    $fieldset.appendChild($br);

    //$queueTable = document.createElement("table");
    //$queueTable.setAttribute("id", "queue-table");
    //$thead = document.createElement("thead");
    //for (var key in queueTableColumns) {
    //    $th = document.createElement("th");
    //    $th.appendChild(document.createTextNode(queueTableColumns[key]));
    //    $thead.appendChild($th);
    //}
    //$queueTable.appendChild($thead);
    //$fieldset.appendChild($queueTable);

    //$br = document.createElement('br');
    //$fieldset.appendChild($br);

    //$queueCanvas = document.createElement("canvas");
    //$queueCanvas.setAttribute("id", "queue-histogram");
    //$queueCanvas.setAttribute("width", canvasWidth);
    //$queueCanvas.setAttribute("height", canvasHeight);

    //$fieldset.appendChild($queueCanvas);

    container.appendChild($fieldset);

    $br = document.createElement('br');
    container.appendChild($br);


    // hidden image
    $cloudImage = document.createElement("img");
    $cloudImage.setAttribute("id", "img-cloud");
    $cloudImage.setAttribute("src", "images/cloud.png");
    $cloudImage.setAttribute("style", "display:none");
    container.appendChild($cloudImage);

    // hidden image
    $siteImage = document.createElement("img");
    $siteImage.setAttribute("id", "img-site");
    $siteImage.setAttribute("src", "images/site.jpg");
    $siteImage.setAttribute("style", "display:none");
    container.appendChild($siteImage);
};

function resetFirstTime(portStatistics) {
    if (isFirstLoad) {
        isFirstLoad = false;
        for (var k = 0; k < portStatistics.length; k++) {
            if (portStatistics[k]["type"] == "GRE") {

                var myData = {
                    "linkId": portStatistics[k]["id"],
                    "delay": 0,
                    "jitter": 0,
                    "packetLossRate": 0
                };


                $.ajax({
                    type: "POST",
                    dataType: "text",
                    contentType: "application/json; charset=utf-8",
                    url: "/wanvRest/pathselector/demo/changeQdiscParams",
                    data: JSON.stringify(myData),
                    success: function (data) {
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        //alert("ERROR: " + xhr.status + " " + thrownError);
                        printError("ERROR: " + xhr.status + " " + thrownError);

                    }
                });
            }
        }
    }
};

function refreshTables() {

    /*
     // do not know if we actually need this block. Yuli
     $.ajax({
     type: "GET",
     url: statsUrl,
     dataType: "text", // "text|html|json|jsonp|script|xml"
     success: function (resp) {

     var response = JSON.parse(resp);
     var portStatistics = response.connectorList;
     for (var k = 0; k < portStatistics.length; k++) {
     if (portStatistics[k]["type"] == "GRE") {
     //updateDemoTable(portStatistics[k]["connectorStatistics"]["linkId"]);
     updateDemoTable(portStatistics[k]["id"]);
     }
     }
     }
     });
     */

    var refresher = setInterval(function () {

        $(document).ready(function () {
            $.ajax({
                type: "GET",/*"GET|POST|DELETE|PUT"*/
                url: statsUrl,
                dataType: "text", /*"text|html|json|jsonp|script|xml"*/
                success: function (resp) {

                    var response = JSON.parse(resp);

                    $legend = document.getElementById("legend");
                    if ($legend.innerText == "") {
                        $legend.innerText = response.siteName;
                    }

                    var portStatistics = response.connectorList;

                    resetFirstTime(portStatistics);

                    for (var k = 0; k < portStatistics.length; k++) {
                        //if (portStatistics[k]["type"] == "GRE") {
                        var row = {};
                        for (var index in portTableColumns) {
                            if (portTableColumns[index] == "Link Status") {
                                row[portTableColumns[index]] = portStatistics[k]["active"];
                            } else {
                                if (portTableColumns[index] == "Link ID") { // TODO: REMOVE
                                    row[portTableColumns[index]] = portStatistics[k]["id"];
                                } else if (portTableColumns[index] == "Link Type") {
                                    row[portTableColumns[index]] = portStatistics[k]["type"];
                                } else if (portTableColumns[index] == "Packet Loss Rate" && portStatistics[k]["active"] == false) {  // TODO: REMOVE
                                    row[portTableColumns[index]] = 100;
                                } else {
                                    row[portTableColumns[index]] = portStatistics[k]["connectorStatistics"][jsonPortFields[index]];
                                    if (portTableColumns[index] == "Frame Delay" || portTableColumns[index] == "Jitter" || portTableColumns[index] == "Packet Loss Rate") {
                                        row[portTableColumns[index]] = Math.round(row[portTableColumns[index]] * 100) / 100;
                                    }
                                }
                            }
                        }

                        updateTable("port-table", row);
                        //}
                    }

                    portCanvas = document.getElementById("port-histogram");
                    if (portCanvas && portCanvas.getContext) {
                        var context = portCanvas.getContext('2d');
                        context.clearRect(0, 0, canvasWidth, canvasHeight);

                        var statistics = new Array();

                        for (var k = 0; k < portStatistics.length; k++) {
                            //if (portStatistics[k]["type"] == "GRE") {

                            var portStats = {};
                            //portStats["Link ID"] = portStatistics[k]["connectorStatistics"]["linkId"]; // TODO: remove
                            portStats["Link ID"] = portStatistics[k]["id"];
                            portStats["Link Type"] = portStatistics[k]["type"];
                            portStats["Link Status"] = portStatistics[k]["active"];
                            portStats["SRC IP Address"] = portStatistics[k]["localIPAddress"];
                            if (portStatistics[k]["destinationAddress"] != null)
                            {
                                portStats["DST IP Address"] = portStatistics[k]["destinationAddress"];
                            } else {
                                portStats["DST IP Address"] = " ";
                            }
                            statistics.push(portStats);
                            //}
                        }

                        drawSite(context, response.siteName, statistics);
                        updatePortHistogram(context, "port-table", portStatistics);
                        displayDonutsNow();
                        displayGraph();
                    }


                },
                error: function (xhr, ajaxOptions, thrownError) {
                    printError( "ERROR: " + xhr.status + " " + thrownError);

                }
            });
        });

    }, refreshRate);
};


function updateTable(tableName, row) {

    if (row["Link ID"] == null) {
        return;
    }

    var rowKey = "entry-" + row["Link ID"] + (tableName.indexOf("queue") > -1 ? row["Queue ID"] : "");
    lastRow = document.getElementById(tableName + rowKey);

    if (lastRow == null) { // INSERT NEW ROW
        var $tr = document.createElement("tr"), $td, key;
        for (var key in row) {
            if (row.hasOwnProperty(key)) {
                $td = document.createElement("td");
                if (key == "Link Status") {
                    var elem = document.createElement("img");
                    elem.setAttribute("id", tableName + rowKey + key + "upDownImg");
                    elem.setAttribute("src", row["Link Status"] == 1 ? "images/up.png" : "images/down.png");
                    $td.appendChild(elem);
                } else {
                    $td.appendChild(document.createTextNode(row[key]));
                }
                $td.setAttribute("id", tableName + rowKey + key);
                $tr.appendChild($td);
            }
        }

        $tr.setAttribute("id", tableName + rowKey);

        table = document.getElementById(tableName);
        table.appendChild($tr);

    } else { // UPDATE ROW
        for (var key in row) {
            if (row.hasOwnProperty(key)) {
                $td = document.getElementById(tableName + rowKey + key);
                if (key == "Link Status") {
                    var elem = document.getElementById(tableName + rowKey + key + "upDownImg");
                    elem.setAttribute("src", row["Link Status"] == 1 ? "images/up.png" : "images/down.png");
                } else {
                    $td.innerHTML = row[key];
                }
            }
        }
    }
};

function updatePortHistogram(context, tableName, portStatistics) {

    for (var x = 0; x < portMetricToDisplayAsHistogram.length; x++) {

        metricName = portMetricToDisplayAsHistogram[x];
        var data = new Array();
        var index = 0;
        for (var k = 0; k < portStatistics.length; k++) {
            if (portStatistics[k]["type"] == "GRE") {

                //var td = document.getElementById(tableName + "entry-" + portStatistics[k]["connectorStatistics"]["linkId"] + metricName); // TODO: REMOVE
                var td = document.getElementById(tableName + "entry-" + portStatistics[k]["id"] + metricName);

                value = td.innerText;
                //data[index] = "P" + parseInt(portStatistics[k]["connectorStatistics"]["linkId"]) + "," + value;
                data[index] = "P" + parseInt(portStatistics[k]["id"]) + "," + value;
                index++;
            }
        }

        drawBarChart(context, data, 20 + x * 75, 17, 5, (canvasHeight - 50), 100, metricName, x);
    }

};


function updateQueueHistogram(context, tableName, queueStatistics) {

    var hashTable = {};
    var queueArray = new Array();
    var portArray = new Array();

    for (var j in queueMetricToDisplayAsHistogram) {

        metricName = queueMetricToDisplayAsHistogram[j];

        for (var k = 0; k < queueStatistics.length; k++) {
            var td = document.getElementById(tableName + "entry-" + queueStatistics[k]["Link ID"] + queueStatistics[k]["Queue ID"] + metricName);
            value = td.innerText;

            queueId = parseInt(queueStatistics[k]["Queue ID"]);
            if (!doesItemExist(queueArray, queueId)) {
                queueArray.push(queueId);
            }

            portId = parseInt(queueStatistics[k]["Link ID"]);
            if (!doesItemExist(portArray, portId)) {
                portArray.push(portId);
            }

            hashTable[metricName + "_" + "Q" + queueId + "_" + "P" + portId] = value;

        }
    }

    drawQueueBarChart(context, hashTable, 20, 10, 30, canvasHeight, 100, queueArray, portArray);
};

function doesItemExist(array, item) {
    for (var i = 0; i < array.length; i++) {
        if (parseInt(array[i]) == parseInt(item)) {
            return true;
        }
    }

    return false;
};

function drawQueueBarChart(context, hashTable, startX, barWidth, margin, chartHeight, markDataIncrementsIn, queueArray, portArray) {

    for (var j in queueMetricToDisplayAsHistogram) {

        metricName = queueMetricToDisplayAsHistogram[j];

        for (var k in portArray) {

            portId = portArray[k];
            startY = ((chartHeight / portArray.length) + k * 50) - 35;

            for (var l in queueArray) {

                queueId = queueArray[l];
                height = hashTable[metricName + "_" + "Q" + queueId + "_" + "P" + portId] / (portArray.length + 2);

                context.fillStyle = (j % 2 == 0) ? "#f2f2f2" : "#d9d9d9";
                drawRectangle(context, startX + j * 90 + (l * 20), startY + 15 - height, barWidth, height, true);

                context.textAlign = "left";
                context.fillStyle = "#000";
                context.font = "8pt Arial";
                context.fillText("Q" + queueId, startX + j * 90 + (l * 20), startY + 25, 200);
            }

            context.textAlign = "left";
            context.fillStyle = "#000";
            context.font = "bold 8pt Arial";
            context.fillText(metricName + " P" + portId, startX + j * 90, startY + 35, 200);
        }
    }

};

function drawBarChart(context, data, startX, barWidth, margin, chartHeight, markDataIncrementsIn, metricName, j) {

    maxHeight = -1;
    for (var i = 0; i < data.length; i++) {
        var values = data[i].split(",");
        height = parseInt(values[1]);
        if (height >= maxHeight) {
            maxHeight = height;
        }
    }

    for (var i = 0; i < data.length; i++) {

        var values = data[i].split(",");
        var name = values[0];
        var height = parseFloat(values[1]) * (maxHeight == 0 ? 1 : (chartHeight / maxHeight));

        // Write the data to the chart
        context.fillStyle = (j % 2 == 0) ? "#f2f2f2" : "#d9d9d9";
        if (metricName == "Flow Count") {
            context.fillStyle = "#00ff00";
        }
        drawRectangle(context, startX + (i * barWidth) + i * margin, (chartHeight - height + 15), barWidth, height, true);

        // Add the column title to the x-axis
        context.textAlign = "left";
        context.fillStyle = "#000";
        context.font = "8pt Arial";
        context.fillText(name, 5 + startX + (i * barWidth) + i * margin - 4, chartHeight + 25, 200);
    }

    // add metric name title
    context.textAlign = "left";
    context.fillStyle = "#000";
    context.font = "bold 8pt Arial";
    context.fillText(metricName, startX, chartHeight + 40, 200);

};


// drawRectangle - draws a rectangle on a canvas context using the dimensions specified
function drawRectangle(contextO, x, y, w, h, fill) {
    contextO.beginPath();
    contextO.rect(x, y, w, h);
    contextO.closePath();
    contextO.stroke();
    if (fill) contextO.fill();
};

function drawSite(context, siteName, statistics) {

    startX = 700;

    var imgSite = document.getElementById("img-site");
    context.drawImage(imgSite, startX, 0);

    context.textAlign = "left";
    context.fillStyle = "#000";
    context.font = "bold 15pt Arial";
    context.fillText(siteName, startX + 10, 30, 200);

    for (var index = 0; index < statistics.length; index++) {
        pathId = statistics[index]["Link ID"];
        linkStatus = statistics[index]["Link Status"];
        srcIp = statistics[index]["SRC IP Address"];
        dstIp = statistics[index]["DST IP Address"];
        linkType = statistics[index]["Link Type"];

        updateStatusOfUpDownButton(pathId, linkStatus);

        drawPath(context, startX + 200, (140 / (statistics.length)) * (index + 1), true, pathId, linkStatus, "port-table", srcIp, dstIp, linkType);
    }

    var imgCloud = document.getElementById("img-cloud");
    context.drawImage(imgCloud, startX + 300, 0);

}
function drawPath(context, x, y, fill, pathId, linkStatus, tableName, srcIp, dstIp, linkType) {

    var elem = document.getElementById(tableName + "entry-" + pathId + "Link Status" + "upDownImg");
    src = elem.getAttribute("src");
    context.fillStyle = (src.indexOf("up") > -1) ? "#00FF00" : "#FF0000";
    context.beginPath();
    context.rect(x, y, 150, 10);
    context.lineWidth="5";
    if (linkType == 'REMOTE')
    {
        context.strokeStyle = '#5882FA';
        //context.fillStyle = '#5882FA';
    } else {
        context.strokeStyle = 'black';
    }
    context.closePath();
    context.stroke();
    if (fill) {
        context.fill();
    }

    context.textAlign = "left";
    context.fillStyle = "#000";
    context.font = "bold 8pt Arial";

    context.fillText("Link ID: " + pathId, x + 35, y - 5);

    context.font = "8pt Arial";
    context.fillText(srcIp, x, y + 8);
    context.fillText(dstIp, x + 55, y + 8);
};

function updateDemoTable(linkName) {

    var $table = document.getElementById("demo-table");

    var isFirstTime = false;

    if ($table == undefined || $table == null) {
        //alert('creating a demo-table');
        isFirstTime = true;
        $table = document.createElement("table");
        $table.setAttribute("id", "demo-table");

        var tableHeaderFields = ["Link ID", "State", "Traffic Generator"];

        $thead = document.createElement("thead");
        for (var index in tableHeaderFields) {
            $th = document.createElement("th");
            $th.appendChild(document.createTextNode(tableHeaderFields[index]));
            $thead.appendChild($th);
        }
        $table.appendChild($thead);
    }

    var content =
        '<tr>' +
        '	<td>link1</td>' +
        '	<td>' +
        '		<div class="updownswitch">' +
        '			<input type="checkbox" class="updownswitch-checkbox" id="link1-updown" checked onclick="demo_on_click(this)">' +
        '			<label class="updownswitch-label" for="link1-updown">' +
        '				<span class="updownswitch-inner"></span>' +
        '				<span class="updownswitch-switch"></span>' +
        '			</label>' +
        '		</div>' +
        '	</td>' +
        '	<td>' +
        '		<input type="range" min="0" max="500" step="50" value="0" id="link1-Delay" onchange="demo_on_click(this)">' +
        '		<input type="range" min="0" max="500" step="10" value="0" id="link1-Jitter" onchange="demo_on_click(this)">' +
        '		<input type="range" min="0" max="100" step="5" value="0" id="link1-Packet Loss Rate" onchange="demo_on_click(this)">&nbsp;&nbsp;&nbsp;&nbsp;' +

        '		<input id="round" name="link1-netem" type="submit" value="Go!" onclick="demo_on_click(this)" />' +
        '<br>'+

        '		<output for="link1-Delay" id="link1-Delay-volume">Delay: 0ms</output>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
        '		<output for="link1-Jitter" id="link1-Jitter-volume">Jitter: 0ms</output>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
        '		<output for="link1-Packet Loss Rate" id="link1-Packet Loss Rate-volume">Packet Loss Rate: 0%</output>' +

        '	</td>' +

        '</tr> ' +
        '<tr>';


    content = content.replace(/link1/g, linkName);
    $tr = document.createElement("tr");
    $tr.innerHTML = content;
    $table.appendChild($tr);

    if (isFirstTime == true) {
        $demoMangementTable = document.getElementById("demo-mangement-table");
        $demoMangementTable.appendChild($table);
    }
};

function updateStatusOfUpDownButton(pathId, linkStatus) {
    var $upDownButton = document.getElementById(pathId + "-updown");
    if ($upDownButton != undefined && $upDownButton != null) {
        $upDownButton.checked = (linkStatus == true);
    }
}


function demo_on_click(element) {

    if (element.id.indexOf("cbr") > -1) {

        state = (element.checked == true);

        $.ajax({
            type: "POST",
            dataType: "text",
            url: "/wanvRest/pathselector/setup/path-selector/enable/" + state,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
            },
            error: function (xhr, ajaxOptions, thrownError) {
                //alert("ERROR: " + xhr.status + " " + thrownError);
                printError( "ERROR: " + xhr.status + " " + thrownError);

            }
        });

    } else if (element.name.indexOf("reset") > -1) {

        $.ajax({
            type: "GET",
            dataType: "text",
            url: "/wanvRest/pathselector/stats/base",
            success: function (data) {
            },
            error: function (xhr, ajaxOptions, thrownError) {
                //alert("ERROR: " + xhr.status + " " + thrownError);
                printError( "ERROR: " + xhr.status + " " + thrownError);

            }
        });

    } else if (element.id.indexOf("updown") > -1) {
        var items = element.id.split("-");
        var linkId = items[0];

        var $upOrDown = (element.checked == true) ? 1 : 0;

        $.ajax({
            type: "GET",
            dataType: "text",
            contentType: "application/json; charset=utf-8",
            url: "/wanvRest/pathselector/demo2/" + "port/" + linkId + "/status/" + $upOrDown,
            success: function (data) {
                element.checked = !element.checked;
            },
            error: function (xhr, ajaxOptions, thrownError) {
                //alert("ERROR: " + xhr.status + " " + thrownError);
                printError( "ERROR: " + xhr.status + " " + thrownError);


            }
        });


    } else if (element.name.indexOf("netem") > -1) {


        var items = element.name.split("-");
        var linkId = items[0];
        delay = document.getElementById(linkId + "-Delay").value;
        jitter = document.getElementById(linkId + "-Jitter").value;
        loss = document.getElementById(linkId + "-Packet Loss Rate").value;

        var myData = {
            "linkId": linkId,
            "delay": 2 * delay,
            "jitter": 2 * jitter,
            "packetLossRate": loss
        };

        isResetClick = (element.style.backgroundColor == "" || element.style.backgroundColor == "rgb(0, 48, 150)");

        element.value = (isResetClick == true ? "Stop" : "Go!");

        element.style.backgroundColor = isResetClick == true ? "rgb(0, 200, 200)" : "rgb(0, 48, 150)";

        if (!isResetClick) { // means reset
            document.getElementById(linkId + "-Delay").value = 0;
            document.getElementById(linkId + "-Jitter").value = 0;
            document.getElementById(linkId + "-Packet Loss Rate").value = 0;

            document.getElementById(linkId + "-Delay" + "-volume").value = "Delay 0ms";
            document.getElementById(linkId + "-Jitter" + "-volume").value = "Jitter 0ms";
            document.getElementById(linkId + "-Packet Loss Rate" + "-volume").value = "Packet Loss Rate 0%";


            var myData = {
                "linkId": linkId,
                "delay": 0,
                "jitter": 0,
                "packetLossRate": 0
            };
        }


        $.ajax({
            type: "POST",
            dataType: "text",
            contentType: "application/json; charset=utf-8",
            url: "/wanvRest/pathselector/demo/changeQdiscParams",
            data: JSON.stringify(myData),
            success: function (data) {
            },
            error: function (xhr, ajaxOptions, thrownError) {
                //alert("ERROR: " + xhr.status + " " + thrownError);
                printError( "ERROR: " + xhr.status + " " + thrownError);

            }
        });


    } else if (element.name.indexOf("traffic") > -1) {
        var items = element.id.split("-");
        mouseOrElephant = items[1];
        element.style.backgroundColor = (element.style.backgroundColor == "" || element.style.backgroundColor == "rgb(0, 48, 150)") ? "rgb(0, 255, 0)" : "rgb(0, 48, 150)";

        $.ajax({
            type: "POST",
            dataType: "text",
            contentType: "application/json; charset=utf-8",
            url: "/wanvRest/pathselector/demo/createHttpRequests/" + mouseOrElephant,
            success: function (data) {
            },
            error: function (xhr, ajaxOptions, thrownError) {
                //alert("ERROR: " + xhr.status + " " + thrownError);
                printError( "ERROR: " + xhr.status + " " + thrownError);

            }
        });




    } else {
        unit = element.id.indexOf("Packet Loss Rate") > -1 ? "%" : "ms";
        var items = element.id.split("-");
        document.getElementById(element.id + "-volume").value = items[1] + ": " + element.value + unit;
    }
};


var colors = ["#3366CC", "#DC3912", "#FF9900", "#109618", "#990099"];

function displayDonuts()
{
    var svg1 = d3.select("#svg1").attr("width",300).attr("height",270);
    var svg20 = d3.select("#svg20").attr("width",300).attr("height",270);

    svg1.append("g").attr("id","gatewayDonut");
    svg20.append("g").attr("id","tunnelDonut");

//   Donut3D.draw("tunnelDonut", randomData(), 150, 150, 130, 100, 30, 0.4);

    $.ajax({type: "GET",
        url: tunnelUrl,
        dataType: "text",
        success: function (resp) {
            //alert(resp);
            var response = JSON.parse(resp);
            Donut3D.draw("tunnelDonut", prepareData(response), 150, 120, 130, 100, 30, 0.4);

        }});

    $.ajax({type: "GET",
        url: gatewayUrl,
        dataType: "text",
        success: function (resp) {
            //alert(resp);
            var response = JSON.parse(resp);
            Donut3D.draw("gatewayDonut", prepareData(response), 150, 120, 130, 100, 30, 0.4);

        }});
}

function displayDonutsNow()
{
    $.ajax({type: "GET",
        url: tunnelUrl,
        dataType: "text",
        success: function (resp) {
            //alert(resp);
            var response = JSON.parse(resp);
            Donut3D.transition("tunnelDonut", prepareData(response), 130, 100, 30, 0.4);

        }});

    $.ajax({type: "GET",
        url: gatewayUrl,
        dataType: "text",
        success: function (resp) {
            //alert(resp);
            var response = JSON.parse(resp);
            Donut3D.transition("gatewayDonut", prepareData(response), 130, 100, 30, 0.4);

        }});

}

function prepareData(resp)
{
    var resultData = [];
    var empty = true;
    var i = 0;
    for (var key in resp)
    {
        var value = resp[key];
        if (value > 0)
        {
            empty = false;
        }
        //alert(key + " -> " + value);
        resultData[i] = {"label":"link "+key, "value":value, "color":colors[i] };
        i = i+1;
    }
    if (empty == true)
    {
        //alert('empty');
        resultData[i] = {"label":'no value', "value":10, "color":colors[i] };
        i++;
    }
    for (i = i; i < colors.length; i++)
    {
        resultData[i] = {"label":'', "value":0, "color":colors[i]};
    }
    return resultData;
}
