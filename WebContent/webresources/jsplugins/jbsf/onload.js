/*
 * 考生预报名专业radio格式化
 * @project SDCMW
 * @pageid preapplyPageClt
 * @author hawkFly
 * @remark 整理radio排布
 * */
function parseRadio2Table(pageid, componentid){
	var nbzyguid = pageid+"_guid_"+componentid+"_NBZY";
	$("input[name='"+nbzyguid+"']").each(function(i){
		if(i%2 == 0 && i != 0){
			$(this).before("<br/>");
		}
	});
}
/**
 * 页面初始化动作
 */
function initPage(){
	//将tabid和parenttabid信息写入页面隐藏域中
	var tabid = $.jbsf.core.getParameter('tabid');
	var parenttabid = $.jbsf.core.getParameter('parenttabid');
	if($("#tabid")[0] == null){//初次进入
		$("body").append("<input type='hidden' id='tabid' name='tabid' value='"+tabid+"'>")
		 .append("<input type='hidden' id='parenttabid' name='parenttabid' value='"+parenttabid+"'>");
	}	
}
/**
 * 替换名字为空的超链接名称内容
 * 名称作为tab页面的重要指标，标示了打开页面的父页面的tabid
 */
function replacenamenullFunc(){
	$("a[name='null']").each(function(){
		this.name = $("#tabid").val();
	});
}
//页面刚加载时，就判断按钮是否需要屏蔽
function timeOut(){
	var obj = document.getElementById('enterpriseAuditPage_guid_enterpriseAuditSecRpt_condition_checkType');
	var val = obj.value;
	var input = document.getElementsByTagName('input');
	if(val=='N'||val=='O')
	{
		for(var i =0;i<input.length;i++)
		{
			if(input[i].value=='审批')
			{
				//alert($);
				//input[i].disabled='disabled';
				//$(input[i]).addClass("hidden"); 
				$(input[i]).attr("class", "hidden");
				
			}
		}
	}	
}
function openPage2Tab(pageid, componentid){
	setTimeout("timeOut()", 100);
	initPage();
	replacenamenullFunc();
	if(pageid != null){
		//PAGEID=newslistPage
		if(pageid == 'indexPage'){
			$(".editnews").each(function(i){
				var url = this.href;
				var txt = $(this).text();
				var newsid = "theme_" + this.id;
				//给url追加tabid以便子页面的tab取到处理父页面tab内容
				url = url + "&tabid="+newsid+"&parenttabid="+this.name;
				this.href = '#';
				if(parent != null){
					$(this).click(function(){
						parent.f_addTab(newsid, txt, url);
						return false;
					});
				}else{
					$(this).click(function(){
						f_addTab(newsid, txt, url);
						return false;
					});
				}
			});
		}else{
			$(".editnews").each(function(i){
				var url = this.href;
				var txt = $(this).text();
				var newsid = this.id;
				//给url追加tabid以便子页面的tab取到处理父页面tab内容
				url = url + "&tabid="+newsid+"&parenttabid="+this.name;
				this.href = '#';
				if(parent != null){
					$(this).click(function(){
						parent.f_addTab(newsid, txt, url);
						return false;
					});
				}else{
					$(this).click(function(){
						f_addTab(newsid, txt, url);
						return false;
					});
				}
			});
		}
	}
}

function hiderolecodecolFunc(pageid, componentid){
	 var guid = getComponentGuidById(pageid,componentid);
	 var rolecodeid = guid+"_rolecode";
	 $("#"+rolecodeid).hide();
}
