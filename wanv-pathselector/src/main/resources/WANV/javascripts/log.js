var defaultLogUrl = "/pathselector/log/event";
//var defaultLogUrl = "log";
var newLog = "/pathselector/log/event/new";
var clearedLog = "/pathselector/log/event/cleared";
var openLog = "/pathselector/log/event/open";
var logUrl = defaultLogUrl;
var actionPurveEvents = "/pathselector/log/event/purge";
var actionClearAll = "/pathselector/log/event/clear/all";

var portTableColumns = ["ID", "Time", "Severity", "Category", "Description", "Status", "Change Status"];

function filterReport(status)
{
    if (status == 'all')
    {
        logUrl = defaultLogUrl;
    } else if (status == 'new')
    {
        logUrl = newLog;
    } else if (status == 'cleared')
    {
        logUrl = clearedLog;
    } else if (status == 'open')
    {
        logUrl = openLog;
    }
    //alert(logUrl);
    var $table = $('#eventTable');
    $table.children( 'tr' ).remove();
    updateTable(logUrl);
}

function changeStatus(id, status)
{
    //alert("going to change status of "+id+ " to "+status);
    var changeStatusUrl = "/pathselector/log/event/id/"+id+"/status/"+status;
    //alert(changeStatusUrl);

    $.ajax({type: "POST",
        url: changeStatusUrl,
        dataType: "text",
        success: function (resp) {
            var $table = $('#eventTable');
            $table.children( 'tr' ).remove();
            updateTable(logUrl);
        }
    });

}

function purgeEvents()
{
    $.ajax({type: "POST",
        url: actionPurveEvents,
        dataType: "text",
        success: function (resp) {
            var $table = $('#eventTable');
            $table.children( 'tr' ).remove();
            updateTable(logUrl);
        }
    });
}

function clearAll()
{
    $.ajax({type: "POST",
        url: actionClearAll,
        dataType: "text",
        success: function (resp) {
            var $table = $('#eventTable');
            $table.children( 'tr' ).remove();
            updateTable(logUrl);
        }
    });
}

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

    $portTable = document.createElement("table");
    $portTable.setAttribute("id", "eventTable");
    $thead = document.createElement("thead");
    for (var key in portTableColumns) {
        $th = document.createElement("th");
        $th.appendChild(document.createTextNode(portTableColumns[key]));
        $thead.appendChild($th);
    }
    $portTable.appendChild($thead);
    container.appendChild($portTable);
}

function updateTable(url)
{
    //var $table = $('#eventTable');
    var $table = document.getElementById('eventTable');
    $.ajax({type: "GET",
        url: url,
        dataType: "text",
        success: function (resp) {
            var records = JSON.parse(resp);
            records.forEach(function(row) {
                //d.date = new Date(year+"-"+month+"-"+day+" "+d.timeStamp);
                //delete d.timeStamp;
                //alert(row.id);
                var $tr = document.createElement("tr");
                for (var key in row)
                {
                    if (!row.hasOwnProperty(key))
                        continue;
                    var $td = document.createElement("td");
                    var $elem = document.createTextNode(row[key]);
                    if (key == "severity")
                    {
                        if (row[key] == 'NORMAL') {
                            $td.setAttribute('style', 'background:green;');
                        } else if (row[key] == 'MINOR') {
                            $td.setAttribute('style', 'background:yellow;');
                        } else if (row[key] == 'MAJOR') {
                            $td.setAttribute('style', 'background:orange;');
                        } else if (row[key] == 'CRITICAL') {
                            $td.setAttribute('style', 'background:red;');
                        }
                    } else if (key == 'status') {
                        if (row[key] == 'NEW' ) {
                            $td.setAttribute('style', 'font-weight:bold;');
                        } else if (row[key] == 'OBSERVED') {
                            $elem = document.createTextNode("\u25C9 "+row[key]);
                        } else if (row[key] == 'CLEARED' ) {
                            $elem = document.createTextNode("\u2713 "+row[key]);
                        }
                    }
                    $td.appendChild($elem);
                    $tr.appendChild($td);
                }
                var $td = document.createElement("td");
                if (row.status == "NEW")
                {
                    $td.innerHTML = "<input type=button onclick='changeStatus("+row.id+",\"OBSERVED\");' value='OBSERVED'>&nbsp;" +
                    "<input type=button onclick='changeStatus("+row.id+",\"CLEARED\");' value='CLEAR'>";
                } else if (row.status == "OBSERVED")
                {
                    $td.innerHTML = //"<input type=button onclick='changeStatus("+row.id+",\"new\");' value='NEW'>&nbsp;" +
                        "<input type=button onclick='changeStatus("+row.id+",\"CLEARED\");' value='CLEAR'>";
                } else if (row.status == "CLEARED")
                {
                    $td.innerHTML = //"<input type=button onclick='changeStatus("+row.id+",\"new\");' value='NEW'>&nbsp;" +
                        "<input type=button onclick='changeStatus("+row.id+",\"OBSERVED\");' value='OBSERVED'>";
                }
                $tr.appendChild($td);
                $table.appendChild($tr);
            });

        }
    });
}

function displayLog()
{
    //alert(1);
    buildTables();
    updateTable(logUrl);
}