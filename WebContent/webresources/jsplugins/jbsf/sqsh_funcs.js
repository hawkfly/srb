function countpayFunc(pageid, reportguid, selectedTrObjArr)//每次对选中行进行重新计算填充
{
	var selectedRowArry = getEditableReportColValues(pageid, reportguid, {plan_pay:true, pay_type:true}, {name:'SELECTEDROW', value:true});
	var count = 0;
	if(selectedRowArry != null){
		for(var i = 0; i < selectedRowArry.length; i++)
		{
			var rowObj = selectedRowArry[i];
			if(rowObj.pay_type.value == 0)count = count + parseInt(rowObj.plan_pay.value);
		}
		$("#count").html(count);
	}else{
		$("#count").html(0);
	}
}

function payFunc(pageid, reportid, sqlbuttonname){
	var selectedRowArry = getEditableReportColValues(pageid, reportid, {id:true}, {name:'SELECTEDROW', value:true});
	if(selectedRowArry != null){
		var idary = [];
		for(var i = 0; i < selectedRowArry.length; i++){
			var idjson = {};
			var rowObj = selectedRowArry[i];
			idjson.id = rowObj.id.value;
			idary.push(idjson);
		}
		invokeComponentSqlActionButton(pageid, reportid, sqlbuttonname, idary, null, false, null);
	}else{
		wx_warn("请至少选择一行进行缴费！");
	}
}

function showdetail(payid,pageid){
	wx_winpage($.jbsf.core.contextpath()+'/jbsf.sr?PAGEID='+pageid+'&txtpayid='+payid, {width:'60%', height:'80%'});
}

function dosearch(pageid, reportid, searchparamobj, postparamobj){
	searchReportData(pageid, reportid, searchparamobj);
	//执行ajax以获取当前用户未交费条数、新的报修条数以及新的问题条数
	$.post($.jbsf.core.contextpath()+"/jf/getnopaycount", postparamobj, function(data){//未缴费条数
		if(data != "0")setTimeout("appendnum2Tabtitle4pay("+data+")",600);
	});
	$.post($.jbsf.core.contextpath()+"/jf/getnewquestioncount", postparamobj, function(data){//新问题条数
		if(data != "0")setTimeout("appendnum2Tabtitle4question("+data+")",600);
	});
}

function dosearchfirst(){
	$.post($.jbsf.core.contextpath()+"/jf/initfirstpptorcount", function(data){
		if(data.paycount != "0")setTimeout("appendnum2Tabtitle4pay("+data.paycount+")",600);
		if(data.questioncount != "0")setTimeout("appendnum2Tabtitle4question("+data.questioncount+")",600);
	});
}

function appendnum2Tabtitle4pay(data){
	$("#pprstreePage_guid_pprsubiteminfoPanel_1_title").append("<font color=red>("+data+")</font>");
}

function appendnum2Tabtitle4question(data){
	$("#pprstreePage_guid_pprsubiteminfoPanel_3_title").append("<font color=red>("+data+")</font>");
}

function canculatePlanpay(pageid, reportid){
	//alert(pageid+reportid);
	var guid = getComponentGuidById(pageid, reportid);
	$("input[id^='"+guid+"_end_num']").blur(function(){
		var selectedRowArry = getEditableReportColValues(pageid, reportid, {price:true, start_num:true, end_num:true}, {name:'SELECTEDROW', value:true});
		if(selectedRowArry != null){
			for(var i = 0; i < selectedRowArry.length; i++){
				var rowObj = selectedRowArry[i];
				var price = rowObj.price.value;
				var crow = rowObj.end_num.value - rowObj.start_num.value;
				if(crow < 0)crow = 0;
				var pp = crow * price;
				setReportInputBoxValue(pageid, reportid, false, {plan_pay: pp+""}, {name:'SELECTEDROW', value:true});
			}
		}
	});
}
