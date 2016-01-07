<%--选择权限 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.wbcs.config.Config"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>${param.name}</title>
		<script type="text/javascript">
		</script>
		<style type="text/css">
			.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
		</style>
	</head>
	<body>
		<div>角色名称：${param.name}</div>
		<div id="authstree4role" style="float:left;">
			<div id="authoritymngDiv">
				<div class="content_wrap" position="left" title="权限信息">
					<div class="zTreeDemoBackground left" id="scr" style="overflow-y: scroll;overflow-x:hidden;height:420px;">
						<ul id="authtree" class="ztree"></ul>
					</div>
				</div>
			</div>
			<div id="buttons">
				<input type="hidden" id="saveParamRoleid" value="${param.roleid}"/>
				<wx:button name="saveCstmBtn"></wx:button>
			</div>
		</div>
	</body>
</html>
