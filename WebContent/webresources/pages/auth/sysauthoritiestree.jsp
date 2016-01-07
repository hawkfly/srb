<%-- 权限管理主页面 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.wbcs.config.Config"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>权限管理</title>
		<script type="text/javascript">
			var log, className = "dark", newCount = 1;
			var setting = {
				view: {
					addHoverDom: addHoverDom,
					removeHoverDom: removeHoverDom,
					selectedMulti: false
				},
				edit: {
					enable: true,
					editNameSelectAll: false,
					showRemoveBtn: showRemoveBtn,
					showRenameBtn: showRenameBtn,
					drag:{
						isCopy:true,
						isMove:true,
						prev:true,
						inner:true,
						next:true
					}
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onClick: function(event, treeId, treeNode, chickFlag){
						$.jbsf.ztree.clickCascadeWbcsReport({iframeid: 'authoritygridframe', iframelinkpageid:"authoritydetailPage", ztreenode:treeNode, accessmode: 'read'});
						if (treeNode.isParent) {
							$.fn.zTree.getZTreeObj(treeId).expandNode(treeNode);
							return false;
						} else {
							//f_addTab(treeNode.tId,treeNode.name,'http://www.baidu.com');
							return true;
						}
					},
					beforeDrag: beforeDrag,
					beforeEditName: beforeEditName,
					beforeRemove: beforeRemove,
					beforeRename: beforeRename,
					onRemove: onRemove,
					onRename: onRename
				}
			};
			function onloadTree(dataObj){
				var zNodes = eval(dataObj.datas);
				$.fn.zTree.init($("#treeDemo"), setting, zNodes);
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				//treeObj.expandNode(treeObj.getNodes()[0],true, true, true);
				treeObj.expandNode(treeObj.getNodeByTId($.jbsf.params.selectnodetid),true, true, true);
				treeObj.selectNode(treeObj.getNodeByTId($.jbsf.params.selectnodetid), false);
				$.jbsf.ztree.clickCascadeWbcsReport({iframeid: 'authoritygridframe', iframelinkpageid:"authoritydetailPage", ztreenode:treeObj.getNodes()[0], accessmode: 'read'});
			};
			var treenodes;
			function addHoverDom(treeId, treeNode) {
				var sObj = $("#" + treeNode.tId + "_span");
				
				if (treeNode.editNameFlag || $("#addBtn_"+treeNode.id).length>0) return;
				var addStr = "<span class='button add' id='addBtn_" + treeNode.id
					+ "' title='add node' onfocus='this.blur();'></span>";
				sObj.after(addStr);
				var btn = $("#addBtn_"+treeNode.id);
				if (btn) btn.bind("click", function(){
					var zTree = $.fn.zTree.getZTreeObj("treeDemo");
					treenode = treeNode;
					var status = actions({'serverClassName':'com.pansoft.jbsf.wbcs.action.IVGetMaxAuthid', 'datas':{key:treeNode.id},'afterCallbackMethod':function(data){
						//wx_success(data.responseText);
						var newid = data.responseText;
						callSqlbutton('authoritymngPage', 'authTreeRpt', 'addauthorityBtn', {id: newid, pId: treeNode.id, name: '新权限', url:''}, null, false, addnewauthorityCallback);
						wx_success("添加成功");					
					}});
					
					//zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new node" + (newCount++)});
					return false;
				});
				$.jbsf.params.selectnodetid = treeNode.tId;
			};
			function removeHoverDom(treeId, treeNode) {
				$("#addBtn_"+treeNode.id).unbind().remove();
			};
			function showRemoveBtn(treeId, treeNode) {
				//return !treeNode.isFirstNode;
				return !(treeNode.getParentNode() == null);
			};
			function showRenameBtn(treeId, treeNode) {
				return false;
			};
			function beforeDrag(treeId, treeNodes) {
				return true;
			};
			function beforeEditName(treeId, treeNode) {
				className = (className === "dark" ? "":"dark");
				showLog("[ "+getTime()+" beforeEditName ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				zTree.selectNode(treeNode);
				return confirm("进入节点 -- " + treeNode.name + " 的编辑状态吗？");
			};
			function beforeRemove(treeId, treeNode) {
				className = (className === "dark" ? "":"dark");
				showLog("[ "+getTime()+" beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				zTree.selectNode(treeNode);
				var selectedNodes = zTree.getSelectedNodes();
				var selectedNodesJsonObjAry = new Array();
				var allnodes = '';
				for(var i = 0; i < selectedNodes.length; i++){
					if(selectedNodes[i].isParent){
						if(allnodes != ''){
							allnodes = allnodes + "," + selectedNodes[i].id;
							allnodes = appendChildId(selectedNodes[i].children, allnodes);
						}else{
							allnodes = selectedNodes[i].id;
							allnodes = appendChildId(selectedNodes[i].children, allnodes);
						}
					}else{
						allnodes =  selectedNodes[i].id;
					}
				}
				var nodesAry = allnodes.split(',');
				for(var j = 0; j < nodesAry.length; j++){
					var x = j+1;
					var selectedNodesJsonObj = {};
					selectedNodesJsonObj['code'] = nodesAry[j];
					selectedNodesJsonObjAry.push(selectedNodesJsonObj);
				}
				if(confirm("确认删除 节点 -- " + treeNode.name + " 吗？将会删除本资源及其所有关系数据！")){
					//invokeComponentSqlActionButton('authoritymngPage', 'authTree', 'delauthoritiesBtn',
					//		selectedNodesJsonObjAry, null, false, delAuthoritiesCallback);
					callSqlbutton('authoritymngPage', 'authTreeRpt', 'delauthoritiesBtn', {id:treeNode.id}, null, false, addnewauthorityCallback);
					return true;
				}else{
					return false;
				}
			};
			function appendChildId(children, parentStr){
				for(var i = 0 ; i < children.length; i++){
					if(children[i].isParent){
						if(parentStr != ''){
							parentStr = parentStr + "," + children[i].id;
							parentStr = appendChildId(children[i].children, parentStr);
						}else{
							parentStr = children[i].id;
							parentStr = appendChildId(children[i].children, parentStr);
						}
					}else{
						parentStr = parentStr + "," + children[i].id;
					}
				}
				return parentStr;
			}
			function onRemove(e, treeId, treeNode) {
				showLog("[ "+getTime()+" onRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
			};
			function beforeRename(treeId, treeNode, newName) {
				className = (className === "dark" ? "":"dark");
				showLog("[ "+getTime()+" beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
				if (newName.length == 0) {
					alert("节点名称不能为空.");
					var zTree = $.fn.zTree.getZTreeObj("treeDemo");
					setTimeout(function(){zTree.editName(treeNode)}, 10);
					return false;
				}
				return true;
			};
			function onRename(e, treeId, treeNode) {
				showLog("[ "+getTime()+" onRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
			};
			function showLog(str) {
				if (!log) log = $("#log");
				log.append("<li class='"+className+"'>"+str+"</li>");
				if(log.children("li").length > 8) {
					log.get(0).removeChild(log.children("li")[0]);
				}
			};
			function getTime() {
				var now= new Date(),
				h=now.getHours(),
				m=now.getMinutes(),
				s=now.getSeconds(),
				ms=now.getMilliseconds();
				return (h+":"+m+":"+s+ " " +ms);
			};
			
			function addnewauthorityCallback(dataObj){
				/*var authtree = $.fn.zTree.getZTreeObj("treeDemo");
				alert(authtree.getNodeByParam("id", 'a1', null));
				authtree.selectNode(authtree.getNodeByParam("id", 'a1', null)); */
				//$.jbsf.ztree.clickCascadeWbcsReport({iframeid: 'authoritygridframe', iframelinkpageid:"authoritydetail", ztreenode:treenode, accessmode: 'update'});
			};
			function delAuthoritiesCallback(dataObj){
				
			};
		</script>
		<style type="text/css">
			.ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
		</style>
	</head>
	<body>
		<div id="authoritymngDiv" style="position: relative; width:100%; height: auto;">
			<div id="authorityCRUD" style="padding-top: 5px; margin-left: 5px; width: auto; height: auto; float: left;">
				<div style="border-top: 1px dashed red;">权限维护</div>
				<div class="content_wrap" title="权限信息" style="float: left; width: 350px; height: auto; overflow:auto;">
					<div class="zTreeDemoBackground left">
						<ul id="treeDemo" class="ztree"></ul>
					</div>
				</div>
				<div title="权限详细信息" style="float: left; width: 500px;">
					<iframe id="authoritygridframe" name="authoritygridframe" src="${pageContext.request.contextPath }/ShowReport.wx?PAGEID=authoritydetailPage" frameborder="0" width="100%" height="600"></iframe>
				</div>
			</div>
		</div>
	</body>
</html>
