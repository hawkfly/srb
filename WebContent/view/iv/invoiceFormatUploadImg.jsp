<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    <title></title>    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="<%=path%>/css/iv/invoiceFormat.css">
 <style type="text/css">
 #leftUp img{
 	width:200px;
 	height:120px;
 	cursor:pointer;
 }
 #leftUp img:active{
 	width:700px !important;
 	height:400px !important;
 	position:absolute;
 	left:70px;
 	bottom:10px;
 	cursor:pointer;
 }
 #rightUp img{
 	width:200px;
 	height:120px;
 	cursor:pointer;
 }
 #rightUp img:active{
 	width:700px !important;
 	height:400px !important;
 	position:absolute;
 	right:70px;
 	bottom:10px;
 	cursor:pointer;
 }
 </style>
  </head>
  <body>
  <center>
  <table>
  		<tr width="100%">
  			<td>
  				<div>
	   				<div id="tcgs">
	   				<span>填充格式:</span>
	   				<div id="rightUp">
	   				<img src="" id="rightImg" ondragstart="return false"/>
	   				</div>
	   					<div id="temp">
		   				<div id="non"></div>
		   					<div id="upBtn">
		   						<input type="file" name="rightup" id="rightup" size="18"/>
		   						<input id="submitBtn2" type="button" value="上传">
		   					</div>
	   					</div>
	   				</div>
	   			</div>
  			</td>
  		</tr>
  </table>	 
  </center>  	
  </body>
</html>
