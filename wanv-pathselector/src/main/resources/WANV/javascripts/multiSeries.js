//var parseTime = d3.date.format("%G:%i:%s").parse;

tunnelStatUrl = "tunnels";
gatewayStatUrl = "/pathselector/stats/site/defaultGateways";
//gatewayStatUrl = "defaultGateways";
siteUrl = "/pathselector/stats/site";

function displayGraph() {
    var today = new Date();
    var currentTime = new Date();
    var month = currentTime.getMonth() + 1;
    var day = currentTime.getDate();
    var year = currentTime.getFullYear();

    $.ajax({
        type: "GET",
        url: gatewayStatUrl,
        dataType: "text",
        success: function (resp) {
            var records = JSON.parse(resp);
            var records2 = [];
            var records3 = [];
            records.forEach(function (d) {
                var point2 = [];
                var point3 = [];
                point2.date = new Date(year + "-" + month + "-" + day + " " + d.timeStamp);
                point3.date = point2.date;
                var d2 = Object.keys(d).sort();
                for (var id in d2)
                {
                    var key = d2[id];
                    if (key.match(/RX/) )
                    {
                        point2[key] = d[key];
                    }
                    else if (key.match(/TX/) )
                    {
                        point3[key] = d[key];
                    }
                }
                records2.push(point2);
                records3.push(point3);
            });
            displayGraphNow(records2, 2);
            displayGraphNow(records3, 3);
        }
    });

    var svgId = 4;
    var num = 0;
    $.ajax({
        type: "GET",
        url: siteUrl,
        dataType: "text",
        success: function (resp) {
            var macs = JSON.parse(resp);
            macs.forEach(function (mac) {
                var ch = $("#graphTunnels").children("p");
                if (ch[num]) {
                    $(ch[num]).text("Site: " + mac);
                } else {
                    d3.select("#graphTunnels").append("p").text("Site: " + mac);
                    d3.select("#graphTunnels").append("svg").attr('id', 'svg' + svgId);
                    d3.select("#graphTunnels").append("svg").attr('id', 'svg' + (1+svgId));
                }

                tunnelStatUrl = siteUrl + "/" + mac + "/tunnels";

                $.ajax({
                    type: "GET",
                    url: tunnelStatUrl,
                    dataType: "text",
                    svgId: svgId,
                    success: function (resp) {
                        $("#graphTunnels").show();
                        var records = JSON.parse(resp);
                        var records2 = [];
                        var records3 = [];
                        records.forEach(function (d) {
                            var point2 = [];
                            var point3 = [];
                            point2.date = new Date(year + "-" + month + "-" + day + " " + d.timeStamp);
                            point3.date = point2.date;
                            var d2 = Object.keys(d).sort();
                            for (var id in d2)
                            {
                                var key = d2[id];
                                if (key.match(/RX/) )
                                {
                                    point2[key] = d[key];
                                }
                                else if (key.match(/TX/) )
                                {
                                    point3[key] = d[key];
                                }
                            }
                            records2.push(point2.sort());
                            records3.push(point3.sort());
                        });
                        displayGraphNow(records2, this.svgId);
                        displayGraphNow(records3, this.svgId+1);
                    }
                });
                num = num + 1;
                svgId = svgId + 2;

            });
        }
    });

}

function displayGraphNow(data, svgId) {

    var margin = {top: 50, right: 20, bottom: 30, left: 60},
        width = 440 - margin.left - margin.right,
        height = 250 - margin.top - margin.bottom;

//var parseDate = d3.time.format("%Y%m%d").parse;

    var x = d3.time.scale()
        .range([0, width]);

    var y = d3.scale.linear()
        .range([height, 0]);

    var color = d3.scale.category10();

    var xAxis = d3.svg.axis()
        .scale(x)
        .orient("bottom")
        .ticks(d3.time.minutes, 2);

    var yAxis = d3.svg.axis()
        .scale(y)
        .orient("left");

    var line = d3.svg.line()
        .interpolate("basis")
        .x(function (d) {
            return x(d.date);
        })
        .y(function (d) {
            return y(d.temperature);
        });

    d3.select("#svg" + svgId).select("g").remove();

    var svg = d3.select("#svg" + svgId)
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    color.domain(d3.keys(data[0]).filter(function (key) {
        return key !== "date";
    }));

    //data.forEach(function(d) {
    //  d.date = parseDate(d.date);
    //});

    var pos = 0;
    var cities = color.domain().map(function (name) {
        return {
            name: name,
            pos: pos++,
            values: data.map(function (d) {
                return {date: d.date, temperature: +d[name]};
            })
        };
    });

    x.domain(d3.extent(data, function (d) {
        return d.date;
    }));

    y.domain([
        d3.min(cities, function (c) {
            return d3.min(c.values, function (v) {
                return v.temperature;
            });
        }),
        d3.max(cities, function (c) {
            return d3.max(c.values, function (v) {
                return v.temperature;
            });
        })
    ]);

    svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis);

    svg.append("g")
        .attr("class", "y axis")
        .call(yAxis)
        .append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", 6)
        .attr("dy", ".71em")
        .style("text-anchor", "end")
        .text("KBytes");

    svg.selectAll(".city")
        .data(cities)
        .enter()
        .append("text")
        .datum(function (d) {
            return {name: d.name, pos: d.pos};
        })
        .attr("fill", function (d) {
            return color(d.name);
        })
        /*      .attr("x", 265) */
        .attr("dx", function (d) {
            return -50 + 100*d.pos;
        })
        .attr("dy", function (d) {
            if (d.pos > 3)
                return 0;
            return -40;
            //return 30 * d.pos;
        })
        .text(function (d) {
            return d.name;
        });

    var city = svg.selectAll(".city")
        .data(cities)
        .enter().append("g")
        .attr("class", "city");

    city.append("path")
        .attr("class", "line")
        .attr("d", function (d) {
            return line(d.values);
        })
        .style("stroke", function (d) {
            return color(d.name);
        });

}