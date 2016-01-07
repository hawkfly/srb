<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>业主集群</title>
		<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/webresources/css/ztree/zTreeStyle/zTreeStyle.css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath }/webresources/script/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/webresources/jsplugins/ztree/jquery.ztree.core-3.5.min.js"></script> --%>
		<script type="text/javascript">
			var setting = {
				async: {
					enable: true,
					url:"${pageContext.request.contextPath }/jf/initpprstree",
					autoParam:["id", "name=n", "level=lv", "parentid=pid"],
					otherParam:{"otherParam":"zTreeAsyncTest"},
					dataFilter: filter
				},
				callback:{
					beforeAsync: beforeAsync,
					onClick: clickpptornodeFunc
				}
			};
	
			function filter(treeId, parentNode, childNodes) {
				if (!childNodes) return null;
				/* for (var i=0, l=childNodes.length; i<l; i++) {
					childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
				} */
				return childNodes;
			}
			
			function beforeAsync(treeId, treeNode){
				var treeObj = $.fn.zTree.getZTreeObj(treeId);
				var asyncotherParams = treeObj.setting.async.otherParam;
				if(treeNode.getParentNode() != null){
					asyncotherParams.parentid = treeNode.getParentNode().id;
				}
				return true;
			}
			
			function clickpptornodeFunc(event, treeId, treeNode, clickFlag){
				if(!treeNode.isParent){
					var currentpageid = $("#currentpageid").val(); var currentrptid = $("#currentrptid").val();
					dosearch(currentpageid, currentrptid, {txtpprsid:treeNode.name}, {txtpprsid:treeNode.name});
				}
			}
	
			/* $(document).ready(function(){
				$.fn.zTree.init($("#treeDemo"), setting);
			}); */
			function onloadTree(dataObj){
				var zNodes = eval(dataObj.datas);
				/* for(var i = 0; i < zNodes.length; i++){
					if(zNodes[i].icon == null){//设置默认图标
						zNodes[i].icon = '${pageContext.request.contextPath }/images/ico-menu-1.gif';
					}
				} */
				$.fn.zTree.init($("#treeDemo"), setting, zNodes);

				//给搜索框添加回车监听   onkeyup="javascript:if(event.keyCode==13){你处理的函数;}
				$("#pprstreePage_guid_pprstreeinitRpt_condition_txthousenum").blur(function(){
					var currentpageid = $("#currentpageid").val(); var currentrptid = $("#currentrptid").val();
					dosearch(currentpageid, currentrptid, {txtpprsid:this.value}, {txtpprsid:this.value});
				});
			}
		</script>
	</head>
	<body>
		<br/>
		<wx:searchbox condition="txthousenum"></wx:searchbox>
		<div class="content_wrap">
			<div class="zTreeDemoBackground left">
				<ul id="treeDemo" class="ztree"></ul>
			</div>
		</div>
	</body>
</html>