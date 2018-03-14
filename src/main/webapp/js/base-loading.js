/*******************************************
 * 
 * 创建人：Quber（qubernet@163.com）
 * 创建时间：2014年6月10日
 * 创建说明：Base=>页面加载（loading）效果
 * 
 * 修改人：
 * 修改时间：
 * 修改说明：
 * 
*********************************************/
var bootPATH = __CreateJSPath("boot.js");
document.write('<link rel="stylesheet" type="text/css" href="'+bootPATH+'/css/loading.css"/>');
document.write('<div id="loadingmain" style="position:absolute;top:0;left:0;right:0;bottom:0;"><div id="loading">'+
			'<span class="three-quarters-loader">Loading…</span>'+
			'<div style="color:#fff;text-align:center;font-family:微软雅黑;font-size:13px">正在加载</div>'+
		'</div></div>');
//监听加载状态改变
document.onreadystatechange = completeLoading;
//加载状态为complete时移除loading效果
function completeLoading() {
    if (document.readyState == 'complete') {
        var loadingMask = document.getElementById('loadingmain');
        loadingMask.parentNode.removeChild(loadingMask);
    }
}
