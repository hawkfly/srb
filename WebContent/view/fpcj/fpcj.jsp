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

<link rel="stylesheet" href="<%=path %>/view/fpcj/fpcj.css">
<script src="<%=path %>/view/fpcj/fpcj.js"></script>

<title>发票认证</title>


<style>

#fpcjbody_div{
	margin-top:30px;
}
</style>

</head>

<script type="text/javascript"> 

 jQuery().ready(function (){
	

	loadgrid("<%=path %>");
	jQuery("#fpcj_list").jqGrid('setFrozenColumns');
	
	 $(window).resize(function(){  
         $("#fpcj_list").setGridWidth($(window).width()-15);
     });
 });
</script>
     
<body>
<div> 

  <div class="fpcjbody_div">
     <table id="fpcj_list"></table> 
     <div id="fpcj_pager"></div>
  </div>
  <div class="fpcjbtn_div">
	
	<button  id="transform_btn "onclick="transformInvoice()" style="float:left ;margin-left:20px"
		class="btn btn-success btnclass">提取发票</button>

	<button  onclick="openFpdr()" class="btn btn-success btnclass">导入</button>
	<button  onclick="deleteInvoice()" class="btn btn-success btnclass">作废</button>
	<button  onclick="submitInvoice()" class="btn btn-success btnclass">流转</button>
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