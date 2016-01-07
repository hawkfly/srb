<%-- 选择角色--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.wbcs.config.Config"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>角色选择</title>
		<script type="text/javascript" src="/webresources/script/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="/webresources/jsplugins/jbsf/jbsfcore.js"></script>
		<script type="text/javascript">
			var hiddenroleids = '';
			$(function(){
				var roleids = $.jbsf.core.getParameter("roleid");
				//alert(roleids);
				if(roleids != '') {
					var roleidAry = roleids.split(",");
					for(var i = 0; i < roleidAry.length; i++){
						//alert(roleidAry[i]);
						$("td[oldvalue='"+roleidAry[i]+"']").each(function(j){//可匹配
							//alert($(this).parent()[0]);
							selectReportDataRow($(this).parent()[0]);
							roleidAry.remove(i);
							i = i - 1;
						});
					}
					hiddenroleids = roleidAry.join(',');
				}
			});
			function doSelect()
			{
				if(hiddenroleids != ''){
					var result = hiddenroleids+',';
				}else{
					var result = '';
				}
				
				var selectedRows = getEditableReportColValues('roleselectPage','roleselectRpt',{id:true},{name:'SELECTEDROW', value:true});
				if(selectedRows != null){
					for(var i=0;i<selectedRows.length;i=i+1){
// 						var isSuspendObj = selectedRows[i]['issuspend'];
						var idObj = selectedRows[i]['id'];
// 						var codeObj = selectedRows[i]['code'];
// 						if(isSuspendObj != null && isSuspendObj.value == '1'){
// 							suspend状态，执行createCustomizeComponent方法，为追加幽灵角色做准备，否则将编号追加到result中
// 							$.jbsf.util.createCustomizeComponent(parent, {roleid:idObj.value, usercode:codeObj.value} );
// 						}else{
							result = result + idObj.value + ',';
// 						}
					}
				}
				if(result.lastIndexOf(',')==result.length-1) result=result.substring(0,result.length-1);
				//alert("result = " + result );
				//parent.frames['roles2userPage'].doAddDataForSaving('roles2userPage_guid_roles2userRpt',true);
				//var phone = parent.frames['roles2userPage'].setEditableReportColValue('roles2userPage','roles2userRpt',{phone:},{name:'SELECTEDROW',value:true});
				//parent.frames['roles2userPage'].setEditableReportColValue('roles2userPage','roles2userRpt',{phone:},{name:'SELECTEDROW',value:true});
				
				parent.frames['roles2userPage'].setEditableListReportColValueInRow(
						'roles2userPage',
						'roles2userRpt',
						selectOK(result, 'roles','所选角色',true, parent),
						{assistrolecode:'选择角色 ', roles2:result}
				);
			}
		</script>
		<style type="text/css">
			.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
		</style>
	</head>
	<body>
		<wx:popuppage/>
		<div>用户名称：${param.username }</div>
		<wx:searchbox></wx:searchbox>
		<wx:data></wx:data>
		<wx:navigate></wx:navigate>
		<div><input type="button" name="selectroleBtn" value="确定" onclick="doSelect();"></input></div>
	</body>
</html>
