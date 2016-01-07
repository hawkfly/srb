//全局变量
var systemrootpath="";

//加载grid 
function loadgrid(datepath){
	 systemrootpath=datepath;
	 


	 jQuery("#fpcj_list").jqGrid({ 
		 url:datepath+'/jf/invoiceGather/findInvoicelist',
		 datatype: "json",
		 jsonReader: {
				userdata : "redate",
				root:  "rows", // 数据行
				page:  "page",  // 当前页
				total: "total",    // 总页数
				records: "records", // 总记录数
				repeatitems : false // 设置成false，在后台设置值的时候，可以乱序。且并非每个值都得设
		 },
		 colNames:['发票代码','发票号码','单据编号','发票状态','开票日期','购货方税号', '购货方名称','总金额','税率','税额', '销货方税号','销货方名称','认证状态','认证结果','认证人',
		           '认证时间'],
		 colModel:[ {name:'FPDM',index:'FPDM', width:90}, 
		            {name:'FPHM',index:'FPHM', width:80},
		            {name:'DJBH',index:'DJBH', width:180},
		            {name:'DJZT',index:'DJZT', width:90}, 
		            {name:'KPRQ',index:'KPRQ', width:80}, 
		            {name:'GFSH',index:'GFSH', width:120}, 
                    {name:'GFMC',index:'GFMC', width:250}, 
                    {name:'WSJE',index:'WSJE', width:80,align:'right', formatter: "number", formatoptions: {thousandsSeparator:",", defaulValue:"",decimalPlaces:2}}, 
		            {name:'SL',index:'SL', width:55,align:'right',formatter: "number", formatoptions: {thousandsSeparator:",", defaulValue:"",decimalPlaces:2}}, 	 
		            {name:'SE',index:'SE', width:80,align:'right',formatter: "number", formatoptions: {thousandsSeparator:",", defaulValue:"",decimalPlaces:2}}, 
		            {name:'XHSH',index:'XHSH', width:120}, 
					{name:'XHMC',index:'XHMC', width:250}, 
					
                    {name:'RZZT',index:'RZZT', width:80},
                    {name:'content',index:'content', width:300},
                    {name:'RZR',index:'RZR', width:80}, 
		            {name:'RZSJ',index:'RZSJ', width:120}, 		
		            ], 
		 rowNum:10, 
		 rowList:[10,20,30], 
		 pager: '#fpcj_pager', 
		 sortname: 'FPDM', 
		 recordpos: 'left', 
		 viewrecords: true,
		 autoScroll: true,
	     autoheight:false,
		 shrinkToFit: false,
		 //width :"800",// 自动填充宽度
		 autowidth:true,
		 rownumbers: true,
		 sortorder: "desc", 
		 height:"340",
		 autoScroll: true,  	
		 multiselect: true, 
		 loadtext: "正在非常卖力的加载...",
		 footerrow:true,//合计
		 caption: "发票列表" ,
		 gridComplete: function() {
			 var rownum = $(this).jqGrid("getGridParam","rowNum");
			 if(rownum>0){
				 $(".ui-jqgrid-sdiv").show();
                 var sum1 = $(this).getCol('WSJE',false,'sum');
                 var sum2 = $(this).getCol('SE',false,'sum');
                 $(this).footerData("set",{"FPDM":"合计","WSJE":sum1,"SE":sum2});
			  }else{
				 $(".ui-jqgrid-sdiv").hide();
			  }
		 }
	});  
}
//重新加载
function gridReload(){ 

	 $("#fpcj_list").jqGrid('setGridParam',{
		 url:systemrootpath+'/jf/invoiceGather/findInvoicelist',page:1}
	 ).trigger("reloadGrid"); 
}

//删除发票
function deleteInvoice(){
	
	var selectrows = jQuery("#fpcj_list").jqGrid('getGridParam','selarrrow'); 
	if(selectrows.length<1){
		alert("请至选择一条要删除的记录！");
		return;
	}
	
	if(!confirm("是否确定删除?")){
		return;
	}
	
	var idArr=[];
	
	for(var i=0;i<selectrows.length;i++){
		
		var BH=$("#fpcj_list").getCell(selectrows[i],"DJBH");
		idArr.push(BH);
	};
	
	var idString=idArr.toString();
	alert(idString);
	
	$.ajax({
		url:systemrootpath+"/jf/invoiceGather/deleteInvoice?idString="+idString,
		type: "get",
		cache:  false,
		success:function (data, textStatus) {
			gridReload();
			alert(data.result);
			
		},
		timeout:6000,
		error:function(XMLHttpRequest, textStatus, errorThrown){
			if(textStatus=="timeout"){
				showdialog("获取数据超时！");
			} else {
				alert("删除数据失败");
			}
		}
	});
	
}

//提交
function submitInvoice(){
	
	var selectrows = jQuery("#fpcj_list").jqGrid('getGridParam','selarrrow'); 
	if(selectrows.length<1){
		alert("请至选择一条要删除的记录！");
		return;
	}
	var idArr=[];
	
	for(var i=0;i<selectrows.length;i++){
		
		var BH=$("#fpcj_list").getCell(selectrows[i],"DJBH");
		idArr.push(BH);
	};
	
	var idString=idArr.toString();
	alert(idString);
	
	$.ajax({
		url:systemrootpath+"/jf/invoiceGather/submitInvoice?idString="+idString,
		type: "get",
		cache:  false,
		success:function (data, textStatus) {
			gridReload()
			alert(data.result);
			
		},
		timeout:6000,
		error:function(XMLHttpRequest, textStatus, errorThrown){
			if(textStatus=="timeout"){
				showdialog("获取数据超时！");
			} else {
				alert("数据提交失败");
			}
		}
	});
	
}



function transformInvoice(){
	
	$.ajax({
		url:systemrootpath+"/jf/invoiceGather/transformInvoice",
		type:"get",
		cache:false,
		success:function(data,textStatus) {
			alert(data);
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			
		},
		timeout:6000
	})
}




//弹出框
function showdialog(showtext){
	
	$('#modal-bodyid').text(showtext);
	$('#myModal').modal('show');
}


//打开发票信息采集页面
function openFpdr(){
	window.parent.f_addTab("cjPage","发票信息采集","/InvoiceCtf//view/fpdr/fpdr.jsp?tabid=fpdrPage");
}




