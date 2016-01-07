function qysubmitOrCancel(type){
	var tip;
	if(type =="submit" )
		tip = "提交"
	else if(type == "cancelsubmit"){
		tip = "取消提交"
	}else{
		wx_error("操作失败");
		return;
	}
	var selectedRoleIds = getSelectRowCols({'pageid':'areareportPage','reportid':'areareportRpt','colnames':['F_DJBH']});
	if(selectedRoleIds == null){
		wx_warn("请选择一条单据！");
		return;
	}
	var djbh = selectedRoleIds[0]['F_DJBH'].value;
	wx_confirm("确定要"+tip+"该单据吗？","提示",null,null,function(){
		$.ajax({
			url:"jf/areawork?djbh="+djbh+"&type="+type,
			dataType:"json",
			type: "get",
			success:function (data, textStatus) {
				if(data.code=="success"){
					wx_success(data.message);
					search({pageid:'areareportPage',reportid:'areareportRpt'});//此处为刷新哪一部分
				}else if(data.code=="fail"){
					wx_error(data.message);
				}else if(data.code=="warn"){
					wx_warn(data.message);
				}
			},
			cache:  false,
			timeout:60000,
			error:function(XMLHttpRequest, textStatus, errorThrown){
				if(textStatus=="timeout"){
					alert("获取数据超时！");
				}
			}
		});
	});
}
