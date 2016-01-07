//全局变量
var systemrootpath="";

//加载grid 
function loadgrid(datepath){
	 systemrootpath=datepath;
	 var from_date = $("#search_from" ).val(); 
	 var to_date   = $("#search_to" ).val(); 
	 jQuery("#fpdr_list").jqGrid({ 
		 url:datepath+'/jf/invoiceUpload/getImgInfoList?from_date='+from_date+
		 '&to_date='+to_date, 
		 datatype: "json",
		 jsonReader: {
				userdata : "redate",
				root:  "rows", // 数据行
				page:  "page",  // 当前页
				total: "total",    // 总页数
				records: "records", // 总记录数
				repeatitems : false // 设置成false，在后台设置值的时候，可以乱序。且并非每个值都得设
		 },
		 colNames:['发票编号','图片名称','转换状态','转换说明','操作人','操作时间','机器名称','机器MAC','机器IP'],
		 colModel:[ {name:'BH',index:'BH', width:150,align:'center'}, 
		            {name:'NAME',index:'NAME', width:150,align:'left'},
		            {name:'F_ZHZT',index:'F_ZHZT', width:100,align:'center'},
		            {name:'F_ZHSM',index:'F_ZHSM', width:200},
		            {name:'F_CZR',index:'F_CZR', width:100}, 
		            {name:'F_CZSJ',index:'F_CZSJ', width:100,align:'center'}, 
		            {name:'F_MACHINE',index:'F_MACHINE', width:150}, 
                    {name:'F_MAC',index:'F_MAC', width:150}, 
		            {name:'F_IP',index:'F_IP', width:150}, 	 
		           ], 
		 rowNum:15, 
		 rowList:[15,30,45], 
		 pager: '#fpdr_pager', 
		 //sortname: 'BH', 
		 recordpos: 'left', 
		 viewrecords: true,
		 autoScroll: true,
	     autoheight:false,
		 shrinkToFit: false,
		 //width :"800",// 自动填充宽度
		 autowidth:true,
		 rownumbers: true,
		 //sortorder: "desc", 
		 height:"300",
		 autoScroll: true,  	
		 multiselect: true, 
		 caption: "发票列表" });  
}


//重新加载
function gridReload(){ 
	 var noteje    = $("#noteje").val(); 
	 var custom    = $("#custom").val(); 
	 var from_date = $("#search_from" ).val(); 
	 var to_date   = $("#search_to" ).val(); 
	 $("#fpdr_list").jqGrid('setGridParam',{
		 url:systemrootpath+'/jf/invoiceUpload/getImgInfoList?from_date='+from_date+
		 '&to_date='+to_date,page:1}
	 ).trigger("reloadGrid"); 
}


//发起发片图片识别请求
function invoiceRecognise(){

	var selectrows = jQuery("#fpdr_list").jqGrid('getGridParam','selarrrow'); 
	if(selectrows.length<1){
		alert("请至少选择一条记录进行识别！");
		return;
	}
	
	var invoiceArray=[];
	
	for(var i=0;i<selectrows.length;i++){
		
		var BH=$("#fpdr_list").getCell(selectrows[i],"BH");
		
		$.ajax({
			url:systemrootpath+"/jf/invoiceUpload/invoiceRecognise?BH="+BH,
			type: "get",
			cache:  false,
			success:function (data, textStatus) {
			},
			timeout:100000000000000000,
			error:function(XMLHttpRequest, textStatus, errorThrown){
		
			}
		});
	};
	
	alert("识别请求已发送");
}

function deletePendImg(){
	
	var selectrows = jQuery("#fpdr_list").jqGrid('getGridParam','selarrrow'); 
	if(selectrows.length<1){
		alert("请至选择一条要删除的记录！");
		return;
	}
	
	if(!confirm("是否确定删除?")){
		return;
	}
	
	var idArr=[];
	
	for(var i=0;i<selectrows.length;i++){
		
		var BH=$("#fpdr_list").getCell(selectrows[i],"BH");
		idArr.push(BH);
	};
	
	var idString=idArr.toString();
	alert(idString);
	
	$.ajax({
		url:systemrootpath+"/jf/invoiceUpload/deletePendImg?idString="+idString,
		type: "get",
		cache:  false,
		success:function (data, textStatus) {
			gridReload();
			alert(data.result);
			
		},
		timeout:100000000000000000,
		error:function(XMLHttpRequest, textStatus, errorThrown){
			if(textStatus=="timeout"){
				showdialog("获取数据超时！");
			} else {
				alert("删除数据失败");
			}
		}
	});
	
}


function init_uploadiy(){
	
	$("#btn_upload").uploadify({
		
		'swf'       : path+'/webresources/jsplugins/uploadify/uploadify.swf',
		'uploader'	: path+'/jf/invoiceUpload/receiveUpload',
		'fileTypeExts' : '*.tif;',
		'removeCompleted' : true,
		'auto'           : true,
		'fileSizeLimit'		 : '1GB',
		'uploadLimit' : 999 ,
		'queueID': 'fileQueue',
		'buttonText':'上传',
		'buttonClass': "btn btn_success",
		'height' : 34,
		'width'  :  90,
		'removeTimeout': 0,
		'onUploadStart' : function(file) {
			
			$('#uploadQueue').modal('show');
			
			
		},
		'onSelect' : function(file) {
		},
		'onQueueComplete' : function(queueData) {
            alert(queueData.uploadsSuccessful + '个文件上传成功.');
            
            $('#uploadQueue').modal('hide');
            gridReload();
            init_uploadiy();
        }
	});
	
}


