var jenkins_refresh = function(){
	$.ajax({ 
		type : 'GET', 
		url : '/jenkins',
		success : function(result) {
			if(result.success){
				var status = result.result.status;
				function getLocalTime(nS) {     
					   return new Date(parseInt(nS)).toLocaleString().replace(/:\d{1,2}$/,' ');     
				}
				function getnum(num)
				{
					if(num <0.000001){
						return 'No yet start';
					}
					if(num>100.01){
						return '99.99%'
					}
					var result = (num+'').substring(0,(num+'').indexOf(".")+3);
					return result + '%';
				}
				function getStatusColor(status,previousStatus){
					if(status=='DONE'){
						return 'background:darkgreen';
					}
					if(status=='UNSTABLE'){
						return 'background:#f59400';
					}
					if(status=='SUCCESS'){
						//return 'background:#19ff80';
						return 'background:#04ad11';
					}
					if(status=='FAILURE'){
						return 'background:red';
					}
					if(status=='ABORTED'){
						return 'background:gray';
					}
					if(status=='IN_PROGRESS'){
						if(previousStatus!=='IN_PROGRESS')
							return getStatusColor(previousStatus);
						else
							return 'background:#aaaaaa';
					}
				}
				function getBarStatusColor(status,previousStatus){
					if(status=='DONE'){
						return 'background:lightgreen';
					}
					if(status=='UNSTABLE'){
						return 'background:#ffd400';
					}
					if(status=='SUCCESS'){
						return 'background:limegreen';
					}
					if(status=='FAILURE'){
						return 'background:#ff5050';
					}
					if(status=='ABORTED'){
						return 'background:#d6d3d3';
					}
					if(status=='IN_PROGRESS'){
						if(previousStatus!=='IN_PROGRESS')
							return getBarStatusColor(previousStatus);
						else
							return 'background:#d6d3d3';
					}
				}
				function getBarTotalWidth(){
					return document.getElementsByClassName("block")[0].offsetWidth - 10;
				}
				function getBarWidth(currentPercentRate){
					return 'width:' + getBarTotalWidth() * (currentPercentRate > 1 ? 0.99 : currentPercentRate)  +'px';
				}
				let html='';
				for(let index in status){
					let tmp=''		
					+'<div class="block">'
					+'	<div class="block_inside" style="' + getStatusColor(status[index].status,status[index].previousStatus) + '">'
					+'		<div class="bar" style="' + getBarStatusColor(status[index].status,status[index].previousStatus) + ';' +  getBarWidth(status[index].currentPercentRate) + '"></div>'
					+'		<div class="block_header">'
					+'			<div class="outside">'
					+'				<div class="inside">'
					+'					<a href="' + status[index].jobURL + '" class="project_name">' + status[index].jobName + '</a>'
					+'				</div>'
					+'			</div>'
					+'			<div class="outside">'
					+'				<ul class="message inside">'
					+'					<li>' + getnum(status[index].currentPercentRate * 100 + 0.0000000001) + '</li>'
					+'				</ul>'
					+'			</div>'
					+'		</div>'
					+'		<div class="block_footer">'
					+'			<div class="current_num">#' + status[index].currentTaskNum + '</div>'
					+'			<div class="last_build_time">' + getLocalTime(status[index].currentTaskBeginTime) + '</div>'
					+'		</div>'
					+'	</div>'
					+'</div>';
					html+=tmp;
					
				}
				document.getElementsByClassName("jenkins_view_content")[0].innerHTML = html;
			}
		}
	}); 
}
jenkins_refresh();
setInterval(jenkins_refresh,2000);
	