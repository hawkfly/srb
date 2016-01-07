<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.wbcs.config.Config"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>菜单树</title>
		<style type="text/css">
			.ztree {
				width: 192px;
				display: block;
				font-size: 12px;
				font-family: "宋体";
			}
			
			.ztree li {
				color: #03294a;
				background-image: url(/CPDO/webresources/images/menuLI-line.jpg);
				background-repeat: no-repeat;
				background-position: bottom;
				text-indent: 0px;
				line-height: 30px;
			}
			
			.ztree li img {height:30px;}
			
			.ztree li a {height:30px;padding-top: 0px;padding-left: 0px;}
			
			.ztree li a img {height:30px;}
			
			.ztree li a:hover {
				color: #03294a;
				font-weight: bold;
			}
			
			.ztree li a.curSelectedNode {border:0;height:30px;}
			.ztree li span {line-height:30px;margin-left: 0px;}
			.ztree li a.level0 span {font-size: 120%;margin-left:0px}
			.ztree li a.level1 span {font-size: 100%;}
			.ztree li span.button.switch.level0 {width: 0px; height:20px}
			.ztree li span.button.switch.level1 {width: 0px; height:20px}
			.ztree li span.button {margin-top: 0px;margin-bottom: 0px;height:27px;width:27px;}
		</style>
		
		<script type="text/javascript">
			var setting = {
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onClick: function(event, treeId, treeNode, chickFlag){
						 var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
						 var sNodes = treeObj.getSelectedNodes();
						 if (sNodes.length > 0) {
						 	var isParent = sNodes[0].isParent;
							 if(isParent != true ){
							 $('.l-tab-content').css({ left: "100%"}).animate({ left: "0%"}, 500);
							 }
						 }
						 var node = treeObj.getNodeByTId(treeNode.tId);
						 var id= treeNode.tId;
						 var nodedd = node.getParentNode();
						 if(nodedd == null){
								
							 //treeObj.expandAll(true);//打开所有节点
							 treeObj.expandAll(false);//关闭所有节点
							 treeObj.selectNode(node);
						 }
						onclickTreeNode(treeNode.id, treeNode.name);
						if (treeNode.isParent) {
							$.fn.zTree.getZTreeObj(treeId).expandNode(treeNode);
							return false;
						} else {
							return true;
						}
					}
				}
			};
			function onloadTree(dataObj){
				var zNodes = eval(dataObj.datas);
				for(var i = 0; i < zNodes.length; i++){
					if(zNodes[i].icon == null){//设置默认图标
						zNodes[i].icon = '${pageContext.request.contextPath }/websresources/images/ico-menu-1.gif';
					}
				}
				$.fn.zTree.init($("#treeDemo"), setting, zNodes);
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				//默认展开第1层节点
				var nodesAry = treeObj.transformToArray(treeObj.getNodes());
				for(var i = 0; i < nodesAry.length; i++){
					var level = nodesAry[i].level;
					if(level == 0)treeObj.expandNode(nodesAry[i]);
				}
			}
		</script>
	</head>
	<body>
		<div class="content_wrap">
			<div class="">
				<ul id="treeDemo" class="ztree"></ul>
			</div>
		</div>
	</body>
</html>