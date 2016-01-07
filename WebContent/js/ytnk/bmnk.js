var obj = "editWorkPage_guid_leftGridRpt_F_GS__0";
var tdobj="editWorkPage_guid_leftGridRpt_F_GS__td0";
var markTr = "editWorkPage_guid_leftGridRpt_tr_0";
var curGS;
var curBtn;
function setSearchVal(){
	addEvent();//绑定事件
	var status = document.getElementById('editWorkPage_guid_serchRpt_condition_status').value;							
	var year = document.getElementById('editWorkPage_guid_serchRpt_condition_year').value;
	$('#editWorkPage_guid_leftGridRpt_condition_yyyy').val(year);
	$('#editWorkPage_guid_leftGridRpt_condition_djzt').val(status);
	//search({pageid:'editWorkPage',reportid:'leftGridRpt'});
	//try{searchReportData('editWorkPage','leftGridRpt')}catch(e){logErrorsAsJsFileLoad(e);}
	//打开时初始化数据
	setTimeout("go()",200);
	//actions({'serverClassName':'com.pansoft.jbsf.wbcs.action.ytnk.initData', 'datas':{key:''}});
}
function changeCSS(){
	$("#editWorkPage_guid_rightCard_F_DJBH").css("width","97%");
	$("#editWorkPage_guid_rightCard_F_DJBH").css("overflow","hidden");
	$("#editWorkPage_guid_rightCard_F_DJBH").css("border","0px");
	$("#editWorkPage_guid_rightCard_F_DJBH").css("background-color","#fff");
	$("#editWorkPage_guid_rightCard_F_DJBH").attr("disabled",true);
	
	$("#editWorkPage_guid_rightCard_F_XMMC").css("width","97%");
	$("#editWorkPage_guid_rightCard_F_XMMC").css("cursor","pointer");
	$("#editWorkPage_guid_rightCard_F_XMMC").css("white-space","nowrap");
	
	$("#editWorkPage_guid_rightCard_F_NOTE").css("width","99%");
	curGS = $("#"+obj).attr("value");
}
function changeCSS2(){
	$("#checkWorkPage_guid_rightTopCardRpt_F_DJBH").css("width","97%");
	$("#checkWorkPage_guid_rightTopCardRpt_F_DJBH").css("overflow","hidden");
	$("#checkWorkPage_guid_rightTopCardRpt_F_DJBH").css("border","0px");
	$("#checkWorkPage_guid_rightTopCardRpt_F_DJBH").css("background-color","#fff");
	$("#checkWorkPage_guid_rightTopCardRpt_F_DJBH").attr("disabled",true);
	
	$("#checkWorkPage_guid_rightTopCardRpt_F_XMMC").css("width","97%");
	$("#checkWorkPage_guid_rightTopCardRpt_F_XMMC").css("border","0px");
	$("#checkWorkPage_guid_rightTopCardRpt_F_XMMC").css("background-color","#fff");
	$("#checkWorkPage_guid_rightTopCardRpt_F_XMMC").css("white-space","nowrap");
	$("#checkWorkPage_guid_rightTopCardRpt_F_XMMC").attr("disabled",true);
	
	$("#checkWorkPage_guid_rightTopCardRpt_F_NOTE").css("width","97%");
	$("#checkWorkPage_guid_rightTopCardRpt_F_NOTE").css("border","0px");
	$("#checkWorkPage_guid_rightTopCardRpt_F_NOTE").css("background-color","#fff");
	$("#checkWorkPage_guid_rightTopCardRpt_F_NOTE").css("white-space","nowrap");
	$("#checkWorkPage_guid_rightTopCardRpt_F_NOTE").attr("disabled",true);
	//changeReportAccessMode('jiaotongwucanbuzhu_guid_jiaotongwucanrpt','add');
}
function changeCSS3(){
	for(var i=0;i<7;i++){
		$("#editWorkPage_guid_tabLeftRpt_F_NOTE__"+i).css("width","99%");
		$("#editWorkPage_guid_tabLeftRpt_F_AMXMMC__"+i).css("width","99%");
		$("#editWorkPage_guid_tabLeftRpt_F_PMXMMC__"+i).css("width","99%");
	}
}
function go(){
	$(".cls-button").click();
	search({pageid:'editWorkPage',reportid:'leftGridRpt'});
	saveEditableReportData();onKeyEvent(event);
}
//取消提交
function cancelSubmit(){
	var selectedRoleIds = getSelectRowCols({'pageid':'editWorkPage','reportid':'leftGridRpt','colnames':['F_DJBH']});
	var djbh = selectedRoleIds[0]['F_DJBH'].value;
	wx_confirm("确定取消提交吗？","提示",null,null,function(){
		$.ajax({
			url:"jf/cancelsubmit?djbh="+djbh,
			dataType:"json",
			type: "get",
			success:function (data, textStatus) {
				if(data.code=="success"){
					wx_success(data.message);
					search({pageid:'editWorkPage',reportid:'leftGridRpt'});
				}else if(data.code=="fail"){
					wx_error(data.message);
				}else if(data.code=="warn"){
					wx_warn(data.message);
				}
			},
			cache:  false,
			timeout:60000,
			error:function(XMLHttpRequest, textStatus, errorThrown){
				if(textStatus=="timeout"){
					alert("获取数据超时！");
				}
			}
		});
	});
}
//审核(退回)
function checkAndBack(type){
	var tip;
	if(type=="check"){
		tip = "审核";
	}else if(type=="back"){
		tip = "退回";
	}else{
		wx_error("数据异常");
		return;
	}
	var selectedRoleIds = getSelectRowCols({'pageid':'checkWorkPage','reportid':'leftListRpt','colnames':['F_DJBH']});
	var djbh = selectedRoleIds[0]['F_DJBH'].value;
	wx_confirm("确定要"+tip+"该单据吗？","提示",null,null,function(){
		$.ajax({
			url:"jf/checkAndBack?djbh="+djbh+"&type="+type,
			dataType:"json",
			type: "get",
			success:function (data, textStatus) {
				if(data.code=="success"){
					wx_success(data.message);
					search({pageid:'checkWorkPage',reportid:'leftListRpt'});
				}else if(data.code=="fail"){
					wx_error(data.message);
				}else if(data.code=="warn"){
					wx_warn(data.message);
				}
			},
			cache:  false,
			timeout:60000,
			error:function(XMLHttpRequest, textStatus, errorThrown){
				if(textStatus=="timeout"){
					alert("获取数据超时！");
				}
			}
		});
	});
}
function resetGS(){
	var selectedRoleIds = getSelectRowCols({'pageid':'editWorkPage','reportid':'leftGridRpt','colnames':['F_GS']});
	var gs = selectedRoleIds[0]['F_GS'].value;
	curGS = gs
}
function addEvent(){
	$("#editWorkPage_guid_leftGridRpt_data tr").live("click",function(){
		var id = this.id;
		 tdobj = $("#"+id+" td:eq(4)").attr("id");
		$("#"+id+" input").attr("disabled",true);
		obj = $("#"+id+" input").attr("id");
	});
	$("#editWorkPage_guid_tabLeftRpt_data tr td input").live("click",function(){
		var id = this.id;
		var checkBoxId = $("#"+id).attr("checked");
		var typename = $("#"+id).attr("typename");
		if(typename =="textbox"||typename =="popupbox"){
			return;
		}
		if(checkBoxId == "checked"){
			var oldvalue = $("#"+obj).attr("value");
			var newvalue = Number(oldvalue)+0.5;
			//$("#"+obj).attr("value",newvalue);
			//$("#"+tdobj).attr("value",newvalue);
			setEditablelistColVal('editWorkPage', 'leftGridRpt', {'F_GS': newvalue+""}, {name:'SELECTEDROW', value:true});
		}else{			
			var oldvalue = $("#"+obj).attr("value");
			var newvalue = Number(oldvalue)-0.5;
			//$("#"+obj).attr("value",newvalue);
			//$("#"+tdobj).attr("oldvalue",newvalue);
			setEditablelistColVal('editWorkPage', 'leftGridRpt', {'F_GS': newvalue+""}, {name:'SELECTEDROW', value:true});
		}
	});
}
function setValBeforeSave(pageid,reportid,type){
	if(curBtn=="save"){
		setMyParamVals({pageid: pageid,reportid: reportid,myparamname: 'djzt',myparamval :'0'});
	}else if(curBtn=="submit"){
		setMyParamVals({pageid: pageid,reportid: reportid,myparamname: 'djzt',myparamval :'1'});
	}
	return WX_SAVE_CONTINUE;
}
function fleshPage(){
	//changeReportAccessMode("editWorkPage_guid_tabLeftRpt", 'readonly');
	search({pageid:'editWorkPage',reportid:'leftGridRpt'});
}
function setMark(data){
	curBtn = data;
}
function doPreSelectRowFunc(rowid){
	var id = rowid.getAttribute("id");
	if(id.indexOf('editWorkPage_guid_leftGridRpt')==0){
		var selectedRoleIds = getSelectRowCols({'pageid':'editWorkPage','reportid':'leftGridRpt','colnames':['F_GS']});
		var gs = selectedRoleIds[0]['F_GS'].value;
		if(gs!=curGS){
			setEditablelistColVal('editWorkPage', 'leftGridRpt', {'F_GS': curGS+""}, {name:'SELECTEDROW', value:true});
		}
	}
}
function changeBtnStatus(){
	var status = document.getElementById('editWorkPage_guid_serchRpt_condition_status').value;
	if(status=="0"){
		var input = document.getElementsByTagName("input");
		for(var i=0;i<input.length;i++){
			var id = input[i].id;
			if(id.indexOf("tabLeftRpt")>=0||id.indexOf("rightCard")>=0){
				$(input[i]).attr("disabled",false);
				$(input[i]).css("border","1px");
				$(input[i]).css("background-color","#fff");
			}
		}
	}else{
		var input = document.getElementsByTagName("input");
		for(var i=0;i<input.length;i++){
			var id = input[i].id;
			if(id.indexOf("tabLeftRpt")>=0||id.indexOf("rightCard")>=0){
				$(input[i]).attr("disabled",true);
				$(input[i]).css("border","0px");
				$(input[i]).css("background-color","#fff");
			}
		}
	}
}