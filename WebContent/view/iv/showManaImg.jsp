<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>   
    <title></title>    
	<link rel="stylesheet" type="text/css" href="<%=path%>/css/iv/invoiceFormat.css">
 <style type="text/css">
	 #ritUp img{
	 	width:320px;
	 	height:150px;
	 	cursor:pointer;
	 }
	 #ritUp{
		width:200px;
		height:120px;
		margin-left:5px;
		margin-top:7.5px;
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
	   				<div id="ritUp">
	   					<img src="" onclick="show(this)" id="rightImg" ondragstart="return false"/>
	   				</div>
	   					<div id="temp">
		   				<div id="non"></div>
	   					</div>
	   				</div>
	   			</div>
  			</td>
  		</tr>
  </table>	 
  </center>	
  </body>
</html>
