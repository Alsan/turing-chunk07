var t_on = {
    'apiChart':1,
    'subsChart':1,
    'serviceTimeChart':1,
    'tempLoadingSpace':1
};
var currentLocation;

var chartColorScheme1 = ["#3da0ea","#bacf0b","#e7912a","#4ec9ce","#f377ab","#ec7337","#bacf0b","#f377ab","#3da0ea","#e7912a","#bacf0b"];
//fault colors || shades of red
var chartColorScheme2 = ["#ED2939","#E0115F","#E62020","#F2003C","#ED1C24","#CE2029","#B31B1B","#990000","#800000","#B22222","#DA2C43"];
//fault colors || shades of blue
var chartColorScheme3 = ["#0099CC","#436EEE","#82CFFD","#33A1C9","#8DB6CD","#60AFFE","#7AA9DD","#104E8B","#7EB6FF","#4981CE","#2E37FE"];
currentLocation=window.location.pathname;

require(["dojo/dom", "dojo/domReady!"], function(dom){
    currentLocation=window.location.pathname;
    //Initiating the fake progress bar
    jagg.fillProgress('apiChart');jagg.fillProgress('subsChart');jagg.fillProgress('serviceTimeChart');jagg.fillProgress('tempLoadingSpace');

    jagg.post("/site/blocks/stats/ajax/stats.jag", { action:"getFirstAccessTime",currentLocation:currentLocation  },
        function (json) {

            if (!json.error) {

                if( json.usage && json.usage.length > 0){
                    var d = new Date();
                    var firstAccessDay = new Date(json.usage[0].year, json.usage[0].month-1, json.usage[0].day);
                    var currentDay = new Date(d.getFullYear(), d.getMonth(), d.getDate());
                    if(firstAccessDay.valueOf() == currentDay.valueOf()){
                        currentDay = new Date(d.getFullYear(), d.getMonth(), d.getDate()+1);
                    }
                    var rangeSlider =  $("#rangeSlider");
                    //console.info(currentDay);
                    rangeSlider.dateRangeSlider({
                        "bounds":{
                            min: firstAccessDay,
                            max: currentDay
                        },
                        "defaultValues":{
                            min: firstAccessDay,
                            max: currentDay
                        }
                    });
                    rangeSlider.bind("valuesChanged", function(e, data){
                        var from = convertTimeString(data.values.min);
                        var to = convertTimeStringPlusDay(data.values.max);
                        drawSubscriberCountByAPIs(from,to);
                        drawProviderAPIServiceTime(from,to);
                        drawProviderAPIUsage(from,to);
                        drawProviderAPIVersionUserLastAccess(from,to);
                        drawAPIUsageByResourcePath(from,to);
                        drawAPIUsageByUser(from,to);
                        drawAPIResponseFaultCountTable(from,to);
                        drawAPIResponseFaultCountChart(from,to);
                    });
                    var width = $("#rangeSliderWrapper").width();
                    $("#rangeSliderWrapper").affix();
                    $("#rangeSliderWrapper").width(width);

                }
                else{
                    $('#middle').html("");
                    $('#middle').append($('<div class="errorWrapper"><span class="label top-level-warning"><i class="icon-warning-sign icon-white"></i>'
                        +i18n.t('errorMsgs.checkBAMConnectivity')+'</span><br/><img src="../themes/default/templates/stats/images/statsThumb.png" alt="Smiley face"></div>'));
                }


            }
            else {
                if (json.message == "AuthenticateError") {
                    jagg.showLogin();
                } else {
                    jagg.message({content:json.message,type:"error"});
                }
            }
            t_on['apiChart'] = 0;
        }, "json");

});

var drawProviderAPIServiceTime = function(from,to){
    var fromDate = from;
    var toDate = to;
    jagg.post("/site/blocks/stats/ajax/stats.jag", { action:"getProviderAPIServiceTime",currentLocation:currentLocation,fromDate:fromDate,toDate:toDate },
        function (json) {
            if (!json.error) {
                var length = json.usage.length,s1 = [];
                var data = [];
                $('#serviceTimeChart').empty();
                for (var i = 0; i < length; i++) {
                    data[i] = [json.usage[i].apiName,parseFloat(json.usage[i].serviceTime)];
                    //add fake value to overcome dojo chart single series issue
                    if(length === 1){
                        data.push(["",0]);
                    }
                    //s1.push(tmp);
                }

                if (length > 0) {
                    var height = 200;
                    if (30 * length > 200) height = 30 * length;
                    $('#serviceTimeChart').height(height);
                    require([
                        // Require the basic chart class
                        "dojox/charting/Chart",

                        // Require the theme of our choosing
                        "dojox/charting/themes/ApimDefault",

                        // Tooltip
                        "dojox/charting/action2d/Tooltip",
                        // Require the highlighter
                        "dojox/charting/action2d/Highlight",

                        //  We want to plot bars
                        "dojox/charting/plot2d/Bars",

                        //  We want to use Markers
                        "dojox/charting/plot2d/Markers",

                        //  We'll use default x/y axes
                        "dojox/charting/axis2d/Default",

                        //mouse zoom and pan
                        "dojox/charting/action2d/MouseZoomAndPan",

                        // Wait until the DOM is ready
                        "dojo/domReady!"
                    ], function(Chart, theme,MouseZoomAndPan,Highlight) {




                        // Create the chart within it's "holding" node
                        var serviceTimeChart = new Chart("serviceTimeChart");

                        // Set the theme
                        serviceTimeChart.setTheme(theme);

                        // Add the only/default plot
                        serviceTimeChart.addPlot("default", {
                            type: "Bars",
                            markers: true,
                            gap: 5,
                            animate:{duration:1000}
                        });

                        // Add axes
                        serviceTimeChart.addAxis("x", {  fixLower: "major", fixUpper: "major" });
                        serviceTimeChart.addAxis("y",{vertical:true,
                            labels:dojo.map(data, function(value, index){
                                return {value: index + 1, text: value[0]};
                            })
                        });

                        // Define the data
                        var chartData; var color = -1;
                        require(["dojo/_base/array"], function(array){
                            chartData= array.map(data, function(d){
                                color++;
                                return {y: d[1],text:d[0], tooltip: "<b>"+d[0]+"</b><br /><i>"+d[1]+"s</i>",fill:"#0099CC"};
                            });
                        });

                        // Add the series of data
                        serviceTimeChart.addSeries("API Service Time",chartData);

                        new MouseZoomAndPan(serviceTimeChart, "default", { axis: "x"});

                        new Highlight(serviceTimeChart,"default");

                        // Render the chart!
                        serviceTimeChart.render();

                    });

                } else {
                    $('#serviceTimeChart').css("fontSize", 14);
                    $('#serviceTimeChart').append($('<span class="label label-info">'+i18n.t('errorMsgs.noData')+'</span>'));
                }


            } else {
                if (json.message == "AuthenticateError") {
                    jagg.showLogin();
                } else {
                    jagg.message({content:json.message,type:"error"});
                }
            }
            t_on['serviceTimeChart'] = 0;
        }, "json");
}

var drawSubscriberCountByAPIs = function(from,to){
    var fromDate = from;
    var toDate = to;
    jagg.post("/site/blocks/stats/ajax/stats.jag", { action:"getSubscriberCountByAPIs",currentLocation:currentLocation  },
        function (json) {
            if (!json.error) {
                var length = json.usage.length,data = [];
                if (length > 0) {
                    $('#subsChart').empty();
                    var easyPieChartDefaults = {
                        animate: 2000,
                        scaleColor: false,
                        lineWidth: 12,
                        lineCap: 'square',
                        size: 100,
                        trackColor: '#e5e5e5'
                    }
                    var allSubscriptionCount = 0;
                    for (var i = 0; i < length; i++) {
                        allSubscriptionCount+=json.usage[i].count;
                    }
                    for (var i = 0; i < length; i++) {
                        var k = i+1;
                        var chartId = "easyPieChart"+k;
                        var chartVal = Math.round((json.usage[i].count / allSubscriptionCount)*100);
                        $('#subsChart').append("<div class='span2 easyPieWrapper'>" +
                            "<div style='color:"+chartColorScheme1[i]+"' data-percent='"+chartVal+"' id='"+chartId+"'>"+chartVal+"% </div>" +
                            "<b>"+json.usage[i].apiName+"</b><br /><i>"+json.usage[i].count+" subscription(s)</i></div>");
                        $('#'+chartId+'').easyPieChart({
                            animate: 2000,
                            scaleColor: false,
                            lineWidth: 12,
                            lineCap: 'square',
                            size: 110,
                            trackColor: '#e5e5e5',
                            barColor: chartColorScheme1[i]
                        });
                    }
                }



            } else {
                if (json.message == "AuthenticateError") {
                    jagg.showLogin();
                } else {
                    jagg.message({content:json.message,type:"error"});
                }
            }
            t_on['subsChart'] = 0;
        }, "json");
}

var drawProviderAPIUsage = function(from,to){
    var fromDate = from;
    var toDate = to;
    jagg.post("/site/blocks/stats/ajax/stats.jag", { action:"getProviderAPIUsage",currentLocation:currentLocation,fromDate:fromDate,toDate:toDate  },
        function (json) {
            if (!json.error) {
                var length = json.usage.length,data = [];
                $('#apiChart').empty();
                $('#apiTable').find("tr:gt(0)").remove();
                for (var i = 0; i < length; i++) {
                    data[i] = [json.usage[i].apiName, parseInt(json.usage[i].count)];
                    $('#apiTable').append($('<tr><td>' + json.usage[i].apiName + '</td><td class="tdNumberCell">' + json.usage[i].count + '</td></tr>'));

                }

                if (length > 0) {
                    $('#apiTable').show();
                    require([
                        // Require the basic chart class
                        "dojox/charting/Chart",

                        // Require the theme of our choosing
                        "dojox/charting/themes/Claro",

                        // Charting plugins:

                        //  We want to plot a Pie chart
                        "dojox/charting/plot2d/Pie",

                        // Retrieve the Legend, Tooltip, and MoveSlice classes
                        "dojox/charting/action2d/Tooltip",
                        "dojox/charting/action2d/MoveSlice",

                        //  We want to use Markers
                        "dojox/charting/plot2d/Markers",

                        //  We'll use default x/y axes
                        "dojox/charting/axis2d/Default"
                    ], function(Chart, theme, Pie, Tooltip, MoveSlice) {



                        // Create the chart within it's "holding" node
                        var apiUsageChart = new Chart("apiChart");

                        // Set the theme
                        apiUsageChart.setTheme(theme);

                        // Add the only/default plot
                        apiUsageChart.addPlot("default", {
                            type: Pie,
                            markers: true,
                            radius:130
                        });

                        // Add axes
                        apiUsageChart.addAxis("x");
                        apiUsageChart.addAxis("y", { min: 5000, max: 30000, vertical: true, fixLower: "major", fixUpper: "major" });

                        // Define the data
                        var chartData; var color = -1;
                        require(["dojo/_base/array"], function(array){
                            chartData= array.map(data, function(d){
                                color++;
                                return {y: d[1], tooltip: "<b>"+d[0]+"</b><br /><i>"+d[1]+" call(s)</i>",fill:chartColorScheme1[color]};

                            });
                        });

                        apiUsageChart.addSeries("API Usage",chartData);


                        // Create the tooltip
                        var tip = new Tooltip(apiUsageChart,"default");

                        // Create the slice mover
                        var mag = new MoveSlice(apiUsageChart,"default");

                        // Render the chart!
                        apiUsageChart.render();

                    });

                } else {
                    $('#apiTable').hide();
                    $('#apiChart').css("fontSize", 14);
                    $('#apiChart').append($('<span class="label label-info">'+i18n.t('errorMsgs.noData')+'</span>'));
                }


            } else {
                if (json.message == "AuthenticateError") {
                    jagg.showLogin();
                } else {
                    jagg.message({content:json.message,type:"error"});
                }
            }
            t_on['apiChart'] = 0;
        }, "json");
}

var drawProviderAPIVersionUserLastAccess = function(from,to){
    var fromDate = from;
    var toDate = to;
    jagg.post("/site/blocks/stats/ajax/stats.jag", { action:"getProviderAPIVersionUserLastAccess",currentLocation:currentLocation,fromDate:fromDate,toDate:toDate  },
        function (json) {
            if (!json.error) {
                $('#lastAccessTable').find("tr:gt(0)").remove();
                var length = json.usage.length;
                $('#lastAccessTable').show();
                for (var i = 0; i < json.usage.length; i++) {
                    $('#lastAccessTable').append($('<tr><td>' + json.usage[i].api_name + '</td><td>' + json.usage[i].api_version + '</td><td>' + json.usage[i].user + '</td><td>' + jagg.getDate(json.usage[i].lastAccess) + '</td></tr>'));
                }
                if (length == 0) {
                    $('#lastAccessTable').hide();
                    $('#tempLoadingSpace').html('');
                    $('#tempLoadingSpace').append($('<span class="label label-info">'+i18n.t('errorMsgs.noData')+'</span>'));

                }else{
                    $('#tempLoadingSpace').hide();
                }

            } else {
                if (json.message == "AuthenticateError") {
                    jagg.showLogin();
                } else {
                    jagg.message({content:json.message,type:"error"});
                }
            }
            t_on['tempLoadingSpace'] = 0;
        }, "json");
}

var drawAPIUsageByResourcePath = function(from,to){
    var fromDate = from;
    var toDate = to;
    jagg.post("/site/blocks/stats/ajax/stats.jag", { action:"getAPIUsageByResourcePath", currentLocation:currentLocation,fromDate:fromDate,toDate:toDate},
        function (json) {
            if (!json.error) {
                $('#resourcePathUsageTable').find("tr:gt(0)").remove();
                var length = json.usage.length;
                $('#resourcePathUsageTable').show();
                for (var i = 0; i < json.usage.length; i++) {
                    $('#resourcePathUsageTable').append($('<tr><td>' + json.usage[i].apiName + '</td><td>' + json.usage[i].version + '</td><td>' + json.usage[i].context + '</td><td>' + json.usage[i].method + '</td><td class="tdNumberCell">' + json.usage[i].count + '</td></tr>'));
                }
                if (length == 0) {
                    $('#resourcePathUsageTable').hide();
                    $('#tempLoadingSpaceResourcePath').html('');
                    $('#tempLoadingSpaceResourcePath').append($('<span class="label label-info">'+i18n.t('errorMsgs.noData')+'</span>'));

                }else{
                    $('#tempLoadingSpaceResourcePath').hide();
                }

            } else {
                if (json.message == "AuthenticateError") {
                    jagg.showLogin();
                } else {
                    jagg.message({content:json.message,type:"error"});
                }
            }
            t_on['tempLoadingSpaceResourcePath'] = 0;
        }, "json");

}

var drawAPIUsageByUser = function(from,to){
    var fromDate = from;
    var toDate = to;
    jagg.post("/site/blocks/stats/ajax/stats.jag", { action:"getAPIUsageByUser", currentLocation:currentLocation,fromDate:fromDate,toDate:toDate},
      function (json) {
          if (!json.error) {
              $('#apiUsageByUserTable').find("tr:gt(0)").remove();
              var length = json.usage.length;
              $('#apiUsageByUserTable').show();
              for (var i = 0; i < json.usage.length; i++) {
                  $('#apiUsageByUserTable').append($('<tr><td>' + json.usage[i].apiName + '</td><td>' + json.usage[i].version + '</td><td>' + json.usage[i].userId + '</td><td width="20%"><p align="right">' + json.usage[i].count + '</td></tr>'));
              }
              if (length == 0) {
                  $('#apiUsageByUserTable').hide();
                  $('#tempLoadingSpaceUsageByUser').html('');
                  $('#tempLoadingSpaceUsageByUser').append($('<span class="label label-info">'+i18n.t('errorMsgs.checkBAMConnectivity')+'</span>'));

              }else{
                  $('#tempLoadingSpaceUsageByUser').hide();
              }

          } else {
              if (json.message == "AuthenticateError") {
                  jagg.showLogin();
              } else {
                  jagg.message({content:json.message,type:"error"});
              }
          }
          t_on['tempLoadingSpaceUsageByUser'] = 0;
      }, "json");

}

var drawAPIResponseFaultCountTable = function(from,to){
    var fromDate = from;
    var toDate = to;
    jagg.post("/site/blocks/stats/ajax/stats.jag", { action:"getAPIResponseFaultCount", currentLocation:currentLocation,fromDate:fromDate,toDate:toDate},
        function (json) {
            if (!json.error) {
                $('#apiFaultyTable').find("tr:gt(0)").remove();
                var length = json.usage.length;
                $('#apiFaultyTable').show();
                for (var i = 0; i < json.usage.length; i++) {
                    $('#apiFaultyTable').append($('<tr><td>' + json.usage[i].apiName + '</td><td>' + json.usage[i].version + '</td><td>' + json.usage[i].count + '</td><td><span class="pull-right">' + json.usage[i].faultPercentage +'%</span></td></tr>'));
                }
                if (length == 0) {
                    $('#apiFaultyTable').hide();
                    $('#tempLoadingAPIFaulty').html('');
                    $('#tempLoadingAPIFaulty').append($('<span class="label label-info">'+i18n.t('errorMsgs.noData')+'</span>'));

                }else{
                    $('#tempLoadingAPIFaulty').hide();
                }

            } else {
                if (json.message == "AuthenticateError") {
                    jagg.showLogin();
                } else {
                    jagg.message({content:json.message,type:"error"});
                }
            }
            t_on['tempLoadingAPIFaulty'] = 0;
        }, "json");

}

var drawAPIResponseFaultCountChart = function(from,to){
    var fromDate = from;
    var toDate = to;
    jagg.post("/site/blocks/stats/ajax/stats.jag", { action:"getAPIResponseFaultCount",currentLocation:currentLocation,fromDate:fromDate,toDate:toDate },
        function (json) {
            if (!json.error) {
                var length = json.usage.length,s1 = [];
                $('#faultyCountChart').empty();
                var data = [];
                for (var i = 0; i < length; i++) {
                    data[i] = [json.usage[i].apiName, parseFloat(json.usage[i].count)];
                    //add fake value to overcome dojo chart single series issue
                    if(length === 1){
                        data.push(["",0]);
                    }
                }
                if (length > 0) {
                    /*var width = 300;
                     if (30 * length > 300) width = 35 * length;
                     $('#faultyCountChart').width(width);*/
                    require([
                        // Require the basic chart class
                        "dojox/charting/Chart",

                        // Require the theme of our choosing
                        "dojox/charting/themes/ApimDefault",

                        // Tooltip
                        "dojox/charting/action2d/Tooltip",
                        // Require the highlighter
                        "dojox/charting/action2d/Highlight",

                        //  We want to plot Columns
                        "dojox/charting/plot2d/Columns",

                        //  We want to use Markers
                        "dojox/charting/plot2d/Markers",

                        //  We'll use default x/y axes
                        "dojox/charting/axis2d/Default",

                        //mouse zoom and pan
                        "dojox/charting/action2d/MouseZoomAndPan",

                        // Wait until the DOM is ready
                        "dojo/domReady!"
                    ], function(Chart, theme,MouseZoomAndPan,Highlight) {




                        // Create the chart within it's "holding" node
                        var faultyCountChart = new Chart("faultyCountChart");

                        // Set the theme
                        faultyCountChart.setTheme(theme);

                        // Add the only/default plot
                        faultyCountChart.addPlot("default", {
                            type: "Columns",
                            markers: true,
                            gap: 5,
                            animate:{duration:1000}
                        });

                        // Add axes
                        faultyCountChart.addAxis("x", { labels:dojo.map(data, function(value, index){
                            return {value: index + 1, text: value[0]};
                        })
                        });
                        faultyCountChart.addAxis("y",{vertical:true,fixLower: "major", fixUpper: "major"});

                        // Define the data
                        var chartData; var color = -1;
                        require(["dojo/_base/array"], function(array){
                            chartData= array.map(data, function(d){
                                color++;
                                return {y: d[1],text:d[0], tooltip: "<b>"+d[0]+"</b><br /><i>"+d[1]+" call(s)</i>",fill:chartColorScheme2[color]};
                            });
                        });

                        // Add the series of data
                        faultyCountChart.addSeries("API Service Time",chartData);

                        new MouseZoomAndPan(faultyCountChart, "default", { axis: "x"});

                        new Highlight(faultyCountChart,"default");

                        // Render the chart!
                        faultyCountChart.render();

                    });

                } else {
                    $('#faultyCountChart').css("fontSize", 14);
                    $('#faultyCountChart').append($('<span class="label label-info">'+i18n.t('errorMsgs.noData')+'</span>'));
                }


            } else {
                if (json.message == "AuthenticateError") {
                    jagg.showLogin();
                } else {
                    jagg.message({content:json.message,type:"error"});
                }
            }
            t_on['faultyCountChart'] = 0;
        }, "json");
}


var convertTimeString = function(date){
    var d = new Date(date);
    var formattedDate = d.getFullYear() + "-" + formatTimeChunk((d.getMonth()+1)) + "-" + formatTimeChunk(d.getDate());
    return formattedDate;
};

var convertTimeStringPlusDay = function(date){
    var d = new Date(date);
    var formattedDate = d.getFullYear() + "-" + formatTimeChunk((d.getMonth()+1)) + "-" + formatTimeChunk(d.getDate()+1);
    return formattedDate;
};

var formatTimeChunk = function (t){
    if (t<10){
        t="0" + t;
    }
    return t;
};

