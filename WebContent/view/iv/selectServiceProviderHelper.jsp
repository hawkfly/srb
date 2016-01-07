<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.wbcs.config.Config"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>服务商字典</title>
	</head>
	<script type="text/javascript">
		function doSelect(){
			var rows = getSelectRowCols({'pageid':'serviceHelperPage', 'reportid':'serviceHelperRpt','colnames':['F_FWBH','F_FWMC']});
			var idObj = rows[0]['F_FWBH'];
			var mcObj = rows[0]['F_FWMC'];
			var id = idObj.value;
			var mc = mcObj.value;
			var ppid = $.jbsf.core.getParameter("parentPageId");
			var selectRowObj = selectOK(mc, 'F_FWMC','服务商名称',false, parent.frames[ppid]);
			alert(selectRowObj);
			setHiddenCols2SelectedRow({"FWBH":id},parent.frames[ppid],selectRowObj);
			parent.frames[ppid].closePopupWin();
		}
	</script>
	<body>
		<div><input type="button" name="selectroleBtn" value="确定" onclick="doSelect();"></input></div>
		<wx:popuppage/>
		<wx:data>
		</wx:data>
	</body>
</html>