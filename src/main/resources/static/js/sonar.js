var sonarChart;
function getPercent(value,total)
{
    let num,result;
    if(value>0.0000000002){
        num = ((value * 1.0) / total) * 100 + 0.0000000001;
        result = (num+'').substring(0,(num+'').indexOf(".")+3);
    }else{
        result = 0;
    }
    return result
}

function getBarColor(value){
    if(value < 60){
        return 'red';
    }else if(value >= 60 && value < 80){
        return 'orange';
    }else if(value >=80){
        return 'green';
    }
    return 'red';
}
function generateProcessBar(title,value,total){
    let html=
        '<div class="progress-bar-view">'+
        '		<div class="progress-bar-header">'+
        '			<div class="progress-bar-title">' + title + '</div>'+
        '			<div class="progress-bar-value">[' + value + "/" + total + ']</div>'+
        '		</div>'+
        '		<div class="progress-bar '+ getBarColor(getPercent(value,total)) +' stripes">'+
        '		    <span style="width: ' + getPercent(value,total) + '%"></span>'+
        '		</div>'+
        '		<div class="progress-bar-precent">' + getPercent(value,total) + '%</div>'+
        '</div>';
    return html;
}
function generateBoxNumber(title,value) {
    let rating = 'box-number-rating-E';
    if(value <= 3){
        rating ='box-number-rating-A';
    }else if(value <= 5){
        rating ='box-number-rating-B';
    }else if(value <= 10){
        rating ='box-number-rating-C';
    }else if(value <= 20){
        rating ='box-number-rating-D';
    }
    return '<div class="box-number ' + rating + '"><div class="box-number-text">' + title +'</div><div class="box-number-value">' + value + '</div></div>';
}
function sonar_refresh(){
    console.log("refresh");
    $.ajax({
        type : "GET",
        url : "/sonar?keys=IRIS_TNM",//,IRIS4_VID,IRIS4_SA
        success : function(result) {
            if(result.success){
                var details = result.content.details;
                var summerize = result.content.summerize;
                var legend={
                    data: []
                };
                var yAxis = {
                    type: 'category',
                    "axisLabel":{
                        interval: 0
                    },
                    data: []
                };
                var series =[];

                var authors=[];
                for(let index in summerize){
                    authors.push({
                        name:index,
                        count:summerize[index]
                    });
                }
                var cmp = function (x, y) {
                    if (x.count < y.count) {
                        return -1;
                    } else if (x.count > y.count) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
                authors.sort(cmp);
                for(let index in authors){
                    yAxis.data.push(authors[index].name);
                }

                for(let index in details){
                    let detail = details[index];
                    legend.data.push(detail.projectKey);
                    let project = {
                        name: detail.projectKey,
                        type: 'bar',
                        stack: 'total',
                        label: {
                            normal: {
                                show: true,
                                position: 'insideRight'
                            }
                        },
                        data: []
                    }
                    for(let j in yAxis.data){
                        if(detail.projectSummerize[yAxis.data[j]]==null){
                            project.data.push(null);
                        }else{
                            project.data.push(detail.projectSummerize[yAxis.data[j]]);
                        }
                    }
                    series.push(project);
                }


                var dom = document.getElementById("sonar_view_chart");
                sonarChart = echarts.init(dom, 'dark');
                var app = {};
                app.title = 'Uncover code chart';

                option = null;
                option = {
                    title: {
                        text: 'Uncover code chart',
                        subtext: 'Data from team pc sonar'
                    },
                    tooltip : {
                        trigger: 'axis',
                        axisPointer : {
                            type : 'shadow'
                        }
                    },
                    legend: legend,
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'value'
                    },
                    yAxis: yAxis,
                    series:series
                };
                ;
                if (option && typeof option === "object") {
                    sonarChart.setOption(option, true);
                }

                function queryXY(param){
                    //seriesIndex：系列序号，dataIndex：数值序列，seriesName：legend名称，name：X轴值，data和value都代表Y轴值
                    var seriesIndex = param.seriesIndex;
                    var dataIndex = param.dataIndex;
                    var seriesName = param.seriesName;
                    var name = param.name;
                    var data = param.data;
                    var value = param.value;
                    console.dir(param);
                    //var str = "seriesIndex:"+seriesIndex+"\n"+"dataIndex:"+dataIndex+"\n"+"seriesName:"+seriesName+"\n"+
                    //         "name:"+name+"\n"+"data:"+data+"\n"+"value:"+value;
                    //alert(str);

                    let items=[];
                    for(let index in details){
                        let detail = details[index];
                        //if(detail.projectKey == seriesName){
                        let projectFiles = detail.projectFiles;
                        for(let i in projectFiles){
                            for(let j in projectFiles[i].uncoverList){
                                if(j == name){
                                    items.push({name:projectFiles[i].name,count:projectFiles[i].uncoverList[j]});
                                }
                            }
                        }

                        //}
                    }

                    showCountingLineDetails(items,name,result.content.host);
                }
                sonarChart.on('click',queryXY);


                var dashboard_html='';
                for (let index in details) {
                    let detail = details[index];
                    let new_conditions_to_cover =  parseInt(detail.projectDetails.new_conditions_to_cover.periods);
                    let new_lines_to_cover = parseInt(detail.projectDetails.new_lines_to_cover.periods);
                    let new_uncovered_conditions = parseInt(detail.projectDetails.new_uncovered_conditions.periods)
                    let new_uncovered_lines = parseInt(detail.projectDetails.new_uncovered_lines.periods);

                    let total = new_conditions_to_cover + new_lines_to_cover;
                    let uncover = new_uncovered_conditions + new_uncovered_lines;
                    let cover = total - uncover;
                    dashboard_html += '<a class="click_link" href="' + result.content.host + '/dashboard?id=' + detail.projectKey + '"><div class="progress-bar-group">' + detail.projectKey + '</div></a>'
                    dashboard_html += '<a class="click_link" href="' + result.content.host + '/component_measures?id=' + detail.projectKey + '&resolved=false&sinceLeakPeriod=true&metric=new_coverage">' + generateProcessBar('Coverage',cover,total) + '</a>';
                    dashboard_html += '<a class="click_link" href="' + result.content.host + '/component_measures?id=' + detail.projectKey + '&resolved=false&sinceLeakPeriod=true&metric=new_line_coverage">' + generateProcessBar('Line',new_lines_to_cover - new_uncovered_lines,new_lines_to_cover) + '</a>';
                    dashboard_html += '<a class="click_link" href="' + result.content.host + '/component_measures?id=' + detail.projectKey + '&resolved=false&sinceLeakPeriod=true&metric=new_conditions_to_cover">' + generateProcessBar('Conditions',new_conditions_to_cover - new_uncovered_conditions, new_conditions_to_cover) + '</a>';
                    dashboard_html += '<a class="click_link" href="' + result.content.host + '/project/issues?id=' + detail.projectKey + '&resolved=false&sinceLeakPeriod=true&types=BUG">' + generateBoxNumber('Bugs',detail.projectDetails.new_bugs.periods) + '</a>';
                    dashboard_html += '<a class="click_link" href="' + result.content.host + '/project/issues?id=' + detail.projectKey + '&resolved=false&sinceLeakPeriod=true&types=VULNERABILITY">' + generateBoxNumber('Weak',detail.projectDetails.new_vulnerabilities.periods) + '</a>';
                    dashboard_html += '<a class="click_link" href="' + result.content.host + '/project/issues?id=' + detail.projectKey + '&resolved=false&sinceLeakPeriod=true&types=CODE_SMELL">' + generateBoxNumber('Smells',detail.projectDetails.new_code_smells.periods) + '</a>';
                    dashboard_html += '<br>'
                }
                document.getElementsByClassName('sonar_dashboard_view')[0].innerHTML = dashboard_html;
            }
        }
    });
}

sonar_refresh();
	
			