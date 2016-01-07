<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.wbcs.config.Config"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户字典</title>
	</head>
	<body>
	<script type="text/javascript" src="<%=path%>/webresources/script/jquery-1.8.3.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		$("#jpsqHelpPage_guid_jpsqHelpRpt div").dblclick(function(){
			selectProject();
		});
		$("#zzjgHelpPage_guid_zzjgHelpRpt div").dblclick(function(){
			selectUsers();
		});
		$("#qyHelpPage_guid_qyHelpRpt div").dblclick(function(){
			selectOrg();
		});
	});
	
		function selectUsers()
		{
			var rows = getSelectRowCols({'pageid':'zzjgHelpPage','reportid':'zzjgHelpRpt','colnames':['F_ZGBH','F_NAME','F_SFZ','F_TEL']});
			var idObj = rows[0]['F_ZGBH'];
			var mcObj = rows[0]['F_NAME'];
			var sfzObj = rows[0]['F_SFZ'];
			var telObj = rows[0]['F_TEL'];
			var id = idObj.value;
			var mc = mcObj.value;
			var sfz = sfzObj.value;
			var tel = telObj.value;
			var ppid = $.jbsf.core.getParameter("parentPageId");
			if(ppid == "organizationPage")
			{
				var pp = parent.frames[ppid].frames['authoritygridframe'];
				var selectRowObj = selectOK(mc, 'F_NAME','负责人',false,pp);
				setHiddenCols2SelectedRow({"F_FZRBH":id},pp,selectRowObj);
				pp.closePopupWin();	
			//	setHiddenCols2SelectedRow({"F_FZRBH":id},parent.frames[ppid]);
			//	selectOK(mc, 'F_YGXM','负责人',true, parent.frames[ppid]);
			}
			if(ppid == "xmwhzrr")
			{//项目字典维护 项目经理选择help弹窗
				ppid = "projectweihuPage";
				var pp = parent.frames[ppid].frames['authoritygridframe'];
				var selectRowObj = selectOK(mc, 'F_NAME','负责人',true,pp);
				setHiddenCols2SelectedRow({"F_ZGBH":id},pp,selectRowObj);
				pp.closePopupWin();
			//	setHiddenCols2SelectedRow({"F_FZRBH":id},parent.frames[ppid]);
			//	selectOK(mc, 'F_YGXM','负责人',true, parent.frames[ppid]);
			}
			
			if(ppid == "qyPage1")
			{
				ppid = "areaPage";
				var pp = parent.frames[ppid].frames['authoritygridframe'];
				var selectRowObj = selectOK(mc, 'F_YGXM1','区域负责人',false,pp);
				setHiddenCols2SelectedRow({"F_QYFZRBH":id},pp,selectRowObj);
				pp.closePopupWin();	
			}
			if(ppid == "qyPage2")
			{
				ppid = "areaPage";
				var pp = parent.frames[ppid].frames['authoritygridframe'];
				var selectRowObj = selectOK(mc, 'F_YGXM2','期报审核人',false,pp);
				setHiddenCols2SelectedRow({"F_QBSYRBH":id},pp,selectRowObj);
				pp.closePopupWin();	
			}
			if(ppid == "jpsqxqPage")
			{
		   		selectOK(id, 'F_YGBH','乘机人编号',false, parent.frames[ppid]);
				selectOK(mc, 'F_YGXM','乘机人',false, parent.frames[ppid]);
				selectOK(sfz, 'F_SFZ','乘机人身份证',false, parent.frames[ppid]);
				selectOK(tel, 'F_TELE','乘机人电话',true, parent.frames[ppid]);     
			}
		}
		function selectArea(){
			var rows = getSelectRowCols({'pageid':'areaHelpPage','reportid':'areaHelpRpt','colnames':['F_QYBH','F_QYQC']});
			var qyqcObj = rows[0]['F_QYQC'];
			var qybhObj = rows[0]['F_QYBH'];
			var qyqc = qyqcObj.value;
			var qybh = qybhObj.value;
			var ppid = $.jbsf.core.getParameter("parentPageId");
			if(ppid == "areaHelper")
			{
				selectOK(qybh, 'F_QYBH','区域编号',false,parent.frames[ppid]);
				selectOK(qyqc, 'F_QYQC','区域名称',true,parent.frames[ppid]);
			}
		}
		function selectOrg()
		{
			var rows = getSelectRowCols({'pageid':'qyHelpPage','reportid':'qyHelpRpt','colnames':['F_ZZJGBH','F_ZZJGJC']});
			var idObj = rows[0]['F_ZZJGBH'];
			var mcObj = rows[0]['F_ZZJGJC'];
			var id = idObj.value;
			var mc = mcObj.value;
			var ppid = $.jbsf.core.getParameter("parentPageId");
			if(ppid == "areaPage")
			{
				var pp = parent.frames[ppid].frames['authoritygridframe'];
				var selectRowObj = selectOK(mc, 'F_ZZJGJC','隶属机构',false,pp);
				setHiddenCols2SelectedRow({"F_ZZJGBH":id},pp,selectRowObj);
				pp.closePopupWin();	
			}
		}
		function selectProject()
		{
			var rows = getSelectRowCols({'pageid':'jpsqHelpPage','reportid':'jpsqHelpRpt','colnames':['f_project_id','f_project_name']});
			var idObj = rows[0]['f_project_id'];
			var mcObj = rows[0]['f_project_name'];
			var id = idObj.value;
			var mc = mcObj.value;
			var ppid = $.jbsf.core.getParameter("parentPageId");
			if(ppid == "jpsqxqPage")
			{
				selectOK(id, 'XMBH','乘机人项目编号',false, parent.frames[ppid]);
				selectOK(mc, 'F_CJRXM','乘机人项目',true, parent.frames[ppid]);
			}
		}
	</script>
	
	<wx:popuppage/>
		<wx:searchbox></wx:searchbox>
		<wx:data>
		</wx:data>
	</body>
</html>