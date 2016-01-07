var tid
//认证地址  保存前塞值
function setRzdzSaveParams()
{
	var cdtOBJ = document.getElementById(getComponentGuidById('identifyAddrPage', 'identifyAddrRpt')+"_condition_ivtypeCdt");
	var label = cdtOBJ.options[cdtOBJ.selectedIndex].text;
	setMyParamVals({pageid:'identifyAddrPage',reportid:'identifyAddrRpt',myparamname:'ivtypeid',myparamval :cdtOBJ.value});
	setMyParamVals({pageid: 'identifyAddrPage',reportid: 'identifyAddrRpt',myparamname: 'ivtypename',myparamval :label});
	return WX_SAVE_CONTINUE;
}
//认证通道  保存前塞值
function setRztdSaveParams()
{
	var cdtOBJ = document.getElementById(getComponentGuidById('identifyChannelPage', 'identifyChannelRpt')+"_condition_ivtypeCdt");
	var label = cdtOBJ.options[cdtOBJ.selectedIndex].text;
	setMyParamVals({pageid:'identifyChannelPage',reportid:'identifyChannelRpt',myparamname:'ivtypeid',myparamval :cdtOBJ.value});
	setMyParamVals({pageid: 'identifyChannelPage',reportid: 'identifyChannelRpt',myparamname: 'ivtypename',myparamval :label});
	return WX_SAVE_CONTINUE;
}
//发票格式配置关系  保存前塞值
function setFpgsSaveParams()
{
	var cdtOBJ = document.getElementById(getComponentGuidById('invoiceFormatRelationPage', 'invoiceFormatRelationRpt')+"_condition_ivtypeCdt");
	var label = cdtOBJ.options[cdtOBJ.selectedIndex].text;
	setMyParamVals({pageid:'invoiceFormatRelationPage',reportid:'invoiceFormatRelationRpt',myparamname:'ivtypeid',myparamval :cdtOBJ.value});
	setMyParamVals({pageid: 'invoiceFormatRelationPage',reportid: 'invoiceFormatRelationRpt',myparamname: 'ivtypename',myparamval :label});
	return WX_SAVE_CONTINUE;
}
//发票格式  保存前塞值
function setSaveParams()
{
	var cdtOBJ = document.getElementById(getComponentGuidById('invoiceFormatPage', 'invoiceFormatRpt')+"_condition_ivtypeCdt");
	var label = cdtOBJ.options[cdtOBJ.selectedIndex].text;
	setMyParamVals({pageid:'invoiceFormatPage',reportid:'invoiceFormatRpt',myparamname:'ivtypeid',myparamval :cdtOBJ.value});
	setMyParamVals({pageid: 'invoiceFormatPage',reportid: 'invoiceFormatRpt',myparamname: 'ivtypename',myparamval :label});
	return WX_SAVE_CONTINUE;
}
//用户维护页面，插入或更新数据前的数据处理
/**
 * var rows = getSelectRowCols({'pageid':'invoiceFormatHelpPage', 'reportid':'invoiceFormatHelpRpt','colnames':['F_GSBH','F_GSMC']});
			var idObj = rows[0]['F_GSBH'];
			var mcObj = rows[0]['F_GSMC'];
			var id = idObj.value;
			var mc = mcObj.value;
			var ppid = $.jbsf.core.getParameter("parentPageId");
			setHiddenCols2SelectedRow({"F_GSBH":id},parent.frames[ppid]);
			selectOK(mc, 'F_GSMC','格式名称',true, parent.frames[ppid]);
 */
/*参考获取选中行的指定列的数据
function setuserid(pageid,rptid,rows)
{
	var selectedRoleIds = getSelectRowCols({'pageid':'roles2userPage','reportid':'roleSelectRpt','colnames':['id']});
	for(var i = 0; i < selectedRoleIds.length; i++){
		alert(selectedRoleIds[i]['id'].value);
	}
	return WX_SAVE_CONTINUE;
}
*/
//保存期塞值
function setFWSVal(pageid,rptid,rows)
{
	var selectedRoleIds = getSelectRowCols({'pageid':pageid,'reportid':rptid,'colnames':['F_CODE']});
	var code = selectedRoleIds[0]['F_CODE'].value;
//	var status = actions({'serverClassName':'com.pansoft.jbsf.service.IVSaveServiceProvider', 'datas':{key:code},'afterCallbackMethod':function(data){
//		var codemc = data.responseText;
//		setMyParamVals({pageid:pageid,reportid:rptid,myparamname:'codemc',myparamval :codemc});
//	}});
	$.ajax({
	    data: "get",
	    url: "jf/fws",
	    data: "key=" + code,
	    cache: false,
	    async: false,
	    success: function (codemc) {
		setMyParamVals({pageid:pageid,reportid:rptid,myparamname:'codemc',myparamval :codemc});
	    }
	});
	return WX_SAVE_CONTINUE;
}
//集团架构管理页面，增加数据时——编号自动获取，保存前 把后台生成的最大编号塞给页面
function setGroupVal(pageid,rptid,rows)
{
	//注释部分不采用原因：actions 为异步加载...
//{	var str;
//	while(str=='undefined'||str==null)
//	{
//		var id = $.jbsf.core.getParameter("txtcode");
//		var status = actions({'serverClassName':'com.pansoft.jbsf.service.IVSaveGroup', 'datas':{key:id},'afterCallbackMethod':function(data){
//			str = data.responseText;
//		}});
//	}
//	var arr = str.split(";");
//	setMyParamVals({pageid:pageid,reportid:rptid,myparamname:'newid',myparamval :arr[0]});
//	setMyParamVals({pageid:pageid,reportid:rptid,myparamname:'newjs',myparamval :arr[1]});
	//var row = getSelectRowCols({'pageid':pageid, 'reportid':rptid,'colnames':['dwname']});
//	var name = row[0]['dwname'].value;
	
	//上两句可能存在问题
	var id = $.jbsf.core.getParameter("txtcode");
	$.ajax({
	    data: "get",
	    url: "jf/group",
	    data: "key=" + id,
	    contentType:"application/x-www-form-urlencoded; charset=gb2312",
	    cache: false,
	    async: false,
	    success: function (str) {
			var arr = str.split(";");
			setMyParamVals({pageid:pageid,reportid:rptid,myparamname:'newid',myparamval :arr[0]});
			setMyParamVals({pageid:pageid,reportid:rptid,myparamname:'newjs',myparamval :arr[1]});
			setMyParamVals({pageid:pageid,reportid:rptid,myparamname:'ssjtbh',myparamval :arr[2]});
	    }
	});
	return WX_SAVE_CONTINUE;
}

function freshTree()
{
	setTimeout("fre()",500);
}
function fre()
{
	var treeObj = window.parent.$.fn.zTree.getZTreeObj("treeDemo");
	var treeNode = treeObj.getSelectedNodes()[0];
	//window.parent.treeObj.updateNode(treeNode);
	treeNode.isParent = true;
	treeObj.reAsyncChildNodes(treeNode, "refresh");
}
//保存之后 将所有的 代理商单位 下面的用户 都设置为代理商
function setperson(pageid,rptid,rows)
{
	$.ajax({
	    data: "get",
	    url: "jf/set",
	    cache: false,
	    async: false,
	    success: function (codemc) {
		
	    }
	});
	return WX_SAVE_CONTINUE;
}
//当单独设置代理商 而且 设置的这个用户 不属于 任何一个代理商单位
function setCompany(pageid,rptid,rows)
{
	$.ajax({
	    data: "get",
	    url: "jf/add",
	    cache: false,
	    async: false,
	    success: function (codemc) {
		
	    }
	});
	return WX_SAVE_CONTINUE;
}