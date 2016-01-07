<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=path %>/css/ui-lightness/jquery-ui.min.css" rel="stylesheet" type="text/css" />
<link href="<%=path %>/css/ui.jqgrid.css" rel="stylesheet" type="text/css" />
<script src="<%=path %>/js/jquery-1.7.2.min.js" type="text/javascript"></script>
<script src="<%=path %>/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
<script src="<%=path %>/js/jquery-ui.min.js" type="text/javascript"></script>
<script src="<%=path %>/js/grid.locale-cn.js" type="text/javascript"></script>
<script src="<%=path %>/js/jquery.jqGrid.min.js" type="text/javascript"></script> 
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="<%=path %>/js/bootstrap-3.2.0/css/bootstrap.min.css">
<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet" href="<%=path %>/js/bootstrap-3.2.0/css/bootstrap-theme.min.css">
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="<%=path %>/js/bootstrap-3.2.0/js/bootstrap.min.js"></script>

<link rel="stylesheet" href="<%=path %>/view/fprz/fprz.css">
<script src="<%=path %>/view/fprz/fprz.js"></script>

<title>发票认证</title>
</head>

<script type="text/javascript"> 
 jQuery().ready(function (){
	$( "#search_from" ).datepicker({
		 //changeYear:true,
		 changeMonth: true,
		 numberOfMonths: 1,
		 onClose: function( selectedDate ) {
		     $( "#search_to" ).datepicker( "option", "minDate", selectedDate );
		 }
	});
	$( "#search_to" ).datepicker({
		 //changeYear:true,
		 changeMonth: true,
		 numberOfMonths: 1,
		 onClose: function( selectedDate ) {
	    	 $( "#search_from" ).datepicker( "option", "maxDate", selectedDate );
		 }
	});
	
	var myDate = new Date();
	$( "#search_from" ).datepicker( "setDate", myDate.getFullYear()+"-01-01" );
	$( "#search_to" ).datepicker( "setDate", myDate.getFullYear()+"-"+(myDate.getMonth()+1)+"-"+myDate.getDate() );
	
	$( "#search_from" ).datepicker( "option", "maxDate", myDate.getFullYear()+"-"+(myDate.getMonth()+1)+"-"+myDate.getDate() );
    $( "#search_to" ).datepicker( "option", "minDate", myDate.getFullYear()+"-01-01" );

	loadgrid("<%=path %>");
	jQuery("#list9").jqGrid('setFrozenColumns');
	
	 $(window).resize(function(){  
         $("#list9").setGridWidth($(window).width()-15);
     });
 });
</script>
     
<body>
<div> 
   <div class="form-group headsearch" >
        <label class="labelclass"> 开票日期：</label>   
        <input type="text" class="form-control inputclass input-sm" readonly="readonly" id="search_from" />
        <label class="labelclass">  至</label>   
        <input type="text" class="form-control inputclass input-sm" readonly="readonly" id="search_to"/> 
        <label class="labelclass">发票状态：</label>   
        <!-- <input type="text"  style="width:100px;" id="custom"/>  -->
        <select class="form-control inputclass input-sm" id="fpzt">
              <option>待认证</option>
<!--               <option>全部</option>
 -->              <option>已认证</option>
              <option>作废</option>
        </select>
        <label class="labelclass">税号：</label>   
        <input type="text" class="form-control inputclass input-sm" id="sh"/>    
        <label class="labelclass">销货方：</label>   
        <input type="text" class="form-control inputclass input-sm" id="xhsh"/> 
        <!-- <label class="labelclass">金额：</label>   
        <input type="text" class="form-control inputclass" id="wsje"/> -->
        <button onclick="gridReload()" class="btn btn-primary btnclass input-sm">查询</button> 
  </div>    
  <div class="fprzbody_div">
     <table id="list9"></table> 
     <div id="pager9"></div>
  </div>
  <div class="fprzbtn_div">
      <button  onclick="selectAll()" class="btn btn-success btnclass">全选</button>
      <button  onclick="selectNotALl()" class="btn btn-success btnclass">全不选</button>
      <button  onclick="LookCtf()" class="btn btn-success btnclass">查看</button>
      <button id="invoiceButton" onclick="invioceCtf()" class="btn btn-success btnclass">认证</button>  
  </div>
</div>

<!-- 弹出层 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title" id="myModalLabel">消息提醒</h4>
      </div>
      <div class="modal-body" id="modal-bodyid">
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>
</body>
</html>