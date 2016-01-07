<%-- 权限管理主页面 --%>
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
		<title>权限管理</title>
		<script type="text/javascript" src="<%=path%>/webresources/script/jquery-1.8.3.js"></script>
		<script type="text/javascript">
		var setting = {  
            data: {  
                simpleData: {  
                    enable: true  
                }  
            },  
            async: {  
                enable: true,  
                url:"jf/tree",  
                type:"post",
                dataType:"text",
                autoParam:["id"]  
              //  dataFilter: filter  
            },
            callback: {
            	onAsyncSuccess: zTreeOnAsyncSuccess,
            	onClick: zTreeOnClick
			},
			view:{
				showIcon: true
			}  
        };  
        function zTreeOnClick(event, treeId, treeNode) {
 	 		 $.jbsf.ztree.clickCascadeWbcsReport({iframeid: 'authoritygridframe', iframelinkpageid:"groupCmyDetailPage", ztreenode:treeNode, accessmode: 'read'});
		};
        function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
        	 var obj;
   			 var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
   			 if(treeNode=="undefined"|treeNode==undefined)
   			 {
   			 	obj = treeObj.getNodes()[0];
   			 }
   			 else
   			 {
   			 	obj = treeNode;
   			 }
   			 treeObj.selectNode(obj);
	    	$.jbsf.ztree.clickCascadeWbcsReport({iframeid: 'authoritygridframe', iframelinkpageid:"groupCmyDetailPage", ztreenode:obj, accessmode: 'read'});
		};

		function zTreeOnAsyncError(event, treeId, treeNode){
			wx_error("数据加载失败!");
		};
	
		function refreshTree() {			
	    	$.fn.zTree.init($("#treeDemo"), setting);
		};

		</script>
		<style type="text/css">
			.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
		</style>
	</head>
	<body>
		<div id="authoritymngDiv" style="position: relative; width:100%; height: auto; overflow: hidden;">
			<div id="authorityCRUD" style="padding-top: 5px; margin-left: 5px; width: 100%; float: left;overflow: hidden;">
				<div style="border-top: 1px dashed red;">&nbsp;</div>
				<div class="content_wrap" style="float: left; width: 25%; height: auto; overflow-y: scroll;overflow-x:hidden;height:470px;">
					<div class="zTreeDemoBackground left">
						<ul id="treeDemo" class="ztree"></ul>
					</div>
				</div>
				<div title="权限详细信息" style="float: right; width: 74%;">
					<iframe id="authoritygridframe" name="authoritygridframe" src="${pageContext.request.contextPath }/jbsf.sr?PAGEID=authoritydetailPage" frameborder="0" width="100%" height="470px" scrolling="no"></iframe>
				</div>
			</div>
		</div>
	</body>
</html>
