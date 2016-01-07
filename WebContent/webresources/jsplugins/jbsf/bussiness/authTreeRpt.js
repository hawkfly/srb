IDMark_A = "_a";
var setting = {
	view: {
		selectedMulti: false
	},
	check: {
		enable: true,
		chkStyle: "checkbox",
		chkboxType: { "Y": "ps", "N": "" }
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback:{
		onCheck: function(event, treeId, treeNode){
			if(!treeNode.checked){//取消勾选节点
				$("input[name='check_"+treeNode.id+"']").each(function(i){
					this.checked = false;	
				});
			}
		}
	}
};
function addDiyDom(treeId, treeNode) {
	if (treeNode.parentNode && treeNode.parentNode.id!=2) return;
	if(treeNode.type != 'button'){
		var aObj = $("#" + treeNode.tId + IDMark_A);
		var check2 = "<input type='checkbox' style='margin-left:20px;' id='check_"+treeNode.id+"' name='check_"+treeNode.id+"' value='write'/>管理权限";
		aObj.append(check2);
		var btn = $("#check_"+treeNode.id);
		if (btn) btn.bind("click", function(){
				$.fn.zTree.getZTreeObj(treeId).checkNode(treeNode, true, true);
			});
	}
};

function save(roleid){
	var treeObj = $.fn.zTree.getZTreeObj("authtree");
	var nodes = treeObj.getCheckedNodes(true);
	//拼接选中节点数据集合,[{authid:'',roleid:'',accesstype:'readonly'},{authid:'',roleid:'',accesstype:'write'}]
	var authesary = [];
	var rights= "";
	for(var i = 0; i < nodes.length; i++){//所有选中节点
	    	if(i==0){
	    	    rights =  nodes[i].id;
		}else {
		    rights = rights + ',' + nodes[i].id;
		}
	}
	var status = actions({'serverClassName':'com.pansoft.jbsf.wbcs.action.IVSaveSelectedNode', 'datas':{key:rights,roleid:roleid},'afterCallbackMethod':function(data){
		wx_success(data.responseText);
	}});
	//wx_warn('保存成功',{title:'提示', showMask: false, interval: 1});
	//var nodejson = {authid: rights, roleid: roleid,type:'write'};
	//authesary.push(nodejson);
	//invokeComponentSqlActionButton('selectAuthes2RolePage', 'authSelectRpt', 'modifyRoleAuthesBtn',authesary, null, true);
	//window.location.reload();
};

function onloadTree(dataObj){
	var zNodes = eval(dataObj.datas);
	$.fn.zTree.init($("#authtree"), setting, zNodes);
	var treeObj = $.fn.zTree.getZTreeObj("authtree");
	var nodesAry = treeObj.transformToArray(treeObj.getNodes());
	var firstleafnode;
	for(var i = 0; i < nodesAry.length; i++){
		if(nodesAry[i].isParent)continue;
		else{
			firstleafnode = nodesAry[i];
			break;
		}
	}
	//treeObj.selectNode(firstleafnode);
	treeObj.expandAll(true);
	var checkedNodes = treeObj.getCheckedNodes(true);
	for(var i = 0; i < checkedNodes.length; i++)
	if(checkedNodes[i].accesstype == 'write'){//做写操作数据还原
		if($("input[name='check_"+checkedNodes[i].id+"']").attr('value') == 'write'){
			$("input[name='check_"+checkedNodes[i].id+"']")[0].checked=true;
		}
	}
};