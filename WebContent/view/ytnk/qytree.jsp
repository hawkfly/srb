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
		<script type="text/javascript">
		var setting1 = {  
            data: {  
                simpleData: {  
                    enable: true  
                }  
            },  
            async: {  
                enable: true,  
              	url:"/bmnk/jf/organizationemployeetree/qytree",  
                type:"post",
                dataType:"text",
                autoParam:["id","pId"]  
              //  dataFilter: filter  
            }, 
            callback: {
            	onAsyncSuccess: zTreeOnAsyncSuccess1,
            	onClick: zTreeOnClick1
			},
			view:{
				showIcon: true
			}  
        };  
        function zTreeOnClick1(event, treeId, treeNode) {alert(111);
 	 		 $.jbsf.ztree.clickCascadeWbcsReport({iframeid: 'authoritygridframe', iframelinkpageid:"employeePage", ztreenode:treeNode, accessmode: 'read'});
		};
        function zTreeOnAsyncSuccess1(event, treeId, treeNode, msg) {
        	 var obj;
   			 var treeObj = $.fn.zTree.getZTreeObj("qytree");
   			 if(treeNode=="undefined"|treeNode==undefined)
   			 {
   			 	obj = treeObj.getNodes()[0];
   			 }
   			 else
   			 {
   			 	obj = treeNode;
   			 }
   			 treeObj.selectNode(obj);
	    	$.jbsf.ztree.clickCascadeWbcsReport({iframeid: 'authoritygridframe', iframelinkpageid:"employeePage", ztreenode:obj, accessmode: 'read'});
		};
	
		function onloadTree() {alert(11);		
	    	$.fn.zTree.init($("#qytree"), setting1);
		};
		</script>
		<style type="text/css">
			.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
		</style>
	</head>
	<body>
		<div id="authoritymngDiv1" style="position: relative; width:100%; height: auto; overflow: hidden;">
			<div id="authorityCRUD1" style="padding-top: 5px; margin-left: 5px; width: 100%; float: left;overflow: hidden;">
				<div style="border-top: 1px dashed red;">&nbsp;</div>
				<div class="content_wrap" style="float: left; width: 100%; height: 100%; overflow-y: scroll;overflow-x:hidden;height:550px;">
					<div class="zTreeDemoBackground left">
						<ul id="qytree" class="ztree"></ul>
					</div>
				</div>
				<%-- <div title="权限详细信息" style="float: left; width: 85%;">
					<iframe id="authoritygridframe" name="authoritygridframe" src="${pageContext.request.contextPath }/jbsf.sr?PAGEID=authoritydetailPage" frameborder="0" style="width:100%;height:550px;overflow:hidden;"></iframe>
				</div> --%>
			</div>
		</div>
	</body>
</html>
