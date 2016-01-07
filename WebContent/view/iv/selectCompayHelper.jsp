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
			var rows = getSelectRowCols({'pageid':'companyHelpPage', 'reportid':'companyHelpRpt','colnames':['F_DWBH','F_DWMC']});
			var idObj = rows[0]['F_DWBH'];
			var mcObj = rows[0]['F_DWMC'];
			var id = idObj.value;
			var mc = mcObj.value;
			var ppid = $.jbsf.core.getParameter("parentPageId");
			if(ppid=="userCompanyPage")
			{
				setHiddenCols2SelectedRow({"F_DWBH":id},parent.frames[ppid]);
				selectOK(mc, 'F_DWMC','单位名称',true, parent.frames[ppid]);
			}
			else if(ppid == "groupArchitectureManagePage")
			{	
				var pp = parent.frames[ppid].frames['authoritygridframe'];
				var selectRowObj = selectOK(mc, 'dwname','系统单位名称',false,pp);
				setHiddenCols2SelectedRow({"dw":id},pp,selectRowObj);
				pp.closePopupWin();
			}
		}
	</script>
	<body>
		<wx:popuppage/>
		<wx:searchbox></wx:searchbox>
		<wx:data>
		</wx:data>
	</body>
</html>