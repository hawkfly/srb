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
		function setAreaqhVal(){
			var rows = getSelectRowCols({'pageid':'areareportqhselectPage','reportid':'areareportqhselectRpt','colnames':['F_ND','F_MC','F_QSRQ','F_ZZRQ']});
			var ndObj = rows[0]['F_ND'];
			var mcObj = rows[0]['F_MC'];
			var qsrqObj = rows[0]['F_QSRQ'];
			var zzrqObj = rows[0]['F_ZZRQ'];
			var nd = ndObj.value;
			var mc = mcObj.value;
			var qsrq = qsrqObj.value;
			var zzrq = zzrqObj.value;
			var ppid = $.jbsf.core.getParameter("parentPageId");
			selectOK(nd, 'F_ND','',false, parent.frames[ppid]);
			selectOK(mc, 'F_MC','',false, parent.frames[ppid]);
			selectOK(qsrq, 'F_QSRQ','',false, parent.frames[ppid]);
			selectOK(zzrq, 'F_ZZRQ','',true, parent.frames[ppid]);
		}
	</script>
	<body>
		<wx:popuppage/>
		<wx:searchbox></wx:searchbox>
		<wx:data>
		</wx:data>
	</body>
</html>