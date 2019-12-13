function toggleBox(){
	document.getElementById('message-box').style.display=document.getElementById('message-box').style.display=='none'?'block':'none';
}
function showBox(){
	document.getElementById('message-box').style.display='block';
}
function loadHtmlAndShow(generator,items,title){
	let html='';
	for(var index in items){
		html+=generator(items[index]);
	}
	document.getElementById('message-box-content-view').innerHTML=html;
	if(title){
		document.getElementById('message-box-title').innerHTML=title;
	}
	document.getElementById("message-box").style.left = '';
	document.getElementById("message-box").style.top = '';
	showBox();
}
function showCountingLineDetails(items,title,host){
	loadHtmlAndShow(function(item){
		return ''
		+'<div class="counting-line">'
		+'	<div class="counting-line-file-name">'
		+(host?'		<a class="counting-line-file-source" target="_blank" href="' + host + '/component?id=' + item.name +'">Code</a>':'')
		+'		' + item.name
		+'	</div>'
		+'	<div class="counting-line-file-count">'
		+'		' + item.count
		+'	</div>'
		+'</div>';
	},items,title);
}
function showGitLogDetails(items,title){
	loadHtmlAndShow(function(item){
		function pad(num, n) {
			var len = num.toString().length;
			while (len < n) {
				num = '&nbsp;&nbsp;' + num;
				len++;
			}
			return num;
		}
		return ''
		+'<div class="counting-line">'
		+'	<div class="counting-line-file-name">'
		+'		<a class="counting-line-add">Add:&nbsp;' + pad(item.addLines,3) + '</a>'
		+'		<a class="counting-line-remove">Remove:&nbsp;' + pad(item.removeLines,3) + '</a>'
		+'		' + item.filePath
		+'	</div>'
		+'	<div class="counting-line-file-count">'
		+'		-'
		+'	</div>'
		+'</div>';
	},items,title);
}
window.onload = function() {　　
    let div1 = document.getElementById("message-box");
    let header = document.getElementById("message-box-header");　　
    let title = document.getElementById("message-box-header");　　
    let onmousedown = function(ev) {　　　　
        var oevent = ev || event;　　　　
        var distanceX = oevent.clientX - div1.offsetLeft;　　　　
        var distanceY = oevent.clientY - div1.offsetTop;　　　　
        document.onmousemove = function(ev) {　　　　　　
            var oevent = ev || event;　　　　　　
            div1.style.left = oevent.clientX - distanceX + 'px';　　　　　　
            div1.style.top = oevent.clientY - distanceY + 'px';　　　　
        };　　　　
        document.onmouseup = function() {　　　　　　
            document.onmousemove = null;　　　　　　
            document.onmouseup = null;　　　　
        };
    };
    title.onmousedown = onmousedown;
    header.onmousedown = onmousedown;
}