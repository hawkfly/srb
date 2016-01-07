/**
 * 给定名称生成指定报表名称和值对应的JSON对象
 * @date 20151015
 */
function getReportDatas(pageid, reportid, propes, conditions){
	if(propes == null || propes.length == undefined)return null;
	var propesjson = {};
	for(var i = 0; i < propes.length; i++){
		var propname = propes[i];
		propesjson[propname] = true;
	}
	if(conditions == undefined){
		var editableRows = getEditableReportColValues(pageid,reportid,propesjson);	
	}else{
		var editableRows = getEditableReportColValues(pageid,reportid,propesjson, conditions);		
	}
	if(isArray(editableRows)){//对于editablelist2的处理
		var rtns = new Array();
		for(var r = 0; r < editableRows.length; r++){
			rtns.push(packform(editableRows[r], propes));
		}
		return rtns;
	}else{//对于form的处理
		return packform(editableRows, propes);
	}
}

function packform(editableRows, propes){
	var rtnjson = {};
	for(var i = 0; i < propes.length; i++){
		rtnjson[propes[i]] = editableRows == null ? '' : editableRows[propes[i]].value;
	}
	return rtnjson;
}

function isArray(obj) {   
	return Object.prototype.toString.call(obj) === '[object Array]';  
}
/*页面初始化的时候计算赎回金额*/
function jsfhje(pageid, reportid){
	var rows = getReportDatas(pageid, reportid, ['shje', 'cjje', 'coral_realshrate', 'pt_yqnh', 'coral_cstmid', 'pt_id', 'pt_tzqx', 'pt_fxfs', 'h_startdatetime']);
	if(isArray(rows)){
		for(var i = 0; i < rows.length; i++){
			setfhje(pageid, reportid, rows[i]);
		}
	}else{
		setfhje(pageid, reportid, rows);
	}
}

function setfhje(pageid, reportid, row){
	var shje = parseFloat(row['shje']);
	var cjje = parseFloat(row['cjje']);
	var realshrate = parseFloat(row['coral_realshrate']);
	var yqnh = parseFloat(row['pt_yqnh']);
	var tzqx = parseInt(row['pt_tzqx']);
	var fxfs = row['pt_fxfs'];
	var gmsj = row['h_startdatetime'];
	
	var cstmid = row['coral_cstmid'];
	var ptid = row['pt_id'];
	/*真实赎回金额  =（日收益*购买天数+本金） * 0.98 */
	//var realfhje = shje * (yqnh - realshrate) + shje;
	var realfhje = jsfhjeCoreFunc(yqnh, shje, tzqx, gmsj, fxfs, realshrate);
	setEditableReportColValue(pageid, reportid, {coral_realfhje:realfhje.toString()},[{name:'coral_cstmid',value:cstmid, matchmode:'include'},{name:'pt_id',value:ptid, matchmode:'include'}]);
}

function calculateFhje(pageid, reportid, row){
	var shje = parseFloat(row['shje']);
	var cjje = parseFloat(row['cjje']);
	var realshrate = parseFloat(row['coral_realshrate']);
	var yqnh = parseFloat(row['pt_yqnh']);
	var tzqx = parseInt(row['pt_tzqx']);
	var fxfs = row['pt_fxfs'];
	var gmsj = row['h_startdatetime'];
	
	var cstmid = row['coral_cstmid'];
	var ptid = row['pt_id'];
	/*真实赎回金额  =（日收益*购买天数+本金） * 0.98 */
	//var realfhje = shje * (yqnh - realshrate) + shje;
	var realfhje = jsfhjeCoreFunc(yqnh, shje, tzqx, gmsj, fxfs, realshrate);
	return realfhje;
}

/**
 * 计算返还金额的核心计算方法，其他使用的地方均调用之！
 * @param yqnh 预期年化
 * @param shje 赎回金额
 * @param tzqx 投资期限
 * @param gmsj 购买时间
 * @param fxfs 返息方式:1按月，2一次性
 * @return 保留两位小数后的返还金额
 */
function jsfhjeCoreFunc(yqnh, shje, tzqx, gmsj, fxfs, realshrate){
	if(realshrate == null || realshrate == undefined)realshrate = 0.02;
	var gmday = $.jbsf.util.timediff(gmsj);
	/*if(fxfs == "1" && gmday > 30){
		gmday = gmday % 30;
	}*/
	var shje = (((yqnh * shje)/(tzqx*30) * gmday) + shje) * (1-realshrate);
	return shje.toFixed(2);
}
/*动态计算赎回金额*/
function dynjssh(pageid, reportid, row){
	//绑定onchange事件，执行动态计算
	var shinput = getcomGuid(pageid, reportid, 'coral_realshrate');
	var rownum = row[0].id.trim().replace(/^(.*[\n])*.*(.|\n)$/g, "$2");
	$('#'+shinput+"__"+rownum).change(function(){
		var rows = getReportDatas(pageid, reportid, ['shje', 'cjje', 'coral_realshrate', 'pt_yqnh', 'coral_cstmid', 'pt_id', 'pt_tzqx', 'pt_fxfs', 'h_startdatetime']);
		var realfhje = calculateFhje(pageid, reportid, rows[rownum]);
		setEditableReportColValue(pageid, reportid, {coral_realfhje:realfhje.toString()},{name:'SELECTEDROW',value:true});
	});
}

function setparashcash(pageid, reportid, rows){
	var thispageid = $.jbsf.wbcs.getPageid();
	setCstmParaVal(thispageid, 'indexRpt', 'whatbtn', 'pass');
}

function setid(pageid, reportid, dataObjArr){//WX_SAVE_IGNORE,WX_SAVE_TERMINAT,WX_SAVE_CONTINUE
	for ( var i = 0; i < dataObjArr.length; i++) {
		//检查可投金额是否小于等于项目规模
		var ktje = parseFloat(dataObjArr[i]['pt_ktje']);
		var xmgm = parseFloat(dataObjArr[i]['pt_xmgm']);
		if(ktje > xmgm){
			wx_error('可投金额不能大于项目规模！');
			return WX_SAVE_TERMINAT;
		}
		//为产品id赋值
		if(dataObjArr[i]['WX_TYPE'] == 'add'){
			var uuid = $.jbsf.util.uuidFast();
			//setInputboxValueForDetailReport(pageid, reportid, 'pt_id', uuid);
			dataObjArr[i]['pt_id'] = uuid;
			
			var fxfs = dataObjArr[i]['pt_fxfs'];
			if(fxfs == '1'){
				dataObjArr[i]['pt_hkfs'] = '按月返利，到期返还本金';
			}else{
				dataObjArr[i]['pt_hkfs'] = '到期一次性返还利息和本金';
			}
		}else if(dataObjArr[i]['WX_TYPE'] == 'update'){
			var fxfs = dataObjArr[i]['pt_fxfs'];
			if(fxfs == '1'){
				dataObjArr[i]['pt_hkfs'] = '按月返利，到期返还本金';
			}else{
				dataObjArr[i]['pt_hkfs'] = '到期一次性返还利息和本金';
			}
		}
	}
}