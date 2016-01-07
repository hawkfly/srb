/**
 * 其他常用方法签名
 * function beforesave(pageid, reportid, rows)  用于<sql befsave="beforesave"/>
 * 		rows[WX_TYPE=>"add、update等等",列名=>"值"]
 * 		在进行一对多关联多行保存的时候可以通过修改列值的方式执行复杂操作；另外，如果涉及到插入关联表的时候就需要使用doSavePerRow拦截器方法执行保存了
 * 		public int doSavePerRow(ReportRequest rrequest, ReportBean rbean, Map<String,String> mRowData, Map<String,String> mParamValues, AbsEditableReportEditDataBean editbean)
 */
/**
 * 开发人员jsAPI调用封装
 * params参数介绍
 * serverClassName 后台调用类全名
 * datas 	       传递参数，例如：{'key','value'}
 * afterCallbackMethod  回调方法  参考selectcallback文件的changedsitecallback方法
 * onErrorMethod	错误方法处理   dataObj.returnValue, dataObj.pageid, dataObj.componentid
 */
function actions(params){
	invokeServerAction(params.serverClassName, params.datas, params.afterCallbackMethod, params.onErrorMethod);
}
/**
 * 针对单个Report的后台调用方法
 *
 * @param pageid 要操作的报表所在页面<page/>的id属性，必需参数
 * @param reportid 要操作的报表ID，必需参数
 * @param serverClassName 要调用的服务器端类的全限定类名，必需参数
 * @param isRefresh 操作完报表数据后是否要刷新报表显示，可选参数，默认值为true
 * @param conditions 对于editablelist2/listform两种报表类型，指定要传入哪些行的数据到服务器端类，指定方式与上面获取和设置这两种报表类型列数据时的conditionsObj参数完全一致，可选参数
 * @param paras 以json方式指定不是报表数据的其它数据，在被调用的JAVA类的方法中会有一个专门的参数存放这个JS参数传入的值
 * @param befcallback 在取到报表数据后准备传入后台调用前执行，可以在此方法中取到报表数据，并可中止调用的执行
 * @param aftcallback 调用完服务器端类后要执行的客户端回调函数
 */
function actionsRpt(params){
	invokeServerActionForReportData(params.pageid, params.reportid, params.serverClassName, params.conditions, params.paras, params.isRefresh, params.befcallback, params.aftcallback);
}
/**
 * callSqlbutton('authoritymngPage', 'authTreeRpt', 'addauthorityBtn', {id: '1111', pId: treeNode.id, name: '新权限', url:''}, null, false, addnewauthorityCallback);
 */
function callSqlbutton(pageid, reportid, buttonname, datas, params, isRefreshPage, callback){
	invokeComponentSqlActionButton(pageid,reportid,buttonname,datas,params,isRefreshPage,callback);
}
/**
 * 
 * params参数介绍
 * pageid 		页面ID
 * reportid 	报表ID
 * myparamname  自定义传入参数名称
 * myparamval	自定义传入参数值
 */
function setMyParamVals(params){
	setCustomizeParamValue(params.pageid,params.reportid,params.myparamname,params.myparamval);
}
/**
 * 查询API
 * pageid 
 * reportid
 * paramsObj 查询参数
 */
function search(params){
	if(params.paramsObj == null){
		searchReportData(params.pageid,params.reportid);
	}else{
		searchReportData(params.pageid,params.reportid,params.paramsObj);
	}
	
}
/**
 * 获取所有选中行
 * pageid
 * reportid
 * colnames: 想过去的列的名字的数组
 */
function getSelectRowCols(params){
	var cols = {};
	for(var i=0; i < params.colnames.length; i++){
		cols[params.colnames[i]] = true;
	}
	var selectedRows = getEditableReportColValues(params.pageid,params.reportid,cols,{name:'SELECTEDROW', value:true});
	return selectedRows;
}

/**
 * 选中行
 * @param trObj 行对象
 * @param isInvokeOnloadMethod 默认为false，不刷新关联报表，改为true则刷新关联报表
 */
function selectTr(trObj, isInvokeOnloadMethod){
	selectReportDataRow(trObj, isInvokeOnloadMethod);
}
/**
 * 刷新整个页面
 * @param params
 * pageid
 */
function refreshPage(params){
	refreshComponentDisplay(params.pageid, params.pageid, true);
}
/**
 * 写入父页面选中行指定隐藏列的值
 * setHiddenCols2SelectedRow({'F_LBBH':'11'},parent.frames['invoiceFormatPage']);
 */
function setHiddenCols2SelectedRow(data, p, trobj){
	if(trobj == null){
		p.$(".cls-data-tr[EDIT_TYPE='add']").each(function(){
			for (var item in data) {
				//新增tr对象
				var trid = this.id;
				//根据tr将其拆分为两段
				var pretrid = trid.substring(0, trid.indexOf("tr"));
				var guid = pretrid.substring(0,pretrid.length-1);
				var lastrid = trid.substring(trid.indexOf("tr")+3);
				var keytdid = pretrid + item+"__td" + lastrid;
				var inputid = pretrid + item+"__" + lastrid;
				var val = data[item];
				p.$(this).find("#"+keytdid).children().append('<span tagtype="COL_CONTENT_WRAP"><input name="'+inputid+'" class="cls-inputbox2-full" id="'+inputid+'" style="text-align: left;" onkeypress="return onKeyEvent(event);" onfocus="try{this.select();}catch(e){logErrorsAsJsFileLoad(e);}" onblur="try{try{addInputboxDataForSaving(\''+guid+'\',this);}catch(e){logErrorsAsJsFileLoad(e);};fillInputBoxValueToParentTd(this,\'textbox\',\''+guid+'\',\'editablelist2\',1);}catch(e){logErrorsAsJsFileLoad(e);}" type="text" typename="textbox" value="'+val+'"/></span>');
			}
		});
	}else{
		for (var item in data) {
			//新增tr对象
			var trid = trobj.id;
			//根据tr将其拆分为两段
			var pretrid = trid.substring(0, trid.indexOf("tr"));
			var lastrid = trid.substring(trid.indexOf("tr")+3);
			var keytdid = pretrid + item+"__td" + lastrid;
			var val = data[item];
			var tdobj = p.$(trobj).find("#"+keytdid);
			var oldname = tdobj.attr("oldvalue_name");
			tdobj.attr({oldvalue_name:oldname+"__old",value_name:oldname,value:val});
		}
	}
}

/**
 * 确认对话框
 * 
 */
function jconfirm(message,title,width,height,okHandler,cancelHandler){
	wx_confirm(message,title,width,height,okHandler,cancelHandler);
}
/**
 * 可用作和jconfirm的配合，确定是否点击了ok按钮
 */
function isokconfirm(input){
	return wx_isOkConfirm(input);
}
/**
 * 获取某个报表所有选中行的<tr/>对象
 * @param pageid 报表所在页面ID
 * @param reportid 报表id
 * @return 返回本报表被选中行的对象数组，且按行号从小到大排好序
 * function getAllSelectedTrObjs(pageid,reportid)
 */
/**
 * 根据指定ID及相应的值获取行对象
 * pageid
 * reportid
 * id 主键编号名称
 * val 主键编号值
 * isInvokeOnloadMethod 默认为false，不刷新关联报表，改为true则刷新关联报表
 */
function selectRow(pageid, reportid, id, val, isInvokeOnloadMethod){
	var guid = getComponentGuidById(pageid, reportid);
	var key = guid+"_"+id+"__td";
	$("td[id^="+key+"]").filter("td[oldvalue="+val+"]").each(function(index, domEle){
		selectTr($(this).parent().get(0),isInvokeOnloadMethod);
	});
}

function unselectRow(pageid, reportid, id, isInvokeOnloadMethod){
	var guid = getComponentGuidById(pageid, reportid);
	var key = guid+"_"+id+"__td";
	$("td[id^="+key+"]").each(function(index, domEle){
		deselectReportDataRow($(this).parent().get(0),isInvokeOnloadMethod);
	});
}