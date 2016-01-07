<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!-- <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> -->
<!DOCTYPE html>
<html>
	<head>
	
		<link rel="stylesheet" href="<%=path %>/css/bootstrap.css" />
		<link href="<%=path %>/css/ui-lightness/jquery-ui.min.css" rel="stylesheet" type="text/css" />
		<link href="<%=path %>/css/ui.jqgrid.css" rel="stylesheet" type="text/css" />
		<link type="text/css" rel="stylesheet" href="jqGrid/themes/ui.jqgrid.css">
		<link rel="stylesheet" href="<%=path %>/webresources/jsplugins/uploadify/uploadify.css" />
		
		<link rel="stylesheet" href="<%=path %>/view/fpdr/fpdr.css">
		<script src="<%=path %>/view/fpdr/fpdr.js"></script>

	</head>
	
	<body>
	
	<div> 
	
<!-- 	
	   <div class="form-group headsearch">
     	<label class="labelclass"> 开票日期：</label>   
       	<input type="text" class="form-control inputclass input-sm" readonly="readonly" id="search_from" />
       	<label class="labelclass">  至</label>   
        <input type="text" class="form-control inputclass input-sm" readonly="readonly" id="search_to"/> 
	        <label>  转换状态</label>
	        
	        <select>
				<option value ="0">未识别</option>
				<option value ="1">识别成功</option>
				<option value="2">识别失败</option>
			</select>
	        
	
	        <button onclick="gridReload()" style="width:90px;" class="btn btn-primary">查询</button> 
	  </div>
	   -->
	      
	  <div class="fpdrbody_div">
	     <table id="fpdr_list"></table> 
	     <div id="fpdr_pager"></div>
	  </div>
	  
	  
	  <div class="fpdrbtn_div">
	      <div class="btn_wrap"><button id="btn_upload" style="width:90px" >导入</button></div>
	      <div class="btn_wrap"><button id="btn_transform" style="width:90px"  onclick="invoiceRecognise()" class="btn btn-success">识别</button></div>
	      <div class="btn_wrap"><button id="btn_delete" style="width:90px"  onclick="deletePendImg()" class="btn btn-success">删除</button></div>
	      <div class="btn_wrap"><button id="btn_delete" style="width:90px"  onclick="returnFpcj()" class="btn btn-success">返回</button></div>
	      
	  </div>
	</div>
	
	<style>
		
		.fpdrbtn_div .btn_wrap {
			margin-left: 10px;
			float:left;
		}
		
		.fpdrbtn_div .btn {
			line-height: 1.42857 !important;
		}
	
	</style>
	
  
    
    
    
	<!-- 弹出层 -->
	<div class="modal fade" id="uploadQueue" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
	        <h4 class="modal-title" id="myModalLabel">上传队列</h4>
	      </div>
	      <div class="modal-body" id="modal-bodyid">
	      
	      	<div id="file_queue_wrap" style="height:200px; overflow:auto;">
	      		<div id="fileQueue" style="">
    			</div>
    		</div>
	      </div>
	      <div class="modal-footer">
	      </div>
	    </div>
	  </div>
	</div>
    
	
	<script src="<%=path %>/js/jquery-1.7.2.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=path %>/js/bootstrap.min.js"></script>
	

	<script src="<%=path %>/js/jquery-ui.min.js" type="text/javascript"></script>
	<script src="<%=path %>/js/grid.locale-cn.js" type="text/javascript"></script>
	<script src="<%=path %>/js/jquery.jqGrid.min.js" type="text/javascript"></script> 
	<script src="<%=path %>/webresources/pages/jf/transform/transform.js" type="text/javascript"></script> 
	<script type="text/javascript" 
			src="<%=path %>/webresources/jsplugins/uploadify/swfobject.js"></script>
	<script type="text/javascript" 
			src="<%=path %>/webresources/jsplugins/uploadify/jquery.uploadify.min.js"></script>
	
	
	
	<script>
	
		var path='<%=path %>';
		
		
		$(document).ready(function(){
			init_uploadiy();
		});
		
		function openScanPage(){
			window.parent.f_addTab("scanPage","扫描图片","/InvoiceCtf/view/fpdr/scan.jsp?tabid=fpcjPage");
		}
		
		function returnFpcj(){
			window.parent.f_addTab("fpcjPage","发票信息采集","/InvoiceCtf//view/fpcj/fpcj.jsp?tabid=fpcjPage");
			
			
			
		}
		
		
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
			
			 $(window).resize(function(){  
		         $("#fpdr_list").setGridWidth($(window).width()-15);
		     });
		});

	
	</script>
		
		
	</body>
</html>
