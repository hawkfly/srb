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
		
		<style>
			.button_wrap {
				margin-top: 10px;
			}
			.button_wrap .btn {
				margin-left:10px;
				float: right;
			}
			.grid_wrap{
				margin-top:10px;
				width:100%;
			}
			
			.clear{clear:both;}
		
		</style>
	
	</head>
	
	<body>
		<div>
		
			<div class="row">
				<div class="col-md-10 col-md-offset-1">
				  	
				  	<div id="tabs">
						<ul>
							<li><a href="#tab_upload">发票上传</a></li>
							<li><a href="#tab_transform">发票转换</a></li>
						</ul>
						
						<div id="tab_transform">
				 			<div class="grid_wrap">
								<table id="grid_transform"></table>
								<div id="grid_pager_transform"></div>
								<div class="button_wrap">
									<button id="btn_single_transform" type="button" class="btn btn-primary">转换选中</button>
									<button id="btn_all_transform" type="button" class="btn btn-primary">转换全部</button>
									
								</div>
								
								<div class="clear"></div>
							</div>
						</div>
						
						
						<div id="tab_upload">
						
							<div class="grid_wrap">
								
								<table id="grid_upload"></table>
								<div id="grid_pager_upload"></div>
								
								<div class="button_wrap">
									<button id="btn_scan" type="button" class="btn btn-primary">扫描发票</button>
									<button id="btn_upload" type="button" class="btn btn-primary">导入发票</button>
								</div>
								<div class="clear"></div>
								
							</div>
						</div>
						
				
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
			
			$( "#tabs" ).tabs();
			
			show_upload_grid();
			show_transform_grid();
			init_uploadiy();
		
	
		});

	
	</script>
		
		
	</body>
</html>
