/*
 * 将文本，图集，视频融在一起的保存前执行方法
 * @project SDCMW
 * @author hawkFly
 * */
function sdcm_newsmtn_befsaveFunc(){
	//图集列表保存
	var newsphotosDom = document.getElementById("J_Urls");
	setCustomizeParamValue('newsmngPage','newsmtRpt','ZYXX',newsphotosDom.value);
	//uploader.uploadFiles("waiting");
	//视频保存
	return WX_SAVE_CONTINUE;
}
function sdcm_newstxt_befsaveFunc(pageid, reportid, type){
	$("input[type=button]").each(function(){
		$(this).attr('disabled',true);
	});
	var cstmBtnType = $.jbsf.params.cstmbtntype;
	if(cstmBtnType){//取用户自定义按钮类型
		if(cstmBtnType == 'craft'){//草稿
			setCustomizeParamValue(pageid, reportid, 'XWZT', '0');
		}else if(cstmBtnType == 'check'){//送审
			setCustomizeParamValue(pageid, reportid, 'XWZT', '1');
		}else if(cstmBtnType == 'goback'){//退回
			var row = getEditableReportColValues(pageid,reportid,{FBR:true});
			setCustomizeParamValue(pageid, reportid, 'XWZT', '3');
			setCustomizeParamValue(pageid, reportid, 'CZR', row.FBR.value);
		}else if(cstmBtnType == 'publish'){//发布
			setCustomizeParamValue(pageid, reportid, 'XWZT', '2');
		}
	}
	return WX_SAVE_CONTINUE;
}
/*
 * 分离后的保存前执行方法
 * @project SDCMW
 * @author hawkFly
 * */
function sdcm_newsphotos_befsaveFunc(pageid, reportid, type){
	//var type = type[0]['WX_TYPE'];//update,add,delete
	$("input[type=button]").each(function(){
		$(this).attr('disabled',true);
	});
	var cstmBtnType = $.jbsf.params.cstmbtntype;
	if(cstmBtnType){//取用户自定义按钮类型
		if(cstmBtnType == 'craft'){//草稿
			setCustomizeParamValue(pageid, reportid, 'XWZT', '0');
		}else if(cstmBtnType == 'check'){//送审
			setCustomizeParamValue(pageid, reportid, 'XWZT', '1');
		}else if(cstmBtnType == 'goback'){//退回
			var row = getEditableReportColValues(pageid,reportid,{FBR:true});
			setCustomizeParamValue(pageid, reportid, 'XWZT', '3');
			setCustomizeParamValue(pageid, reportid, 'CZR', row.FBR.value);
		}else if(cstmBtnType == 'publish'){//发布
			setCustomizeParamValue(pageid, reportid, 'XWZT', '2');
		}
	}
	var newsphotosDom = document.getElementById("J_Urls");
	if(newsphotosDom.value == null || newsphotosDom.value == ''){//图集新闻未上传图片集
		wx_warn('图集新闻必须上传至少一张图片！');
		$("input[type=button]").each(function(){
			$(this).attr('disabled',false);
		});
		return WX_SAVE_TERMINAT;
	}
	setCustomizeParamValue(pageid,reportid,'ZYXX',newsphotosDom.value);
	/*var newssytpNum = document.getElementById("sytpinputext").value;
	if(!$.jbsf.validate.isDigit(newssytpNum)){
		wx_warn('指定的索引图片编号必选为数字！');
		return WX_SAVE_TERMINAT;
	}
	if(newssytpNum < 1 || newssytpNum > newsphotosDom.value.split(',').length){
		wx_warn('指定的索引图片编号必选在1至'+newsphotosDom.value.split(',').length+'之间！');
		return WX_SAVE_TERMINAT;
	}
	setCustomizeParamValue('newsphotosPage','newsphotosRpt','SYTP',newssytpNum);*/
	setCustomizeParamValue(pageid,reportid,'newsid',document.getElementById("newsid").value);
	//alert(WX_SAVE_CONTINUE);
	return WX_SAVE_CONTINUE;
}

function sdcm_newsphotos_sqlbutton_befsaveFunc(dataArray, paramsObj){
	//将按钮置于不可编辑状态
	$("input[type=button]").each(function(){
		$(this).attr('disabled',true);
	});
	var newsphotosDom = document.getElementById("J_Urls");
	if(newsphotosDom.value == null || newsphotosDom.value == ''){//图集新闻未上传图片集
		wx_warn('图集新闻必须上传至少一张图片！');
		return false;
	}
	dataArray[0].ZYXX = newsphotosDom.value;
	//alert(dataArray[0].ZYXX + "(wbcsbefsave 76Line)")
	dataArray[0].newsid = document.getElementById("newsid").value;
	setTimeout("delmsgdialog()", 10);
	
	return true;
}

function sdcm_newsvideo_befsaveFunc(pageid, reportid, type){
	$("input[type=button]").each(function(){
		$(this).attr('disabled',true);
	});
	var cstmBtnType = $.jbsf.params.cstmbtntype;
	if(cstmBtnType){//取用户自定义按钮类型
		if(cstmBtnType == 'craft'){//草稿
			setCustomizeParamValue(pageid, reportid, 'XWZT', '0');
		}else if(cstmBtnType == 'check'){//送审
			setCustomizeParamValue(pageid, reportid, 'XWZT', '1');
		}else if(cstmBtnType == 'goback'){//退回
			var row = getEditableReportColValues(pageid,reportid,{FBR:true});
			setCustomizeParamValue(pageid, reportid, 'XWZT', '3');
			setCustomizeParamValue(pageid, reportid, 'CZR', row.FBR.value);
		}else if(cstmBtnType == 'publish'){//发布
			setCustomizeParamValue(pageid, reportid, 'XWZT', '2');
		}
	}
	
	var guid = getComponentGuidById(pageid, reportid);
	var manualimgurl = $("#"+guid+"_SYTPLJ").attr("src");
	
	var newsvideoDom = document.getElementById("videoUrl");
	if(newsvideoDom.value == ''){
		wx_warn('必须上传一个视频并生效，如果您已经上传等耐心等待片刻再次点击保存!');
		$("input[type=button]").each(function(){
			$(this).attr('disabled',false);
		});
		return WX_SAVE_TERMINAT;
	}
	else{
		setCustomizeParamValue(pageid, reportid, 'ZYLJB', newsvideoDom.value);
	}
	
	var newsvideotitleDom = document.getElementById("videoname");
	setCustomizeParamValue(pageid, reportid, 'VIDEOBT', newsvideotitleDom.value);
	
	var cutimgrdis = document.getElementsByName("cutimgvalue");
	var isChecked = false;
	var cutimgurl = manualimgurl;
	if(cutimgurl == '' || cutimgurl == null || cutimgurl.endWith('nopicture.gif')){//用户未上传
		for(var i = 0; i < cutimgrdis.length; i++){
			if(cutimgrdis[i].checked){
				cutimgurl = cutimgrdis[i].value;
				isChecked = true;
				break;
			}
		}
	}
	
	if(cutimgurl == ''){
		wx_warn('必须指定一张索引图片!');
		$("input[type=button]").each(function(){
			$(this).attr('disabled',false);
		});
		return WX_SAVE_TERMINAT;
	}else{
		setCustomizeParamValue(pageid, reportid, 'ZYLJA', cutimgurl);//索引图片路径
	}
	return WX_SAVE_CONTINUE;
}

function sdcm_activevent_befsaveFunc(pageid, reportid, type){
	var cstmBtnType = $.jbsf.params.cstmbtntype;
	if(cstmBtnType){//取用户自定义按钮类型
		if(cstmBtnType == 'craft'){//草稿
			setCustomizeParamValue(pageid, reportid, 'XWZT', '0');
		}else if(cstmBtnType == 'check'){//送审
			setCustomizeParamValue(pageid, reportid, 'XWZT', '1');
		}else if(cstmBtnType == 'goback'){//退回
			var row = getEditableReportColValues(pageid,reportid,{FBR:true});
			setCustomizeParamValue(pageid, reportid, 'XWZT', '3');
			//setCustomizeParamValue(pageid, reportid, 'CZR', row.FBR.value);
		}
	}
	return WX_SAVE_CONTINUE;
}

function sdcm_videoInternetTVUpload_befsaveFunc(pageid, reportid, type){
	//setCustomizeParamValue(pageid, reportid, 'path', path);//视频路径
	var videourl = $("#videoUrl").val();
	setCustomizeParamValue(pageid, reportid, 'path', videourl);
	//setCustomizeParamValue(pageid, reportid, 'ico', ico)//展示图片
	var cutimgrdis = document.getElementsByName("cutimgvalue");
	var isChecked = false;
	var cutimgurl = '';
	for(var i = 0; i < cutimgrdis.length; i++){
		if(cutimgrdis[i].checked){
			cutimgurl = cutimgrdis[i].value;
			isChecked = true;
			break;
		}
	}
	setCustomizeParamValue(pageid, reportid, 'ico', cutimgurl);
	
	return WX_SAVE_CONTINUE;
}
/**
 * 在使用sqlbutton按钮的时候取消掉“否放弃保存”提示
 * @param datasArray 存放本次调用要传入到后台服务器端类的所有行所有列的数据
 * @param paramsObj  
 */
function sqlbutton_nonalertmessage_befCallbackFunc(datasArray, paramsObj){
	/*var currentpageid = $('#currentpageid').val();
	var currentrptid = $('#currentrptid').val();
	var componentguid=getComponentGuidById(currentpageid, currentrptid);
	doAddDataForSaving(componentguid,true);*/
	$.jbsf.params.searchmsgdialogSum = 0;
	setTimeout("delmsgdialog()", 5);
	
	return true;
}
function delmsgdialog(){
	var isin = false;
	parent.$(".aui_content").each(function(){
		if($(this).text() == "是否放弃对报表数据的修改？"){
			parent.$(".aui_state_highlight").click();
			$(this).text("是否确定发布此新闻？");
			isin = true;
		}
	});
	if(isin){
		return;
	}else{
		$.jbsf.params.searchmsgdialogSum = $.jbsf.params.searchmsgdialogSum+1;
		if($.jbsf.params.searchmsgdialogSum < 11){
			setTimeout("delmsgdialog()", 5);
		}else{
			return;	
		}	
	}
}
/*-----------------------------------------------------------------------------------------------------------*/
/**
 * 权限管控部分
 */
function auth_userrole_befsaveFunc(pageid, reportid,dataObjArr){
	for ( var i = 0; i < dataObjArr.length; i++) {
		if(dataObjArr[i]['WX_TYPE'] == 'update'){
			var suspendroleid = $.jbsf.util.getCustomizeComponentValue('roleid');
			var deletedusercode = $.jbsf.util.getCustomizeComponentValue('usercode');
			setCustomizeParamValue(pageid, reportid, 'usercode', deletedusercode);
			setCustomizeParamValue(pageid, reportid, 'roleid', suspendroleid);
		}
	}
}

function auth_usersections_befsaveFunc(pageid, reportid, dataObjArr){
	
}
/*
 * 修改密码
 */
function isInputPwd2True(pageid, reportid, dataObjArr){
	if(dataObjArr[0]["password"] == dataObjArr[0]["passwordvalidate"]){
		return WX_SAVE_CONTINUE;
	}else{
		wx_error("两次密码输入不一致！");
		setInputboxValueForDetailReport(pageid, reportid, "password", "");
		setInputboxValueForDetailReport(pageid, reportid, "passwordvalidate", "");
		return WX_SAVE_TERMINAT;
	}
}

/**
 * 权限人员管理，添加左侧组织结构树后，将组织结构编号和名称，在添加用户的时候进行动态写入
 */
function auth_user_befsaveOrgidnameFunc(pageid, reportid, dataObjArr){
	for ( var i = 0; i < dataObjArr.length; i++) {
		if(dataObjArr[i]['WX_TYPE'] == 'insert'){
			var orgname = $('#orgname').val();
			var orgcdtguid = getCdtGuid(pageid, reportid, 'tjorg');
			var orgid = $('#'+orgcdtguid).val();
			alert(orgid+"|"+orgname);
			setCustomizeParamValue(pageid, reportid, 'orgid', orgid);
			setCustomizeParamValue(pageid, reportid, 'orgname', orgname);
		}
	}
}