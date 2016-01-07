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
		<title>组织管理</title>
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
                url:"jf/organizationemployeetree",  
                type:"post",
                dataType:"text",
                autoParam:["id","pId"]  
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
      	  var id = treeNode.id;
       	  var pid = treeNode.pId;
       	  var txtcodeobj = document.getElementById('authoritygridframe').contentWindow.document.getElementById('workDetailPage_guid_workDetailRpt_condition_txtcode');
       	  var txtpcodeobj = document.getElementById('authoritygridframe').contentWindow.document.getElementById('workDetailPage_guid_workDetailRpt_condition_txtpcode');
 	 		txtcodeobj.value = id;
 	 		txtpcodeobj.value = pid;
 	 		var input = document.getElementById('authoritygridframe').contentWindow.document.getElementsByTagName("input");
 	 		for(var i=0;i<input.length;i++){
 	 			if(input[i].value=="查 询"){
 	 				$(input[i]).click();
 	 			}
 	 		}
 	 		// $.jbsf.ztree.clickCascadeWbcsReport({iframeid: 'authoritygridframe', iframelinkpageid:"workDetailPage", ztreenode:treeNode, accessmode: 'read'});
		};
        function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
        	 var obj;
   			 var treeObj = $.fn.zTree.getZTreeObj("organizationemployeetreeDemo");
   			 if(treeNode=="undefined"|treeNode==undefined)
   			 {
   			 	obj = treeObj.getNodes()[0];
   			 }
   			 else
   			 {
   			 	obj = treeNode;
   			 }
   			 treeObj.selectNode(obj);
	    	$.jbsf.ztree.clickCascadeWbcsReport({iframeid: 'authoritygridframe', iframelinkpageid:"workDetailPage", ztreenode:obj, accessmode: 'read'});
		};

		function zTreeOnAsyncError(event, treeId, treeNode){
			wx_error("数据加载失败!");
		};
	
		function refreshTree() {		
	    	$.fn.zTree.init($("#organizationemployeetreeDemo"), setting);
		};

		</script>
		<style type="text/css">
			.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
		</style>
	</head>
	<body>
		<div id="authoritymngDiv" style="position: absolute; width:100%; top:60px;bottom:0px; overflow: hidden;">
			<div id="authorityCRUD" style="padding-top: 5px; margin-left: 5px; width: 100%; float: left;overflow: hidden;">
				<div class="content_wrap" style="float: left; width: 15%; height: 100%; overflow-y: scroll;overflow-x:hidden;height:420px;">
					<div class="zTreeDemoBackground left">
						<ul id="organizationemployeetreeDemo" class="ztree"></ul>
					</div>
				</div>
				<div title="权限详细信息" style="float: left; width: 85%;">
					<iframe id="authoritygridframe" name="authoritygridframe" src="${pageContext.request.contextPath }/jbsf.sr?PAGEID=authoritydetailPage" frameborder="0" style="width:100%;height:550px;overflow:hidden;"></iframe>
				</div>
			</div>
		</div>
	</body>
</html>
