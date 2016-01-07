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
		<title></title>
	</head>
	<script type="text/javascript" src="<%=path%>/webresources/script/jquery-1.8.3.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#projectHelpPage_guid_projectHelpRpt div").dblclick(function(){
				setProjectVal();
			});
		});
		function setProjectVal(){
			var rows = getSelectRowCols({'pageid':'projectHelpPage', 'reportid':'projectHelpRpt','colnames':['F_PROJECT_ID','F_PROJECT_NAME']});
			var idObj = rows[0]['F_PROJECT_ID'];
			var mcObj = rows[0]['F_PROJECT_NAME'];
			var id = idObj.value;
			var mc = mcObj.value;
			var ppid = $.jbsf.core.getParameter("parentPageId");
			var type = $.jbsf.core.getParameter("type");
			if(ppid=="editWorkPage"&&type=="am")
			{
				var pp = parent.frames[ppid];
				var selectRowObj = selectOK(mc, 'F_AMXMMC','上午项目',false,pp);
				setHiddenCols2SelectedRow({"F_AMXM":id},pp,selectRowObj);
				pp.closePopupWin();					
			}else if(ppid=="editWorkPage"&&type=="pm"){
				var pp = parent.frames[ppid];
				var selectRowObj = selectOK(mc, 'F_PMXMMC','下午项目',false,pp);
				setHiddenCols2SelectedRow({"F_PMXM":id},pp,selectRowObj);
				pp.closePopupWin();	
			}else if(ppid=="editWorkPage"&&type=="card"){
				setWholeWeek(ppid,id,mc);
				parent.frames[ppid].document.getElementById("editWorkPage_guid_rightCard_F_SSXM").value=id;
				selectOK(mc, 'F_XMMC','项目名称',true, parent.frames[ppid]);
			}else{
				var pp = parent.frames['checkWorkPage'];				
				parent.frames['checkWorkPage'].document.getElementById("checkWorkPage_guid_serch2Rpt_condition_project").value=id;
				pp.closePopupWin();
			}

		}
		function setWholeWeek(ppid,id,mc){
		//	parent.frames[ppid].setEditablelistColVal('editWorkPage', 'tabLeftRpt', {'F_AMXMMC': mc+"",'F_AMXM':id+"",'F_PMXMMC':mc+"",'F_PMXM':id+""}, {name:'SELECTEDROW', value:false});
		//	parent.frames[ppid].setEditablelistColVal('editWorkPage', 'tabLeftRpt', {'F_AMXMMC': mc+"",'F_AMXM':id+"",'F_PMXMMC':mc+"",'F_PMXM':id+""}, {name:'SELECTEDROW', value:true});
		//	var note = parent.frames[ppid].document.getElementById("editWorkPage_guid_tabLeftRpt_F_NOTE__0").value;
		//	parent.frames[ppid].document.getElementById("editWorkPage_guid_tabLeftRpt_F_NOTE__0").value = note+" ";
		}
	</script>
	<body>
		<wx:popuppage/>
		<wx:searchbox></wx:searchbox>
		<wx:data>
		</wx:data>
	</body>
</html>