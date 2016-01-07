/*
 * 将文本，图集，视频融在一起的保存后执行方法
 * @project SDCMW
 * @author hawkFly
 * */
function sdcm_newstxt_aftsaveFunc(paramsObj){
	//alert($.jbsf.wbcs.ueditor());newstxtPage_guid_newstxtRpt_XWNR
	var openpageid = $("#openpageid").val();
	var openrptid = $("#openrptid").val();
	var parenttabid = $("#parenttabid").val();
	$("input[type=button]").each(function(){
		$(this).attr('disabled',false);
	});
	if(paramsObj.reportid == null)//tab中的新闻编辑状态
	{
		paramsObj.reportid = paramsObj.componentid;
		paramsObj.updatetype = 'update';
	}
	if($.jbsf.params.cstmbtntype != 'goback'){//草稿、推送、发布
		if($.jbsf.params.cstmbtntype == 'publish'){//如果是编审直接发布新闻则更新前台
			var siteid = $("#siteid").val();
			if(paramsObj.pageid == 'themenewstxtPage' || paramsObj.pageid == 'themenewslistPage'){
				siteid = $.jbsf.core.getParameter("txtthemeid");
			}
			var serveractionurl = sdcm_aftsaveUtil_parseSiteid2TemplateFunc(siteid);
			$.post(serveractionurl);
		}
		$.jbsf.wbcs.ldconfirm('/webresources/pages/newspreview.jsp', {/*okval:'继续编辑', */cancelval:'关闭当前页面', title:'新闻预览'},
							paramsObj.pageid, paramsObj.reportid, ['XWNR'], function(){}, 
							function(){
								if(paramsObj.updatetype == 'add'){
									opener.searchReportData(openpageid,openrptid);
									window.close();
								}else if(paramsObj.updatetype == 'update'){
									var frameId = window.frameElement && window.frameElement.id || '';
									if(parent){
										setTimeout("parent.f_removeTab('"+frameId+"')", 2000);
										parent.$("#"+parenttabid)[0].contentWindow.searchReportData(openpageid,openrptid);
									}else{
										setTimeout("f_removeTab('"+frameId+"')",2000);
									}
								}
							});
	}else if(paramsObj.operatype == 'published'){
		var frameId = window.frameElement && window.frameElement.id || '';
		if(parent){
			setTimeout("parent.f_removeTab('"+frameId+"')", 2000);
			parent.$('#'+parenttabid)[0].contentWindow.searchReportData(openpageid,openrptid);
		}else{
			setTimeout("f_removeTab('"+frameId+"')",2000);
		}
	}
};
function sdcm_newstxt_publish_launchTemplate(paramsObj){
	//ZDBM
	if(paramsObj.updatetype != 'delete')
	{
		paramsObj.operatype = 'published';
		$("input[type=button]").each(function(){
			$(this).attr('disabled',false);
		});
		sdcm_newstxt_aftsaveFunc(paramsObj);
	}
	var siteid = $("#siteid").val();
	if(paramsObj.pageid == 'themenewstxtPage' || paramsObj.pageid == 'themenewslistPage'){
		siteid = $.jbsf.core.getParameter("txtthemeid");
	}
	
	var serveractionurl = sdcm_aftsaveUtil_parseSiteid2TemplateFunc(siteid);
	$.post(serveractionurl);
};

function sdcm_newsphotos_aftsaveFunc(paramsObj){
	//newsphotos.jsp--->refreshParentComponent执行独立刷新
	var openpageid = $("#openpageid").val();
	var openrptid = $("#openrptid").val();
	var parenttabid = $("#parenttabid").val();
	$("input[type=button]").each(function(){
		$(this).attr('disabled',false);
	});
	var reportguid=getComponentGuidById(paramsObj.pageid,paramsObj.reportid); 
	//判断是存草稿还是推送、发布等
	/*var dataObjArr=WX_ALL_SAVEING_DATA[reportguid];
	for(var item in dataObjArr[0]){
		document.write(item + ":" +dataObjArr[0][item] + "---->");
	}*/
	if(paramsObj.updatetype == 'add'){
		if($.jbsf.params.cstmbtntype == 'publish'){//如果是编审直接发布新闻则更新前台
			var siteid = $('#'+reportguid+'_ZDBM').val();
			if(paramsObj.pageid == 'themenewsphotosPage'){
				siteid = $.jbsf.core.getParameter("txtthemeid");
			}
			var serveractionurl = sdcm_aftsaveUtil_parseSiteid2TemplateFunc(siteid);
			$.post(serveractionurl);
		}
		opener.searchReportData(openpageid,openrptid);
		window.close();
	}else if(paramsObj.updatetype == 'update'){
		if($.jbsf.params.cstmbtntype != 'craft'){//推送、发布、退回
			var frameId = window.frameElement && window.frameElement.id || '';
			if(parent){
				setTimeout("parent.f_removeTab('"+frameId+"')", 2000);
				parent.frames[parenttabid].searchReportData(openpageid,openrptid);
			}else{
				setTimeout("f_removeTab('"+frameId+"')",2000);
			}
		}
	}
};
function sdcm_newsphotos_publish_launchTemplate(paramsObj)
{
	/*var reportguid=getComponentGuidById(paramsObj.pageid,paramsObj.reportid);
	var zdbmComponentid = reportguid + '_ZDBM';*/
	var reportguid=getComponentGuidById(paramsObj.pageid,paramsObj.componentid); 
	paramsObj.updatetype = 'update';
	$("input[type=button]").each(function(){
		$(this).attr('disabled',false);
	});
	sdcm_newsphotos_aftsaveFunc(paramsObj);
	var siteid = $('#'+reportguid+'_ZDBM').val();
	if(paramsObj.pageid == 'themenewsphotosPage'){
		siteid = $.jbsf.core.getParameter("txtthemeid");
	}
	var serveractionurl = sdcm_aftsaveUtil_parseSiteid2TemplateFunc(siteid);
	$.post(serveractionurl);
};

function sdcm_newsvideo_aftsaveFunc(paramsObj){
	/*if(paramsObj.updatetype == 'add'){
		if(opener != null)opener.refreshComponentDisplay('newslistPage', 'newslistRpt', true);
	}else if(paramsObj.updatetype == 'update'){
		$.jbsf.wbcs.refreshParentKeepOnPageNo();
	}	*/
	var openpageid = $("#openpageid").val();
	var openrptid = $("#openrptid").val();
	var parenttabid = $("#parenttabid").val();
	$("input[type=button]").each(function(){
		$(this).attr('disabled',false);
	});
	if(paramsObj.updatetype == 'add'){
		if($.jbsf.params.cstmbtntype == 'publish'){//如果是编审直接发布新闻则更新前台
			var siteid = $('#'+reportguid+'_ZDBM').val();
			if(paramsObj.pageid == 'themenewsvideoPage'){
				siteid = $.jbsf.core.getParameter("txtthemeid");
			}
			var serveractionurl = sdcm_aftsaveUtil_parseSiteid2TemplateFunc(siteid);
			$.post(serveractionurl);
		}
		opener.searchReportData(openpageid,openrptid);
		window.close();
	}else if(paramsObj.updatetype == 'update'){
		if($.jbsf.params.cstmbtntype != 'craft'){//推送、发布、退回
			var frameId = window.frameElement && window.frameElement.id || '';
			if(parent){
				setTimeout("parent.f_removeTab('"+frameId+"')", 2000);
				parent.$("#"+parenttabid)[0].searchReportData(openpageid,openrptid);
			}else{
				setTimeout("f_removeTab('"+frameId+"')",2000);
			}
		}
	}
};

function sdcm_newsvideo_publish_launchTemplate(paramsObj){
	var reportguid=getComponentGuidById(paramsObj.pageid,paramsObj.componentid); 
	paramsObj.updatetype = 'update';
	$("input[type=button]").each(function(){
		$(this).attr('disabled',false);
	});
	sdcm_newsvideo_aftsaveFunc(paramsObj);
	var siteid = $('#'+reportguid+'_ZDBM').val();
	if(paramsObj.pageid == 'themenewsvideoPage'){
		siteid = $.jbsf.core.getParameter("txtthemeid");
	}
	var serveractionurl = sdcm_aftsaveUtil_parseSiteid2TemplateFunc(siteid);
	$.post(serveractionurl);
};

function sdcm_activevent_publish_launchTemplate(paramsObj){
	$("input[type=button]").each(function(){
		$(this).attr('disabled',false);
	});
	var siteid = '1';
	var serveractionurl = sdcm_aftsaveUtil_parseSiteid2TemplateFunc(siteid);
	$.post(serveractionurl);
	sdcm_activevent_aftsaveFunc(paramsObj);
};

function sdcm_newsad_publish_launchTemplate(paramsObj){
	$("input[type=button]").each(function(){
		$(this).attr('disabled',false);
	});
	var siteid = $("#siteid").val();
	var serveractionurl = sdcm_aftsaveUtil_parseSiteid2TemplateFunc(siteid);
	$.post(serveractionurl);
	sdcm_newsad_aftsaveFunc(paramsObj);
};

function sdcm_newsad_aftsaveFunc(paramsObj){
	refreshComponentDisplay(paramsObj.pageid, 'newsAdlistRpt', true);
};
function sdcm_newsexpert_aftsaveFunc(paramsObj){
	refreshComponentDisplay(paramsObj.pageid, 'expertintroRpt', true);
}
function sdcm_activevent_aftsaveFunc(paramsObj){
	refreshComponentDisplay(paramsObj.pageid, 'activeventslistRpt', true);
}
/*-----------------------------------------------------------------------------------------------------------*/
/**
 * 根据站点产生对应的模板生成方法
 * @param siteid
 * @returns siteurl
 */
function sdcm_aftsaveUtil_parseSiteid2TemplateFunc(siteid)
{
	var funstr = '';
	if(siteid == '1'){//学院主站
		funstr = $.jbsf.core.contextpath() + '/xyzz/creatXyzz.do';
	 }else if(siteid == '2'){//招生信息网
		 funstr = $.jbsf.core.contextpath() + '/zsw/creatZsw.do';
	 }else if(siteid == '3'){//播音主持系
		 funstr = $.jbsf.core.contextpath() + '/xbzd/byzcx/creatByzcx.do';
	 }else if(siteid == '4'){//影视艺术系
		 funstr = $.jbsf.core.contextpath() + '/xbzd/ysys/creatYsys.do';
	 }else if(siteid == '5'){//新闻传播系
		 funstr = $.jbsf.core.contextpath() + '/xbzd/xwcb/creat.do';
	 }else if(siteid == '6'){//信息工程系
		 funstr = $.jbsf.core.contextpath() + '/xbzd/xxgc/creat.do';
	 }else if(siteid == '7'){//动画系
		 funstr = $.jbsf.core.contextpath() + '/xbzd/dh/creat.do';
	 }else if(siteid == '8'){//成人教育
		 funstr = $.jbsf.core.contextpath() + '/sdcmw/cj/creat.do';
	 }else if(siteid == '10'){//就业网 
		 funstr = $.jbsf.core.contextpath() + '/sdcmw/jy/creat.do';
	 }else{//各部门
		 //funstr = 'http://www.sdcmc.net:8080/sdcmw/dep/CreatView.do?depid='+this.label+'&mid='+this.lang+'&id='+this.value;
	 }
	
	return funstr;
}