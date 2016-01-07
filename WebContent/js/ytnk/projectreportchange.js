//项目期报页面,下拉框切换时调用
function xmztselectChange(selectBoxOBJ){
	var xmzt = document.getElementById('projectreportPage_guid_projectreportRpt_condition_xmzt').value;							
	$('#projectreportPage_guid_projectreportleftRpt_condition_xmzt2').val(xmzt);
	search({pageid:'projectreportPage',reportid:'projectreportleftRpt'});
}
function djztselectChange(selectBoxOBJ){
	var djzt = document.getElementById('projectreportPage_guid_projectreportRpt_condition_djzt').value;							
	$('#projectreportPage_guid_projectreportrightRpt_condition_djzt2').val(djzt);
	search({pageid:'projectreportPage',reportid:'projectreportrightRpt'});
}
function xmztselectviewChange(selectBoxOBJ){
	var xmzt = document.getElementById('projectreportviewPage_guid_projectreportviewRpt_condition_xmzt').value;							
	$('#projectreportviewPage_guid_projectreportviewleftRpt_condition_xmzt2').val(xmzt);
	search({pageid:'projectreportviewPage',reportid:'projectreportviewleftRpt'});
}
function djztselectviewChange(selectBoxOBJ){
	var djzt = document.getElementById('projectreportviewPage_guid_projectreportviewRpt_condition_djzt').value;							
	$('#projectreportviewPage_guid_projectreportviewrightRpt_condition_djzt2').val(djzt);
	search({pageid:'projectreportviewPage',reportid:'projectreportviewrightRpt'});
}
function addclick(){
	var selectedRoleIds = getSelectRowCols({'pageid':'projectreportPage','reportid':'projectreportleftRpt','colnames':['F_PROJECT_ID']});
	//alert(JSON.stringify(selectedRoleIds));
	var code = selectedRoleIds[0]['F_PROJECT_ID'].value;
	//alert(code);
	//window.focus("http://localhost:8080/bmnk/jbsf.sr?PAGEID=zhoubaoaddPage");
	location.href="http://localhost:8080/bmnk/jbsf.sr?PAGEID=zhoubaoaddPage&code="+code;
	//window.open ('http://localhost:8080/bmnk/jbsf.sr?PAGEID=zhoubaoaddPage&code='+code,'newwindow')  
}
function saveclick(){
	try{saveEditableReportData({
			pageid:'zhoubaoaddPage',
			savingReportIds:[{
				reportid:'zhoubaoaddRpt',
				updatetype:'save'}]
				})
		}catch(e){
			logErrorsAsJsFileLoad(e);
		};
	try{saveEditableReportData({
			pageid:'zhoubaoaddPage',
			savingReportIds:[{reportid:'tabRpt',
				updatetype:'save'}]
				})
		}catch(e){
			logErrorsAsJsFileLoad(e);
		}
}
function guolvedit(){
	changeStyle('zhoubaoaddPage_guid_zhoubaoaddRpt_F_DJBH');
	changeStyle('zhoubaoaddPage_guid_zhoubaoaddRpt_F_ND');
	changeStyle('zhoubaoaddPage_guid_zhoubaoaddRpt_F_QSRQ');
	changeStyle('zhoubaoaddPage_guid_zhoubaoaddRpt_F_ZZRQ');
	changeStyle('zhoubaoaddPage_guid_zhoubaoaddRpt_F_XM');
	changeStyle('zhoubaoaddPage_guid_zhoubaoaddRpt_F_LRRXM');
	changeStyle('zhoubaoaddPage_guid_zhoubaoaddRpt_F_LRRQ');
	changeStyle('zhoubaoaddPage_guid_zhoubaoaddRpt_F_TJRXM');
	changeStyle('zhoubaoaddPage_guid_zhoubaoaddRpt_F_TJRQ');
	changeStyle('zhoubaoaddPage_guid_zhoubaoaddRpt_F_SHRXM');
	changeStyle('zhoubaoaddPage_guid_zhoubaoaddRpt_F_SHRQ');
	changeStyle('zhoubaoaddPage_guid_zhoubaoaddRpt_F_DJZT');
}
function changeStyle(obj){
	$("#"+obj).attr("disabled",true);
	$("#"+obj).css("border","0px");
	$("#"+obj).css("overflow","hidden");
	$("#"+obj).css("width","95%");
	$("#"+obj).css("background-color","#fff");
}
//提交单据和取消提交单据
function submitOrCancel(type){
	var tip;
	if(type =="submit" )
		tip = "提交"
	else if(type == "cancelsubmit"){
		tip = "取消提交"
	}else{
		wx_error("操作失败");
		return;
	}
	var selectedRoleIds = getSelectRowCols({'pageid':'projectreportPage','reportid':'projectreportrightRpt','colnames':['F_DJBH']});
	if(selectedRoleIds == null){
		wx_warn("请选择一条单据！");
		return;
	}
	var djbh = selectedRoleIds[0]['F_DJBH'].value;
	wx_confirm("确定要"+tip+"该单据吗？","提示",null,null,function(){
		$.ajax({
			url:"jf/projectwork?djbh="+djbh+"&type="+type,
			dataType:"json",
			type: "get",
			success:function (data, textStatus) {
				if(data.code=="success"){
					wx_success(data.message);
					search({pageid:'projectreportPage',reportid:'projectreportleftRpt'});//此处为刷新哪一部分
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
function zhoubaoeditsave(){
	try{saveEditableReportData({
		pageid:'zhoubaoeditPage',
		savingReportIds:[{reportid:'zhoubaoaeditRpt',updatetype:'save'}]
		})
		}catch(e){
			logErrorsAsJsFileLoad(e);
		}
}
//周报审核
function shenhe(){
	var selectedRoleIds = getSelectRowCols({'pageid':'xmzhoubaoshenhePage','reportid':'xmzhoubaoshenherightRpt','colnames':['F_DJBH']});
	if(selectedRoleIds == null){
		wx_warn("请选择一条单据！");
		return;
	}
	var djbh = selectedRoleIds[0]['F_DJBH'].value;
	wx_confirm("确定要审核该单据吗？","提示",null,null,function(){
		$.ajax({
			url:"jf/projectshenhe?djbh="+djbh,
			dataType:"json",
			type: "get",
			success:function (data, textStatus) {
				if(data.code=="success"){
					wx_success(data.message);
					search({pageid:'xmzhoubaoshenhePage',reportid:'xmzhoubaoshenheleftRpt'});//此处为刷新哪一部分
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