<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.wbcs.config.Config"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户字典</title>
	</head>
	<script type="text/javascript">
		function doSelect(){
			var rows = getSelectRowCols({'pageid':'invoiceFormatHelpPage', 'reportid':'invoiceFormatHelpRpt','colnames':['F_FPLX','F_LXMC']});
			var idObj = rows[0]['F_FPLX'];
			var mcObj = rows[0]['F_LXMC'];
			var id = idObj.value;
			var mc = mcObj.value;
			var ppid = $.jbsf.core.getParameter("parentPageId");
			setHiddenCols2SelectedRow({"F_FPLX":id},parent.frames[ppid]);
			selectOK(mc, 'F_LXMC','格式名称',true, parent.frames[ppid]);
		}
	</script>
	<body>
		<wx:searchbox></wx:searchbox>
		<%-- <wx:popuppage/> --%>
	</body>
</html>