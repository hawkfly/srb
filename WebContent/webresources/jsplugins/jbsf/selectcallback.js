/*
 * 单行选择点击下拉列表与复选框进行联动
 * @project SDCMW
 * @author hawkFly
 * @remark checkbox
 * */
function changeCheckboxes(pageid, reportguid, selectedTrObjArr){
	var trid = selectedTrObjArr[0].id;
	trindex = trid.substring(trid.lastIndexOf("_")+1, trid.length);
	var reportid = reportguid.substring(reportguid.lastIndexOf("_")+1, reportguid.length);
	$("#"+pageid+"_guid_"+reportid+"_siteid__"+trindex).change(function(){
		//{name:'SELECTEDROW', value:true}
		invokeServerActionForReportData(pageid,reportid,'com.wbcs.jbsf.action.ChangeCheckboxesAction',{name:'SELECTEDROW', value:true},{},false,null,changedsitecallback);
	});
}

function changedsitecallback(dataObj){//dataObj.returnValue, dataObj.pageid, dataObj.componentid
	//alert(dataObj.returnValue);
	var checkboxValAry = eval(dataObj.returnValue);
	var brstr = "";
	var checkboxitem = "";
	var j = 1;
	for ( var i = 0; i < checkboxValAry.length; i++) {
		for(var item in checkboxValAry[i]){
			if(j%8 == 0){
				brstr = "<br/>";
			}else{
				brstr = "";
			}
			var checkboxitem = checkboxitem +  
			'<input id="roles2userPage_guid_relationRpt_sectionid__'+trindex+'" type="checkbox" separator=" " onkeypress="return onInputBoxKeyPress(event);" onclick="this.focus();" '+
			"onblur=\"try{addInputboxDataForSaving('roles2userPage_guid_relationRpt',this);}catch(e){logErrorsAsJsFileLoad(e);};try{fillInputBoxValueToParentTd(this,'checkbox','roles2userPage_guid_relationRpt','listform',1);}catch(e){logErrorsAsJsFileLoad(e);}\""+ 
			'value="'+item+'" label="'+checkboxValAry[i][item]+'" name="roles2userPage_guid_relationRpt_sectionid__'+trindex+'" typename="checkbox">'+checkboxValAry[i][item] + brstr;
			j = j+1;
		}
		 j = 1;
	}
	if($('#roles2userPage_guid_relationRpt_sectionid__'+trindex).parent()[0] == null){
		//roles2userPage_guid_relationRpt_sectionid__td0
		$('#roles2userPage_guid_relationRpt_sectionid__td'+trindex).html(checkboxitem);
	}else{
		$('#roles2userPage_guid_relationRpt_sectionid__'+trindex).parent().html(checkboxitem);
	}
	
	checkboxitem = "";
}
//单行被选中时，触发该方法
function onselectRow(pageid,rptid,rows){
	
	unselectRow('roles2userPage','roleSelectRpt','id');
	var selectedRoleIds = getSelectRowCols({'pageid':pageid,'reportid':rptid,'colnames':['id']});
	var userid = selectedRoleIds[0]['id'].value;
	var status = actions({'serverClassName':'com.pansoft.jbsf.wbcs.action.IVGetSelectedRoleByUserId', 'datas':{key:userid},'afterCallbackMethod':function(data){
		var str = data.responseText;
		var roleids = str.trim().split(',');
		for(var i = 0;i<roleids.length;i++)
		{
			//还原选中的用户的角色————》
			selectRow('roles2userPage','roleSelectRpt','id',roleids[i]);
		}
	}});
}