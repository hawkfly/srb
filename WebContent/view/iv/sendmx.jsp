<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="wbcs" prefix="wx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<style type="text/css">
	#content{
		width: 1150px;
		height: 60px;
		border-top: 1px solid #666;
	}
	#gfdm,#xfdm,#month{
		width: 200px;
		height: 34px;
		border-radius: 3px;
		border: 1px solid #C7C7C7;
		background-color: #fff;
		text-indent: 6px;
		font-size: 16px;
		margin-top: 10px;
	}
	#gfdm:hover,#xfdm:hover{
		border: 1px solid #6466CB;
		width: 200px;
		height: 34px;
		border-radius: 3px;
		background-color: #fff;
		text-indent: 6px;
		font-size: 16px;
	}
	.btn{
		line-height: 33px;
		background-color: #40A7EC;
		width: 64px;
		display: block;
		text-align: center;
		color: #FFF;
		font-size: 14px;
		border-radius: 3px;
		cursor: pointer;
	}
	#import{
		position: relative;
		left: 840px;
		top: -36px;
	}
	#send{
		position: relative;
		left: 940px;
		top: -69px;
	}
	.btn:hover{
		font-weight: bold;
		color:#fff;
	}
	.content_list{
		width: 1150px; 
		overflow-x: auto; 
		overflow-y: hidden; 
		height: 90%;
		border-top: 1px solid #666;
		padding-top: 10px;
	}
	.content_page{
		width: 1150px; 
		overflow-x: auto; 
		overflow-y: hidden; 
		height: 10%;
		margin-top: 5px;
	}
	.content_top span,.list_top span{
		font-size: 17px;
		line-height: 30px;
		margin-left: 40px;
		color:#666;
	}
	.content_top{
		background-image: url('<%=path%>/images/login/wh.png');
		background-repeat: no-repeat;
		width: 1150px;
		height: 35px;
	}
	.list_top{
		background-image: url('<%=path%>/images/login/list.png');
		background-repeat: no-repeat;
		width: 1150px;
		height: 35px;
	}
	</style>
	<script type="text/javascript" src="<%=path%>/webresources/script/jquery-1.8.3.js"></script>
	<script type="text/javascript">
		function importData(){
			var gfdm = $("#gfdm").val();
			var xfdm = $("#xfdm").val();
			var month = $("#month").val();
			//alert(gfdm+xfdm+month);
			window.open("http://www.taxserver.cn/purchase/administrator?gfdw="+gfdm+"&xfdw="+xfdm+"&rzsj="+month+"");
		}
		function sendData(){
			window.open("http://www.taxserver.cn/purchase/jspro");
		}
	</script>
  </head>
  
  <body>
  	<div class="content_top"><span>导入条件</span></div>
    <div id="content">
   	 <label>购方单位：</label><input type="text" id="gfdm">
     <label>销方单位：</label><input type="text" id="xfdm" >
     <label>月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;份：</label><select id="month">
             <option value="05">05</option>
             <option value="06">06</option>
             <option value="07">07</option>
             <option value="08">08</option>
       </select>
    <a onclick="importData();" id="import" class="btn">导入</a>
    <a onclick="sendData();" id="send" class="btn">上传</a>
    </div>
    <div class="list_top"><span>明细列表</span></div>
    <div class="content_list">
    <wx:data>
	</wx:data>
	</div>
	<div class="content_page">
	<wx:navigate>
	</wx:navigate>
	</div>
  </body>
</html>
