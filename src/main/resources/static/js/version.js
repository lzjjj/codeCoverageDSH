var param = [];
	
function version_refresh(){
	$.ajax({
		type : "POST",
		url : '/versions',
		data:{'param':JSON.stringify({urlMap:param})},
		success : function(data) {
			console.log(param);
			console.log(data);
			if(data.success){
				clear();
				let result = data.result.list;
				for(let index in result){
					let item = result[index];
					let html=
						'<div class="version_block">' +
						'	<div class="version_block_inside" ' + (item[1].version == 'UNKNOWN'?'style="background:#ff5050"':'') + '>'+
						'		<div class="version_project_name inline">' +
						'			<span>' + item[0] + '</span>' +
						'		</div>' +
						'		<div class="version inline">' +
						'			<span>' + item[1].version + '|' + item[1].buildtime + '</span>' +
						'		</div>' +
						'	</div>'+
						'</div>';
					document.getElementsByClassName("version_view")[0].innerHTML += html;
				}
				
			}

		}
	});
}

function addVersion(project_name, url) {
	param.push([project_name,url]);
}

function clear(){
	document.getElementsByClassName("version_view")[0].innerHTML = '';
}

function addIRISPRSVersion(qa, system) {
	addVersion('IRIS_'+ system.toUpperCase()  +'_PRS_QA' + qa, 'http://irisqa'+ qa +'.oocl.com/wls_prs_'+ system +'/verification/version.jsp');
}

function addIRISDOMVersion(qa, system) {
	addVersion('IRIS_'+ system.toUpperCase() +'_DOM_QA' + qa, 'http://irisbackendqa' + qa +'.oocl.com/wls_dom_'+ system +'/verification/version.jsp');
}

function addIRISPPVersion(system) {
	addVersion('IRIS_'+ system.toUpperCase()  +'_PRS_PP', 'http://irispp.oocl.com/wls_prs_'+ system +'/verification/version.jsp');
	addVersion('IRIS_'+ system.toUpperCase() +'_DOM_PP', 'http://irisbackendpp.oocl.com/wls_dom_'+ system +'/verification/version.jsp')
}


function addIRISVersion(qa, system){
	addIRISPRSVersion(qa,system);
	addIRISDOMVersion(qa,system);
}

addIRISVersion('1','sps');
addIRISVersion('2','sps');
addIRISVersion('3','sps');
addIRISVersion('4','sps');


addIRISVersion('1','sppm');
addIRISVersion('2','sppm');
addIRISVersion('3','sppm');
addIRISVersion('4','sppm');

addIRISPPVersion('sps')
addIRISPPVersion('sppm');

addVersion('Supplier Portal QA','http://hk3cvdv00149:8111/wls_dom_sp/verification/version.jsp');
addVersion('Supplier Portal UAT','http://hk3cvdv00319:8111/wls_dom_sp/verification/version.jsp');

version_refresh();