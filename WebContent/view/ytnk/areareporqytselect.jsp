<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.wbcs.config.Config"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
	</head>
	<script type="text/javascript">
		function setAreaqyVal(){
			var rows = getSelectRowCols({'pageid':'areareportqyselectPage','reportid':'areareportqyselectRpt','colnames':['F_QYBH']});
			var qyObj = rows[0]['F_QYBH'];
			var qy = qyObj.value;
			var ppid = $.jbsf.core.getParameter("parentPageId");
			selectOK(qy,'F_QY','',true, parent.frames[ppid]);
		}
	</script>
	<body>
		<wx:popuppage/>
		<wx:searchbox></wx:searchbox>
		<wx:data>
		</wx:data>
	</body>
</html>