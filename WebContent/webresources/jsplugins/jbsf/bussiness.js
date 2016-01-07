/*
 * 关键字合并
 * @project SDCMW
 * @author hawkFly
 * @remark checkbox
 * */
function mergeKeywords(pageid, componentid, inputid, assistinputid){
	var asskeywordary = document.getElementsByName(pageid+"_guid_"+componentid+"_"+assistinputid);
	var txtinputjq = $('#'+pageid+'_guid_'+componentid+'_'+inputid);
	for(var i = 0; i < asskeywordary.length; i++){
		asskeywordary[i].onclick=function(){
			if(this.checked){//复选框被选中
				txtinputjq.val(txtinputjq.val() == '' ? this.value : txtinputjq.val() + ' ' + this.value);
			}else{
				var newval = txtinputjq.val() == '' ? txtinputjq.val() : txtinputjq.val().replace(this.value, '');
				txtinputjq.val($.trim(newval));
			}
			doAddDataForSaving(pageid+'_guid_'+componentid,'true');
		}
		
		//处理数据还原
		var txtinputvalary = txtinputjq.val().split(' ');
		for(var j = 0; j < txtinputvalary.length; j++){
			if(asskeywordary[i].value == $.trim(txtinputvalary[j])){
				asskeywordary[i].checked = true;
			}
		}
	}
}

function changecheckbox(pageid, componentid, inputid, assistinputid, isinit){
	var txtinputjq = $('#'+pageid+'_guid_'+componentid+'_'+inputid);
	var contextpath = $.jbsf.core.contextpath();
	var data = {mark:'news' ,Lists:[]}; // template data object
	if(isinit){
		$("#"+pageid+"_guid_"+componentid+"_"+assistinputid+" option").each(function(i){
			data.Lists.push({comid:pageid+"_guid_"+componentid+"_"+assistinputid, comname:pageid+"_guid_"+componentid+"_"+assistinputid, name:this.value});
			$("#checkboxdiv").setTemplateURL(contextpath+"/webresources/pages/templates/sdcm.html").processTemplate(data);
		});
		mergeKeywords(pageid, componentid, inputid, assistinputid);
	}else{
		setTimeout(function(){
			$("#"+pageid+"_guid_"+componentid+"_"+assistinputid+" option").each(function(i){
				data.Lists.push({comid:pageid+"_guid_"+componentid+"_"+assistinputid, comname:pageid+"_guid_"+componentid+"_"+assistinputid, name:this.value});
				$("#checkboxdiv").setTemplateURL(contextpath+"/webresources/pages/templates/sdcm.html").processTemplate(data);
			});
			mergeKeywords(pageid, componentid, inputid, assistinputid);
		},100);
	}
}
/*
 * 将selectbox转换为checkbox组件（用于关键字转换）
 * @project SDCMW
 * @author hawkFly
 * @remark selectbox to checkbox
 * */
function parseKeywordSelect(pageid, componentid){
	initPage();
	$("#"+pageid+"_guid_"+componentid+"_keyword").attr("style","display: none;");
	$("#"+pageid+"_guid_"+componentid+"_keyword").parent().append("<div id='checkboxdiv'></div>");
	changecheckbox(pageid, componentid, 'GJZ', 'keyword',true);
	$("#"+pageid+"_guid_"+componentid+"_keywordtype").change(function(){
		changecheckbox(pageid, componentid, 'GJZ','keyword',false);
	});
	if(pageid == 'newsphotosPage' || pageid == 'themenewsphotosPage')
	{
		initUploadImage();
	}else if(pageid == 'newsvideoPage' || pageid == 'themenewsvideoPage'){
		initVideonews();
	}
	bindselectFunc(pageid, componentid);
	//绑定标题换行监听
	$("#"+pageid+"_guid_"+componentid+"_BT").keypress(function(e){
		var key = e.which;
		var oldval = $(this).val();
		if(key == 13)//回车
		{
			$(this).val(oldval+"<br/>");
		}
	});
}

function initUploadImage(){
	//newsphotopathstr
	newsphotopathstr = $("#newsphotopathstr").val();
	//newsphototitlestr
	newsphototitlestr = $("#newsphototitlestr").val();
	$("#J_Urls").attr("value", newsphotopathstr);
	myEditorImage= new UE.ui.Editor();
	myEditorImage.render('myEditorImage');
	myEditorImage.ready(function(){
		 myEditorImage.setDisabled();
		 myEditorImage.hide();//隐藏UE框体 
		
		 myEditorImage.addListener('beforeInsertImage',function(t, arg){
			//alert($('#imgSort', document.getElementById('edui48_iframe').contentWindow.document).html());
			/* for(var item in arg[0]){
				document.write(item + "=" + arg[0][item]+", ");
			} */
			 //alert(arg[0].src);//arg就是上传图片的返回值，是个数组，如果上传多张图片，请遍历该值。
			//遍历ul[id=sortable]的所有li并取出其value的值URL，将这些url拼接成以逗号分隔的字符串并回写到J_Urls隐藏域
		 });
	});
	$(".grid.uploaderbtn").click(function(){
		upImage();
	});
}

function upImage(){
	d = myEditorImage.getDialog("insertimage");
	d.iframeUrl= $.jbsf.core.contextpath() + '/webresources/jsplugins/ueditor/dialogs/image/jbsfimage.html';
	d.render;
	d.open();
	$("#edui50_body").click(function(){
		var imgulrsAry = [];
		document.getElementById('edui48_iframe').contentWindow.$('#sortable li').each(function(i){
			imgulrsAry.push($(this).attr("old_value"));
		});
		var imgurls = '';
		if(imgulrsAry.length > 0){
			imgurls = imgulrsAry.join(",");
		}
		$("#J_Urls").attr("value", imgurls);//把图片地址赋值给页面input，我这里使用了jquery，可以根据自己的写法赋值，到这里就很简单了，会js的都会写了
		//alert($("#J_Urls").attr("value"));
	});
}
function loadUploader(params){}
function refreshParentComponent(params){}

/**
 * 视频新闻页面初始化
 */
function initVideonews(){
	var prjpath = $.jbsf.core.contextpath();
	
	$("#uploadify").uploadify({
		'swf'       : prjpath+'/webresources/jsplugins/uploadify/uploadify.swf',
		'uploader'	: prjpath+'/wldst/upload',
		'buttonImage'	:	prjpath+'/webresources/jsplugins/uploadify/upload_button.png',
		'fileTypeExts' : '*.mp3; *.mp4; *.flv; *.mov; *.avi; *.mpg; *.wmv; *.3gp; *asf; *.asx; *.mpge; *.wmv9; *.rm; *.rmvb',
		'width'    : 190,
		'height'    : 45,
		'removeCompleted' : false,
		'auto'           : true,
		'fileSizeLimit'		 : '1GB',
		'uploadLimit' : 1 ,
		'onUploadStart' : function(file) {
			//$("#videoInfo").show();
			//$("#saveBtn").show();
			$("#cancel").show();
			var filename = file.name;
			filename = filename.substring(0,filename.lastIndexOf("."));
			if($("#videoname").val() == ""){
				$("#videoname").val(filename);
			}
		},
		'onSelect' : function(file) {
			$("#uploadify").uploadify('disable', true);
		},
		'onUploadSuccess' : function(file, data, response) {
			var videourl = data.substring(data.indexOf("/"),data.length);
			$("#videoUrl").val(videourl);
			var ismp3 = false;
			if(videourl.endWith("mp3")||videourl.endWith("MP3")){
				ismp3 = true;
			}
			initScreenshot(ismp3);
		}
	});
}
/*
 * 将selectbox转换为checkbox组件（用于用户栏目挂钩）
 * @project SDCMW
 * @author hawkFly
 * @remark selectbox to checkbox
 * */
function parseSectionselect(pageid, componentid){
	var i = 0;
	while(true){
		if($("#"+pageid+"_guid_"+componentid+"_selectsectionids__"+i)[0] == null){break;}
		$("#"+pageid+"_guid_"+componentid+"_selectsectionids__"+i).attr("style","display: none;");
		$("#"+pageid+"_guid_"+componentid+"_selectsectionids__"+i).parent().append("<div id='checkboxdiv'></div>");
		changecheckbox(pageid, componentid, 'sectionid__'+i, 'selectsectionids__'+i, true);
		$("#"+pageid+"_guid_"+componentid+"_siteid").change(function(){
			changecheckbox(pageid, componentid, 'sectionid__'+i, 'selectsectionids__'+i, false);
		});
		i++;
	}
}

/*
 * 格式化关键字分类为 div block, 并未这些div添加click事件
 * @project SDCMW
 * @author hawkFly
 * @remark 当前未启用，因为使用下拉列表+checkbox的方式更简单直接
 * */
function parseKeywordTypes(pageid, componentid){
	var keywordtypename = pageid + '_guid_'+componentid+'_keyword';
	var data = {Lists:[]}; // template data object
	var parentjq; //外层父对象
	$("input[name='"+keywordtypename+"']").each(function(i){//取到所有关键字类别待格式化组件
		var label = $(this).attr('label'); var value = this.value;
		parentjq = $(this).parent();
		data.Lists.push({id: value, name: label});
	});
	parentjq.setTemplateURL($.jbsf.core.contextpath()+"/webresources/pages/templates/sdcm.html").processTemplate(data);
}

/**
 * 获取最新置顶数字
 */
function getTopnum(pageid, reportid){
	displayLoadingMessage();
	invokeServerActionForReportData(pageid,reportid,'com.wbcs.jbsf.action.CreateTopNumAction',{name:'SELECTEDROW', value:true},{},false,null,createtopnumcallback);
}

function createtopnumcallback(dataObj){//dataObj.returnValue, dataObj.pageid, dataObj.componentid
	setReportInputBoxValue(dataObj.pageid,dataObj.componentid,false,{SHUNXU:dataObj.returnValue},{name:'SELECTEDROW',value:true});
	hideLoadingMessage();
}