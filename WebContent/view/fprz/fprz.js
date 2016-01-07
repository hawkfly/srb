//全局变量
var systemrootpath="";

//加载grid 
function loadgrid(datepath){
	 systemrootpath=datepath;
	 var from_date = $("#search_from" ).val(); 
	 var to_date   = $("#search_to" ).val(); 
	 var fpzt        = $("#fpzt option:selected").text();
	 if(fpzt=="全部")  fpzt="";
	 if(fpzt=="待认证") fpzt="CSSB";
	 if(fpzt=="已认证") fpzt="RZ";
	 if(fpzt=="作废")  fpzt="ZF";

	 jQuery("#list9").jqGrid({ 
		 url:datepath+'/jf/invoiceCtf/serverfpsearchlist?from_date='+from_date+
		 '&to_date='+to_date+"&fpzt="+fpzt, 
		 datatype: "json",
		 jsonReader: {
				userdata : "redate",
				root:  "rows", // 数据行
				page:  "page",  // 当前页
				total: "total",    // 总页数
				records: "records", // 总记录数
				repeatitems : false // 设置成false，在后台设置值的时候，可以乱序。且并非每个值都得设
		 },
		 colNames:['发票代码','发票号码','开票日期','购货方税号', '购货方名称','总金额','税率','税额', '销货方税号','销货方名称','发票状态','认证状态','认证结果','认证人',
		           '认证时间','单据编号'],
		 colModel:[ {name:'FPDM',index:'FPDM', width:90}, 
		            {name:'FPHM',index:'FPHM', width:80},
		            {name:'KPRQ',index:'KPRQ', width:80}, 
		            {name:'GFSH',index:'GFSH', width:120}, 
                    {name:'GFMC',index:'GFMC', width:250}, 
                    {name:'WSJE',index:'WSJE', width:80,align:'right', formatter: "number", formatoptions: {thousandsSeparator:",", defaulValue:"",decimalPlaces:2}}, 
		            {name:'SL',index:'SL', width:55,align:'right',formatter: "number", formatoptions: {thousandsSeparator:",", defaulValue:"",decimalPlaces:2}}, 	 
		            {name:'SE',index:'SE', width:80,align:'right',formatter: "number", formatoptions: {thousandsSeparator:",", defaulValue:"",decimalPlaces:2}}, 
		            {name:'XHSH',index:'XHSH', width:120}, 
					{name:'XHMC',index:'XHMC', width:250}, 
					{name:'DJZT',index:'DJZT', width:90}, 
                    {name:'RZZT',index:'RZZT', width:80},
                    {name:'content',index:'content', width:300},
                    {name:'RZR',index:'RZR', width:80}, 
		            {name:'RZSJ',index:'RZSJ', width:120}, 		         
		            {name:'DJBH',index:'DJBH', width:255}], 
		 rowNum:10, 
		 rowList:[10,20,30], 
		 pager: '#pager9', 
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
		 height:"260",
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
	 var fpzt        = $("#fpzt option:selected").text();
	 if(fpzt=="全部")  {fpzt="";}
	 if(fpzt=="待认证") {fpzt="CSSB"; $("#invoiceButton").removeAttr("disabled");}//}//
	 if(fpzt=="已认证") {fpzt="RZ";   $("#invoiceButton").attr("disabled", "disabled");}
	 if(fpzt=="作废")  {fpzt="ZF";   $("#invoiceButton").attr("disabled", "disabled");}

	 var sh          = $("#sh").val(); 
	 var wsje        = $("#wsje").val(); 
	 var xhsh        = $("#xhsh").val(); 
	 var from_date   = $("#search_from" ).val(); 
	 var to_date     = $("#search_to" ).val(); 
	 $("#list9").jqGrid('setGridParam',{
		 url:systemrootpath+'/jf/invoiceCtf/serverfpsearchlist?from_date='+from_date+
		 '&to_date='+to_date+'&wsje='+wsje+'&xhsh='+encodeURIComponent(encodeURIComponent(xhsh))+'&sh='+sh+"&fpzt="+fpzt,page:1}
	 ).trigger("reloadGrid"); 
}
//认证服务
function invioceCtf(){

	var selectrows = jQuery("#list9").jqGrid('getGridParam','selarrrow'); 
	if(selectrows.length<1){
		showdialog("请至少选择一条记录进行认证！");
		return;
	}
	var FPDM=$("#list9").getCell(selectrows[0],"FPDM");
	var FPHM=$("#list9").getCell(selectrows[0],"FPHM");
	for(var mm=1;mm<selectrows.length;mm++){
		FPDM+= ("@"+$("#list9").getCell(selectrows[mm],"FPDM"));
		FPHM+= ("@"+$("#list9").getCell(selectrows[mm],"FPHM"));
	}
	$.ajax({
		url:systemrootpath+"/jf/invoiceCtf/invoicectf?FPDM="+FPDM+"&FPHM="+FPHM,
		type: "get",
		cache:  false,
		success:function (data, textStatus) {
			showdialog(data);
			gridReload();
		},
		timeout:6000000,
		error:function(XMLHttpRequest, textStatus, errorThrown){
			if(textStatus=="timeout"){
				showdialog("获取数据超时！");
			}
		}
	}); 
}

//弹出框
function showdialog(showtext){
	
	$('#modal-bodyid').text(showtext);
	$('#myModal').modal('show');
}
//全选 
function selectAll(){
	var rownum = $("#list9").jqGrid("getGridParam","rowNum");
	var ss = jQuery("#list9").jqGrid('getGridParam','selarrrow');
    for(var m=1;m<=rownum;m++){
    	if((","+ss+",").indexOf(","+m+",")<0){
        	jQuery("#list9").jqGrid('setSelection',m);
    	}
    }
}

//全不选
function selectNotALl(){
	var rownum = $("#list9").jqGrid("getGridParam","rowNum");
	var ss = jQuery("#list9").jqGrid('getGridParam','selarrrow');
    for(var m=1;m<=rownum;m++){
    	if((","+ss+",").indexOf(","+m+",")>=0){
        	jQuery("#list9").jqGrid('setSelection',m);
    	}
    }
}
//查看
function LookCtf(showtext){
	showdialog("敬请期待奥！");
}




