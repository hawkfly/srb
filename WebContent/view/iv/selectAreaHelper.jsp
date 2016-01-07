<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.wbcs.config.Config"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>地区帮助字典</title>
	</head>
	<script type="text/javascript">
		function doSelect(){
			var rows = getSelectRowCols({'pageid':'areasHelpPage', 'reportid':'areasHelpRpt','colnames':['F_DQBH','F_DQMC']});
			/* if(rows != null){
				for(var i=0;i<rows.length;i=i+1){
					var idObj = rows[i]['id'];//获取ID文本框对象
					var idval = idObj.value;
				}
			}*/
			var idObj = rows[0]['F_DQBH'];
			var mcObj = rows[0]['F_DQMC'];
			var id = idObj.value;
			var mc = mcObj.value;
			alert(parent.getAllSelectedTrObjs('invoiceFormatPage','invoiceFormatRpt'));
			selectOK(id, 'F_DQBH','地区编号',true, parent.frames['invoiceFormatPage']);
			//selectOK(mc, 'F_DWMC','单位名称',true, parent.frames['userCompanyPage']);
		}
	</script>
	<body>
		<div><input type="button" name="selectroleBtn" value="确定" onclick="doSelect();"></input></div>
		<wx:popuppage/>
		<wx:data>
		</wx:data>
	</body>
</html>