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
		
		<link rel="stylesheet" href="<%=path %>/view/fpdr/fpdr.css">
		
		

	</head>
	
	<body>
	
	<div> 
	  <div id = "scanWrap">
	  
		<object classid="clsid:15D142CD-E529-4B01-9D62-22C9A6C00E9B" id="scaner" width="100%" height="600" codebase="<%=path %>/cabs/ScanOnWeb.cab#version=1,0,0,1">
		    <param name="Visible" value="0">
		    <param name="AutoScroll" value="0">
		    <param name="AutoSize" value="0">
		    <param name="AxBorderStyle" value="1">
		    <param name="Caption" value="scaner">
		    <param name="Color" value="4278190095">
		    <param name="Font" value="宋体">
		    <param name="KeyPreview" value="0">
		    <param name="PixelsPerInch" value="96">
		    <param name="PrintScale" value="1">
		    <param name="Scaled" value="-1">
		    <param name="DropTarget" value="0">
		    <param name="HelpFile" value>
		    <param name="PopupMode" value="0">
		    <param name="ScreenSnap" value="0">
		    <param name="SnapBuffer" value="10">
		    <param name="DockSite" value="0">
		    <param name="DoubleBuffered" value="0">
		    <param name="ParentDoubleBuffered" value="0">
		    <param name="UseDockManager" value="0">
		    <param name="Enabled" value="-1">
		    <param name="AlignWithMargins" value="0">
		    <param name="ParentCustomHint" value="-1">
		  </object>
	  
	  </div>
		
		
	  	
	  <div class="fpdrbtn_div">
	  	<div class="center-block" style="float:right;margin-left:500px">
	  	 <button id="btn_delete" style="width:90px;o" class="btn btn-success">表单上传</button>
	     <button id="btn_delete" style="width:90px;o" class="btn btn-success">AJAX上传</button>
	     <button id="btn_delete" style="width:90px;o" class="btn btn-success">表单上传</button>
	  	</div>
	  </div>
	  
	  <form >
	  	<input id="pictureData" type="hidden" value=""/>
	  </form>
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
	

		
	<script src="<%=path %>/js/jquery-1.7.2.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=path %>/js/bootstrap.min.js"></script>

	
	<script>
	
		var path='<%=path %>';
		
		$(document).ready(function(){
			
			document.getElementById('scaner').setDpi(300,300);
			document.getElementById('scaner').showDialog=true;
			//document.getElementById('scaner').setShowDialogEnable(false) ;
			document.getElementById('scaner').setDpiEnable(false) ;
			
		});
		
		function uploadByAJAX(){
			document.getElementById('scaner').allImageAsTIFFData();
			
		}
		
		function uploadByForm(){
			
		}
		
	</script>
		
		
	</body>
</html>
